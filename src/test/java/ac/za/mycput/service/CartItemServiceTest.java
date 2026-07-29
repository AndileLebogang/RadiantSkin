package ac.za.mycput.service;

// Lebogang Andile Mahlangu 230561454 //
import ac.za.mycput.domain.CartItem;
import ac.za.mycput.domain.HairCareProduct;
import ac.za.mycput.domain.Product;
import ac.za.mycput.factory.CartItemFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CartItemServiceTest {

    @Autowired
    private CartItemService service;

    @Autowired
    private ProductService productService;

    @Test
    void create() {
        Product product = new HairCareProduct.Builder()
                .setName("Moisturizing Shampoo")
                .setDescription("Sulfate-free shampoo for dry hair")
                .setBrand("TestBrand")
                .setPrice(new BigDecimal("89.99"))
                .setStockQuantity(50)
                .setImageUrl("")
                .setVolumeMl(750)
                .setHairConcern("Dryness")
                .build();
        product = productService.create(product);

        CartItem cartItem = CartItemFactory.createCartItem(
                1L,
                2,
                product
        );

        CartItem created = service.create(cartItem);
        assertNotNull(created);
    }

    @Test
    void read() {
        Product product = new HairCareProduct.Builder()
                .setName("Moisturizing Shampoo")
                .setDescription("Sulfate-free shampoo for dry hair")
                .setBrand("TestBrand")
                .setPrice(new BigDecimal("89.99"))
                .setStockQuantity(50)
                .setImageUrl("")
                .setVolumeMl(750)
                .setHairConcern("Dryness")
                .build();
        product = productService.create(product);

        CartItem cartItem = CartItemFactory.createCartItem(
                1L,
                2,
                product
        );

        CartItem created = service.create(cartItem);
        CartItem found = service.read(created.getCartItemId());

        assertNotNull(found);
        assertEquals(created.getCartItemId(), found.getCartItemId());
    }

    @Test
    void update() {
        Product product = new HairCareProduct.Builder()
                .setName("Moisturizing Shampoo")
                .setDescription("Sulfate-free shampoo for dry hair")
                .setBrand("TestBrand")
                .setPrice(new BigDecimal("89.99"))
                .setStockQuantity(50)
                .setImageUrl("")
                .setVolumeMl(750)
                .setHairConcern("Dryness")
                .build();
        product = productService.create(product);

        CartItem cartItem = CartItemFactory.createCartItem(
                1L,
                2,
                product
        );

        CartItem created = service.create(cartItem);

        CartItem updatedCartItem = new CartItem.Builder()
                .copy(created)
                .setQuantity(5)
                .build();

        CartItem updated = service.update(updatedCartItem);
        assertNotNull(updated);
        assertEquals(5, updated.getQuantity());
    }

    @Test
    void delete() {
        Product product = new HairCareProduct.Builder()
                .setName("Moisturizing Shampoo")
                .setDescription("Sulfate-free shampoo for dry hair")
                .setBrand("TestBrand")
                .setPrice(new BigDecimal("89.99"))
                .setStockQuantity(50)
                .setImageUrl("")
                .setVolumeMl(750)
                .setHairConcern("Dryness")
                .build();
        product = productService.create(product);

        CartItem cartItem = CartItemFactory.createCartItem(
                1L,
                2,
                product
        );

        CartItem created = service.create(cartItem);
        boolean deleted = service.delete(created.getCartItemId());

        assertTrue(deleted);
        assertNull(service.read(created.getCartItemId()));
    }

    @Test
    void getAll() {
        Product product = new HairCareProduct.Builder()
                .setName("Moisturizing Shampoo")
                .setDescription("Sulfate-free shampoo for dry hair")
                .setBrand("TestBrand")
                .setPrice(new BigDecimal("89.99"))
                .setStockQuantity(50)
                .setImageUrl("")
                .setVolumeMl(750)
                .setHairConcern("Dryness")
                .build();
        product = productService.create(product);

        CartItem cartItem = CartItemFactory.createCartItem(
                1L,
                2,
                product
        );

        service.create(cartItem);
        List<CartItem> cartItems = service.getAll();
        assertNotNull(cartItems);
        assertFalse(cartItems.isEmpty());
    }
}