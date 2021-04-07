import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;

// A solver based on the A* algorithm for the 8-puzzle and its generalizations.
public class Solver {
    private LinkedStack<Board> ls;
    private int moves;

    // Helper search node class.
    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode previous;

        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }
    }

    // Find a solution to the initial board (using the A* algorithm).
    public Solver(Board initial) {
        if (initial == null)
            throw new NullPointerException();
        if (!initial.isSolvable())
            throw new IllegalArgumentException();

        MinPQ<SearchNode> mpq = new MinPQ<>(new ManhattanOrder());
        this.ls = new LinkedStack<>();
        mpq.insert(new SearchNode(initial, 0, null));

        while (!mpq.isEmpty()) {
            SearchNode node = mpq.delMin();
            if (node.board.isGoal()) {
                moves = node.moves;
                ls.push(node.board);
                while (node.previous != null) {
                    ls.push(node.previous.board);
                    node = node.previous;
                }
                break;
            }
            Iterator<Board> itr = node.board.neighbors().iterator();
            while (itr.hasNext()) {
                SearchNode n = new SearchNode(itr.next(), node.moves + 1, node);
                if (!n.board.equals(node.board))
                    mpq.insert(n);
            }
        }
    }

    // The minimum number of moves to solve the initial board.
    public int moves() {
        return moves;
    }

    // Sequence of boards in a shortest solution.
    public Iterable<Board> solution() {
        return ls;
    }

    // Helper hamming priority function comparator.
    private static class HammingOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            return ((a.board.hamming() + a.moves) - (b.board.hamming() + b.moves));
        }
    }

    // Helper manhattan priority function comparator.
    private static class ManhattanOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            return ((a.board.manhattan() + a.moves) - (b.board.manhattan() + b.moves));
        }
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
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        } else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
