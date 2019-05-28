package server.generator;

import java.util.ArrayList;
import java.util.LinkedList;
import shared.general.Level;

public class Generator {

    private class Connection {
        Node from, to;
        int weight;
        public Edge(Cell from, Cell to, int weight){
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    private class Cell {
        private int row, col, ID;
        private Block block;

        public Node(int row, int col, int ID, Block block){
            this.row = row;
            this.col = col;
            this.block = block;
            this.ID = ID;
        }

        public int getRow(){return this.row;}
        public int getCol(){return this.col;}

        public Block getBlock(){ return this.block; }
        public void setBlock(Block b){this.block = b;}
        public int getID(){return this.ID;}

        @Override
        public boolean equals(Node b){
            return b.getRow()==this.getRow() && b.getCol() == this.getCol();
        }

    }

    private enum Block {
        Empty, Room, Road;
    }

    private class BlockGrid {
        private int n_rows, n_cols;
        private Block grid;
        private ArrayList<Cell> rooms;

        void connect(int[] UFDS, Edge e){
            UFDS[parent(UFDS, e.from.getID())] = parent(UFDS, e.to.getID());
        }

        int parent(int[] UFDS, int v){
            if(UFDS[v]==v)
                return v;
            else
                return UFDS[v] = parent(UFDS, UFDS[v]);
        }

        boolean connected(int[] UFDS, Edge e){
            return parent(UFDS, UFDS[e.from.getID()])==parent(UFDS, UFDS[e.to.getID()]);
        }

        /**
         * Uses Kruskal's algorithm to construct the minimum spanning tree that connects all rooms
         * @author Robert Koprinkov
         * @param N, edges
         * */

        public ArrayList<Connection> kruskals(int N, ArrayList<Connection> edges){
            ArrayList<Connection> ret = new ArrayList<>();
            int[] UFDS = new int[N];
            for(int i=0; i<N; i++)
                UFDS[i] = i;
            for(int i=0; i<edges.size()&&ret.size()<N-1; i++){
                if(!connected(UFDS, edges.get(i))){
                    connect(UFDS, edges.get(i));
                    ret.add(edges.get(i));
                }
            }
            return ret;
        }

        /**
         * Builds a road from cell a to cell b in the grid using breadth-first-search
         * @author Robert Koprinkov
         * @param a
         * @param b
         * */

        void build_bfs(Cell a, Cell b){
            int row_a = a.getRow();
            int col_a = a.getCol();
            int row_b = b.getRow();
            int col_b = b.getCol();
            int dx[] = {1, 1, -1, -1};
            int dy[] = {1, -1, 1, -1};
            int v[][] = new int[n_rows][n_columns];
            for(int r=0; r<n_rows; r++)
                for(int c=0; c<n_columns; c++)
                    v[r][c] = 0;
            LinkedList <Integer> rowq = new LinkedList<>(), colq = new LinkedList<>();
            rowq.add(row_a);
            colq.add(col_a);
            LinkedList<String> seq = new LinkedList<>();
            seq.add("");
            while(rowq.size()>0){
                int r = rowq.getFirst();
                int c = colq.getFirst();
                String sequence = seq.getFirst();
                seq.removeFirst();
                rowq.removeFirst();
                colq.removeFirst();
                if(r<0||c<0||r>=n_rows||c>=n_columns)
                    continue;
                if(v[r][c] > 0)
                    continue;
                v[r][c] = 1;
                if(r==row_b&&c==col_b){
                    //found a path!
                    for(int i=seq.size()-1; i>0; i--){
                        r -= dx[(int)(sequence.charAt(i)-'0')];
                        c -= dy[(int)(sequence.charAt(i)-'0')];
                        grid[r][c] = Block.Road;
                    }
                    return;
                }
                for(int i=0; i<4; i++){
                    rowq.add(r+dx[i]);
                    colq.add(c+dy[i]);
                    seq.add(sequence.concat(Character.toString((char)('0'+i))));
                }
            }
        }

        public BlockGrid(int n_rows, int n_columns){
            this.n_cols = n_columns;
            this.n_rows = n_rows;
            grid = new Block[n_rows][n_columns];
            rooms = new ArrayList<>();
        }

        /**
         * Generates the grid of rooms and road nodes (note: this divides the grid into blocks with some function. They need to be further specified before being converted to a level.)
         * @author Robert Koprinkov
         * @param n_rooms
         * */

        public void generateGrid(int n_rooms) {
            for (int n = 0; n < n_rows; n++){
                for (int m = 0; m < n_cols; n++) {
                    grid[n][m] = Block.Empty;
                }
            }
            rooms = new ArrayList<>();
            while(n_rooms>0){
                int row = (int)Math.random()*n_rows;
                int column = (int)Math.random()*n_columns;
                if(field[row][column]==Block.Empty){
                    field[row][column] = Block.Room;
                    rooms.add(new Cell(row, col, n_rooms-1, Block.Room));
                    n_rooms--;
                }
            }
            //now we are ready to apply kurskal's to get a minimum spanning (so smallest amount of tunnels) that connects all rooms
            ArrayList<Connection> graph_edges = new ArrayList<>();
            for(int i=0; i<room_rows.size(); i++){
                for(int j=0; j<i; j++){
                    graph_edges.add(new Connection(rooms[i], rooms[j], Math.abs(room_rows.get(i)-room_rows.get(j))+Math.abs(room_cols.get(i)-room_cols.get(j))));
                }
            }
            ArrayList<Connection> mst_edges = kruskals(room_rows.size(), graph_edges);
            //now we can connect the individual rooms
            for(int i=0; i<mst_edges.size(); i++) {
                //we can do that by doing a breadth-first search
                Cell a = mst_edges.get(i).from;
                Cell b = mst_edges.get(i).to;
                build_bfs(a, b);
            }
        }
        /**
        * Convert this grid of blocks to a Level
        * @author Robert Koprinkov
        * @param n_levels, blockheight, blockwidth
        * */
        public Level convertToLevel(int n_levels, int blockheight, int blockwidth){
            int st = -1, fin = -1;
            int n_rooms_seen = 0;
            while(st==fin){
                st = Math.random()*n_levels;
                fin = Math.random()*n_levels;
            }
            Tile fullgrid = new Tile[blockheight*this.n_rows][blockwidth*n_cols];
            for(int i=0; i<n_rows; i++){
                for(int j=0; j<n_cols; j++){
                    if(grid[i][j]==Block.Empty){
                        for(int n=0; n<blockheight; n++){
                            for(int m=0; m<blockwidth; m++){
                                fullgrid[i*blockheight+n][j*blockwidth+m] = new Wall();
                            }
                        }
                    } else {
                        for(int n=0; n<blockheight; n++){
                            for(int m=0; m<blockwidth; m++){
                                fullgrid[i*blockheight+n][j*blockwidth+m] = new Empty();
                            }
                        }
                        if(grid[i][j] == Block.Room){
                            if(n_rooms_seen==st){
                                fullgrid[i*blockheight+Math.random()*blockheight][j*blockwidth+Math.random()*blockwidth] = new Start();
                            } else if (n_rooms_seen==fin){
                                fullgrid[i*blockheight+Math.random()*blockheight][j*blockwidth+Math.random()*blockwidth] = new Finish();
                            }
                            n_rooms_seen--;
                        }
                    }
                }
            }
        }
        return new Level(fullgrid, new ArrayList<Entity>());
    }

    private int n_rooms, n_columns, block_columns, block_rows;
    /**
    * Generates a generator for dungeons where there are n_rows rows of blocks, n_columns blocks of column, and each column has block_rowsxblock_columns tiles
    * @author Robert Koprinkov
    * @param n_rows, n_columns, block_columns, block_rows
    * */
    public Generator(int n_rows, int n_columns, int block_columns, int block_rows) {
        this.n_columns = n_columns;
        this.n_rows = n_rows;
        this.block_columns = block_columns;
        this.block_rows = block_rows;
    }

    /**
     * Generates a level with n_rooms rooms where the dungeon and each room are within the boundaries specified above
     * @author Robert Koprinkov
     * @param n_rooms
     *
     * */

    Level generate(int n_rooms) {
        BlockGrid grid = new BlockGrid(n_rows, n_columns);
        grid.generateGrid(n_rooms);
        return grid.convertToLevel(block_rows, block_columns);
    }
}
