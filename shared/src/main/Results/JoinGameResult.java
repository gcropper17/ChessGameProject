package Results;
/**
 * Join Game Object that will be passed to user after determining if joining game is possible.
 */
public class JoinGameResult {
    /**
     * Message sent if request fails.
     */
    private String message;

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}
}