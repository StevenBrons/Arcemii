package Chess;

import java.util.ArrayList;

public class Rook extends ChessPiece {

    public Rook(Square square){super(square);}

    int dx[] = {1, 0, -1, 0};
    int dy[] = {0, 1, 0, -1};

    @Override
    public ArrayList<Move> getMoves() {
        ArrayList<Move> ret = new ArrayList<>();
        for(int i=0; i<dx.length; i++){
            for(int j=1; j<8; j++){
                if(square.getRow()+j*dx[i] >= 0 && square.getRow()+j*dx[i] < 8 && square.getCol() + j*dy[i] >= 0 && square.getCol() + j*dy[i] < 8 && g.getBoard()[square.getRow()+dx[i]*j][square.getCol()+dy[i]*j].getPlayer() != this.getPlayer()){
                    //we can go here, provided that we don't get our king checked
                    Game gc = g.duplicate();
                    Move mv = new Move(square, new Square(square.getRow()+dx[i]*j, square.getCol()+dy[i]*j));
                    gc.move(mv);
                    if(gc.isValid()){
                        ret.add(mv);
                    }
                }
            }
        }
        return ret;
    }

    @Override
    public void markAttackedTiles(boolean[][] a) {
        for(int i=0; i<dx.length; i++){
            for(int j=1; j<8; j++){
                if(square.getRow()+j*dx[i] >= 0 && square.getRow()+j*dx[i] < 8 && square.getCol() + j*dy[i] >= 0 && square.getCol() + j*dy[i] < 8){
                    a[square.getRow()+j*dx[i]][square.getCol()+j*dy[i]] = true;
                }
            }
        }
    }

    @Override
    public ChessPieceType getPieceType() {
        return ChessPieceType.Rook;
    }
}
