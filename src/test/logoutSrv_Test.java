package test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;

import com.shashi.srv.LogoutSrv;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class logoutSrv_Test {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher rd;

    private LogoutSrv logoutSrv;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        logoutSrv = new LogoutSrv();
    }

    @Test
    public void testDoGet() throws ServletException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");
        when(request.getRequestDispatcher("login.jsp?message=Successfully Logged Out!")).thenReturn(rd);

        Method doGetMethod = LogoutSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        // Act
        doGetMethod.invoke(logoutSrv, request, response);

        // Assert
        verify(session).setAttribute("username", null);
        verify(session).setAttribute("password", null);
        verify(session).setAttribute("usertype", null);
        verify(session).setAttribute("userdata", null);
        verify(rd).forward(request, response);
    }
    

    @Test
    public void testDoPost() throws ServletException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testUser");
        when(request.getRequestDispatcher("login.jsp?message=Successfully Logged Out!")).thenReturn(rd);

        Method doPostMethod = LogoutSrv.class.getDeclaredMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
        doPostMethod.setAccessible(true);

        // Act
        doPostMethod.invoke(logoutSrv, request, response);

        // Assert
        verify(session).setAttribute("username", null);
        verify(session).setAttribute("password", null);
        verify(session).setAttribute("usertype", null);
        verify(session).setAttribute("userdata", null);
        verify(rd).forward(request, response);
    }
    @Test(expected = MockitoException.class)
    public void testDoGetThrowsServletException() throws Throwable {
        // Arrange
        doThrow(new ServletException()).when(request).getSession();

        Method doGetMethod = LogoutSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        // Act
        try {
            doGetMethod.invoke(logoutSrv, request, response);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }
    @Test
    public void testDoGetThrowsIOException() throws ServletException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Arrange
        doThrow(new RuntimeException(new IOException())).when(response).setContentType(anyString());

        Method doGetMethod = LogoutSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        // Act and Assert
        try {
            doGetMethod.invoke(logoutSrv, request, response);
            fail("Expected IOException");
        } catch (InvocationTargetException e) {
            assertTrue(e.getCause().getCause() instanceof IOException);
        }
    }
}