package ac.za.mycput.controller;

// Lebogang Andile Mahlangu 230561454 //

import ac.za.mycput.domain.Cart;
import ac.za.mycput.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart.Builder()
                .setCartId(1L)
                .setCreatedDate(LocalDate.now())
                .setCustomer(null)
                .setCartItems(Collections.emptyList())
                .build();
    }

    @Test
    void create() throws Exception {
        Mockito.when(service.create(any(Cart.class))).thenReturn(cart);

        mockMvc.perform(post("/cart/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isOk());

    }

    @Test
    void read() throws Exception {
        Mockito.when(service.read(1L)).thenReturn(cart);

        mockMvc.perform(get("/cart/read/1"))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        Mockito.when(service.update(any(Cart.class))).thenReturn(cart);

        mockMvc.perform(put("/cart/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCart() throws Exception {
        Mockito.when(service.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/cart/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll() throws Exception {
        Mockito.when(service.getAll()).thenReturn(Collections.singletonList(cart));

        mockMvc.perform(get("/cart/all"))
                .andExpect(status().isOk());
    }
}
