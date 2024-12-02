package test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertTrue;
import com.shashi.service.impl.DemandServiceImpl;
import com.shashi.utility.DBUtil;

import org.junit.Before;
import org.mockito.*;

import java.sql.*;

public class DemandServiceTest {

    @InjectMocks
    private DemandServiceImpl demandService; // The class under test

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @org.junit.Test
    public void testAddProduct_ProductNotYetDemanded() throws SQLException {
        // Given
        String userId = "user123";
        String prodId = "prod456";
        int demandQty = 2;

        // Mock the static method using mockStatic
        try (MockedStatic<DBUtil> dbUtilMock = mockStatic(DBUtil.class)) {
            // Mock DBUtil to return the mocked connection
            dbUtilMock.when(DBUtil::provideConnection).thenReturn(connection);

            // Mock the prepareStatement to return the mocked preparedStatement
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

            // Mock the executeQuery method to return the mocked resultSet
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            // Simulate that the product demand doesn't exist
            when(resultSet.next()).thenReturn(false);

            // When
            boolean result = demandService.addProduct(userId, prodId, demandQty);

            // Then
            assertTrue(result); // The method should return true when the product is successfully added
            verify(preparedStatement, times(1)).executeQuery(); // Verify the query was executed
            verify(preparedStatement, times(1)).executeUpdate(); // The insert query should have been executed
        }
    }
}

