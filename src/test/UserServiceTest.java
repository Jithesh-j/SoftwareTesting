package test;


import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        // Initialize the user service
        userService = new UserServiceImpl();
        initializeTestDatabase();
    }



    /**
     * This method inserts a test user into the database if not already present
     * (we assume that the table already exists with the required columns).
     */
    private void initializeTestDatabase() {
        try (Connection conn = DBUtil.provideConnection()) {
            // Insert test data (user) only if the user doesn't already exist
            String insertSQL = "INSERT INTO user (email, name, mobile, address, pincode, password) "
                               + "SELECT ?, ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM user WHERE email = ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setString(1, "test@example.com");
                ps.setString(2, "John Doe");
                ps.setLong(3, 1234567890L);
                ps.setString(4, "123 Test St");
                ps.setInt(5, 12345);
                ps.setString(6, "password123");
                ps.setString(7, "test@example.com");
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Error initializing test data: " + e.getMessage());
        }
    }



    @Test
    public void testRegisterUserExists() {
        // Test registration of an already registered email
        String result = userService.registerUser("John Doe", 1234567890L, "test@example.com", "123 Test St", 12345, "password123");
        assertEquals("Email Id Already Registered!", result);
    }



    @Test
    public void testIsRegistered() {
        // Test registered and unregistered email checks
        assertTrue(userService.isRegistered("test@example.com"));
        assertFalse(userService.isRegistered("notregistered@example.com"));
        

        assertFalse(userService.isRegistered("test@example.com"));
    }

    @Test
    public void testIsValidCredential() {
        // Test valid credentials
        assertEquals("valid", userService.isValidCredential("test@example.com", "password123"));

        // Test invalid credentials
        assertEquals("Login Denied! Incorrect Username or Password", userService.isValidCredential("test@example.com", "wrongpassword"));
        assertEquals("Login Denied! Incorrect Username or Password", userService.isValidCredential("notregistered@example.com", "password123"));
        

        assertEquals("valid", userService.isValidCredential("test@example.com", "password123"));
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

        // Test invalid user details retrieval (wrong password)
        user = userService.getUserDetails("test@example.com", "wrongpassword");
        assertNull(user);
        

        user = userService.getUserDetails("test@example.com", "wrongpassword");
        assertNull(user);
    }

    @Test
    public void testGetFName() {
        // Test extracting first name from email
        assertEquals("John", userService.getFName("test@example.com"));

        // Test for non-existent user
        assertEquals("", userService.getFName("notregistered@example.com"));

        assertEquals("", userService.getFName("notregistered@example.com"));
    }

    @Test
    public void testGetUserAddr() {
        // Test retrieving user address
        assertEquals("123 Test St", userService.getUserAddr("test@example.com"));

        // Test for non-existent user
        assertEquals("", userService.getUserAddr("notregistered@example.com"));
        

        assertEquals("", userService.getUserAddr("notregistered@example.com"));
    }
}
