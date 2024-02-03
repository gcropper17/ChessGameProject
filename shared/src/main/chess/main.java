package chess;

public class main {
    public static void main(String[]args) {
        ChessGame newGame = new ChessGameImpl();
        ChessPosition position = new ChessPositionImpl();
        ChessPosition position2 = new ChessPositionImpl();
        ChessPosition position3 = new ChessPositionImpl();


        position.setRow(2);
        position.setColumn(1);
        position2.setRow(4);
        position2.setColumn(1);
        position3.setRow(4);
        position3.setColumn(3);
        ChessPiece queen = new Queen(ChessGame.TeamColor.BLACK);
        ChessPiece pawn = new Pawn(ChessGame.TeamColor.WHITE);
        ChessPiece pawn3 = new Pawn(ChessGame.TeamColor.BLACK);


        newGame.getBoard().addPiece(position, queen);
        newGame.getBoard().addPiece(position2, pawn);
        newGame.getBoard().addPiece(position3, pawn3);
        //position.setRow(1);
        //position.setColumn(0);
        //newGame.validMoves(position);
        queen.pieceMoves(newGame.getBoard(), position);
        System.out.println(newGame.getBoard().toString());
    }
}
