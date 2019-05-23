package Chess;

public class Move {
    private Square from = null, to = null;
    private CastleType castle;

    public Move(Square from, Square to){
        this.from = from;
        this.to = to;
    }

    public Move(CastleType t){
        this.castle = t;
    }

    public CastleType isCastle(){return this.castle; }
    public Square getOrigin(){return this.from;}
    public Square getDestination(){return this.to;}
    public String toString(){

    }
}
