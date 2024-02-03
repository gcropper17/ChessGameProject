package Models;
import chess.ChessGame;
import chess.ChessGameImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
/**
 * Class contains all details that go into the chess game.
 */
public class Game {
    /**
     * Games integer ID.
     */
    private String gameID;
    /**
     * Username of white player.
     */
    private String whiteUsername;
    /**
     * Username of black user.
     */
    private String blackUsername;
    /**
     * Game that is played.
     */
    private ChessGameImpl game;
    /**
     * String name of the game.
     */
    private String gameName;

    private Collection<User> viewers = new ArrayList<>();

    public Collection<User> getViewers() {
        return viewers;
    }

    public void addViewer(User user) {
        viewers.add(user);
    }

    public String getGameID() {return gameID;}

    public void generateGameID() {
        Random random = new Random();
        int gameID = random.nextInt(9000) + 1000;
        this.gameID = String.format("%d", gameID);
    }

    public String getWhiteUsername() {return whiteUsername;}

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void setWhiteUsername(String whiteUsername) {this.whiteUsername = whiteUsername;}

    public String getBlackUsername() {return blackUsername;}

    public void setBlackUsername(String blackUsername) {this.blackUsername = blackUsername;}

    public ChessGame getGame() {return game;}

    public void setGame(ChessGame game) {
        this.game = (ChessGameImpl) game;}

    public String getGameName() {return gameName;}

    public void setGameName(String gameName) {this.gameName = gameName;}


}