package test;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.Failure;
import org.junit.runner.Result;

public class TestResultListner extends RunListener {
    @Override
    public void testRunFinished(Result result) throws Exception {
        // Prepare email content
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Test Report:\n")
                 .append("Total Tests: ").append(result.getRunCount()).append("\n")
                 .append("Failures: ").append(result.getFailureCount()).append("\n")
                 .append("Ignored: ").append(result.getIgnoreCount()).append("\n")
                 .append("Passed: ").append(result.getRunCount() - result.getFailureCount()).append("\n");

        if (!result.getFailures().isEmpty()) {
            emailBody.append("\nFailure Details:\n");
            for (Failure failure : result.getFailures()) {
                emailBody.append(failure.toString()).append("\n");
            }
        }

        // Send the email
        TesTEmail.sendEmail("jitheshjalapothu@gmail.com", "JUnit Test Report", emailBody.toString());
    }
}
