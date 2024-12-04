package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.el.util.ReflectionUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import com.shashi.beans.ProductBean;
import com.shashi.service.impl.ProductServiceImpl;
import com.shashi.srv.AddtoCart;

public class AddtoCart_Serv_Test {

    private AddtoCart servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private PrintWriter writer;

    @Before
    public void setUp() throws IOException {
        servlet = new AddtoCart();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        writer = mock(PrintWriter.class);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    public void testDoGet_LoginCheckSuccessfull() throws Exception {
        // Arrange
        when(session.getAttribute("username")).thenReturn("test@example.com");
        when(session.getAttribute("password")).thenReturn("password123");
        when(session.getAttribute("usertype")).thenReturn("customer");

        String prodId = "P20230423084161";
        int pQty = 1;
        when(request.getParameter("pid")).thenReturn(prodId);
        when(request.getParameter("pqty")).thenReturn(String.valueOf(pQty));

        RequestDispatcher rd1 = mock(RequestDispatcher.class);
        RequestDispatcher rd2 = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("userHome.jsp")).thenReturn(rd1);
        when(request.getRequestDispatcher("cartDetails.jsp")).thenReturn(rd2);

        Method method = AddtoCart.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);

        // Act
        method.invoke(servlet, request, response);

        // Assert
        verify(response).setContentType("text/html");
    }
    @Test
    public void testDoGet_LoginCheckFailed() throws Exception {
        // Arrange
        when(session.getAttribute("username")).thenReturn(null);

        Method method = AddtoCart.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        method.setAccessible(true);

        // Act
        method.invoke(servlet, request, response);

        // Assert
        verify(response).sendRedirect("login.jsp?message=Session Expired, Login Again to Continue!");
    }

    


   
}