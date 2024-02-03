package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessGameImpl implements ChessGame {
    TeamColor teamTurn;
    ChessBoardImpl board;
    ChessBoardImpl clonedBoard;
    Collection<ChessMove> validPieceMoves;
    int numberOfTurns = 0;

    public ChessGameImpl() {
        board = new ChessBoardImpl();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
        validPieceMoves = new ArrayList<>();
    }
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }



    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        clonedBoard = board.deepCopyBoard();
        validPieceMoves.clear();
        Collection<ChessMove> currentPieceMoves = new ArrayList<>();
        TeamColor color;
        if (clonedBoard == null) {
            clonedBoard = board.deepCopyBoard();
        }
        if (clonedBoard.getPiece(startPosition) != null) {
            color = clonedBoard.getPiece(startPosition).getTeamColor();
            if (clonedBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.QUEEN) {
                ChessPiece queen = new Queen(color);
                currentPieceMoves.addAll(queen.pieceMoves(clonedBoard, startPosition));
            } else if ((clonedBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.BISHOP) ) {
                ChessPiece bishop = new Bishop(color);
                currentPieceMoves.addAll(bishop.pieceMoves(clonedBoard, startPosition));
            } else if ((clonedBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KNIGHT)) {
                ChessPiece knight = new Knight(color);
                currentPieceMoves.addAll(knight.pieceMoves(clonedBoard, startPosition));
            } else if ((clonedBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.ROOK)) {
                ChessPiece rook = new Rook(color);
                currentPieceMoves.addAll(rook.pieceMoves(clonedBoard, startPosition));
            } else if ((clonedBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN) ) {
                ChessPiece pawn = new Pawn(color);
                currentPieceMoves.addAll(pawn.pieceMoves(clonedBoard, startPosition));
            } else if ((clonedBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING) ) {
                ChessPiece king = new King(color);
                currentPieceMoves.addAll(king.pieceMoves(clonedBoard, startPosition));
            }
            for (ChessMove move : currentPieceMoves) {
                clonedBoard = board.deepCopyBoard();
                clonedBoard.addPiece(move.getEndPosition(), clonedBoard.getPiece(startPosition));
                clonedBoard.addPiece(move.getStartPosition(), null);

                if (!hypoIsInCheck(color)) {
                    validPieceMoves.add(move);
                }
            }

        }
        return validPieceMoves;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves = validMoves(move.getStartPosition());
        Collection<ChessMove> currentPieceMoves = new ArrayList<>();
        TeamColor color;

        if ((move.getEndPosition().getRow() >= 8) || (move.getEndPosition().getRow() < 0)
                || (move.getEndPosition().getColumn() <= -1) || (move.getEndPosition().getColumn() >=8)) {
            throw new InvalidMoveException();
        }
        if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN) {
            color = board.getPiece(move.getStartPosition()).getTeamColor();

            ChessPiece pawn = new Pawn(color);
            currentPieceMoves.addAll(pawn.pieceMoves(board, move.getStartPosition()));
        } else if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING) {
            color = board.getPiece(move.getStartPosition()).getTeamColor();
            ChessPiece king = new King(color);
            currentPieceMoves.addAll(king.pieceMoves(board, move.getStartPosition()));

        } else if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.QUEEN) {
            color = board.getPiece(move.getStartPosition()).getTeamColor();
            ChessPiece queen = new Queen(color);
            currentPieceMoves.addAll(queen.pieceMoves(board, move.getStartPosition()));

        } else if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.BISHOP) {
            color = board.getPiece(move.getStartPosition()).getTeamColor();
            ChessPiece bishop = new Bishop(color);
            currentPieceMoves.addAll(bishop.pieceMoves(board, move.getStartPosition()));
        } else if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KNIGHT) {
            color = board.getPiece(move.getStartPosition()).getTeamColor();
            ChessPiece knight = new Knight(color);
            currentPieceMoves.addAll(knight.pieceMoves(board, move.getStartPosition()));
        } else if (board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.ROOK) {
            color = board.getPiece(move.getStartPosition()).getTeamColor();
            ChessPiece rook = new Rook(color);
            currentPieceMoves.addAll(rook.pieceMoves(board, move.getStartPosition()));
        }

        if (teamTurn == TeamColor.WHITE) {
            if (validMoves.contains(move) && currentPieceMoves.contains(move) &&
                    (board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.WHITE)) {
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                board.addPiece(move.getStartPosition(), null);
                if ((board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.PAWN) && move.getEndPosition().getRow() == 7) {
                    promotePawn(TeamColor.WHITE, move);
                }
                numberOfTurns++;
                teamTurn = TeamColor.BLACK;
            } else {
                throw new InvalidMoveException("Invalid move");
            }
        } else {

            if (validMoves.contains(move) && currentPieceMoves.contains(move)) {
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                board.addPiece(move.getStartPosition(), null);
                numberOfTurns++;
                teamTurn = TeamColor.WHITE;
                if ((board.getPiece(move.getEndPosition()).getPieceType() == ChessPiece.PieceType.PAWN) && move.getEndPosition().getRow() == 0) {
                    promotePawn(TeamColor.BLACK, move);
                }

            } else {
                throw new InvalidMoveException("Invalid move");
            }
        }


    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = new ChessPositionImpl();
        ChessPosition currentPosition = new ChessPositionImpl();
        Collection<ChessMove> currentPieceMoves = new ArrayList<>();
        boolean correctKingExists = false;
        boolean check = false;
        if (clonedBoard == null) {
            clonedBoard = board.deepCopyBoard();
        }
        //locate king
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentPosition.setRow(i);
                currentPosition.setColumn(j);
                if (board.getPiece(currentPosition) != null) {
                    if ((board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KING) &&
                            board.getPiece(currentPosition).getTeamColor() == teamColor) {
                        kingPosition.setRow(i);
                        kingPosition.setColumn(j);
                        correctKingExists = true;
                        break;
                    }
                }
            }
        }
        if (!correctKingExists) {
            return false;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentPosition.setColumn(j);
                currentPosition.setRow(i);
                if (board.getPiece(currentPosition) != null) {
                    if ((board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.QUEEN) &&
                            (board.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece queen = new Queen(teamColor);
                        currentPieceMoves.addAll(queen.pieceMoves(board, currentPosition));
                    } else if ((board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.BISHOP) &&
                            (board.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece bishop = new Bishop(teamColor);
                        currentPieceMoves.addAll(bishop.pieceMoves(board, currentPosition));
                    } else if ((board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KNIGHT) &&
                            (board.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece knight = new Knight(teamColor);
                        currentPieceMoves.addAll(knight.pieceMoves(board, currentPosition));
                    } else if ((board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.ROOK) &&
                            (board.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece rook = new Rook(teamColor);
                        currentPieceMoves.addAll(rook.pieceMoves(board, currentPosition));
                    } else if ((board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.PAWN) &&
                            (board.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece pawn = new Pawn(teamColor);
                        currentPieceMoves.addAll(pawn.pieceMoves(board, currentPosition));
                    } else if ((board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KING) &&
                            (board.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece king = new King(teamColor);
                        currentPieceMoves.addAll(king.pieceMoves(board, currentPosition));
                    }
                }
                for (ChessMove move : currentPieceMoves) {
                    if (move.getEndPosition().equals(kingPosition)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean hypoIsInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = new ChessPositionImpl();
        ChessPosition currentPosition = new ChessPositionImpl();
        Collection<ChessMove> currentPieceMoves = new ArrayList<>();
        boolean correctKingExists = false;
        boolean check = false;
        if (clonedBoard == null) {
            clonedBoard = board.deepCopyBoard();
        }
        //locate king
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentPosition.setRow(i);
                currentPosition.setColumn(j);
                if (clonedBoard.getPiece(currentPosition) != null) {
                    if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KING) &&
                            clonedBoard.getPiece(currentPosition).getTeamColor() == teamColor) {
                        kingPosition.setRow(i);
                        kingPosition.setColumn(j);
                        correctKingExists = true;
                        break;
                    }
                }
            }
        }
        if (!correctKingExists) {
            return false;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentPosition.setColumn(j);
                currentPosition.setRow(i);
                if (clonedBoard.getPiece(currentPosition) != null) {
                    if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.QUEEN) &&
                            (clonedBoard.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece queen = new Queen(teamColor);
                        currentPieceMoves.addAll(queen.pieceMoves(clonedBoard, currentPosition));
                    } else if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.BISHOP) &&
                            (clonedBoard.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece bishop = new Bishop(teamColor);
                        currentPieceMoves.addAll(bishop.pieceMoves(clonedBoard, currentPosition));
                    } else if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KNIGHT) &&
                            (clonedBoard.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece knight = new Knight(teamColor);
                        currentPieceMoves.addAll(knight.pieceMoves(clonedBoard, currentPosition));
                    } else if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.ROOK) &&
                            (clonedBoard.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece rook = new Rook(teamColor);
                        currentPieceMoves.addAll(rook.pieceMoves(clonedBoard, currentPosition));
                    } else if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.PAWN) &&
                            (clonedBoard.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece pawn = new Pawn(teamColor);
                        currentPieceMoves.addAll(pawn.pieceMoves(clonedBoard, currentPosition));
                    } else if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KING) &&
                            (clonedBoard.getPiece(currentPosition).getTeamColor() != teamColor)) {
                        ChessPiece king = new King(teamColor);
                        currentPieceMoves.addAll(king.pieceMoves(clonedBoard, currentPosition));
                    }
                }
                for (ChessMove move : currentPieceMoves) {
                    if (move.getEndPosition().equals(kingPosition)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor)  {
        ChessPosition kingPosition = new ChessPositionImpl();
        ChessPosition currentPosition = new ChessPositionImpl();
        Collection<ChessMove> currentPieceMoves = new ArrayList<>();

        boolean correctKingExists = false;
        if (clonedBoard == null) {
            clonedBoard = board.deepCopyBoard();
        }
        boolean isInCheck = isInCheck(teamColor);
        if (!isInCheck) {
            return false;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentPosition.setRow(i);
                currentPosition.setColumn(j);
                if (clonedBoard.getPiece(currentPosition) != null) {
                    if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KING) &&
                            clonedBoard.getPiece(currentPosition).getTeamColor() == teamColor) {
                        kingPosition.setRow(i);
                        kingPosition.setColumn(j);
                        correctKingExists = true;
                        break;
                    }
                }
            }
        }
        if (!correctKingExists) {
            return false;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentPosition.setColumn(j);
                currentPosition.setRow(i);
                if (clonedBoard.getPiece(currentPosition) != null) {
                    if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.QUEEN) &&
                            (clonedBoard.getPiece(currentPosition).getTeamColor() == teamColor)) {
                        ChessPiece currentPiece = new Queen(teamColor);
                        currentPieceMoves.addAll(currentPiece.pieceMoves(clonedBoard, currentPosition));
                    } else if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.BISHOP) &&
                            (clonedBoard.getPiece(currentPosition).getTeamColor() == teamColor)) {
                        ChessPiece currentPiece = new Bishop(teamColor);
                        currentPieceMoves.addAll(currentPiece.pieceMoves(clonedBoard, currentPosition));
                    } else if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KNIGHT) &&
                            (clonedBoard.getPiece(currentPosition).getTeamColor() == teamColor)) {
                        ChessPiece currentPiece = new Knight(teamColor);
                        currentPieceMoves.addAll(currentPiece.pieceMoves(clonedBoard, currentPosition));
                    } else if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.ROOK) &&
                            (clonedBoard.getPiece(currentPosition).getTeamColor() == teamColor)) {
                        ChessPiece currentPiece = new Bishop(teamColor);
                        currentPieceMoves.addAll(currentPiece.pieceMoves(clonedBoard, currentPosition));
                    } else if ((clonedBoard.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.PAWN) &&
                            (clonedBoard.getPiece(currentPosition).getTeamColor() == teamColor)) {
                        ChessPiece currentPiece = new Bishop(teamColor);
                        currentPieceMoves.addAll(currentPiece.pieceMoves(clonedBoard, currentPosition));
                    }
                }
                for (ChessMove move : currentPieceMoves) {
                    clonedBoard.addPiece(move.getEndPosition(), clonedBoard.getPiece(move.getEndPosition()));
                    clonedBoard.addPiece(move.getStartPosition(), null);
                    if(!isInCheck(teamColor)) {

                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        validPieceMoves.clear();
        ChessPosition currentPosition = new ChessPositionImpl();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                currentPosition.setColumn(j);
                currentPosition.setRow(i);
                if (board.getPiece(currentPosition) != null && (board.getPiece(currentPosition).getTeamColor() == teamColor)) {
                    validMoves(currentPosition);
                }
            }
        }
        if (validPieceMoves.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = (ChessBoardImpl) board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public void promotePawn(TeamColor pawnColor, ChessMove move) {
        if (move.getPromotionPiece() == ChessPiece.PieceType.QUEEN) {
            ChessPiece queen = new Queen(pawnColor);
            board.addPiece(move.getEndPosition(), queen);
        } else if (move.getPromotionPiece() == ChessPiece.PieceType.ROOK) {
            ChessPiece rook = new Rook(pawnColor);
            board.addPiece(move.getEndPosition(), rook);
        } else if (move.getPromotionPiece() == ChessPiece.PieceType.KNIGHT) {
            ChessPiece knight = new Knight(pawnColor);
            board.addPiece(move.getEndPosition(), knight);
        } else if (move.getPromotionPiece() == ChessPiece.PieceType.BISHOP) {
            ChessPiece bishop = new Bishop(pawnColor);
            board.addPiece(move.getEndPosition(), bishop);
        }

    }
}
