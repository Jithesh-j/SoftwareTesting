package test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import org.junit.After;
import org.junit.Before;

import com.shashi.service.TransService;
import com.shashi.service.impl.TransServiceImpl;
import com.shashi.utility.DBUtil;

public class TransServiceTest {
	private TransService transService;

    @Before
    public void setUp() throws Exception {
        transService = new TransServiceImpl();
        initializeTransactionTestDatabase();
    }
//    @After
//    public void tearDown() {
//        clearTestDatabase();
//    }


    private void initializeTransactionTestDatabase() {
        try (Connection conn = DBUtil.provideConnection()) {
            // Create table
            String createTableSQL = 
                "CREATE TABLE IF NOT EXISTS transactions ("
                + "transid VARCHAR(45) PRIMARY KEY, "
                + "username VARCHAR(60), "
                + "time DATETIME, "
                + "amount DECIMAL(10,2))";
            try (PreparedStatement ps = conn.prepareStatement(createTableSQL)) {
                ps.executeUpdate();
            }

            // Insert test data
            String insertSQL = "INSERT INTO transactions VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
				ps.setString(1, "123");
				ps.setString(2, "example@example.com");
				Date now = new Date(5);
				ps.setDate(3, now);
				ps.setDouble(4, 1.99);
				ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void clearTestDatabase() {
        try (Connection conn = DBUtil.provideConnection()) {
            String dropTableSQL = "DROP TABLE IF EXISTS transactions";
            try (PreparedStatement ps = conn.prepareStatement(dropTableSQL)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @org.junit.Test
    public void getUserIdTest() {
    	String transId = "123";
    	String result = transService.getUserId(transId);
    	assertEquals("example@example.com", result);
    	clearTestDatabase();
    	result = transService.getUserId(transId);
    	assertNotEquals("example@example.com", result);
    }
}

