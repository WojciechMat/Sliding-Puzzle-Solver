import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import java.util.Comparator;

public class Solver {

    private boolean is_solvable_var = true; // I will change this variable in constructor if necessary, then return it in isSolvable().
    private int moves = 0;
    private final Stack<Board> sequence = new Stack<>(); // stack for solving sequence


    private static class BoardNode {
        public BoardNode previous;
       public Board b;
       public int moves_to_node;
       public int manhattan;

       public BoardNode(Board board, int moves, BoardNode previous){
           b=board;
           moves_to_node=moves;
           manhattan = board.manhattan();
           this.previous = previous;
       }

    }

    public Solver(Board initial)
    {

        if(initial == null)
            throw new IllegalArgumentException();
            if (initial.isGoal()) {
                sequence.push(initial);
                return;
            }
            class ManhattanOrder implements Comparator<BoardNode>
            {
                public int compare(BoardNode b1, BoardNode b2)
                {
                    return b1.manhattan + b1.moves_to_node - b2.manhattan - b2.moves_to_node;
                }
            }
            Comparator<BoardNode> manhattan_order = new ManhattanOrder();
            //solves the board while solving its 'twin'
            // (board with one pair of tiles swapped)
            // in order to check whether the initial board is solvable
            Board check = initial.twin();
            MinPQ<BoardNode> ch_que = new MinPQ<>(manhattan_order); //MinPQ for the twin board
            MinPQ<BoardNode> que = new MinPQ<>(manhattan_order); //MinPQ for the initial board
            BoardNode ch_search = new BoardNode(check, 0,null); //checking search node
            BoardNode search = new BoardNode(initial, 0, null); //search node
            for(Board i : check.neighbors()){
                BoardNode next = new BoardNode(i, 1, ch_search);
                ch_que.insert(next);
            }
            for (Board i : initial.neighbors()) {
                BoardNode next = new BoardNode(i, 1, search);
                que.insert(next);
            }

            while (!search.b.isGoal() && !que.isEmpty() && !ch_search.b.isGoal()) {
                if (!ch_que.isEmpty()){
                    ch_search = ch_que.delMin();
                    for (Board i : ch_search.b.neighbors()) {
                        if (!i.equals(ch_search.previous.b)) {
                            BoardNode next = new BoardNode(i, ch_search.moves_to_node+1, ch_search);
                            ch_que.insert(next);

                        }
                    }
                }

                search = que.delMin();
                for (Board i : search.b.neighbors()) {
                    if (!i.equals(search.previous.b)) {
                        BoardNode next = new BoardNode(i, search.moves_to_node+1, search);
                        que.insert(next);
                    }
                }
            }

            if ((que.isEmpty() && !search.b.isGoal()) || ch_search.b.isGoal() ){
                //if the while loop stopped, but search is not a goal board
                // (but maybe the checking board is), then the board is not solvable
                is_solvable_var = false;
            }
            else {
                // pushes the steps of the solution in reverse order onto a stack
                moves = search.moves_to_node;
                while (!(search == null)) {
                    sequence.push(search.b);
                    search = search.previous;
                }
            }
    }

    public boolean isSolvable()
    {
        return is_solvable_var;
    }

    public int moves()
    {
        if(!isSolvable()) return -1;
        return moves;
    }
    //returns the stack of boards that lead to the goal board
    public Iterable<Board> solution()
    {
        if(!isSolvable()) return null;
        else return sequence;

    }

    public static void main(String[] args) {
        //main method provided by the course
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
