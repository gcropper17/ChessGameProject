package ui;

import com.sun.nio.sctp.HandlerResult;
import com.sun.nio.sctp.Notification;
import ui.websocket.NotificationHandler;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;


public class Repl implements NotificationHandler {
    ChessClient client;
    State state;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_BLUE + "Welcome to CS240 Chess. Sign up or log into start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        state = client.getState();
        System.out.print("\n" + "[" + state + "]" + " >>> " + SET_TEXT_COLOR_BLUE);
    }

    @Override
    public HandlerResult handleNotification(Notification notification, Object attachment) {
        return null;
    }

    @Override
    public void notify(ServerMessage message) {

    }
}
