package test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.shashi.srv.RemoveProductSrv;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

import java.io.IOException;

public class RemoveProduct_Srv_Test extends RemoveProductSrv {

    @InjectMocks
    private RemoveProductSrv removeProductSrv;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoGet_AdminUser() throws ServletException, IOException {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usertype")).thenReturn("admin");
        when(session.getAttribute("username")).thenReturn("admin@gmail.com");
        when(session.getAttribute("password")).thenReturn("admin");
        when(request.getParameter("prodid")).thenReturn("1");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
//        when(requestDispatcher.forward(request, response)).thenReturn(null);

        // Act
        super.doGet(request, response);

        // Assert
        verify(request, times(1)).getSession();
        verify(session, times(3)).getAttribute(anyString());
        verify(request, times(1)).getParameter("prodid");
        verify(request, times(1)).getRequestDispatcher(anyString());
//        verify(requestDispatcher, times(1)).forward(request, response);
    }


    @Test
    public void testDoGet_ProductIdNull() throws ServletException, IOException {
        // Arrange
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("usertype")).thenReturn("admin");
        when(session.getAttribute("username")).thenReturn("adminUser");
        when(session.getAttribute("password")).thenReturn("password");
        when(request.getParameter("prodid")).thenReturn(null);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
//        when(requestDispatcher.forward(request, response)).thenReturn(null);

        // Act
        super.doGet(request, response);

        // Assert
        verify(request, times(1)).getSession();
        verify(session, times(3)).getAttribute(anyString());
        verify(request, times(1)).getParameter("prodid");
        verify(request, times(1)).getRequestDispatcher(anyString());
//        verify(requestDispatcher, times(1)).forward(request, response);
    }
}