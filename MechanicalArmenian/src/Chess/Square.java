package Chess;

public class Square {
    private int row, col;

    public Square(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){return this.row;}
    public int getCol(){return this.col;}

    @Override
    public String toString(){
        return ((char)col+'A'-1) + Integer.toString(row);
    }
}
