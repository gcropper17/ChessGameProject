package Results;
/**
 * ClearApp result object that will be passed back to user after determining if clearapp is possible.
 */
public class ClearAppResult {
    /**
     * message that will be sent depending on if the clear request failed or succeeded.
     */
    public String message;

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}
}