package test;



import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.shashi.beans.ProductBean;
import com.shashi.service.impl.ProductServiceImpl;
import com.shashi.srv.UpdateProductSrv;

public class UpdateToCartSrv_Test {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ProductServiceImpl productService;

    @InjectMocks
    private UpdateProductSrv updateProductSrv;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoGet_SessionExpired() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usertype")).thenReturn(null);

        Method doGetMethod = UpdateProductSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(updateProductSrv, request, response);

        verify(response).sendRedirect("login.jsp?message=Access Denied, Login As Admin!!");
    }

    @Test
    public void testDoGet_InvalidUserType() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usertype")).thenReturn("user");

        Method doGetMethod = UpdateProductSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(updateProductSrv, request, response);

        verify(response).sendRedirect("login.jsp?message=Access Denied, Login As Admin!!");
    }

    @Test
    public void testDoGet_UpdateProduct() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usertype")).thenReturn("admin");
        when(session.getAttribute("username")).thenReturn("admin@gmail.com");
        when(session.getAttribute("password")).thenReturn("admin");

        when(request.getParameter("pid")).thenReturn("P20230423084144");
        when(request.getParameter("name")).thenReturn("MOTOROLA G32 Mobile");
        when(request.getParameter("type")).thenReturn("mobile");
        when(request.getParameter("info")).thenReturn("With a mind-blowing 16.51 cm (6.5) FHD+ Ultra-wide display and a refresh rate of 90 Hz, this phone makes it possible to multitask while being entertained.");
        when(request.getParameter("price")).thenReturn("11999.00");
        when(request.getParameter("quantity")).thenReturn("10");

        ProductBean product = new ProductBean();
        product.setProdId("P20230423084144");
        product.setProdName("MOTOROLA G32 Mobile");
        product.setProdType("mobile");
        product.setProdInfo("With a mind-blowing 16.51 cm (6.5) FHD+ Ultra-wide display and a refresh rate of 90 Hz, this phone makes it possible to multitask while being entertained.");
        product.setProdPrice(11999.00);
        product.setProdQuantity(10);

        when(productService.updateProductWithoutImage("P20230423084144", product)).thenReturn("Product updated successfully");

        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(toString())).thenReturn(requestDispatcher);

        Method doGetMethod = UpdateProductSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
        doGetMethod.setAccessible(true);
        doGetMethod.invoke(updateProductSrv, request, response);

        verify(requestDispatcher).forward(any(ServletRequest.class), any(ServletResponse.class));
    }
}
