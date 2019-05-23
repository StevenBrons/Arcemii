package server.generator;

import java.util.ArrayList;
import java.util.LinkedList;
import shared.general.Level;

public class Generator {

    private class Edge{
        int from, to;
        int weight;
        public Edge(int from, int to, int weight){
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    void connect(int[] UFDS, Edge e){
        UFDS[parent(UFDS, e.from)] = parent(UFDS, e.to);
    }

    int parent(int[] UFDS, int v){
        if(UFDS[v]==v)
            return v;
        else
            return UFDS[v] = parent(UFDS, UFDS[v]);
    }

    boolean connected(int[] UFDS, Edge e){
        return parent(UFDS, UFDS[e.from])==parent(UFDS, UFDS[e.to]);
    }

    public ArrayList<Edge> kruskals(int N, ArrayList<Edge> edges){
        ArrayList<Edge> ret = new ArrayList<>();
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

    //number of rows of blocks and columns of blocks
    //a block is a collection of cells, encompassing a single georgaphical unit
    //a room encompasses one block

    private int n_rows = 15, n_columns = 15;
    private int block_rows, block_columns;

    public Generator(int n_rows, int n_columns, int block_columns, int block_rows) {
        this.n_columns = n_columns;
        this.n_rows = n_rows;
        this.block_columns = block_columns;
        this.block_rows = block_rows;
    }

    void build_bfs(int f[][], int row_a, int col_a, int row_b, int col_b){
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
                    f[r][c] = 2;
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

    /*
     * Generates a level with n_rooms rooms where the dungeon and each room are within the boundaries specified above
     * @author Robert Koprinkov
     * @param n_rooms
     *
     * */

    Level generate(int n_rooms) {
        //generates a dungeon of n_rooms rooms
        /*if(n_rooms>n_rows*n_columns)
            throw Exception();*/
        int[][] field = new int[n_rows][n_columns];
        //field contains a 0 if the given block is solid, a 1 of it is a room and a 2 if it is a transfer node (so it contains a road)
        for(int r=0; r<n_rows; r++){
            for(int c=0; c<n_columns; c++){
                field[r][c] = 0;
            }
        }
        ArrayList<Integer> room_rows = new ArrayList<>(), room_cols = new ArrayList<>();
        while(n_rooms>0){
            int row = (int)Math.random()*n_rows;
            int column = (int)Math.random()*n_columns;
            if(field[row][column]==0){
                field[row][column] = 1;
                room_rows.add(row);
                room_cols.add(column);
                n_rooms--;
            }
        }
        //we have n_rooms rooms, now we need to build infrastructure
        //first we will use Kruskal's Minimum Spanning Tree algorithm to generate a minimum spanning tree that spans the entire network
        //first we will store the graph of rooms in an arraylist of edges
        ArrayList<Edge> graph_edges = new ArrayList<>();
        for(int i=0; i<room_rows.size(); i++){
            for(int j=0; j<i; j++){
                graph_edges.add(new Edge(i, j, Math.abs(room_rows.get(i)-room_rows.get(j))+Math.abs(room_cols.get(i)-room_cols.get(j))));
            }
        }
        ArrayList<Edge> mst_edges = kruskals(room_rows.size(), graph_edges);
        //now we have a minimum spanning tree!

        //let's build the roads
        for(int i=0; i<mst_edges.size(); i++) {
            //we can do that by doing a breadth-first search
            int a = mst_edges.get(i).from;
            int b = mst_edges.get(i).to;
            build_bfs(field, room_rows.get(a), room_cols.get(a), room_rows.get(b), room_cols.get(b));
        }
        //now we have assigned each block a value and we are ready to fill in the individual blocks
        return null;
    }
}
