package Handlers;
import java.util.Objects;

public class HandlerHelpers {
    public int setResStatus(String message) {
        int status = 200;
        if (Objects.equals(message, "Error: bad request")) {
            status = 400;
        } else if (Objects.equals(message, "Error: unauthorized")) {
            status = 401;
        } else if (Objects.equals(message, "Error: already taken")) {
            status = 403;
        } else if (Objects.equals(message, "Error: description")) {
            status = 500;
        } 
        return status;
    }
}