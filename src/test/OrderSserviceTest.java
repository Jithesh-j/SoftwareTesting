package test;


import org.junit.Before;
import org.junit.Test;

import com.shashi.beans.CartBean;
import com.shashi.beans.OrderBean;
import com.shashi.beans.OrderDetails;
import com.shashi.beans.TransactionBean;
import com.shashi.service.impl.CartServiceImpl;
import com.shashi.service.impl.OrderServiceImpl;
import com.shashi.service.impl.ProductServiceImpl;
import com.shashi.service.impl.UserServiceImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class OrderSserviceTest {

    private OrderServiceImpl orderService;
    private CartServiceImpl cartService;
    private ProductServiceImpl productService;
    private UserServiceImpl userService;

    private CartBean cartBean;
    private OrderBean orderBean;
    private TransactionBean transactionBean;

    @Before
    public void setup() {
        orderService = new OrderServiceImpl();
        cartService = new CartServiceImpl();
        productService = new ProductServiceImpl();
        userService = new UserServiceImpl();

        cartBean = new CartBean("user1", "product1", 2);

        orderBean = new OrderBean();
        orderBean.setTransactionId("transaction1");
        orderBean.setProductId("product1");
        orderBean.setQuantity(2);
        orderBean.setAmount(100.0);

        transactionBean = new TransactionBean();
        transactionBean.setTransactionId("transaction1");
        transactionBean.setUserName("user1");
        transactionBean.setTransAmount(100.0);
    }

    @Test
    public void testPaymentSuccess() {
        cartService.addProductToCart("guest@gmail.com", "P20230423083830", 2);

        String result = orderService.paymentSuccess("guest@gmail.com", 100.0);
        assertEquals("Order Placed Successfully!", result);
    }

    @Test
    public void testPaymentSuccess_NoCartItems() {
        String result = orderService.paymentSuccess("guest@gmail.com", 100.0);
        assertEquals("Order Placement Failed!", result);
    }

    @Test
    public void testPaymentSuccess_AddOrderFails() {
        cartService.addProductToCart("guest@gmail.com", "P20230423083830", 2);

        // Simulate addOrder failure
        orderService = new OrderServiceImpl() {
            @Override
            public boolean addOrder(OrderBean order) {
                return false;
            }
        };

        String result = orderService.paymentSuccess("guest@gmail.com", 100.0);
        assertEquals("Order Placement Failed!", result);
    }
    
    @Test
    public void testAddOrder() {
        boolean result = orderService.addOrder(orderBean);
        assertEquals(true, result);
    }

    @Test
    public void testAddOrder_Failure() {
        orderService = new OrderServiceImpl() {
            @Override
            public boolean addOrder(OrderBean order) {
                return false;
            }
        };

        boolean result = orderService.addOrder(orderBean);
        assertEquals(false, result);
    }
    @Test
    public void testAddTransaction() {
        boolean result = orderService.addTransaction(transactionBean);
        assertEquals(true, result);
    }

    @Test
    public void testAddTransaction_Failure() {
        orderService = new OrderServiceImpl() {
            @Override
            public boolean addTransaction(TransactionBean transaction) {
                return false;
            }
        };

        boolean result = orderService.addTransaction(transactionBean);
        assertEquals(false, result);
    }
    @Test
    public void testCountSoldItem() {
        int result = orderService.countSoldItem("P20230423084144");
        assertEquals(37, result);
    }

    @Test
    public void testCountSoldItem_NoSoldItems() {
        int result = orderService.countSoldItem("P20230423084144");
        assertEquals(37, result);
    }
    @Test
    public void testGetAllOrders() {
        List<OrderBean> orders = orderService.getAllOrders();
        assertNotNull(orders);
    }

    @Test
    public void testGetAllOrders_NoOrders() {
        orderService = new OrderServiceImpl() {
            @Override
            public List<OrderBean> getAllOrders() {
                return new ArrayList<>();
            }
        };

        List<OrderBean> orders = orderService.getAllOrders();
        assertEquals(0, orders.size());
    }
    @Test
    public void testGetOrdersByUserId() {
        List<OrderBean> orders = orderService.getOrdersByUserId("guest@gmail.com");
        assertNotNull(orders);
    }

    @Test
    public void testGetOrdersByUserId_NoOrders() {
        orderService = new OrderServiceImpl() {
            @Override
            public List<OrderBean> getOrdersByUserId(String emailId) {
                return new ArrayList<>();
            }
        };

        List<OrderBean> orders = orderService.getOrdersByUserId("guest@gmail.com");
        assertEquals(0, orders.size());
    }
    @Test
    public void testGetAllOrderDetails() {
        List<OrderDetails> orderDetails = orderService.getAllOrderDetails("guest@gmail.com");
        assertNotNull(orderDetails);
    }

    @Test
    public void testGetAllOrderDetails_NoOrders() {
        orderService = new OrderServiceImpl() {
            @Override
            public List<OrderDetails> getAllOrderDetails(String userEmailId) {
                return new ArrayList<>();
            }
        };

        List<OrderDetails> orderDetails = orderService.getAllOrderDetails("guest@gmail.com");
        assertEquals(0, orderDetails.size());
    }
    @Test
    public void testShipNow_Success() {
        String result = orderService.shipNow("T20241204093327", "P20230423084144");
        assertEquals("Order Has been shipped successfully!!", result);
    }

    @Test
    public void testShipNow_Failure_NoOrderFound() {
        String result = orderService.shipNow("P20230423084144", "P20230423084144");
        assertEquals("FAILURE", result);
    }
    @Test
    public void testShipNow_Failure_InvalidOrderId() {
        String result = orderService.shipNow(null, "P20230423084144");
        assertEquals("FAILURE", result);
    }

    @Test
    public void testShipNow_Failure_InvalidProductId() {
        String result = orderService.shipNow("T20241204093327", null);
        assertEquals("FAILURE", result);
    }
    @Test
    public void testGetOrderDetails() {
        List<OrderDetails> result = orderService.getAllOrderDetails("order1");
        assertNotNull(result);
    }

    @Test
    public void testGetOrderDetails_NoOrderFound() {
        orderService = new OrderServiceImpl() {
            @Override
            public List<OrderDetails> getAllOrderDetails(String orderId) {
                return new ArrayList<>();
            }
        };

        List<OrderDetails> result = orderService.getAllOrderDetails("order1");
        assertTrue(result.isEmpty());
    }



}