package test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.shashi.beans.UserBean;
import com.shashi.service.UserService;
import com.shashi.service.impl.UserServiceImpl;
import com.shashi.utility.DBUtil;

public class UserServiceTest {

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl();
        initializeTestDatabase();
    }

    @After
    public void tearDown() {
        clearTestDatabase();
    }

    /**
     * Initializes the test database by creating the `user` table and adding sample data.
     */
    private void initializeTestDatabase() {
        try (Connection conn = DBUtil.provideConnection()) {
            // Create table
            String createTableSQL = 
                "CREATE TABLE IF NOT EXISTS user ("
                + "email VARCHAR(50) PRIMARY KEY, "
                + "name VARCHAR(100), "
                + "mobile BIGINT, "
                + "address VARCHAR(200), "
                + "pincode INT, "
                + "password VARCHAR(100))";
            try (PreparedStatement ps = conn.prepareStatement(createTableSQL)) {
                ps.executeUpdate();
            }

            // Insert test data
            String insertSQL = "INSERT INTO user VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setString(1, "test@example.com");
                ps.setString(2, "John Doe");
                ps.setLong(3, 1234567890L);
                ps.setString(4, "123 Test St");
                ps.setInt(5, 12345);
                ps.setString(6, "password123");
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the test database by dropping the `user` table.
     */
    private void clearTestDatabase() {
        try (Connection conn = DBUtil.provideConnection()) {
            String dropTableSQL = "DROP TABLE IF EXISTS user";
            try (PreparedStatement ps = conn.prepareStatement(dropTableSQL)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegisterUser() {
        // Test successful registration
        String result = userService.registerUser("Jane Doe", 9876543210L, "jane@example.com", "456 Test Ln", 54321, "password456");
        assertEquals("User Registered Successfully!", result);

        // Test registration of already registered email
        result = userService.registerUser("Jane Doe", 9876543210L, "jane@example.com", "456 Test Ln", 54321, "password456");
        assertEquals("Email Id Already Registered!", result);
    }

    @Test
    public void testIsRegistered() {
        // Test registered and unregistered email checks
        assertTrue(userService.isRegistered("test@example.com"));
        assertFalse(userService.isRegistered("notregistered@example.com"));
    }

    @Test
    public void testIsValidCredential() {
        // Test valid credentials
        assertEquals("valid", userService.isValidCredential("test@example.com", "password123"));

        // Test invalid credentials
        assertEquals("Login Denied! Incorrect Username or Password", userService.isValidCredential("test@example.com", "wrongpassword"));
        assertEquals("Login Denied! Incorrect Username or Password", userService.isValidCredential("notregistered@example.com", "password123"));
    }

    @Test
    public void testGetUserDetails() {
        // Test valid user details retrieval
        UserBean user = userService.getUserDetails("test@example.com", "password123");
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals(Long.valueOf(1234567890L), user.getMobile());
        assertEquals("123 Test St", user.getAddress());
        assertEquals(12345, user.getPinCode());
        assertEquals("password123", user.getPassword());

        // Test invalid user details retrieval
        user = userService.getUserDetails("test@example.com", "wrongpassword");
        assertNull(user);
    }

    @Test
    public void testGetFName() {
        // Test extracting first name
        assertEquals("John", userService.getFName("test@example.com"));

        // Test for non-existent user
        assertEquals("", userService.getFName("notregistered@example.com"));
    }

    @Test
    public void testGetUserAddr() {
        // Test retrieving user address
        assertEquals("123 Test St", userService.getUserAddr("test@example.com"));

        // Test for non-existent user
        assertEquals("", userService.getUserAddr("notregistered@example.com"));
    }
}

