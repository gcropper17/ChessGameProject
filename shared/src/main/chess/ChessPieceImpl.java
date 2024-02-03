package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessPieceImpl implements ChessPiece {
    PieceType pieceType;
    ChessGame.TeamColor teamColor;
    int numberOfMoves;
    int numOfRookMoves;

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    public ChessGame.TeamColor setTeamColor(ChessGame.TeamColor teamColor1) {
        teamColor = teamColor1;
        return teamColor;
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    public PieceType setPieceType(PieceType piece) {
        pieceType = piece;
        return pieceType;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove> possibleMoves = new ArrayList<ChessMove>();
        ChessPiece piece = board.getPiece(startPosition);
        //ChessGame.TeamColor pawnColor = piece.getTeamColor();
        board.getPiece(startPosition);
        if (piece != null) {
            if (piece.getPieceType() == PieceType.QUEEN) {
                possibleMoves = possibleQueenMoves(startPosition, possibleMoves, board);
            }
            if (piece.getPieceType() == PieceType.BISHOP) {
                possibleMoves = possibleBishopMoves(startPosition, possibleMoves, board);
            }
            if (piece.getPieceType() == PieceType.ROOK) {
                possibleMoves = possibleRookMoves(startPosition, possibleMoves, board);
            }
            if (piece.getPieceType() == PieceType.PAWN) {
                possibleMoves = possiblePawnMoves(startPosition, possibleMoves, board);
            }
            if (piece.getPieceType() == PieceType.KNIGHT) {
                possibleMoves = possibleKnightMoves(startPosition, possibleMoves, board);
            }
            if (piece.getPieceType() == PieceType.KING) {
                possibleMoves = possibleKingMoves(startPosition, possibleMoves, board);
            }
        }

        return possibleMoves;
    }

    Collection<ChessMove> possibleQueenMoves(ChessPosition startPosition, Collection<ChessMove> queenMoves, ChessBoard currentBoard) {
        ChessMove currentQueenMove;
        /*---------------------Up and Down moves---------------------*/
        for (int i = startPosition.getRow() + 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setRow(i);
            endPosition.setColumn(startPosition.getColumn());
            currentQueenMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    queenMoves.add(currentQueenMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            queenMoves.add(currentQueenMove);

        }
        if (startPosition.getRow() != 0) {
            for (int j = startPosition.getRow() - 1; 0 <= j; j--) {
                ChessPosition endPosition = new ChessPositionImpl();
                endPosition.setRow(j);
                endPosition.setColumn(startPosition.getColumn());
                currentQueenMove = new ChessMoveImpl(endPosition, startPosition, null);
                if (currentBoard.getPiece(endPosition) != null) {
                    if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                        queenMoves.add(currentQueenMove);
                        break;
                    }
                }
                if (currentBoard.getPiece(endPosition) != null) {
                    if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                        break;
                    }
                }
                queenMoves.add(currentQueenMove);
            }
        }

        /*---------------------side to side move---------------------*/
        for (int i = startPosition.getColumn() + 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setRow(startPosition.getRow());
            endPosition.setColumn(i);
            currentQueenMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    queenMoves.add(currentQueenMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            queenMoves.add(currentQueenMove);
        }
        if (startPosition.getColumn() != 0) {
            for (int j = startPosition.getColumn() - 1; 0 <= j; j--) {
                ChessPosition endPosition = new ChessPositionImpl();
                endPosition.setRow(startPosition.getRow());
                endPosition.setColumn(j);
                currentQueenMove = new ChessMoveImpl(endPosition, startPosition, null);
                if (currentBoard.getPiece(endPosition) != null) {
                    if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                        queenMoves.add(currentQueenMove);
                        break;
                    }
                }
                if (currentBoard.getPiece(endPosition) != null) {
                    if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                        break;
                    }
                }
                queenMoves.add(currentQueenMove);
            }
        }
        /*---------------------Down Right diagonal---------------------*/
        int j = 1;
        for (int i = startPosition.getColumn() + j; i < 8; i++) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setColumn(startPosition.getColumn() + j);
            endPosition.setRow(startPosition.getRow() + j);
            if (endPosition.getColumn() >= 8) {
                break;
            }

            if (endPosition.getRow() >= 8) {
                break;
            }

            //Passed the passoffs for this class, but the code below was added because it continued to fail when testing game

            currentQueenMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    queenMoves.add(currentQueenMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            queenMoves.add(currentQueenMove);
            j++;
        }
        /*---------------------Up Left Diagonal---------------------*/
        j = 1;
        for (int i = startPosition.getColumn(); i > 0; i--) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setColumn(startPosition.getColumn() - j);
            if (endPosition.getColumn() < 0) {
                break;
            }

            endPosition.setRow(startPosition.getRow() - j);
            if (endPosition.getRow() < 0) {
                break;
            }
            currentQueenMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    queenMoves.add(currentQueenMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            queenMoves.add(currentQueenMove);
            j++;
        }
        //Up Right Diagonal
        j = 1;
        for (int i = startPosition.getColumn() + j; i < 8; i++) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setColumn(startPosition.getColumn() + j);
            if (endPosition.getColumn() >= 8) {
                break;
            }

            endPosition.setRow(startPosition.getRow() - j);
            if (endPosition.getRow() < 0) {
                break;
            }
            currentQueenMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    queenMoves.add(currentQueenMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            queenMoves.add(currentQueenMove);
            j++;
        }
        /*---------------------Down Left---------------------*/
        j = 1;
        for (int i = startPosition.getColumn() ; i >= 0; i--) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setColumn(startPosition.getColumn() - j);
            if (endPosition.getColumn() < 0) {
                break;
            }

            endPosition.setRow(startPosition.getRow() + j);
            if (endPosition.getRow() >= 8) {
                break;
            }
            currentQueenMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    queenMoves.add(currentQueenMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            queenMoves.add(currentQueenMove);
            j++;
        }
        return queenMoves;
    }

    Collection<ChessMove> possibleBishopMoves(ChessPosition startPosition, Collection<ChessMove> bishopMoves, ChessBoard currentBoard) {
        ChessMove currentBishopMove;
        int j = 1;
        for (int i = startPosition.getColumn(); i < 8; i++) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setColumn(startPosition.getColumn() + j);
            if (endPosition.getColumn() >= 8) {
                break;
            }

            endPosition.setRow(startPosition.getRow() + j);
            if (endPosition.getRow() >= 8) {
                break;
            }
            currentBishopMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    bishopMoves.add(currentBishopMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            bishopMoves.add(currentBishopMove);
            j++;
        }
        /*---------------------Up Left Diagonal---------------------*/
        j = 1;
        for (int i = startPosition.getColumn(); i > 0; i--) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setColumn(startPosition.getColumn() - j);
            if (endPosition.getColumn() < 0) {
                break;
            }

            endPosition.setRow(startPosition.getRow() - j);
            if (endPosition.getRow() < 0) {
                break;
            }
            currentBishopMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    bishopMoves.add(currentBishopMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            bishopMoves.add(currentBishopMove);
            j++;
        }
        /*---------------------Up Right Diagonal---------------------*/
        j = 1;
        for (int i = startPosition.getColumn(); i < 8; i++) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setColumn(startPosition.getColumn() + j);
            if (endPosition.getColumn() >= 8) {
                break;
            }

            endPosition.setRow(startPosition.getRow() - j);
            if (endPosition.getRow() < 0) {
                break;
            }
            currentBishopMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    bishopMoves.add(currentBishopMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            bishopMoves.add(currentBishopMove);
            j++;
        }
        /*---------------------Down Left---------------------*/
        j = 1;
        for (int i = startPosition.getColumn(); i >= 0; i--) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setColumn(startPosition.getColumn() - j);
            if (endPosition.getColumn() < 0 || endPosition.getColumn() >= 8) {
                break;
            }
            endPosition.setRow(startPosition.getRow() + j);
            if (endPosition.getRow() < 0 || endPosition.getRow() >= 8) {
                break;
            }
            currentBishopMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    bishopMoves.add(currentBishopMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            bishopMoves.add(currentBishopMove);
            j++;
        }
        return bishopMoves;
    }

    Collection<ChessMove> possibleRookMoves(ChessPosition startPosition, Collection<ChessMove> rookMoves, ChessBoard currentBoard) {
        ChessMove currentRookMove;
        /*---------------------Up and Down moves---------------------*/
        for (int i = startPosition.getRow() + 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setRow(i);
            endPosition.setColumn(startPosition.getColumn());
            currentRookMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    rookMoves.add(currentRookMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            rookMoves.add(currentRookMove);

        }
        if (startPosition.getRow() != 0) {
            for (int j = startPosition.getRow() - 1; 0 <= j; j--) {
                ChessPosition endPosition = new ChessPositionImpl();
                endPosition.setRow(j);
                endPosition.setColumn(startPosition.getColumn());
                currentRookMove = new ChessMoveImpl(endPosition, startPosition, null);
                if (currentBoard.getPiece(endPosition) != null) {
                    if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                        rookMoves.add(currentRookMove);
                        break;
                    }
                }
                if (currentBoard.getPiece(endPosition) != null) {
                    if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                        break;
                    }
                }
                rookMoves.add(currentRookMove);
            }
        }

        /*---------------------side to side move---------------------*/
        for (int i = startPosition.getColumn() + 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPositionImpl();
            endPosition.setRow(startPosition.getRow());
            endPosition.setColumn(i);
            currentRookMove = new ChessMoveImpl(endPosition, startPosition, null);
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                    rookMoves.add(currentRookMove);
                    break;
                }
            }
            if (currentBoard.getPiece(endPosition) != null) {
                if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    break;
                }
            }
            rookMoves.add(currentRookMove);
        }
        if (startPosition.getColumn() != 0) {
            for (int j = startPosition.getColumn() - 1; 0 <= j; j--) {
                ChessPosition endPosition = new ChessPositionImpl();
                endPosition.setRow(startPosition.getRow());
                endPosition.setColumn(j);
                currentRookMove = new ChessMoveImpl(endPosition, startPosition, null);
                if (currentBoard.getPiece(endPosition) != null) {
                    if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                        rookMoves.add(currentRookMove);
                        break;
                    }
                }
                if (currentBoard.getPiece(endPosition) != null) {
                    if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                        break;
                    }
                }
                rookMoves.add(currentRookMove);
            }
        }
        return rookMoves;
    }

    Collection<ChessMove> possiblePawnMoves(ChessPosition startPosition, Collection<ChessMove> pawnMoves, ChessBoard currentBoard) {
        Collection<ChessMove> promotionMoves = new ArrayList<>();
        ChessMove currentPawnMove;
        ChessPosition endPosition = new ChessPositionImpl();
        ChessPosition endPosition10 = new ChessPositionImpl();
        /*-------------------------Black Pawn Moves-------------------------*/
        if (currentBoard.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            if (startPosition.getRow() == 6) {
                endPosition.setRow(startPosition.getRow() - 2);
                endPosition.setColumn(startPosition.getColumn());
                endPosition10.setRow(startPosition.getRow() - 1);
                endPosition10.setColumn(startPosition.getColumn());
                currentPawnMove = new ChessMoveImpl(endPosition, startPosition, null);

                if (currentBoard.getPiece(endPosition) != null) {
                    if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                        pawnMoves.add(currentPawnMove);
                    }
                    if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    }
                } else if (currentBoard.getPiece(endPosition10) == null) {
                    pawnMoves.add(currentPawnMove);
                }
            }

            ChessPosition endPosition1 = new ChessPositionImpl();
            endPosition1.setRow(startPosition.getRow() - 1);
            endPosition1.setColumn(startPosition.getColumn());
            currentPawnMove = new ChessMoveImpl(endPosition1, startPosition, null);
            if (currentBoard.getPiece(endPosition1) != null) {}
            else {
                if (endPosition1.getRow() == 0) {
                    ChessMove newPiece = new ChessMoveImpl(endPosition1, startPosition, PieceType.ROOK);
                    ChessMove newPiece2 = new ChessMoveImpl(endPosition1, startPosition, PieceType.QUEEN);
                    ChessMove newPiece3 = new ChessMoveImpl(endPosition1, startPosition, PieceType.BISHOP);
                    ChessMove newPiece4 = new ChessMoveImpl(endPosition1, startPosition, PieceType.KNIGHT);
                    promotionMoves.add(newPiece);
                    promotionMoves.add(newPiece2);
                    promotionMoves.add(newPiece3);
                    promotionMoves.add(newPiece4);
                } else {
                    pawnMoves.add(currentPawnMove);

                }
            }


            ChessPosition endPosition3 = new ChessPositionImpl();
            endPosition3.setRow(startPosition.getRow() - 1);
            endPosition3.setColumn(startPosition.getColumn() + 1);
            if (endPosition3.getColumn() <= 7) {
                currentPawnMove = new ChessMoveImpl(endPosition3, startPosition, null);
                if (currentBoard.getPiece(endPosition3) != null) {
                    if (currentBoard.getPiece(endPosition3).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                        if (endPosition1.getRow() == 0) {
                            ChessMove newPiece = new ChessMoveImpl(endPosition3, startPosition, PieceType.ROOK);
                            ChessMove newPiece2 = new ChessMoveImpl(endPosition3, startPosition, PieceType.QUEEN);
                            ChessMove newPiece3 = new ChessMoveImpl(endPosition3, startPosition, PieceType.BISHOP);
                            ChessMove newPiece4 = new ChessMoveImpl(endPosition3, startPosition, PieceType.KNIGHT);
                            promotionMoves.add(newPiece);
                            promotionMoves.add(newPiece2);
                            promotionMoves.add(newPiece3);
                            promotionMoves.add(newPiece4);
                        } else {
                            pawnMoves.add(currentPawnMove);
                        }
                    }
                    if (currentBoard.getPiece(endPosition3).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    }
                }
            }
            ChessPosition endPosition9 = new ChessPositionImpl();
            endPosition9.setRow(startPosition.getRow() - 1);
            endPosition9.setColumn(startPosition.getColumn() - 1);
            if (endPosition9.getColumn() <= 7 && endPosition9.getColumn() >= 0) {
                currentPawnMove = new ChessMoveImpl(endPosition9, startPosition, null);

                if (currentBoard.getPiece(endPosition9) != null) {
                    if (currentBoard.getPiece(endPosition9).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                        if (endPosition1.getRow() == 0) {
                            ChessMove newPiece = new ChessMoveImpl(endPosition9, startPosition, PieceType.ROOK);
                            ChessMove newPiece2 = new ChessMoveImpl(endPosition9, startPosition, PieceType.QUEEN);
                            ChessMove newPiece3 = new ChessMoveImpl(endPosition9, startPosition, PieceType.BISHOP);
                            ChessMove newPiece4 = new ChessMoveImpl(endPosition9, startPosition, PieceType.KNIGHT);
                            promotionMoves.add(newPiece);
                            promotionMoves.add(newPiece2);
                            promotionMoves.add(newPiece3);
                            promotionMoves.add(newPiece4);
                        } else {
                            pawnMoves.add(currentPawnMove);
                        }
                    }
                    if (currentBoard.getPiece(endPosition9).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    }
                }
            }

        } else {//White Piece Moves
            if (startPosition.getRow() == 1) {
                endPosition.setRow(startPosition.getRow() + 2);
                endPosition.setColumn(startPosition.getColumn());
                currentPawnMove = new ChessMoveImpl(endPosition, startPosition, null);
                if (currentBoard.getPiece(endPosition) != null) {}
                else {
                    pawnMoves.add(currentPawnMove);
                }

            }

            ChessPosition endPosition1 = new ChessPositionImpl();
            endPosition1.setRow(startPosition.getRow() + 1);
            endPosition1.setColumn(startPosition.getColumn());
            currentPawnMove = new ChessMoveImpl(endPosition1, startPosition, null);
            if (currentBoard.getPiece(endPosition1) != null) {}
            else {
                pawnMoves.add(currentPawnMove);
            }
            if (endPosition1.getRow() == 7) {
                ChessMove newPiece = new ChessMoveImpl(endPosition1, startPosition, PieceType.ROOK);
                ChessMove newPiece2 = new ChessMoveImpl(endPosition1, startPosition, PieceType.QUEEN);
                ChessMove newPiece3 = new ChessMoveImpl(endPosition1, startPosition, PieceType.BISHOP);
                ChessMove newPiece4 = new ChessMoveImpl(endPosition1, startPosition, PieceType.KNIGHT);
                promotionMoves.add(newPiece);
                promotionMoves.add(newPiece2);
                promotionMoves.add(newPiece3);
                promotionMoves.add(newPiece4);
            }
            ChessPosition endPosition2 = new ChessPositionImpl();
            endPosition2.setRow(startPosition.getRow() + 1);
            endPosition2.setColumn(startPosition.getColumn() + 1);
            if (endPosition2.getColumn() <= 7) {
                currentPawnMove = new ChessMoveImpl(endPosition2, startPosition, null);
                if (currentBoard.getPiece(endPosition2) != null) {
                    if (currentBoard.getPiece(endPosition2).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                        if (endPosition1.getRow() == 7) {
                            ChessMove newPiece = new ChessMoveImpl(endPosition1, startPosition, PieceType.ROOK);
                            ChessMove newPiece2 = new ChessMoveImpl(endPosition1, startPosition, PieceType.QUEEN);
                            ChessMove newPiece3 = new ChessMoveImpl(endPosition1, startPosition, PieceType.BISHOP);
                            ChessMove newPiece4 = new ChessMoveImpl(endPosition1, startPosition, PieceType.KNIGHT);
                            promotionMoves.add(newPiece);
                            promotionMoves.add(newPiece2);
                            promotionMoves.add(newPiece3);
                            promotionMoves.add(newPiece4);
                        } else {
                            pawnMoves.add(currentPawnMove);

                        }
                    }
                    if (currentBoard.getPiece(endPosition2).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {}
                }

            }
            ChessPosition endPosition3 = new ChessPositionImpl();
            endPosition3.setRow(startPosition.getRow() + 1);
            endPosition3.setColumn(startPosition.getColumn() - 1);
            if (endPosition3.getColumn() >= 0) {
                currentPawnMove = new ChessMoveImpl(endPosition3, startPosition, null);
                if (currentBoard.getPiece(endPosition3) != null) {
                    if (currentBoard.getPiece(endPosition3).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                        if (endPosition1.getRow() == 7) {
                            ChessMove newPiece = new ChessMoveImpl(endPosition3, startPosition, PieceType.ROOK);
                            ChessMove newPiece2 = new ChessMoveImpl(endPosition3, startPosition, PieceType.QUEEN);
                            ChessMove newPiece3 = new ChessMoveImpl(endPosition3, startPosition, PieceType.BISHOP);
                            ChessMove newPiece4 = new ChessMoveImpl(endPosition3, startPosition, PieceType.KNIGHT);
                            promotionMoves.add(newPiece);
                            promotionMoves.add(newPiece2);
                            promotionMoves.add(newPiece3);
                            promotionMoves.add(newPiece4);
                        } else {
                            pawnMoves.add(currentPawnMove);
                        }
                    }
                    if (currentBoard.getPiece(endPosition3).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {}
                }

            }
        }
        if (!promotionMoves.isEmpty()) {
            return promotionMoves;
        }

        return pawnMoves;
    }

    Collection<ChessMove> possibleKnightMoves(ChessPosition startPosition, Collection<ChessMove> knightMoves, ChessBoard currentBoard) {
        ChessMove currentKnightMove;
        int[] rowOffsets = {1,  1, 2, 2, -1, -1, -2, -2};
        int[] colOffsets = {2, -2, 1,-1,  2, -2, 1, -1};
        for (int i = 0; i < 8; i++) {
            int endRow = startPosition.getRow() + rowOffsets[i];
            int endColumn = startPosition.getColumn() + colOffsets[i];
            if (endRow >= 0 && endRow < 8 && endColumn >= 0 && endColumn < 8) {
                ChessPosition endPosition = new ChessPositionImpl();
                endPosition.setRow(endRow);
                endPosition.setColumn(endColumn);
                currentKnightMove = new ChessMoveImpl(endPosition, startPosition, null);
                if (currentBoard.getPiece(endPosition) != null) {
                    if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                        knightMoves.add(currentKnightMove);
                    }
                    if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    }
                } else {
                    knightMoves.add(currentKnightMove);
                }

            }
        }

        return knightMoves;
    }

    Collection<ChessMove> possibleKingMoves(ChessPosition startPosition, Collection<ChessMove> kingMoves, ChessBoard currentBoard){
        ChessMove currentKingMove;
        int[] rowOffsets = {1,  1, 1, 0, 0, -1, -1, -1};
        int[] colOffsets = {1, 0, -1, 1, -1, 1, 0, -1};
        for (int i = 0; i < 8; i++) {
            int endRow = startPosition.getRow() + rowOffsets[i];
            int endColumn = startPosition.getColumn() + colOffsets[i];
            if (endRow >= 0 && endRow < 8 && endColumn >= 0 && endColumn < 8) {
                ChessPosition endPosition = new ChessPositionImpl();
                endPosition.setRow(endRow);
                endPosition.setColumn(endColumn);
                currentKingMove = new ChessMoveImpl(endPosition, startPosition, null);
                if (currentBoard.getPiece(endPosition) != null) {
                    if (currentBoard.getPiece(endPosition).getTeamColor() != currentBoard.getPiece(startPosition).getTeamColor()) {
                        kingMoves.add(currentKingMove);
                    }
                    if (currentBoard.getPiece(endPosition).getTeamColor() == currentBoard.getPiece(startPosition).getTeamColor()) {
                    }
                } else {
                    kingMoves.add(currentKingMove);
                }

            }
        }
        return kingMoves;
    }

    public static ChessPiece getNewPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type){
        return switch (type){
            case KING -> new King(pieceColor);
            case QUEEN -> new Queen(pieceColor);
            case BISHOP -> new Bishop(pieceColor);
            case KNIGHT -> new Knight(pieceColor);
            case ROOK -> new Rook(pieceColor);
            case PAWN -> new Pawn(pieceColor);
        };
    }

}

class King extends ChessPieceImpl {
    King(ChessGame.TeamColor color){
        pieceType = PieceType.KING;
        teamColor = color;
        numberOfMoves = 0;

    }
}

class Queen extends ChessPieceImpl {
    Queen(ChessGame.TeamColor color) {
        pieceType = PieceType.QUEEN;
        teamColor = color;
        numberOfMoves = 0;
    }
}

class Bishop extends ChessPieceImpl {
    public Bishop(ChessGame.TeamColor color) {
        pieceType = PieceType.BISHOP;
        teamColor = color;
        numberOfMoves = 0;
    }
}

class Knight extends ChessPieceImpl {
    public Knight(ChessGame.TeamColor color) {
        pieceType = PieceType.KNIGHT;
        teamColor = color;
        numberOfMoves = 0;
    }
}

class Rook extends ChessPieceImpl {
    Rook (ChessGame.TeamColor color) {
        pieceType = PieceType.ROOK;
        teamColor = color;
        numberOfMoves = 0;
    }
}

class Pawn extends ChessPieceImpl {
    Pawn(ChessGame.TeamColor color){
        pieceType = PieceType.PAWN;
        teamColor = color;
        numberOfMoves = 0;
    }
}

