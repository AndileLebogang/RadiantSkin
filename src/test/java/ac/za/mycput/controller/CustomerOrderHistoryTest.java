package ac.za.mycput.controller;

import ac.za.mycput.domain.Admin;
import ac.za.mycput.domain.Customer;
import ac.za.mycput.domain.Order;
import ac.za.mycput.domain.OrderItem;
import ac.za.mycput.domain.OrderStatus;
import ac.za.mycput.domain.Product;
import ac.za.mycput.repository.OrderRepository;
import ac.za.mycput.repository.UserRepository;
import ac.za.mycput.service.AccountSession;
import ac.za.mycput.service.StorefrontOrderHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerOrderHistoryTest {
    private final UserRepository users = mock(UserRepository.class);
    private final OrderRepository orders = mock(OrderRepository.class);
    private final AccountSession sessions = new AccountSession(users);
    private final Customer customer = mock(Customer.class);
    private MockMvc mvc;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new StorefrontOrderHistoryController(
                sessions, new StorefrontOrderHistoryService(orders))).build();
        when(customer.getUserId()).thenReturn(9L);
        when(users.findById(9L)).thenReturn(Optional.of(customer));
        var request = new MockHttpServletRequest();
        sessions.start(request, customer);
        session = (MockHttpSession) request.getSession(false);
    }

    @Test
    void historyUsesTheSessionCustomerAndReturnsReceiptsWithoutPrivateAccountData() throws Exception {
        Product product = mock(Product.class);
        when(product.getName()).thenReturn("Daily Cream");
        var item = new OrderItem.Builder().setOrderItemId(3L).setProduct(product)
                .setQuantity(2).setUnitPrice(new BigDecimal("245.00")).build();
        var order = new Order.Builder().setOrderId(42L).setCustomer(customer)
                .setOrderDate(LocalDateTime.of(2026, 11, 13, 12, 0))
                .setStatus(OrderStatus.PROCESSING).setTotalAmount(new BigDecimal("490.00"))
                .setOrderItems(List.of(item)).build();
        when(orders.findByCustomerOrderByOrderDateDescOrderIdDesc(customer)).thenReturn(List.of(order));

        mvc.perform(get("/order/mine").session(session).param("customerId", "999"))
                .andExpect(status().isOk()).andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].orderId").value(42))
                .andExpect(jsonPath("$[0].totalAmount").value(490))
                .andExpect(jsonPath("$[0].orderItems[0].product.name").value("Daily Cream"))
                .andExpect(jsonPath("$[0].orderItems[0].quantity").value(2))
                .andExpect(jsonPath("$[0].customer").doesNotExist())
                .andExpect(jsonPath("$[0].checkoutDetails").doesNotExist())
                .andExpect(jsonPath("$[0].checkoutReference").doesNotExist());
        verify(orders).findByCustomerOrderByOrderDateDescOrderIdDesc(customer);
        verifyNoMoreInteractions(orders);
    }

    @Test
    void customersWithoutOrdersReceiveAnEmptyList() throws Exception {
        when(orders.findByCustomerOrderByOrderDateDescOrderIdDesc(customer)).thenReturn(List.of());
        mvc.perform(get("/order/mine").session(session))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void missingOrExpiredSessionCannotReadOrders() throws Exception {
        mvc.perform(get("/order/mine")).andExpect(status().isUnauthorized());
        mvc.perform(get("/order/mine").session(new MockHttpSession()))
                .andExpect(status().isUnauthorized());
        when(users.findById(9L)).thenReturn(Optional.empty());
        mvc.perform(get("/order/mine").session(session)).andExpect(status().isUnauthorized());
        assertTrue(session.isInvalid());
        verifyNoInteractions(orders);
    }

    @Test
    void adminCannotReadCustomerHistory() throws Exception {
        when(users.findById(9L)).thenReturn(Optional.of(mock(Admin.class)));
        mvc.perform(get("/order/mine").session(session)).andExpect(status().isForbidden());
        verifyNoInteractions(orders);
    }
}
