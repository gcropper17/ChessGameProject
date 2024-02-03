package chess;

public class ChessBoardImpl implements ChessBoard {
    ChessPieceImpl[][] chessBoard;

    public ChessBoardImpl () {
        chessBoard = new ChessPieceImpl[8][8];
    }

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {

        chessBoard[position.getRow()][position.getColumn()] = (ChessPieceImpl) piece;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return chessBoard[position.getRow()][position.getColumn()];
    }
    public ChessPiece[][] getBoard() {
        return chessBoard;
    }
    @Override
    public void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoard[i][j] = null;
            }
        }

    ChessPieceImpl whiteKing = new King(ChessGame.TeamColor.WHITE);
        chessBoard[0][4] = whiteKing;
        ChessPieceImpl whitePawn1 = new Pawn(ChessGame.TeamColor.WHITE);
        chessBoard[1][0] = whitePawn1;
        ChessPieceImpl whitePawn2 = new Pawn(ChessGame.TeamColor.WHITE);
        chessBoard[1][1] = whitePawn2;
        ChessPieceImpl whitePawn3 = new Pawn(ChessGame.TeamColor.WHITE);
        chessBoard[1][2] = whitePawn3;
        ChessPieceImpl whitePawn4 = new Pawn(ChessGame.TeamColor.WHITE);
        chessBoard[1][3] = whitePawn4;
        ChessPieceImpl whitePawn5 = new Pawn(ChessGame.TeamColor.WHITE);
        chessBoard[1][4] = whitePawn5;
        ChessPieceImpl whitePawn6 = new Pawn(ChessGame.TeamColor.WHITE);
        chessBoard[1][5] = whitePawn6;
        ChessPieceImpl whitePawn7 = new Pawn(ChessGame.TeamColor.WHITE);
        chessBoard[1][6] = whitePawn7;
        ChessPieceImpl whitePawn8 = new Pawn(ChessGame.TeamColor.WHITE);
        chessBoard[1][7] = whitePawn8;
        ChessPieceImpl whiteRook1 = new Rook(ChessGame.TeamColor.WHITE);
        chessBoard[0][0] = whiteRook1;
        ChessPieceImpl whiteRook2 = new Rook(ChessGame.TeamColor.WHITE);
        chessBoard[0][7] = whiteRook2;
        ChessPieceImpl whiteKnight1 = new Knight(ChessGame.TeamColor.WHITE);
        chessBoard[0][1] = whiteKnight1;
        ChessPieceImpl whiteKnight2 = new Knight(ChessGame.TeamColor.WHITE);
        chessBoard[0][6] = whiteKnight2;
        ChessPieceImpl whiteBishop1 = new Bishop(ChessGame.TeamColor.WHITE);
        chessBoard[0][2] = whiteBishop1;
        ChessPieceImpl whiteBishop2 = new Bishop(ChessGame.TeamColor.WHITE);
        chessBoard[0][5] = whiteBishop2;
        ChessPieceImpl whiteQueen = new Queen(ChessGame.TeamColor.WHITE);
        chessBoard[0][3] = whiteQueen;

        ChessPieceImpl blackKing = new King(ChessGame.TeamColor.BLACK);
        chessBoard[7][4] = blackKing;
        ChessPieceImpl blackPawn1 = new Pawn(ChessGame.TeamColor.BLACK);
        chessBoard[6][0] = blackPawn1;
        ChessPieceImpl blackPawn2 = new Pawn(ChessGame.TeamColor.BLACK);
        chessBoard[6][1] = blackPawn2;
        ChessPieceImpl blackPawn3 = new Pawn(ChessGame.TeamColor.BLACK);
        chessBoard[6][2] = blackPawn3;
        ChessPieceImpl blackPawn4 = new Pawn(ChessGame.TeamColor.BLACK);
        chessBoard[6][3] = blackPawn4;
        ChessPieceImpl blackPawn5 = new Pawn(ChessGame.TeamColor.BLACK);
        chessBoard[6][4] = blackPawn5;
        ChessPieceImpl blackPawn6 = new Pawn(ChessGame.TeamColor.BLACK);
        chessBoard[6][5] = blackPawn6;
        ChessPieceImpl blackPawn7 = new Pawn(ChessGame.TeamColor.BLACK);
        chessBoard[6][6] = blackPawn7;
        ChessPieceImpl blackPawn8 = new Pawn(ChessGame.TeamColor.BLACK);
        chessBoard[6][7] = blackPawn8;
        ChessPieceImpl blackRook1 = new Rook(ChessGame.TeamColor.BLACK);
        chessBoard[7][0] = blackRook1;
        ChessPieceImpl blackRook2 = new Rook(ChessGame.TeamColor.BLACK);
        chessBoard[7][7] = blackRook2;
        ChessPieceImpl blackKnight1 = new Knight(ChessGame.TeamColor.BLACK);
        chessBoard[7][1] = blackKnight1;
        ChessPieceImpl blackKnight2 = new Knight(ChessGame.TeamColor.BLACK);
        chessBoard[7][6] = blackKnight2;
        ChessPieceImpl blackBishop1 = new Bishop(ChessGame.TeamColor.BLACK);
        chessBoard[7][2] = blackBishop1;
        ChessPieceImpl blackBishop2 = new Bishop(ChessGame.TeamColor.BLACK);
        chessBoard[7][5] = blackBishop2;
        ChessPieceImpl blackQueen = new Queen(ChessGame.TeamColor.BLACK);
        chessBoard[7][3] = blackQueen;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = chessBoard[i][j];
                sb.append("|");
                if (piece == null) {
                    sb.append(" ");
                } else {
                    if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            sb.append('p');
                        } else {
                            sb.append('P');
                        }
                    } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            sb.append('r');
                        } else {
                            sb.append('R');
                        }
                    } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            sb.append('k');
                        } else {
                            sb.append('K');
                        }
                    } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            sb.append('q');
                        } else {
                            sb.append('Q');
                        }
                    } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            sb.append('n');
                        } else {
                            sb.append('N');
                        }
                    } else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            sb.append('b');
                        } else {
                            sb.append('B');
                        }
                    }
                }
            }
            sb.append("|\n");
        }
        return sb.toString();


    }

    public ChessBoardImpl deepCopyBoard() {
        ChessBoardImpl duplicateBoard = new ChessBoardImpl();
        for (int i = 0; i < 8; i++) {
            for (int j= 0; j < 8; j++) {
                duplicateBoard.chessBoard[i][j] = chessBoard[i][j];
            }
        }

        return duplicateBoard;
    }
}
