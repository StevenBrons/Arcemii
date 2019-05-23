package Chess;

import java.util.ArrayList;

public class Knight extends ChessPiece {

    public Knight(Square square){super(square);}
    private int dx[] = {-1, -2, -2, -1, 1, 2, 2, 1};
    private int dy[] = {-2, -1, 1, 2, 2, 1, -1, -2};

    @Override
    public ArrayList<Move> getMoves() {
        ArrayList<Move> ret = new ArrayList<>();
        for(int i=0; i<dx.length; i++){
            if((0 <= square.getRow() + dx[i] && square.getRow()+dx[i]<8 && 0 <= square.getCol() && square.getCol() < 8) && g.getBoard()[square.getRow()+dx[i]][square.getCol()+dy[i]].getPlayer() != this.getPlayer()){
                //we can go here, provided that we don't get our king checked
                Game gc = g.duplicate();
                Move mv = new Move(square, new Square(square.getRow()+dx[i], square.getCol()+dy[i]));
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
        for(int i=0; i<dx.length; i++){
            if((0 <= square.getRow() + dx[i] && square.getRow()+dx[i]<8 && 0 <= square.getCol() && square.getCol() < 8)){
                //we can attack here!
                a[square.getRow()+dx[i]][square.getCol()+dy[i]] = true;
            }
        }
    }

    @Override
    public ChessPieceType getPieceType() {
        return ChessPieceType.Knight;
    }
}
