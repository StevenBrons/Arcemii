package Chess;

public class Game {
    private ChessPiece[][] board;
    private boolean castled;

    public Game(ChessPiece[][] board){this.board = board;}

    public ChessPiece[][] getBoard(){return this.board;}
    public boolean isValid(){return false;}
    public void move(Move m){}
    public Game duplicate(){return new Game(this.board);}
}
