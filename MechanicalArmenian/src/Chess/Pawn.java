package Chess;

import java.util.ArrayList;

public class Pawn extends ChessPiece {

    public Pawn(Square square){
        super(square);
    }

    @Override
    public ArrayList<Move> getMoves() {
        ArrayList<Move> ret = new ArrayList<>();
        if(this.player==Player.White){
            if(g.getBoard()[square.getRow()+1][square.getCol()].getPieceType() == ChessPieceType.Empty){
                Move mv = new Move(new Square(square.getRow(), square.getCol()), new Square(square.getRow()+1, square.getCol()));
                Game gc = g.duplicate();
                gc.move(mv);
                if(gc.isValid()){
                    ret.add(mv);
                }
            }
        } else {
            if(g.getBoard()[square.getRow()-1][square.getCol()].getPieceType() == ChessPieceType.Empty){
                Move mv = new Move(new Square(square.getRow(), square.getCol()), new Square(square.getRow()-1, square.getCol()));
                Game gc = g.duplicate();
                gc.move(mv);
                if(gc.isValid()){
                    ret.add(mv);
                }
            }
        }
        return ret;
    }

    @Override
    public void markAttackedTiles(boolean[][] a) {
        if(this.player==Player.White){
            if(this.square.getCol()>0){
                a[this.square.getRow()+1][this.square.getCol()-1] = true;
            }
            if(this.square.getCol()<7){
                a[this.square.getRow()+1][this.square.getCol()+1] = true;
            }
        } else {
            if(this.square.getCol()>0){
                a[this.square.getRow()-1][this.square.getCol()-1] = true;
            }
            if(this.square.getCol()<7){
                a[this.square.getRow()-1][this.square.getCol()+1] = true;
            }
        }
    }

    @Override
    public ChessPieceType getPieceType() {
        return ChessPieceType.Pawn;
    }
}
