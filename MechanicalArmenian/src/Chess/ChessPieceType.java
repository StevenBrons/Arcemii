package Chess;

public enum ChessPieceType {
    Empty, Pawn, Knight, Bishop, Rook, Queen, King;
    public int getValue(){
        switch(this){
            case Empty:
                return 0;
            case Pawn:
                return 1;
            case Knight:
                return 3;
            case Bishop:
                return 3;
            case Rook:
                return 5;
            case Queen:
                return 9;
            default:
                return (int)1e9;
        }
    }
}
