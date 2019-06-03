package server.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import shared.entities.Boss;
import shared.entities.Entity;
import shared.entities.Skeleton;
import shared.entities.Slime;
import shared.general.Level;
import shared.tiles.Empty;
import shared.tiles.Finish;
import shared.tiles.Start;
import shared.tiles.Tile;
import shared.tiles.Wall;

public class Generator {

	private class Connection implements Comparable<Connection>{
		Cell from, to;
		int weight;
		Connection(Cell from, Cell to, int weight){
			this.from = from;
			this.to = to;
			this.weight = weight;
		}

		@Override
		public int compareTo(Connection other) {
			return this.weight-other.weight;
		}
	}

	private class Cell {
		private int row, col, ID;
//        private Block block;

		Cell(int row, int col, int ID){//}, Block block){
			this.row = row;
			this.col = col;
//            this.block = block;
			this.ID = ID;
		}

		int getRow(){return this.row;}
		int getCol(){return this.col;}

		//        public Block getBlock(){ return this.block; }
//        public void setBlock(Block b){this.block = b;}
		int getID(){return this.ID;}

		@Override
		public boolean equals(Object b){
			if (!(b instanceof Cell)) return false;
			return ((Cell)b).getRow()==this.getRow() && ((Cell)b).getCol() == this.getCol();
		}

	}

	private enum Block {
		Empty, Room, Road, RoomEdge
	}

    private class BlockGrid {
        private int n_rows, n_cols;
        private Block[][] grid;
        private ArrayList<Cell> roomBottomLeft;
        private ArrayList<Entity> entities;
        private int max_distance_between_rooms = -1;
        private Cell start, finish;

		void connect(int[] UFDS, Connection e) {
			UFDS[parent(UFDS, e.from.getID())] = parent(UFDS, e.to.getID());
		}

		int parent(int[] UFDS, int v) {
			if (UFDS[v] == v)
				return v;
			else
				return UFDS[v] = parent(UFDS, UFDS[v]);
		}

		boolean connected(int[] UFDS, Connection e) {
			return parent(UFDS, UFDS[e.from.getID()]) == parent(UFDS, UFDS[e.to.getID()]);
		}

		/**
		 * Uses Kruskal's algorithm to construct the minimum spanning tree that connects all rooms
		 *
		 * @param N, edges
		 * @author Robert Koprinkov
		 */

		ArrayList<Connection> kruskals(int N, ArrayList<Connection> edges) {
			ArrayList<Connection> ret = new ArrayList<>();
			int[] UFDS = new int[N];
			for (int i = 0; i < N; i++)
				UFDS[i] = i;
			for (int i = 0; i < edges.size() && ret.size() < N - 1; i++) {
				if (!connected(UFDS, edges.get(i))) {
					connect(UFDS, edges.get(i));
					ret.add(edges.get(i));
				}
			}
			return ret;
		}

		/**
		 * Builds a road from cell a to cell b in the grid using breadth-first-search
		 *
		 * @param a Start cell
		 * @param b End cell
		 * @author Robert Koprinkov
		 */
        void build_bfs(Cell a, Cell b) {
            int row_a = a.getRow();
            int col_a = a.getCol();
            int row_b = b.getRow();
            int col_b = b.getCol();
            int[] dx = {1, -1, 0, 0};
            int[] dy = {0, 0, 1, -1};
            int[][] v = new int[n_rows][n_columns];
            for (int r = 0; r < n_rows; r++)
                for (int c = 0; c < n_columns; c++)
                    v[r][c] = 0;
            LinkedList<Integer> rowq = new LinkedList<>(), colq = new LinkedList<>();
            rowq.add(row_a);
            colq.add(col_a);
            LinkedList<String> seq = new LinkedList<>();
            seq.add("");
            while (rowq.size() > 0) {
                int r = rowq.getFirst();
                int c = colq.getFirst();
                String sequence = seq.getFirst();
                seq.removeFirst();
                rowq.removeFirst();
                colq.removeFirst();
                if (r < 0 || c < 0 || r >= n_rows || c >= n_columns)
                    continue;
                if (v[r][c] > 0)
                    continue;
                v[r][c] = 1;
                if (r == row_b && c == col_b) {
                    //found a path!
                    for (int i = sequence.length() - 1; i > 0; i--) {
                        c -= dx[(sequence.charAt(i) - '0')];
                        r -= dy[(sequence.charAt(i) - '0')];
                        grid[c][r] = Block.Road;
                    }
                    return;
                }
                for (int i = 0; i < 4; i++) {
                    colq.add(c + dx[i]);
                    rowq.add(r + dy[i]);
                    seq.add(sequence.concat(Character.toString((char) ('0' + i))));
                }
            }
        }

        BlockGrid(int n_rows, int n_columns) {
            this.n_cols = n_columns;
            this.n_rows = n_rows;
            grid = new Block[n_rows][n_columns];
            roomBottomLeft = new ArrayList<>();
            this.entities = new ArrayList<>();
        }

		/**
		 * Generates the grid of rooms and road nodes (note: this divides the grid into blocks with some function. They need to be further specified before being converted to a level.)
		 *
		 * @param n_rooms Number of rooms to generate
		 * @author Robert Koprinkov
		 */
        void generateGrid(int n_rooms,int room_width, int room_height) {
            Random rand = new Random(System.currentTimeMillis());
            for (int n = 0; n < n_rows; n++) {
                for (int m = 0; m < n_cols; m++) {
                    grid[n][m] = Block.Empty;
                }
            }
            ArrayList<Cell> roomCenters = new ArrayList<>();
            while (n_rooms > 0) {
                int row = rand.nextInt(n_rows-room_height);
                int column = rand.nextInt(n_columns-room_width);
                boolean valid = true;
                for (int x = 0;x<room_width;x++){
                    for (int y = 0;y<room_height;y++){
                        valid &= (grid[column+x][row+y] == Block.Empty);
                    }
                }
                if (valid) {
                    for (int y = row-1;y<row+room_height+1;y++) if (0 <= y && y < n_rows){
                        for (int x = column-1;x<column+room_width+1;x++) if (0 <= x && x < n_columns){
                            if (y == row-1 || y == row+room_height || x == column-1 || x == column+room_width){
                                grid[x][y] = Block.RoomEdge;
                            }
                            else{
                                grid[x][y] = Block.Room;
                            }
                        }
                    }
                    roomCenters.add(new Cell(row+room_height/2, column+room_width/2, n_rooms - 1));//, Block.Room));
                    roomBottomLeft.add(new Cell(row,column,n_rooms-1));
                    n_rooms--;
                }
            }
            //now we are ready to apply Kruskall's to get a minimum spanning (so smallest amount of tunnels) that connects all rooms
            ArrayList<Connection> graph_edges = new ArrayList<>();
            for (int i = 0; i < roomCenters.size(); i++) {
                for (int j = 0; j < i; j++) {
                    int distance = Math.abs(roomCenters.get(i).getRow() - roomCenters.get(j).getRow()) +
                            Math.abs(roomCenters.get(i).getCol() - roomCenters.get(j).getCol());
                    graph_edges.add(new Connection(roomCenters.get(i), roomCenters.get(j), distance));
                    if(distance>max_distance_between_rooms){
                        max_distance_between_rooms = distance;
                        start = roomCenters.get(i);
                        finish = roomCenters.get(j);
                    }
                }
            }
            Collections.sort(graph_edges);
            ArrayList<Connection> mst_edges = kruskals(roomCenters.size(), graph_edges);
            //now we can connect the individual rooms
            for (int i = 0; i < mst_edges.size(); i++) {
                //we can do that by doing a breadth-first search
                Cell a = mst_edges.get(i).from;
                Cell b = mst_edges.get(i).to;
                build_bfs(a, b);
            }
        }

        /**
         * Put enemies into rooms
         * @author Jelmer Firet
         */
        void generateEnemies(int block_height, int block_width, int room_width, int room_height){
            for (Cell room:roomBottomLeft) {
            	if (room.getRow()+room_height/2 == start.getRow() && room.getCol()+room_width/2 == start.getCol()){
            		continue;
				}
                double bottom = room.getRow() * block_height;
                double top = (room.getRow() + room_height) * block_height;
                double left = room.getCol() * block_width;
                double right = (room.getCol() + room_width) * block_width;
                Random rand = new Random();
                int roomType = rand.nextInt(3);
				if (room.getRow()+room_height/2 == finish.getRow() && room.getCol()+room_width/2 == finish.getCol()){
					roomType = -1;
				}
                if (roomType == -1) {
                    entities.add(new Boss((left + right) / 2, (top + bottom) / 2));
                }
                if (roomType == 0) {
                    for (int i = 0; i < 5; i++) {
                        entities.add(new Skeleton(left + rand.nextDouble() * (right - left),
                                bottom + rand.nextDouble() * (top - bottom)));
                    }
                }
                if (roomType == 1) {
                    for (int i = 0; i < 5; i++) {
                        entities.add(new Slime(left + rand.nextDouble() * (right - left),
                                bottom + rand.nextDouble() * (top - bottom)));
                    }
                }
                if (roomType == 2) {
                    for (int i = 0; i < 3; i++) {
                        entities.add(new Skeleton(left + rand.nextDouble() * (right - left),
                                bottom + rand.nextDouble() * (top - bottom)));
                    }
                    for (int i = 0; i < 2; i++) {
                        entities.add(new Slime(left + rand.nextDouble() * (right - left),
                                bottom + rand.nextDouble() * (top - bottom)));
                    }
                }
            }
        }

        /**
        * Convert this grid of blocks to a Level
        * @author Robert Koprinkov
        * @param blockheight number of tiles each block is high
        * @param blockwidth number of tiles each block is wide
        * */
        Level convertToLevel(int blockheight, int blockwidth) {
            Random rand = new Random(System.currentTimeMillis());

            Tile[][] fullgrid = new Tile[blockheight * this.n_rows][blockwidth * n_cols];
            for (int i = 0; i < n_rows; i++) {
                for (int j = 0; j < n_cols; j++) {
                    if (grid[j][i] == Block.Empty || grid[j][i] == Block.RoomEdge) {
                        for (int n = 0; n < blockheight; n++) {
                            for (int m = 0; m < blockwidth; m++) {
                                fullgrid[j * blockwidth + m][i * blockheight + n] = new Wall();
                            }
                        }
                    } else {
                        for (int n = 0; n < blockheight; n++) {
                            for (int m = 0; m < blockwidth; m++) {
                                fullgrid[j * blockwidth + m][i * blockheight + n] = new Empty();
                            }
                        }
                        if (grid[j][i] == Block.Room) {
                            if(i==start.getRow()&&j==start.getCol()){
                                //start cell
                                fullgrid[j * blockwidth + rand.nextInt(blockwidth)][i * blockheight + rand.nextInt(blockheight)] = new Start();
                            } else if(i==finish.getRow() && j==finish.getCol()){
                                fullgrid[j * blockwidth + rand.nextInt(blockwidth)][i * blockheight + rand.nextInt(blockheight)] = new Finish();
                            }
                        }
                    }
                }
            }
            return new Level(fullgrid, entities);
        }
    }

    private int n_rows, n_columns, block_columns, block_rows, room_width, room_height;
    /**
    * Generates a generator for dungeons where there are n_rows rows of blocks, n_columns blocks of column, and each column has block_rowsxblock_columns tiles
    * @author Robert Koprinkov
    * @param n_rows, n_columns, block_columns, block_rows
    * */
    public Generator(int n_rows, int n_columns, int block_columns, int block_rows, int room_width, int room_height) {
        this.n_columns = n_columns;
        this.n_rows = n_rows;
        this.block_columns = block_columns;
        this.block_rows = block_rows;
        this.room_width = room_width;
        this.room_height = room_height;
    }

    /**
     * Generates a level with n_rooms rooms where the dungeon and each room are within the boundaries specified above
     * @author Robert Koprinkov
     * @param n_rooms Number of rooms
     * */
    public Level generate(int n_rooms) {
        if (n_rooms > (n_rows/(2*room_height+1))*(n_columns/(2*room_width+1))){
            System.out.println("WARNING: Level might not finish generating");
            System.out.println("Maximum safe number of rooms is: "+
                    (n_rows/(2*room_height+1))*(n_columns/(2*room_width+1)));
        }
        BlockGrid grid = new BlockGrid(n_rows, n_columns);
        grid.generateGrid(n_rooms,room_width,room_height);
        grid.generateEnemies(block_rows,block_columns,room_width,room_height);
        return grid.convertToLevel(block_rows, block_columns);
    }
  
}
