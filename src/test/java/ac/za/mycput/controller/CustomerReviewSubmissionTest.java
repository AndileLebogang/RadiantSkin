package ac.za.mycput.controller;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Review;
import ac.za.mycput.repository.UserRepository;
import ac.za.mycput.service.AccountSession;
import ac.za.mycput.service.IReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerReviewSubmissionTest {
    final IReviewService reviews = mock(IReviewService.class);
    final UserRepository users = mock(UserRepository.class);
    final AccountSession sessions = new AccountSession(users);
    MockMvc mvc;
    MockHttpSession session;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new ReviewController(reviews, sessions)).build();
        Customer customer = mock(Customer.class);
        when(customer.getUserId()).thenReturn(9L);
        when(users.findById(9L)).thenReturn(Optional.of(customer));
        MockHttpServletRequest request = new MockHttpServletRequest();
        sessions.start(request, customer);
        session = (MockHttpSession) request.getSession(false);
    }

    @Test
    void loggedInCustomerPostsWithoutCredentialsAndCannotOverrideTheSessionIdentity() throws Exception {
        Review saved = new Review.Builder()
                .setReviewId(21L)
                .setRating(5)
                .setComment("Gentle cleanser")
                .setReviewDate(LocalDate.of(2026, 10, 25))
                .build();
        when(reviews.addReview(9L, 7L, 5, "Gentle cleanser")).thenReturn(saved);
        when(reviews.findByProductId(7L)).thenReturn(List.of(saved));

        mvc.perform(post("/review/submit").session(session).contentType("application/json")
                        .content("""
                                {"productId":7,"rating":5,"comment":"Gentle cleanser",
                                 "customerId":99,"email":"someone-else@example.com","password":"ignored"}
                                """))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.reviewId").value(21))
                .andExpect(jsonPath("$.comment").value("Gentle cleanser"))
                .andExpect(jsonPath("$.customer").doesNotExist())
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.password").doesNotExist());
        mvc.perform(get("/review/product/7")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewId").value(21));
        verify(reviews).addReview(9L, 7L, 5, "Gentle cleanser");
    }

    @Test
    void credentialsInTheBodyCannotReplaceAMissingOrExpiredSession() throws Exception {
        mvc.perform(post("/review/submit").contentType("application/json").content("""
                {"productId":7,"rating":5,"comment":"Gentle cleanser",
                 "customerId":9,"email":"customer@example.com","password":"secret"}
                """))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/review/submit").session(new MockHttpSession()).contentType("application/json")
                        .content("{\"productId\":7,\"rating\":5,\"comment\":\"Gentle cleanser\"}"))
                .andExpect(status().isUnauthorized());
        when(users.findById(9L)).thenReturn(Optional.empty());
        mvc.perform(post("/review/submit").session(session).contentType("application/json")
                        .content("{\"productId\":7,\"rating\":5,\"comment\":\"Gentle cleanser\"}"))
                .andExpect(status().isUnauthorized());
        assertTrue(session.isInvalid());
        verifyNoInteractions(reviews);
    }

    @Test
    void adminSessionsCannotSubmitCustomerReviews() throws Exception {
        Admin admin = mock(Admin.class);
        when(users.findById(9L)).thenReturn(Optional.of(admin));
        mvc.perform(post("/review/submit").session(session).contentType("application/json")
                        .content("{\"productId\":7,\"rating\":5,\"comment\":\"Gentle cleanser\"}"))
                .andExpect(status().isForbidden());
        verifyNoInteractions(reviews);
    }

    @Test
    void invalidReviewIsRejectedForAnAuthenticatedCustomer() throws Exception {
        mvc.perform(post("/review/submit").session(session).contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest());
        when(reviews.addReview(9L, 7L, 6, "Gentle cleanser"))
                .thenThrow(new IllegalArgumentException("Rating must be between 1 and 5"));
        mvc.perform(post("/review/submit").session(session).contentType("application/json")
                        .content("{\"productId\":7,\"rating\":6,\"comment\":\"Gentle cleanser\"}"))
                .andExpect(status().isBadRequest());
    }
}
