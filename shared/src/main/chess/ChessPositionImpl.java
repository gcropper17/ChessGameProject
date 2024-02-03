package chess;

import java.util.Objects;

public class ChessPositionImpl implements ChessPosition {
    int currentRow;
    int currentColumn;
    public ChessPositionImpl(){
        currentColumn = 0;
        currentRow = 0;
    }

    @Override
    public int getRow() {
        return currentRow;
    }

    @Override
    public int getColumn() {
        return currentColumn;
    }

    @Override
    public void setRow(int row) {
        currentRow = row;
    }

    public void setColumn(int column) {
        currentColumn = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImpl position = (ChessPositionImpl) o;
        return currentRow == position.currentRow && currentColumn == position.currentColumn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentRow, currentColumn);
    }


}

