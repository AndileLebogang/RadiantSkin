package ac.za.mycput.controller;

import ac.za.mycput.domain.CartItem;
import ac.za.mycput.service.CartItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartItemController.class)
public class CartItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartItemService service;

    @Autowired
    private ObjectMapper objectMapper;

    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        cartItem = new CartItem.Builder()
                .setCartItemId(1L)
                .setQuantity(2)
                .setCart(null)
                .setProduct(null)
                .build();
    }

    @Test
    void create() throws Exception {
        Mockito.when(service.create(any(CartItem.class))).thenReturn(cartItem);

        mockMvc.perform(post("/cart-item/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItem)))
                .andExpect(status().isOk());
    }

    @Test
    void read() throws Exception {
        Mockito.when(service.read(1L)).thenReturn(cartItem);

        mockMvc.perform(get("/cart-item/read/1"))
                .andExpect(status().isOk());
    }

    @Test
    void update() throws Exception {
        Mockito.when(service.update(any(CartItem.class))).thenReturn(cartItem);

        mockMvc.perform(put("/cart-item/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartItem)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCartItem() throws Exception {
        Mockito.when(service.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/cart-item/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAll() throws Exception {
        Mockito.when(service.getAll()).thenReturn(Collections.singletonList(cartItem));

        mockMvc.perform(get("/cart-item/all"))
                .andExpect(status().isOk());
    }
}
