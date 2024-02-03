package DataAccess;
import Models.Game;
import Models.User;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessBoardImpl;
import chess.ChessGameImpl;
import chess.ChessPieceImpl;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.DataAccessException;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * Data access object class that provides methods to manage the Games in the DB.
 */
public class GameDAO {
    /**
     * Collection of Games that will be returned in findAllGames method.
     */
    static Collection<Game> games;
    static Collection<User> viewers;
    static {
        games = new ArrayList<>();
        viewers = new ArrayList<>();
    }

    /**
     * Creates new game in DB.
     * @param game Game that will be created
     * @throws DataAccessException Thrown if gameId exists.
     */
    public void insertGame(Game game) throws DataAccessException, SQLException {
        if (game.getGameID() == null || game.getGameName() == null || game.getGame() == null) {
            throw new DataAccessException("Error: bad request");
        } else {
            var DbConnection = Database.getConnection();
            try {
                var preparedStatement = DbConnection.prepareStatement("INSERT INTO Games (GameName, gameID, game, whiteUsername, blackUsername) " +
                        "VALUES(?, ?, ?, ?, ?)");
                String GameName = game.getGameName();
                String gameID = game.getGameID();
                ChessGame sqlGame = game.getGame();
                String whiteUsername = game.getWhiteUsername();
                String blackUsername = game.getBlackUsername();
                Gson gson = new Gson();
                String jsonGame = gson.toJson(sqlGame);
                DbConnection.setCatalog("chess");

                preparedStatement.setString(1, GameName);
                preparedStatement.setString(2, gameID);
                preparedStatement.setString(3, jsonGame);
                preparedStatement.setString(4, whiteUsername);
                preparedStatement.setString(5, blackUsername);
                preparedStatement.executeUpdate();
            } catch (SQLException ignored) {}
            finally {
                Database.returnConnection(DbConnection);
            }
        }
        }

    /**
     * Finds specific game in DB.
     * @param gameID Game that is being searched for in DB.
     * @return Returns game if found.
     * @throws DataAccessException Thrown if game does not exist.
     */
    public Game findGame(String gameID) throws DataAccessException, SQLException {
        var DbConnection = Database.getConnection();
        try {
            String query = "SELECT * FROM Games WHERE gameID = ?";
            var preparedStatement = DbConnection.prepareStatement(query);
            preparedStatement.setString(1, gameID);
            var gameFind = preparedStatement.executeQuery();
            if (gameFind.next()) {
                String gameJSON = gameFind.getString(3);
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(ChessGame.class, new ChessGame.ChessGameAdapter());
                gsonBuilder.registerTypeAdapter(ChessPiece.class, new ChessPiece.ChessPieceAdapter());
                ChessGame gsonGame = gsonBuilder.create().fromJson(gameJSON, ChessGame.class);
                Game game = new Game();
                game.setGameName(gameFind.getString(1));
                game.setGameID(gameFind.getString(2));
                game.setGame(gsonGame);
                if (gameFind.getString(4) != null) {
                    game.setWhiteUsername(gameFind.getString(4));
                }
                if (gameFind.getString(5) != null){
                    game.setBlackUsername(gameFind.getString(5));
                }

                return game;

            } else {
                throw new DataAccessException("Error: bad request");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        } finally {
            Database.returnConnection(DbConnection);
        }
    }

    /**
     * Finds all games in DB.
     * @return Collection of games that will be set and returned in method.
     * @throws DataAccessException Thrown if no games exist.
     */
    public Collection<Game> findAllGames () throws DataAccessException, SQLException {
        games.clear();
        var DbConnection = Database.getConnection();
        try {
            String query = "SELECT * FROM Games";
            var preparedStatement = DbConnection.prepareStatement(query);
            var allGames = preparedStatement.executeQuery();
            while (allGames.next()) {
                Game game = new Game();
                game.setGameName(allGames.getString(1));
                game.setGameID(allGames.getString(2));

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(ChessPieceImpl.class, new ChessPiece.ChessPieceAdapter());
                gsonBuilder.registerTypeAdapter(ChessBoardImpl.class, new ChessBoard.ChessBoardAdapter());
                gsonBuilder.registerTypeAdapter(ChessGameImpl.class, new ChessGame.ChessGameAdapter());


                Gson gson = gsonBuilder.create();

                game.setGame(gson.fromJson(allGames.getString(3), ChessGameImpl.class));

                game.setWhiteUsername(allGames.getString(4));
                game.setBlackUsername(allGames.getString(5));
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Database.returnConnection(DbConnection);

        }
        return games;
    }

    /**
     * Claims a spot in a specific game in DB.
     * @param u user that is joining/claiming game.
     * @param game game that will be claimed.
     * @throws DataAccessException Thrown if game is full, username does not exist, or game does not exist.
     */
    public void claimSpot(User u, Game game, String color) throws DataAccessException, SQLException {
        var DbConnection = Database.getConnection();
        String userName = u.getUsername();
        color = color.toLowerCase();
        try {
            if (Objects.equals(color, "white") && game.getWhiteUsername() == null) {
                String query = "SELECT * FROM Games WHERE whiteUsername IS NULL";
                var preparedStatement1 = DbConnection.prepareStatement(query);
                var nullCheck = preparedStatement1.executeQuery();
                if (nullCheck.next()){
                    String possibleNullString = nullCheck.getString(4);
                    if (Objects.equals(possibleNullString, null)) {
                        String updateQuery = "UPDATE Games SET whiteUsername = ? WHERE gameID = ?";
                        var preparedStatement2 = DbConnection.prepareStatement(updateQuery);
                        preparedStatement2.setString(1, userName);
                        preparedStatement2.setString(2, game.getGameID());
                        preparedStatement2.executeUpdate();
                    }
                } else {
                        throw new DataAccessException("Error: already taken");
                }

            } else if (Objects.equals(color, "black") && game.getBlackUsername() == null) {
                String query = "SELECT * FROM Games WHERE blackUsername IS NULL";
                var preparedStatement3 = DbConnection.prepareStatement(query);
                var nullCheck = preparedStatement3.executeQuery();
                if (nullCheck.next()) {
                    String possibleNullString = nullCheck.getString(5);
                    if (Objects.equals(possibleNullString, null)) {
                        String updateQuery = "UPDATE Games SET blackUsername = ? WHERE gameID = ?";
                        var preparedStatement4 = DbConnection.prepareStatement(updateQuery);
                        preparedStatement4.setString(1, userName);
                        preparedStatement4.setString(2, game.getGameID());
                        preparedStatement4.executeUpdate();
                    }
                } else {
                    throw new DataAccessException("Error: already taken");
                }
            } else if (!Objects.equals(color, "black") && !Objects.equals(color, "WHITE")) {
                game.addViewer(u);
            }
            else {
                throw new DataAccessException("Error: already taken");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: unauthorized");
        } finally {
            Database.returnConnection(DbConnection);
        }
    }

    /**
     * All games removed from DB.
     */
    public static void clearAll() throws DataAccessException {
        var DbConnection = Database.getConnection();
        try {
            String query = "DELETE FROM Games";
            var preparedStatement = DbConnection.prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error: description");
        } finally {
            Database.returnConnection(DbConnection);
        }
    }
    public static void addViewer(User u) {
        viewers.add(u);
    }
}
