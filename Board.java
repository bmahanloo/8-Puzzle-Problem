import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    private int[][] tiles;
    private int N;
    private int hamming;
    private int manhattan;

    // Construct a board from an N-by-N array of tiles, where
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank
    // square.
    public Board(int[][] tiles) {
        this.N = tiles.length;
        this.tiles = tiles;
        hamming = 0;
        manhattan = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != (i * N + j + 1)) {
                    hamming++;
                    int row = (tiles[i][j] - 1) / N;
                    int col = (tiles[i][j] - 1) % N;
                    manhattan += Math.abs(row - i) + Math.abs(col - j);
                }
            }
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        if (i < 0 || i >= N || j < 0 || j >= N)
            throw new IllegalArgumentException();
        return tiles[i][j];
    }

    // Size of this board.
    public int size() {
        return N;
    }

    // Number of tiles out of place.
    public int hamming() {
        return hamming;
    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
        return manhattan;
    }

    // Is this board the goal board?
    public boolean isGoal() {
        return tiles[N - 1][N - 1] == 0;
    }

    // Is this board solvable?
    public boolean isSolvable() {
        if (N % 2 == 0)
            return (inversions() + blankPos()) % 2 == 1;
        else
            return inversions() % 2 == 0;
    }

    // Does this board equal that?
    public boolean equals(Board that) {
        if (that == this) return true;
        if (that == null) return false;
        if ((this.hamming() != that.hamming()) || (this.manhattan() != that.manhattan())
                || (this.N != that.N)) {
            return false;
        }
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (this.tileAt(i, j) != that.tileAt(i, j))
                    return false;
        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        int index = blankPos();
        int i = index / N;
        int j = index % N;
        int val;

        // If blank is not in first row
        if (i > 0) {
            Board board = new Board(cloneTiles());
            val = board.tiles[i][j];
            board.tiles[i][j] = board.tiles[i - 1][j];
            board.tiles[i - 1][j] = val;
            q.enqueue(board);
        }

        // If blank is not in last row
        if (i < N - 1) {
            Board board = new Board(cloneTiles());
            val = board.tiles[i][j];
            board.tiles[i][j] = board.tiles[i + 1][j];
            board.tiles[i + 1][j] = val;
            q.enqueue(board);
        }

        // If blank is not in first column
        if (j > 0) {
            Board b = new Board(cloneTiles());
            val = b.tiles[i][j];
            b.tiles[i][j] = b.tiles[i][j - 1];
            b.tiles[i][j - 1] = val;
            q.enqueue(b);
        }

        // If blank is not in last column
        if (j < N - 1) {
            Board b = new Board(cloneTiles());
            val = b.tiles[i][j];
            b.tiles[i][j] = b.tiles[i][j + 1];
            b.tiles[i][j + 1] = val;
            q.enqueue(b);
        }
        return q;
    }

    // String representation of this board.
    public String toString() {
        String s = N + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s += String.format("%2d", tiles[i][j]);
                if (j < N - 1) {
                    s += " ";
                }
            }
            if (i < N - 1) {
                s += "\n";
            }
        }
        return s;
    }

    // Helper method that returns the position (in row-major order) of the
    // blank (zero) tile.
    private int blankPos() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tiles[i][j] == 0)
                    return i * N + j;
        return -1;
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
        int inversions = 0;
        for (int i = 0; i < N * N; i++) {
            int row = i / N;
            int col = i % N;

            for (int j = i; j < N * N; j++) {
                int r = j / N;
                int c = j % N;

                if (tileAt(r, c) != 0 && tileAt(r, c) < tileAt(row, col))
                    inversions++;
            }
        }
        return inversions;
    }

    // Helper method that clones the tiles[][] array in this board and
    // returns it.
    private int[][] cloneTiles() {
        int[][] arr = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                arr[i][j] = tiles[i][j];
        return arr;

    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}
