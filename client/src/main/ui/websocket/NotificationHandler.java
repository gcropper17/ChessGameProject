package ui.websocket;
import com.sun.nio.sctp.HandlerResult;
import com.sun.nio.sctp.Notification;
import webSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {
    HandlerResult handleNotification(Notification notification, Object attachment);

    void notify(ServerMessage message);
}
