package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.shashi.beans.ProductBean;
import com.shashi.service.impl.ProductServiceImpl;
import com.shashi.utility.DBUtil;
import com.shashi.utility.IDUtil;

public class ProductServiceTest {

    private ProductServiceImpl productService;

    private String prodId = "P12345";
    private String prodName = "Test Product";
    private String prodType = "Electronics";
    private String prodInfo = "A test product for testing purposes.";
    private double prodPrice = 99.99;
    private int prodQuantity = 5;
    private InputStream prodImage = new ByteArrayInputStream("dummyImage".getBytes());

    @Before
    public void setUp() {
        // Initialize the service
        productService = new ProductServiceImpl();

        // Create and add a product to simulate actual data (you may need to replace this with actual database logic)
        ProductBean product = new ProductBean(prodId, prodName, prodType, prodInfo, prodPrice, prodQuantity, prodImage);
        productService.addProduct(product);  // Assuming this method saves the product in the service
    }
    @Test
    public void testAddProduct() {
        String prodName = "New Product";
        String prodType = "Electronics";
        String prodInfo = "A great new product!";
        double prodPrice = 199.99;
        int prodQuantity = 10;
        InputStream prodImage = new ByteArrayInputStream(new byte[0]); // Example empty image stream
        
        // Call the addProduct method
        String status = productService.addProduct(prodName, prodType, prodInfo, prodPrice, prodQuantity, prodImage);
        String prodId = IDUtil.generateId();
        // Assert that the product addition was successful
        assertEquals("Product Added Successfully with Product Id: "+ prodId, status);
    }


    @Test
    public void testGetProductDetails() {
        // Test fetching product details by ID
        ProductBean product = productService.getProductDetails(prodId);
        
        // Check that the product is not null
        assertNotNull("Product should not be null", product);

        // Check that the product ID matches
        assertEquals("Product ID should match", prodId, product.getProdId());
    }
    @Test
    public void testUpdateProduct() {
        // Fetch the original product details
        ProductBean product = productService.getProductDetails(prodId);
        
        // Create a copy of the original product and update its properties
        ProductBean updatedProduct = new ProductBean();
        updatedProduct.setProdId(product.getProdId());
        updatedProduct.setProdName("Updated Product Name");
        updatedProduct.setProdPrice(109.99);
        
        // Update the product in the service
        String status = productService.updateProduct(product, updatedProduct);

        // Verify the status
        assertEquals("Product Update status should match", "Product Updated Successfully!", status);

        // Fetch the updated product and verify
        ProductBean updatedProductFromDb = productService.getProductDetails(prodId);
        
        assertNotNull("Updated product should not be null", updatedProductFromDb);
        assertEquals("Updated product name should match", "Updated Product Name", updatedProductFromDb.getProdName());
        assertEquals("Updated product price should match", 109.99, updatedProductFromDb.getProdPrice(), 0.01);
    }
    @Test
    public void testUpdateProductPrice() {
        String prodId = "P20230423082243";
        double updatedPrice = 150.00;

        // Update the product price
        String status = productService.updateProductPrice(prodId, updatedPrice);

        // Verify that the status indicates success
        assertEquals("Price Updated Successfully!", status);

        // Retrieve the updated product and check if the price has changed
        ProductBean updatedProduct = productService.getProductDetails(prodId);
        assertNotNull("Updated product should not be null", updatedProduct);
        assertEquals("Updated product price should match", updatedPrice, updatedProduct.getProdPrice(), 0.01);
    }
    @Test
    public void testGetAllProducts() {
        // Get all products from the service
        List<ProductBean> products = productService.getAllProducts();

        // Verify that the result is not null and has products (if available)
        assertNotNull("Product list should not be null", products);
        assertTrue("Product list should have at least one product", products.size() > 0);

        // Optionally, verify the details of the first product
        ProductBean firstProduct = products.get(0);
        assertNotNull("First product should not be null", firstProduct);
        assertNotNull("Product ID should not be null", firstProduct.getProdId());
        assertNotNull("Product name should not be null", firstProduct.getProdName());
    }
    @Test
    public void testGetAllProductsByType() {
        String productType = "electronics"; // Example type

        // Get all products of the specified type
        List<ProductBean> products = productService.getAllProductsByType(productType);

        // Verify that the list is not null and contains products
        assertNotNull("Product list should not be null", products);
        assertTrue("Product list should have at least one product", products.size() > 0);

        // Optionally, check that the products returned have the expected type
        for (ProductBean product : products) {
            assertTrue("Product type should match the search type",
                       product.getProdType().toLowerCase().contains(productType.toLowerCase()));
        }
    }
    @Test
    public void testSearchAllProducts() {
        String searchTerm = "laptop"; // Example search term

        // Call the search method
        List<ProductBean> products = productService.searchAllProducts(searchTerm);

        // Verify that the list is not null and contains products
        assertNotNull("Product list should not be null", products);
        assertTrue("Product list should have at least one product", products.size() > 0);

        // Optionally, verify that the products returned match the search term
        for (ProductBean product : products) {
            assertTrue("Product name, type, or info should contain the search term",
                       product.getProdName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                       product.getProdType().toLowerCase().contains(searchTerm.toLowerCase()) ||
                       product.getProdInfo().toLowerCase().contains(searchTerm.toLowerCase()));
        }
    }
    @Test
    public void testGetProductDetails_1() {
        String prodId = "P20230423082243"; // Example product ID

        // Call the method to fetch product details
        ProductBean product = productService.getProductDetails(prodId);

        // Assert that the product is not null
        assertNotNull("Product should not be null", product);

        // Verify that the returned product has the correct product ID
        assertEquals("Product ID should match", prodId, product.getProdId());
//        assertEquals("Product name should match", "APPLE iPhone 13 Pro (Graphite, 512 GB)", product.getProdName());
        assertEquals("Product price should match", 150.0, product.getProdPrice(), 0.01);
    }
    @Test
    public void testGetProductPrice() {
        String prodId = "P20230423082243"; 

        // Call the method to fetch the product price
        double price = productService.getProductPrice(prodId);

        // Assert that the price is greater than or equal to zero (assuming prices are non-negative)
        assertTrue("Product price should be valid", price >= 0);

        // Optionally, test for an invalid product ID, ensuring the return value is -1
        String invalidProdId = "INVALID123";
        double invalidPrice = productService.getProductPrice(invalidProdId);
        assertEquals("Product price for invalid product ID should be -1", 0.0, invalidPrice, 0.01);
    }
    
    @Test
    public void testGetProductQuantity() {
        String prodId = "P20230423082243"; // Example product ID

        // Call the method to fetch the product quantity
        int quantity = productService.getProductQuantity(prodId);

        // Assert that the quantity is greater than or equal to 0 (assuming quantities are non-negative)
        assertTrue("Product quantity should be valid", quantity >= 0);

        // Optionally, test for an invalid product ID, ensuring the return value is 0
        String invalidProdId = "INVALID123";
        int invalidQuantity = productService.getProductQuantity(invalidProdId);
        assertEquals("Product quantity for invalid product ID should be 0", 0, invalidQuantity);
    }
    @Test
    public void testGetImage_ValidProdId() {
        // Example valid product ID
        String prodId = "P20230423083830";  // The prodId should match the one in your test database

        // Retrieve the expected image from the database (in this case, you'd know the byte array of the image)
        // This is an example and assumes you've set the image into the database before running the test
        byte[] expectedImage = retrieveExpectedImageFromDatabase(prodId);

        // Call the method from the service
        byte[] actualImage = productService.getImage(prodId);

        // Assert that the image is not null
        assertNotNull("Image should not be null for valid product ID", actualImage);

        // Verify that the returned image matches the expected image byte array
        assertArrayEquals("Returned image bytes should match", expectedImage, actualImage);
    }

    // Helper method to retrieve expected image from the database
    private byte[] retrieveExpectedImageFromDatabase(String prodId) {
        // Connect to your test database and fetch the image
        byte[] image = null;

        // Assuming you have a method to get a database connection (replace with your actual DB connection logic)
        try (Connection con = DBUtil.provideConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT image FROM product WHERE pid = ?");
            ps.setString(1, prodId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                image = rs.getBytes("image");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return image;
    }

    @Test
    public void testUpdateProductWithoutImage_Success() {
        String prevProductId = "P20230423082243";
        ProductBean updatedProduct = new ProductBean();
        updatedProduct.setProdId(prevProductId);
        updatedProduct.setProdName("Updated Product");
        updatedProduct.setProdType("Electronics");
        updatedProduct.setProdInfo("Updated Info");
        updatedProduct.setProdPrice(159.99);
        updatedProduct.setProdQuantity(20); // Assuming the previous product had a quantity less than this.

        // Call the updateProductWithoutImage method
        String status = productService.updateProductWithoutImage(prevProductId, updatedProduct);

        // Assert that the update was successful
        assertEquals("Product Updated Successfully!", status);
    }

    @Test
    public void testUpdateProductWithoutImage_ProductIdsDoNotMatch() {
        String prevProductId = "P20230423082243";
        ProductBean updatedProduct = new ProductBean();
        updatedProduct.setProdId("P99999"); // A different ID
        updatedProduct.setProdName("Invalid Product");
        updatedProduct.setProdType("Electronics");
        updatedProduct.setProdInfo("Updated Info");
        updatedProduct.setProdPrice(159.99);
        updatedProduct.setProdQuantity(20);

        // Call the updateProductWithoutImage method
        String status = productService.updateProductWithoutImage(prevProductId, updatedProduct);

        // Assert that the update failed due to product ID mismatch
        assertEquals("Both Products are Different, Updation Failed!", status);
    }

    @Test
    public void testUpdateProductWithoutImage_NoQuantityChange() {
        String prevProductId = "P20230423082243";
        ProductBean updatedProduct = new ProductBean();
        updatedProduct.setProdId(prevProductId);
        updatedProduct.setProdName("Updated Product No Quantity Change");
        updatedProduct.setProdType("Electronics");
        updatedProduct.setProdInfo("Updated Info");
        updatedProduct.setProdPrice(159.99);
        updatedProduct.setProdQuantity(10); // Same quantity as the previous product.

        // Call the updateProductWithoutImage method
        String status = productService.updateProductWithoutImage(prevProductId, updatedProduct);

        // Assert that the product was updated without triggering any demand notifications
        assertEquals("Product Updated Successfully!", status);
    }

    @Test
    public void testUpdateProductWithoutImage_ProductNotFound() {
        String prevProductId = "P99999"; // Non-existing product ID
        ProductBean updatedProduct = new ProductBean();
        updatedProduct.setProdId(prevProductId);
        updatedProduct.setProdName("Invalid Product");
        updatedProduct.setProdType("Electronics");
        updatedProduct.setProdInfo("Updated Info");
        updatedProduct.setProdPrice(159.99);
        updatedProduct.setProdQuantity(20);

        // Call the updateProductWithoutImage method
        String status = productService.updateProductWithoutImage(prevProductId, updatedProduct);

        // Assert that the product was not found
        assertEquals("Product Not available in the store!", status);
    }
    @Test
    public void testSellNProduct_Success() {
        String prodId = "P20241118070022"; // A product with enough stock
        int quantityToSell = 5; // A valid quantity to sell

        // Assuming the initial quantity is more than 5
        boolean result = productService.sellNProduct(prodId, quantityToSell);

        // Assert that the sale was successful
        assertTrue(result);
    }



    @Test
    public void testSellNProduct_InvalidProductId() {
        String prodId = "P99999"; // An invalid product ID that doesn't exist
        int quantityToSell = 5; // Any quantity to sell

        // Call the method with an invalid product ID
        boolean result = productService.sellNProduct(prodId, quantityToSell);

        // Assert that the sale was unsuccessful due to invalid product ID
        assertFalse(result);
    }








    

    // You can add other test methods as needed
}

