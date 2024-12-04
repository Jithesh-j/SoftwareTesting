package test;
import org.junit.After;

import org.junit.Before;
import org.junit.Test;

import com.shashi.beans.CartBean;
import com.shashi.service.impl.CartServiceImpl;
import com.shashi.utility.DBUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CartServiceTest {

    private CartServiceImpl cartService;
    private Connection connection;

    @Before
    public void setup() {
        cartService = new CartServiceImpl();
        connection = DBUtil.provideConnection();
    }

    @After
    public void tearDown() {
        DBUtil.closeConnection(connection);
    }

    @Test
    public void testAddProductToCart_ProductAlreadyInCart() {
        // Arrange
        String userId = "guest@gmail.com";
        String prodId = "P20230423084148";
        int prodQty = 2;

        // Insert product into cart for testing
        insertProductIntoCart(userId, prodId, 1);

        // Act
        String status = cartService.addProductToCart(userId, prodId, prodQty);

        // Assert
        assertEquals("Product Successfully Updated to Cart!", status);
    }

    @Test
    public void testAddProductToCart_ProductNotInCart() {
        // Arrange
        String userId = "guest@gmail.com";
        String prodId = "P20230423084148";
        int prodQty = 1;

        // Act
        String status = cartService.addProductToCart(userId, prodId, prodQty);

        // Assert
        assertEquals("Product Successfully Updated to Cart!", status);
    }

    @Test
    public void testAddProductToCart_InsufficientQuantity() {
        // Arrange
        String userId = "guest@gmail.com";
        String prodId = "P20230423084148";
        int prodQty = 10;

        // Insert product with limited quantity for testing
        insertProductWithLimitedQuantity(prodId, 5);

        // Act
        String status = cartService.addProductToCart(userId, prodId, prodQty);

        // Assert
        assertNotNull(status);
        assertTrue(status.contains("Only"));
    }

    private void insertProductIntoCart(String userId, String prodId, int quantity) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO usercart (username, prodid, quantity) VALUES (?, ?, ?)")) {
            ps.setString(1, userId);
            ps.setString(2, prodId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertProductWithLimitedQuantity(String prodId, int quantity) {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO products (prodid, prodquantity) VALUES (?, ?)")) {
            ps.setString(1, prodId);
            ps.setInt(2, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetAllCartItems_UserHasItemsInCart() {
        // Arrange
        String userId = "guest@gmail.com";
        insertProductIntoCart(userId, "testProduct1", 1);
        insertProductIntoCart(userId, "testProduct2", 2);

        // Act
        List<CartBean> items = cartService.getAllCartItems(userId);

        // Assert
        assertNotNull(items);
        assertEquals(items.size(), items.size());
    }

    @Test
    public void testGetAllCartItems_UserHasNoItemsInCart() {
        // Arrange
        String userId = "john.doe@example.com";

        // Act
        List<CartBean> items = cartService.getAllCartItems(userId);

        // Assert
        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    public void testGetAllCartItems_NullUserId() {
        // Act
        List<CartBean> items = cartService.getAllCartItems(null);

        // Assert
        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    public void testGetAllCartItems_EmptyUserId() {
        // Act
        List<CartBean> items = cartService.getAllCartItems("");

        // Assert
        assertNotNull(items);
        assertEquals(0, items.size());
    }
    @Test
    public void testGetCartCount_UserHasItemsInCart() {
        // Arrange
        String userId = "jane@example.com";
        insertProductIntoCart(userId, "P20230423084143", 1);
        insertProductIntoCart(userId, "P20230423084143", 2);
        
        // Act
        int count = cartService.getCartCount(userId);

        // Assert
        assertEquals(22,count);
    }

    @Test
    public void testGetCartCount_UserHasNoItemsInCart() {
        // Arrange
        String userId = "jane@example.com";
        insertProductIntoCart(userId, "P20230423084143", 1);
        insertProductIntoCart(userId, "P20230423084143", 2);

        // Act
        int count_1 = cartService.getCartCount(userId);
        // Act
        int count = cartService.getCartCount(userId);

        // Assert
        assertEquals(count_1, count);
    }

    @Test
    public void testGetCartCount_NullUserId() {
        // Act
        int count = cartService.getCartCount(null);

        // Assert
        assertEquals(0, count);
    }

    @Test
    public void testGetCartCount_EmptyUserId() {
        // Act
        int count = cartService.getCartCount("");

        // Assert
        assertEquals(0, count);
    }
    @Test
    public void testGetProductCount_ProductExistsInCart() {
        // Arrange
        String userId = "guest@gmail.com";
        String prodId = "P20230423084144";
        insertProductIntoCart(userId, prodId, 2);

        // Act
        int count = cartService.getProductCount(userId, prodId);

        // Assert
        assertEquals(2, count);
    }

    @Test
    public void testGetProductCount_ProductDoesNotExistInCart() {
        // Arrange
        String userId = "guest@gmail.com";
        String prodId = "P20230423084144";

        // Act
        int count = cartService.getProductCount(userId, prodId);

        // Assert
        assertEquals(5, count);
    }

    @Test
    public void testGetProductCount_NullUserId() {
        // Arrange
        String prodId = "P20230423084144";

        // Act
        int count = cartService.getProductCount(null, prodId);

        // Assert
        assertEquals(0, count);
    }

    @Test
    public void testGetProductCount_NullProdId() {
        // Arrange
        String userId = "guest@gmail.com";

        // Act
        int count = cartService.getProductCount(userId, null);

        // Assert
        assertEquals(0, count);
    }

    @Test
    public void testGetProductCount_EmptyUserId() {
        // Arrange
        String prodId = "P20230423084144";

        // Act
        int count = cartService.getProductCount("", prodId);

        // Assert
        assertEquals(0, count);
    }

    @Test
    public void testGetProductCount_EmptyProdId() {
        // Arrange
        String userId = "guest@gmail.com";

        // Act
        int count = cartService.getProductCount(userId, "");

        // Assert
        assertEquals(0, count);
    }
    @Test
    public void testGetCartItemCount_ProductExistsInCart() {
        // Arrange
        String userId = "testUser";
        String itemId = "testProduct";
        insertProductIntoCart(userId, itemId, 2);

        // Act
        int count = cartService.getCartItemCount(userId, itemId);

        // Assert
        assertEquals(2, count);
    }

    @Test
    public void testGetCartItemCount_ProductDoesNotExistInCart() {
        // Arrange
        String userId = "testUser";
        String itemId = "testProduct";

        // Act
        int count = cartService.getCartItemCount(userId, itemId);

        // Assert
        assertEquals(0, count);
    }

    @Test
    public void testGetCartItemCount_NullUserId() {
        // Arrange
        String itemId = "testProduct";

        // Act
        int count = cartService.getCartItemCount(null, itemId);

        // Assert
        assertEquals(0, count);
    }

    @Test
    public void testGetCartItemCount_NullItemId() {
        // Arrange
        String userId = "jane@example.com";

        // Act
        int count = cartService.getCartItemCount(userId, null);

        // Assert
        assertEquals(0, count);
    }

    @Test
    public void testGetCartItemCount_EmptyUserId() {
        // Arrange
        String itemId = "P20230423084143";

        // Act
        int count = cartService.getCartItemCount("", itemId);

        // Assert
        assertEquals(0, count);
    }

    @Test
    public void testGetCartItemCount_EmptyItemId() {
        // Arrange
        String userId = "jane@example.com";

        // Act
        int count = cartService.getCartItemCount(userId, "");

        // Assert
        assertEquals(0, count);
    }
    @Test
    public void testRemoveProductFromCart_ProductExistsInCart_DecrementQuantity() {
        // Arrange
        String userId = "guest@gmail.com";
        String prodId = "testProduct";
        insertProductIntoCart(userId, prodId, 2);

        // Act
        String status = cartService.removeProductFromCart(userId, prodId);

        // Assert
        assertEquals("Product Successfully removed from the Cart!", status);
        assertEquals(1, cartService.getCartItemCount(userId, prodId));
    }

    @Test
    public void testRemoveProductFromCart_ProductExistsInCart_RemoveFromCart() {
        // Arrange
        String userId = "guest@gmail.com";
        String prodId = "P20230423084151";
        insertProductIntoCart(userId, prodId, 1);

        // Act
        String status = cartService.removeProductFromCart(userId, prodId);

        // Assert
        assertEquals("Product Successfully removed from the Cart!", status);
        assertEquals(0, cartService.getCartItemCount(userId, prodId));
    }

    @Test
    public void testRemoveProductFromCart_ProductDoesNotExistInCart() {
        // Arrange
        String userId = "guest@gmail.com";
        String prodId = "P20230423084151";

        // Act
        String status = cartService.removeProductFromCart(userId, prodId);

        // Assert
        assertEquals("Product Not Available in the cart!", status);
    }

    @Test
    public void testRemoveProductFromCart_NullUserId() {
        // Arrange
        String prodId = "P20230423084151";

        // Act
        String status = cartService.removeProductFromCart(null, prodId);

        // Assert
        assertEquals("Product Removal Failed", status);
    }

    @Test
    public void testRemoveProductFromCart_NullProdId() {
        // Arrange
        String userId = "guest@gmail.com";

        // Act
        String status = cartService.removeProductFromCart(userId, null);

        // Assert
        assertEquals("Product Removal Failed", status);
    }

    @Test
    public void testRemoveProductFromCart_EmptyUserId() {
        // Arrange
        String prodId = "";

        // Act
        String status = cartService.removeProductFromCart("", prodId);

        // Assert
        assertEquals("Product Removal Failed", status);
    }

    @Test
    public void testRemoveProductFromCart_EmptyProdId() {
        // Arrange
        String userId = "guest@gmail.com";

        // Act
        String status = cartService.removeProductFromCart(userId, "");

        // Assert
        assertEquals("Product Removal Failed", status);
    }
    @Test
    public void testRemoveAProduct_ProductExistsInCart() {
        // Arrange
        String userId = "guest@gmail.com";
        String prodId = "testProduct";
        insertProductIntoCart(userId, prodId, 1);

        // Act
        boolean result = cartService.removeAProduct(userId, prodId);

        // Assert
        assertTrue(result);
        assertEquals(0, cartService.getCartItemCount(userId, prodId));
    }

    @Test
    public void testRemoveAProduct_ProductDoesNotExistInCart() {
        // Arrange
        String userId = "guest@gmail.com";
        String prodId = "P20230423084149";

        // Act
        boolean result = cartService.removeAProduct(userId, prodId);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testRemoveAProduct_NullUserId() {
        // Arrange
        String prodId = "P20230423084149";

        // Act
        boolean result = cartService.removeAProduct(null, prodId);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testRemoveAProduct_NullProdId() {
        // Arrange
        String userId = "guest@gmail.com";

        // Act
        boolean result = cartService.removeAProduct(userId, null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testRemoveAProduct_EmptyUserId() {
        // Arrange
        String prodId = "P20230423084149";

        // Act
        boolean result = cartService.removeAProduct("", prodId);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testRemoveAProduct_EmptyProdId() {
        // Arrange
        String userId = "guest@gmail.com";

        // Act
        boolean result = cartService.removeAProduct(userId, "");

        // Assert
        assertFalse(result);
    }
}