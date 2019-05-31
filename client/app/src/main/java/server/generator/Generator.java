package server.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import shared.entities.Entity;
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
		private ArrayList<Cell> rooms;
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
						r -= dx[(sequence.charAt(i) - '0')];
						c -= dy[(sequence.charAt(i) - '0')];
						grid[r][c] = Block.Road;
					}
					return;
				}
				for (int i = 0; i < 4; i++) {
					rowq.add(r + dx[i]);
					colq.add(c + dy[i]);
					seq.add(sequence.concat(Character.toString((char) ('0' + i))));
				}
			}
		}

		BlockGrid(int n_rows, int n_columns) {
			this.n_cols = n_columns;
			this.n_rows = n_rows;
			grid = new Block[n_rows][n_columns];
			rooms = new ArrayList<>();
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
			rooms = new ArrayList<>();
			while (n_rooms > 0) {
				int row = rand.nextInt(n_rows-room_height);
				int column = rand.nextInt(n_columns-room_width);
				boolean valid = true;
				for (int y = 0;y<room_height;y++){
					for (int x = 0;x<room_width;x++){
						valid &= (grid[row+y][column+x] == Block.Empty);
					}
				}
				if (valid) {
					for (int y = row-1;y<row+room_height+1;y++) if (0 <= y && y < n_rows){
						for (int x = column-1;x<column+room_width+1;x++) if (0 <= x && x < n_columns){
							if (y == row-1 || y == row+room_height || x == column-1 || x == column+room_width){
								grid[y][x] = Block.RoomEdge;
							}
							else{
								grid[y][x] = Block.Room;
							}
						}
					}
					rooms.add(new Cell(row+room_height/2, column+room_width/2, n_rooms - 1));//, Block.Room));
					n_rooms--;
				}
			}
			//now we are ready to apply Kruskall's to get a minimum spanning (so smallest amount of tunnels) that connects all rooms
			ArrayList<Connection> graph_edges = new ArrayList<>();
			for (int i = 0; i < rooms.size(); i++) {
				for (int j = 0; j < i; j++) {
					int distance = Math.abs(rooms.get(i).getRow() - rooms.get(j).getRow()) +
						Math.abs(rooms.get(i).getCol() - rooms.get(j).getCol());
					graph_edges.add(new Connection(rooms.get(i), rooms.get(j), distance));
					if(distance>max_distance_between_rooms){
						max_distance_between_rooms = distance;
						start = rooms.get(i);
						finish = rooms.get(j);
					}
				}
			}
			Collections.sort(graph_edges);
			ArrayList<Connection> mst_edges = kruskals(rooms.size(), graph_edges);
			//now we can connect the individual rooms
			for (int i = 0; i < mst_edges.size(); i++) {
				//we can do that by doing a breadth-first search
				Cell a = mst_edges.get(i).from;
				Cell b = mst_edges.get(i).to;
				build_bfs(a, b);
			}
		}
		/**
		 * Convert this grid of blocks to a Level
		 * @author Robert Koprinkov
		 * @param n_rooms, blockheight, blockwidth
		 * */
		Level convertToLevel(int n_rooms, int blockheight, int blockwidth) {
			Random rand = new Random(System.currentTimeMillis());

			Tile[][] fullgrid = new Tile[blockheight * this.n_rows][blockwidth * n_cols];
			for (int i = 0; i < n_rows; i++) {
				for (int j = 0; j < n_cols; j++) {
					if (grid[i][j] == Block.Empty || grid[i][j] == Block.RoomEdge) {
						for (int n = 0; n < blockheight; n++) {
							for (int m = 0; m < blockwidth; m++) {
								fullgrid[i * blockheight + n][j * blockwidth + m] = new Wall();
							}
						}
					} else {
						for (int n = 0; n < blockheight; n++) {
							for (int m = 0; m < blockwidth; m++) {
								fullgrid[i * blockheight + n][j * blockwidth + m] = new Empty();
							}
						}
						if (grid[i][j] == Block.Room) {
							if(i==start.getRow()&&j==start.getCol()){
								//start cell
								fullgrid[i * blockheight + rand.nextInt(blockheight)][j * blockwidth + rand.nextInt(blockwidth)] = new Start();
							} else if(i==finish.getRow() && j==finish.getCol()){
								fullgrid[i * blockheight + rand.nextInt(blockheight)][j * blockwidth + rand.nextInt(blockwidth)] = new Finish();
							}
						}
					}
				}
			}
			return new Level(fullgrid, new ArrayList<Entity>());
		}
	}

	private int n_rows, n_columns, block_columns, block_rows;
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
	 * @param n_rooms Number of rooms
	 * */
	public Level generate(int n_rooms) {
		BlockGrid grid = new BlockGrid(n_rows, n_columns);
		grid.generateGrid(n_rooms,5,3);
		return grid.convertToLevel(n_rooms,block_rows, block_columns);
	}

}
