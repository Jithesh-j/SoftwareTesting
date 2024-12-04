package test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.shashi.beans.DemandBean;
import com.shashi.service.impl.DemandServiceImpl;


public class DemandServiceTest {

    private DemandServiceImpl demandService;

    @Before
    public void setup() {
        demandService = new DemandServiceImpl();
    }

    @After
    public void tearDown() {
        demandService = null;
    }

    @Test
    public void testAddProductSuccess() {
        String userId = "guest@gmail.com";
        String prodId = "P20230423082243";
        int demandQty = 10;

        boolean result = demandService.addProduct(userId, prodId, demandQty);

        assertTrue(result);
    }

    @Test
    public void testAddProductFailure() {
        String userId = "testUser";
        String prodId = "testProd";
        int demandQty = 10;

        // Add the product first to make the second addition fail
        demandService.addProduct(userId, prodId, demandQty);

        boolean result = demandService.addProduct(userId, prodId, demandQty);

        assertFalse(result);
    }

    @Test
    public void testAddProductInvalidInput() {
        String userId = null;
        String prodId = "P20230423082243";
        int demandQty = 10;

        boolean result = demandService.addProduct(userId, prodId, demandQty);

        assertFalse(result);
    }
    @Test
    public void testRemoveProductSuccess() {
        String userId = "testUser";
        String prodId = "P20230423082243";
        int demandQty = 10;

        // Add the product first
        demandService.addProduct(userId, prodId, demandQty);

        boolean result = demandService.removeProduct(userId, prodId);

        assertTrue(result);
    }

    @Test
    public void testRemoveProductFailure() {
        String userId = "testUser";
        String prodId = "P20230423082243";

        // Try to remove a product that doesn't exist
        boolean result = demandService.removeProduct(userId, prodId);

        assertTrue(result); // Note: The method returns true even if the product doesn't exist
    }

    @Test
    public void testRemoveProductInvalidInput() {
        String userId = null;
        String prodId = "P20230423082243";

        boolean result = demandService.removeProduct(userId, prodId);

        assertFalse(result);
    }
    @Test
    public void testAddProductWithDemandBeanSuccess() {
        String userId = "testUser";
        String prodId = "P20230423082243";
        int demandQty = 10;

        DemandBean demandBean = new DemandBean(userId, prodId, demandQty);

        boolean result = demandService.addProduct(demandBean);

        assertTrue(result);
    }


    @Test
    public void testHaveDemandedSuccess() {
        String userId = "testUser";
        String prodId = "P20230423082243";
        int demandQty = 10;

        DemandBean demandBean = new DemandBean(userId, prodId, demandQty);

        demandService.addProduct(demandBean);

        List<DemandBean> demandList = demandService.haveDemanded(prodId);

        assertNotNull(demandList);
        assertEquals(1, demandList.size());
    }

    @Test
    public void testHaveDemandedFailure() {
        String prodId = "kjf93845y85";

        List<DemandBean> demandList = demandService.haveDemanded(prodId);

        assertNotNull(demandList);
        assertEquals(0, demandList.size());
    }

    @Test
    public void testHaveDemandedInvalidInput() {
        String prodId = null;

        List<DemandBean> demandList = demandService.haveDemanded(prodId);

        assertNotNull(demandList);
        assertEquals(0, demandList.size());
    }
}