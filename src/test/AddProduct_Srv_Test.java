package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.shashi.service.impl.ProductServiceImpl;
import com.shashi.srv.AddProductSrv;

public class AddProduct_Srv_Test {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private Part part;

    @Mock
    private ProductServiceImpl productServiceImpl;

    private AddProductSrv servlet;

    @SuppressWarnings("deprecation")
	@Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        servlet = new AddProductSrv();
    }
    @Test
    public void testDoGet_AccessDenied() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usertype")).thenReturn(null);

        when(request.getParameter("name")).thenReturn("my_product");
        when(request.getParameter("type")).thenReturn("mobile");
        when(request.getParameter("info")).thenReturn("info");
        when(request.getParameter("price")).thenReturn("10.0");
        when(request.getParameter("quantity")).thenReturn("10");

        InputStream inputStream = new ByteArrayInputStream("image".getBytes());
        when(request.getPart("image")).thenReturn(part);
        when(part.getInputStream()).thenReturn(inputStream);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        Method doGetMethod = AddProductSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);

        verify(response).sendRedirect("login.jsp?message=Access Denied!");
    }

    @Test
    public void testDoGet_SessionExpired() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usertype")).thenReturn("admin");
        when(session.getAttribute("username")).thenReturn(null);

        when(request.getParameter("name")).thenReturn("my_product");
        when(request.getParameter("type")).thenReturn("mobile");
        when(request.getParameter("info")).thenReturn("info");
        when(request.getParameter("price")).thenReturn("10.0");
        when(request.getParameter("quantity")).thenReturn("10");

        InputStream inputStream = new ByteArrayInputStream("image".getBytes());
        when(request.getPart("image")).thenReturn(part);
        when(part.getInputStream()).thenReturn(inputStream);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        Method doGetMethod = AddProductSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);

        verify(response).sendRedirect("login.jsp?message=Session Expired, Login Again to Continue!");
    }

    @Test
    public void testDoGet_ProductRegistrationFailed() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usertype")).thenReturn("admin");
        when(session.getAttribute("username")).thenReturn("guest@gmail.com");
        when(session.getAttribute("password")).thenReturn("guest");

        when(request.getParameter("name")).thenReturn("my_guest_product");
        when(request.getParameter("type")).thenReturn("mobile");
        when(request.getParameter("info")).thenReturn("info");
        when(request.getParameter("price")).thenReturn("10.0");
        when(request.getParameter("quantity")).thenReturn("10");

        InputStream inputStream = new ByteArrayInputStream("image".getBytes());
        when(request.getPart("image")).thenReturn(part);
        when(part.getInputStream()).thenReturn(inputStream);

        when(productServiceImpl.addProduct(anyString(), anyString(), anyString(), anyDouble(), anyInt(), any(InputStream.class))).thenReturn("Product Registration Failed!");

        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        servlet = new AddProductSrv();

        Method doGetMethod = AddProductSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_ProductRegistrationSuccess() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usertype")).thenReturn("admin");
        when(session.getAttribute("username")).thenReturn("admin@gmail.com");
        when(session.getAttribute("password")).thenReturn("admin");

        when(request.getParameter("name")).thenReturn("my_admin");
        when(request.getParameter("type")).thenReturn("mobile");
        when(request.getParameter("info")).thenReturn("info");
        when(request.getParameter("price")).thenReturn("10.0");
        when(request.getParameter("quantity")).thenReturn("10");

        InputStream inputStream = new ByteArrayInputStream("image".getBytes());
        when(request.getPart("image")).thenReturn(part);
        when(part.getInputStream()).thenReturn(inputStream);

        when(productServiceImpl.addProduct(anyString(), anyString(), anyString(), anyDouble(), anyInt(), any(InputStream.class))).thenReturn("Product Registration Success!");

        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        servlet = new AddProductSrv();

        Method doGetMethod = AddProductSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(servlet, request, response);

        verify(requestDispatcher).forward(request, response);
    }
}