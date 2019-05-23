package Chess;

import java.util.ArrayList;

public abstract class ChessPiece {
    protected Square square;
    protected Player player;
    protected Game g;

    public ChessPiece(Square square){
        this.square = square;
    }

    public Square getSquare(){
        return this.square;
    }

    public int getValue(){
        return getPieceType().getValue();
    }

    public Player getPlayer(){return this.player;}

    public abstract ArrayList<Move> getMoves();
    public abstract void markAttackedTiles(boolean a[][]);
    public abstract ChessPieceType getPieceType();
}
