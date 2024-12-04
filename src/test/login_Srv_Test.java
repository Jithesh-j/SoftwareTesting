package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;



import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.shashi.service.impl.UserServiceImpl;
import com.shashi.srv.LoginSrv;

public class login_Srv_Test {

	@Test
	public void testValidAdminLogin() throws ServletException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    RequestDispatcher rd = mock(RequestDispatcher.class);
	    HttpSession session = mock(HttpSession.class);

	    when(request.getParameter("username")).thenReturn("admin@gmail.com");
	    when(request.getParameter("password")).thenReturn("admin");
	    when(request.getParameter("usertype")).thenReturn("admin");
	    when(request.getRequestDispatcher("adminViewProduct.jsp")).thenReturn(rd);
	    when(request.getSession()).thenReturn(session);

	    Method doGetMethod = LoginSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
	    doGetMethod.setAccessible(true);

	    doGetMethod.invoke(new LoginSrv(), request, response);

	    verify(rd).forward(request, response);
	}
	@Test
	public void testValidCustomerLogin() throws ServletException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    RequestDispatcher rd = mock(RequestDispatcher.class);
	    HttpSession session = mock(HttpSession.class);
	    UserServiceImpl udao = mock(UserServiceImpl.class);

	    when(request.getParameter("username")).thenReturn("guest@gmail.com");
	    when(request.getParameter("password")).thenReturn("guest");
	    when(request.getParameter("usertype")).thenReturn("guest");
	    when(udao.isValidCredential("guest@gmail.com", "customer")).thenReturn("valid");
	    when(request.getRequestDispatcher("userHome.jsp")).thenReturn(rd);
	    when(request.getSession()).thenReturn(session);

	    Method doGetMethod = LoginSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
	    doGetMethod.setAccessible(true);

	    doGetMethod.invoke(new LoginSrv(), request, response);

	    verify(rd).forward(request, response);
	}
}
