package test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;

import com.shashi.beans.OrderDetails;

public class OrderBeanTest {

    private OrderDetails orderDetails;

    @Before
    public void setup() {
        orderDetails = new OrderDetails();
    }

    @Test
    public void testGetOrderId() {
        orderDetails.setOrderId("order1");
        assertEquals("order1", orderDetails.getOrderId());
    }

    @Test
    public void testGetProductId() {
        orderDetails.setProductId("product1");
        assertEquals("product1", orderDetails.getProductId());
    }

    @Test
    public void testGetProdName() {
        orderDetails.setProdName("Product 1");
        assertEquals("Product 1", orderDetails.getProdName());
    }

    @Test
    public void testGetQty() {
        orderDetails.setQty("2");
        assertEquals("2", orderDetails.getQty());
    }

    @Test
    public void testGetAmount() {
        orderDetails.setAmount("100.0");
        assertEquals("100.0", orderDetails.getAmount());
    }

    @Test
    public void testGetShipped() {
        orderDetails.setShipped(1);
        assertEquals(1, orderDetails.getShipped());
    }

    @Test
    public void testGetTime() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        orderDetails.setTime(time);
        assertEquals(time, orderDetails.getTime());
    }

    @Test
    public void testGetProdImage() {
        InputStream prodImage = new ByteArrayInputStream(new byte[0]);
        orderDetails.setProdImage(prodImage);
        assertNotNull(orderDetails.getProdImage());
    }

    @Test
    public void testSetOrderId() {
        orderDetails.setOrderId("order1");
        assertEquals("order1", orderDetails.getOrderId());
    }

    @Test
    public void testSetProductId() {
        orderDetails.setProductId("product1");
        assertEquals("product1", orderDetails.getProductId());
    }

    @Test
    public void testSetProdName() {
        orderDetails.setProdName("Product 1");
        assertEquals("Product 1", orderDetails.getProdName());
    }

    @Test
    public void testSetQty() {
        orderDetails.setQty("2");
        assertEquals("2", orderDetails.getQty());
    }

    @Test
    public void testSetAmount() {
        orderDetails.setAmount("100.0");
        assertEquals("100.0", orderDetails.getAmount());
    }

    @Test
    public void testSetShipped() {
        orderDetails.setShipped(1);
        assertEquals(1, orderDetails.getShipped());
    }

    @Test
    public void testSetTime() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        orderDetails.setTime(time);
        assertEquals(time, orderDetails.getTime());
    }

    @Test
    public void testSetProdImage() {
        InputStream prodImage = new ByteArrayInputStream(new byte[0]);
        orderDetails.setProdImage(prodImage);
        assertNotNull(orderDetails.getProdImage());
    }
}