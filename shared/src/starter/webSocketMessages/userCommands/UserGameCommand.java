package webSocketMessages.userCommands;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {
    public String gameID;
    public final String playerColor;
    protected CommandType commandType;
    private final String authToken;

    public UserGameCommand(String authToken, String gameID, String color) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = color;
    }


    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }



    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }


    public String getGameID() {
        return gameID;
    }


    public String getPlayerColor() {
        return playerColor;
    }



    public String getAuthToken() {
        return authToken;
    }
}