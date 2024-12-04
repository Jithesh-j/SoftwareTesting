package test;



import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.shashi.beans.UserBean;
import com.shashi.service.impl.UserServiceImpl;
import com.shashi.srv.RegisterSrv;
import static org.mockito.Mockito.lenient; 
public class RegisterSrv_Test {



	@Test
	public void testRegisterUserPasswordMismatch() throws ServletException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    RequestDispatcher rd = mock(RequestDispatcher.class);

	    when(request.getParameter("username")).thenReturn("testUser");
	    when(request.getParameter("mobile")).thenReturn("1234567890");
	    when(request.getParameter("email")).thenReturn("test@example.com");
	    when(request.getParameter("address")).thenReturn("Test Address");
	    when(request.getParameter("pincode")).thenReturn("123456");
	    when(request.getParameter("password")).thenReturn("password");
	    when(request.getParameter("confirmPassword")).thenReturn("wrongPassword");
	    when(request.getRequestDispatcher("register.jsp?message=Password not matching!")).thenReturn(rd);

	    Method doGetMethod = RegisterSrv.class.getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
	    doGetMethod.setAccessible(true);

	    doGetMethod.invoke(new RegisterSrv(), request, response);

	    verify(rd).forward(request, response);
	}

}
