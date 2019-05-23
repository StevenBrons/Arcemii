package Chess;

import java.util.ArrayList;

public class Empty extends ChessPiece {

    public Empty(Square square){super(square);}

    @Override
    public ArrayList<Move> getMoves() {
        return new ArrayList<Move>();
    }

    @Override
    public void markAttackedTiles(boolean[][] a) {

    }

    @Override
    public ChessPieceType getPieceType() {
        return ChessPieceType.Empty;
    }
}
