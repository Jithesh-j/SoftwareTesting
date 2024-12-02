package test;
import org.junit.Before;

import org.junit.Test;

import com.shashi.beans.CartBean;
import com.shashi.service.CartService;
import com.shashi.service.impl.CartServiceImpl;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

public class CartServiceTest  {

    private CartService cartService;

    @Before
    public void setUp() throws Exception {
        cartService = new CartServiceImpl();
    }

    @Test
    public void testAddProductToCart() {
        // Add a product to the cart
        String userId = "testUser1";
        String prodId = "P12345";
        int prodQty = 2;

        String result = cartService.addProductToCart(userId, prodId, prodQty);
        assertTrue(result.contains("Successfully"));

        // Validate CartBean after addition
        List<CartBean> items = cartService.getAllCartItems(userId);
        CartBean addedItem = items.stream()
                .filter(item -> item.getProdId().equals(prodId))
                .findFirst()
                .orElse(null);

        assertNotNull("Product should be added to the cart", addedItem);
        assertEquals("User ID should match", userId, addedItem.getUserId());
        assertEquals("Product ID should match", prodId, addedItem.getProdId());
        assertEquals("Quantity should match", prodQty, addedItem.getQuantity());
    }

    @Test
    public void testGetProductCount() throws SQLException {
        // Test getting the product count in the cart
        String userId = "testUser1";
        String prodId = "P12345";

        // Add product to cart
        cartService.addProductToCart(userId, prodId, 3);

        // Verify product count
        int productCount = ((CartServiceImpl) cartService).getProductCount(userId, prodId);
        assertEquals(3, productCount);

        // Check for non-existent product
        productCount = ((CartServiceImpl) cartService).getProductCount(userId, "P67890");
        assertEquals(0, productCount);
    }

    @Test
    public void testGetAllCartItems() {
        String userId = "testUser1";

        List<CartBean> items = cartService.getAllCartItems(userId);
        assertNotNull(items);
        assertTrue(items.size() > 0);

        // Validate one of the items
        CartBean item = items.get(0);
        assertNotNull("Cart item should not be null", item);
        assertNotNull("User ID should not be null", item.getUserId());
        assertNotNull("Product ID should not be null", item.getProdId());
    }

    @Test
    public void testGetCartCount() {
        String userId = "testUser1";

        int cartCount = cartService.getCartCount(userId);
        assertTrue(cartCount >= 0);
    }

    @Test
    public void testGetCartItemCount() {
        String userId = "testUser1";
        String itemId = "P12345";

        int itemCount = cartService.getCartItemCount(userId, itemId);
        assertTrue(itemCount >= 0);
    }

    @Test
    public void testRemoveProductFromCart() {
        String userId = "testUser1";
        String prodId = "P12345";

        String result = cartService.removeProductFromCart(userId, prodId);
        assertTrue(result.contains("Successfully"));

        // Validate that the product is no longer in the cart
        List<CartBean> items = cartService.getAllCartItems(userId);
        assertTrue(items.stream().noneMatch(item -> item.getProdId().equals(prodId)));
    }

    @Test
    public void testRemoveAProduct() {
        String userId = "testUser1";
        String prodId = "P12345";

        boolean result = cartService.removeAProduct(userId, prodId);
        assertTrue(result);

        // Validate that the product is removed
        List<CartBean> items = cartService.getAllCartItems(userId);
        assertTrue(items.stream().noneMatch(item -> item.getProdId().equals(prodId)));
    }

    @Test
    public void testUpdateProductToCart() {
        String userId = "testUser1";
        String prodId = "P12345";
        int newQuantity = 5;

        String result = cartService.updateProductToCart(userId, prodId, newQuantity);
        assertTrue(result.contains("Successfully"));

        // Validate the updated quantity in CartBean
        List<CartBean> items = cartService.getAllCartItems(userId);
        CartBean updatedItem = items.stream()
                .filter(item -> item.getProdId().equals(prodId))
                .findFirst()
                .orElse(null);

        assertNotNull("Updated item should exist", updatedItem);
        assertEquals("Updated quantity should match", newQuantity, updatedItem.getQuantity());
    }

    @Test
    public void testRemoveProductWhenQuantityZero() {
        String userId = "testUser1";
        String prodId = "P12345";

        cartService.updateProductToCart(userId, prodId, 1);
        cartService.removeProductFromCart(userId, prodId);

        // Verify removal
        String result = cartService.removeProductFromCart(userId, prodId);
        assertTrue(result.contains("Not Available"));
    }
    @Test
    public void testCartBeanConstructorAndGetters() {
        // Test the parameterized constructor
        String userId = "testUser1";
        String prodId = "P12345";
        int quantity = 2;

        CartBean cartBean = new CartBean(userId, prodId, quantity);

        // Validate the properties using getters
        assertEquals("User ID should match", userId, cartBean.getUserId());
        assertEquals("Product ID should match", prodId, cartBean.getProdId());
        assertEquals("Quantity should match", quantity, cartBean.getQuantity());
    }
    @Test
    public void testCartBeanSetters() {
        // Create an empty CartBean object
        CartBean cartBean = new CartBean();

        // Set properties using setters
        String userId = "testUser2";
        String prodId = "P67890";
        int quantity = 5;

        cartBean.setUserId(userId);
        cartBean.setProdId(prodId);
        cartBean.setQuantity(quantity);

        // Validate the properties using getters
        assertEquals("User ID should match", userId, cartBean.getUserId());
        assertEquals("Product ID should match", prodId, cartBean.getProdId());
        assertEquals("Quantity should match", quantity, cartBean.getQuantity());
    }
    @Test
    public void testCartBeanDefaultConstructor() {
        // Test the default constructor
        CartBean cartBean = new CartBean();

        // Validate that properties are initialized to default values
        assertNull("User ID should be null by default", cartBean.getUserId());
        assertNull("Product ID should be null by default", cartBean.getProdId());
        assertEquals("Quantity should be 0 by default", 0, cartBean.getQuantity());
    }

}
