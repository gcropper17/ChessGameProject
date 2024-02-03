package ui;

import Models.Game;
import Models.User;
import Requests.*;
import Results.*;
import chess.*;
import ui.exception.ResponseException;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ui.EscapeSequences.*;


public class ChessClient {
    private String userName = null;
    private final ServerFacade server;
    private final String serverUrl;
    private WebSocketFacade ws;
    private NotificationHandler notificationHandler;
    private State state = State.SIGNED_OUT;
    private String authToken;
    Map<Integer, String> listGamesMap = new HashMap<>();
    private Game currentGame;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "join", "watch" -> joinGame(params);
                case "logout" -> logoutGame();
                case "quit" -> "quit";
                case "move" -> makeMove(params);

                default -> help();
            };
        } catch (ResponseException | IOException ex) {
            return ex.getMessage();
        }
    }

    private String logoutGame() throws ResponseException {
        LogoutRequest request = new LogoutRequest(authToken);
        LogoutResult result = server.logoutUser(request);
        state = State.SIGNED_OUT;
        return String.format("Goodbye");
    }

    private String register(String... params) throws ResponseException{
        if (params.length == 3) {

            User user = new User();
            user.setUsername(params[0]);
            user.setPassword(params[1]);
            user.setEmail(params[2]);
            RegisterResult result = server.registerUser(user);
            authToken = result.getAuthToken();
            state = State.SIGNED_IN;
            ListGamesRequest request = new ListGamesRequest(authToken);
            Collection<Game> games;
            ListGamesResult listResult = server.listGames(request);
            games = listResult.getGames();
            int j = 1;
            for (Game game : games) {
                listGamesMap.put(j,game.getGameID());
                j++;
            }
            return String.format("You signed in as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <yourname> <yourpassword> <youremail>");
    }

    private String login(String... params) throws ResponseException{
        if (params.length == 2) {
            LoginResult loginResult;
            userName = params[0];
            User user = new User();
            user.setUsername(userName);
            user.setPassword(params[1]);
            loginResult = server.loginUser(user);
            authToken = loginResult.getAuthToken();
            state = State.SIGNED_IN;
            ListGamesRequest request = new ListGamesRequest(authToken);
            Collection<Game> games;
            ListGamesResult result = server.listGames(request);
            games = result.getGames();
            int j = 1;
            for (Game game : games) {
                listGamesMap.put(j,game.getGameID());
                j++;
            }
            return String.format("You signed in as %s.", userName);
        }
        throw new ResponseException(400, "Expected: <yourname> <yourpassword>");
    }

    private String createGame(String... params) throws ResponseException{
        if (params.length == 1) {
            CreateGameRequest request = new CreateGameRequest(params[0]);
            Game game = new Game();
            game.setGameName(params[0]);
            server.createNewGame(game, authToken);
            ListGamesRequest listRequest = new ListGamesRequest(authToken);
            Collection<Game> games;
            ListGamesResult result = server.listGames(listRequest);
            games = result.getGames();
            int j = 1;
            for (Game game1 : games) {
                listGamesMap.put(j,game1.getGameID());
                j++;
            }
            return String.format("New game %s created", game.getGameName());
        }
        throw new ResponseException(400, "Expected: <gameName>");


    }

    private String listGames(String... params) throws  ResponseException {
        if (listGamesMap.size() == 0) {
            return "No games found";
        }
        if (params.length == 0) {
            ListGamesRequest request = new ListGamesRequest(authToken);
            Collection<Game> games;
            ListGamesResult result = server.listGames(request);
            games = result.getGames();
            int j = 1;
            StringBuilder sb = new StringBuilder();
            int i = 0;
            for (Game game : games) {
                sb.append(SET_BG_COLOR_BLACK);
                sb.append("Game: ").append(i+1).append('\n');
                sb.append("GameID: ").append(game.getGameID()).append('\n');
                sb.append("Game Name: ").append(game.getGameName()).append('\n');
                sb.append("White Username: ");
                if (game.getWhiteUsername() != null) {
                    sb.append(game.getWhiteUsername()).append('\n');
                } else {sb.append('\n');}
                sb.append("Black Username: ");
                if (game.getBlackUsername() != null) {
                    sb.append(game.getBlackUsername()).append('\n');
                } else {sb.append('\n');}
                sb.append(whiteBoardDrawer(game)).append('\n');
                sb.append(SET_BG_COLOR_BLACK);
                sb.append("------------------------------");
                if (i != games.size()) {
                    sb.append('\n');
                }
                i++;
            }
            return String.format(sb.toString());
        }
        throw new ResponseException(400, "Fail");

    }

    private String joinGame(String... params) throws ResponseException, IOException {
        if (listGamesMap.size() == 0) {
            return "Game not found";
        }
        int key = Integer.parseInt(params[0]);
        params[0] = listGamesMap.get(key);
        StringBuilder sb = new StringBuilder();
        JoinGameRequest request = new JoinGameRequest();
        ChessBoardImpl board = new ChessBoardImpl();
        board.resetBoard();
        Game game = new Game();
        ChessGame newGame = new ChessGameImpl();
        newGame.setBoard(board);
        game.setGame(newGame);
        if (params.length == 2) {
            request.setAuthToken(authToken);
            request.setPlayerColor(params[1]);
            listGamesMap.get(params[0]);
            request.setGameID(params[0]);
            server.join(request);
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.joinGame(authToken, request.getGameID(), request.getPlayerColor());
            request.setAuthToken(authToken);
            if (params[1].equalsIgnoreCase("white")) {
                sb.append(whiteBoardDrawer(game));
            } else {
                sb.append(blackBoardDrawer(game));
            }
            sb.append("game join successful");
            state = State.PLAYING;
            currentGame = game;
            return String.format(sb.toString());
        } else if (params.length == 1) {
            request.setGameID(params[0]);
            request.setAuthToken(authToken);
            JoinGameResult result = server.join(request);
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.joinGame(authToken, request.getGameID(), request.getPlayerColor());
            sb.append(whiteBoardDrawer(game));
            return String.format(sb.toString());
        }
         else {
            System.out.println("Incorrect move format");
        }
        throw new ResponseException(400, "Fail");
    }

    public String help() {
        if (state == State.SIGNED_OUT) {
            return """
                    
                    - register <USERNAME> <PASSWORD> <EMAIL> - to create account 
                    - login <USERNAME> <PASSWORD> - to play chess
                    - quit - playing chess
                    - help - to display possible commands
                    """;
        } else if (state == State.PLAYING) {
            return """
                    -move <CURRENTPOSTION> <NEWPOSITION> - to move piece
                    """;
        }
        return """
                - Help
                - Create <name>
                - List
                - Join <ID> [WHITE|BLACK|<empty>]
                - Watch <ID> 
                - Logout
                - Quit
                """;
    }

    private StringBuilder whiteBoardDrawer(Game game) {
        ChessPositionImpl position = new ChessPositionImpl();
        ChessPiece piece;
        position.setColumn(0);
        position.setRow(0);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_BG_COLOR_DARK_GREY);
        stringBuilder.append("    a  b  c  d  e  f  g  h    ").append(SET_BG_COLOR_BLACK).append("\n");
        for (int i = 7; i >= 0; i--) {
            stringBuilder.append(SET_BG_COLOR_DARK_GREY);
            stringBuilder.append(" ").append(i+1).append(" ");

            for (int j = 0; j < 8; j++) {
                drawPieces(game, position, stringBuilder, i, j);
            }
            stringBuilder.append(SET_BG_COLOR_DARK_GREY).append(SET_TEXT_COLOR_BLUE);
            stringBuilder.append(" ").append(i+1).append(" ").append(SET_BG_COLOR_BLACK).append("\n");


        }
        stringBuilder.append(SET_BG_COLOR_DARK_GREY).append("    a  b  c  d  e  f  g  h    ").append(SET_BG_COLOR_BLACK).append("\n");


        return stringBuilder;
    }

    private StringBuilder blackBoardDrawer(Game game) {
        ChessPositionImpl position = new ChessPositionImpl();
        ChessPiece piece;
        position.setColumn(0);
        position.setRow(0);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SET_BG_COLOR_DARK_GREY);
        stringBuilder.append("    h  g  f  e  d  c  b  a    ").append(SET_BG_COLOR_BLACK).append("\n");
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(SET_BG_COLOR_DARK_GREY);
            stringBuilder.append(" ").append(i+1).append(" ");

            for (int j = 7; j >= 0; j--) {
                drawPieces(game, position, stringBuilder, i, j);
            }
            stringBuilder.append(SET_BG_COLOR_DARK_GREY).append(SET_TEXT_COLOR_BLUE);
            stringBuilder.append(" ").append(i+1).append(" ").append(SET_BG_COLOR_BLACK).append("\n");
        }
        stringBuilder.append(SET_BG_COLOR_DARK_GREY).append("    h  g  f  e  d  c  b  a    ").append(SET_BG_COLOR_BLACK).append("\n");

        return stringBuilder;
    }

    private void drawPieces(Game game, ChessPositionImpl position, StringBuilder stringBuilder, int i, int j) {
        ChessPiece piece;
        if (((i == 0 || i == 2 || i == 4 || i == 6) && (j == 0 || j == 2 || j == 4 || j == 6)) || ((i == 1 || i == 3 || i == 5 || i == 7) && (j == 1 || j == 3 || j == 5 || j == 7))) {
            stringBuilder.append(SET_BG_COLOR_BLACK);
        } else {
            stringBuilder.append(SET_BG_COLOR_LIGHT_GREY);
        }
        position.setRow(i);
        position.setColumn(j);
        if (game.getGame().getBoard().getPiece(position) == null) {
            stringBuilder.append("   ");
        }  else {
            piece = game.getGame().getBoard().getPiece(position);
            if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    stringBuilder.append(SET_TEXT_COLOR_BLUE).append(" R ");
                } else {
                    stringBuilder.append(SET_TEXT_COLOR_GREEN).append(" R ");

                }
            } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    stringBuilder.append(SET_TEXT_COLOR_BLUE).append(" N ");
                } else {
                    stringBuilder.append(SET_TEXT_COLOR_GREEN).append(" N ");

                }
            } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    stringBuilder.append(SET_TEXT_COLOR_BLUE).append(" B ");
                } else {
                    stringBuilder.append(SET_TEXT_COLOR_GREEN).append(" B ");
                }
            }  else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    stringBuilder.append(SET_TEXT_COLOR_BLUE).append(" Q ");
                } else {
                    stringBuilder.append(SET_TEXT_COLOR_GREEN).append(" Q ");

                }
            }  else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    stringBuilder.append(SET_TEXT_COLOR_BLUE).append(" K ");
                } else {
                    stringBuilder.append(SET_TEXT_COLOR_GREEN).append(" K ");

                }
            }  else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    stringBuilder.append(SET_TEXT_COLOR_BLUE).append(" P ");
                } else {
                    stringBuilder.append(SET_TEXT_COLOR_GREEN).append(" P ");
                }
            }
        }
    }

    private String makeMove(String...params) throws ResponseException {
        if ((params.length == 3) && state==State.PLAYING) {
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            //ws.makeMove();
        }
        int i = 0;
        currentGame.getGameID();

        return "hello";

    }

}
