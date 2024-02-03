package Results;
/**
 * Logout result object that will be passed back to user after determining if logging out is possible.
 */
public class LogoutResult {
    /**
     * message that will be sent if logout is successful or not
     */
    private String message;

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}
}