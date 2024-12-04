package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.shashi.service.impl.OrderServiceImpl;
import com.shashi.srv.OrderServlet;

public class OrderServeletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher rd;

    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private PrintWriter pw;

    private StringWriter stringWriter;

    private OrderServlet servlet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        servlet = new OrderServlet();
        stringWriter = new StringWriter();
    }

    @Test
    public void testDoGet_PaymentSuccess() throws ServletException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        OrderServlet servlet = new OrderServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        // Set up request and response mocks
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("user");
        when(session.getAttribute("password")).thenReturn("pass");
        when(request.getParameter("amount")).thenReturn("10.99");
        when(orderService.paymentSuccess(anyString(), anyDouble())).thenReturn("Payment successful");
        PrintWriter pw = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(pw);
        RequestDispatcher rd = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("orderDetails.jsp")).thenReturn(rd);
        doNothing().when(rd).include(request, response);

        Method doGetMethod = OrderServlet.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);

        try {
            doGetMethod.invoke(servlet, request, response);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ServletException) {
                throw (ServletException) cause;
            } else if (cause instanceof IOException) {
                throw (IOException) cause;
            } else {
                throw new RuntimeException(cause);
            }
        }
        verify(pw).println(anyString());
    }
}