package test;

//import org.apache.catalina.ssi.ByteArrayServletOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.shashi.srv.FansMessage;
import com.shashi.utility.MailMessage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class FansMessageTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher rd;

    private FansMessage fansMessage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        fansMessage = new FansMessage();
    }

//    @Test
//    public void testServiceGet() throws ServletException, IOException {
//        // Arrange
//        when(request.getMethod()).thenReturn("GET");
//        when(request.getParameter("name")).thenReturn("John Doe");
//        when(request.getParameter("email")).thenReturn("john.doe@example.com");
//        when(request.getParameter("comments")).thenReturn("This is a test comment.");
//        when(request.getRequestDispatcher("index.jsp")).thenReturn(rd);
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(writer);
//        when(response.getOutputStream()).thenReturn(new ByteArrayServletOutputStream());
//
//        // Act
//        fansMessage.service(request, response);
//
//        // Assert
//        verify(response, times(2)).setContentType("text/html");
//        verify(rd).include(request, response);
//    }

    @Test
    public void testServicePost() throws ServletException, IOException {
        // Arrange
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("name")).thenReturn("John Doe");
        when(request.getParameter("email")).thenReturn("john.doe@example.com");
        when(request.getParameter("comments")).thenReturn("This is a test comment.");
        when(request.getRequestDispatcher("index.jsp")).thenReturn(rd);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Act
        fansMessage.service(request, response);

        // Assert
        verify(response, times(2)).setContentType("text/html");
        verify(rd).include(request, response);
    }

    @Test
    public void testSendMessageSuccess() {
        // Arrange
        String htmlTextMessage = "<html><body>Hello World!</body></html>";

        // Act
        String message = MailMessage.sendMessage("guest@gmail.com", "Test Message", htmlTextMessage);

        // Assert
        if ("FAILURE".equals(message)) {
            fail("Email sending failed");
        } else {
            assertEquals("SUCCESS", message);
        }
    }

    @Test
    public void testSendMessageFailure() {
        // Arrange
        String htmlTextMessage = "<html><body>Hello World!</body></html>";

        // Act
        String message = MailMessage.sendMessage("sdf", "Test", htmlTextMessage);

        // Assert
        assertNotEquals("SUCCESS", message);
    }
}
