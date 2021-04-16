
import java.util.ArrayList;

public class Board {
    private final int n; // the size of the column of a square initial board 
    private final int[][] board; //two-dimensional array enabling easy acess outside constructor

    public Board(int[][] tiles)
    {
        n = tiles.length;
        board = new int[tiles.length][tiles.length];
        //creating copy of the initial array
        for(int i = 0; i < tiles.length; ++i)
        {
            System.arraycopy(tiles[i], 0, board[i], 0, tiles.length);
        }
    }

    // string representation of this board
    public String toString()
    {
        StringBuilder represent = new StringBuilder("");
        represent.append(dimension()).append("\n");
        for(int i = 0; i < board.length; ++i)
        {
            for(int j = 0; j < board.length; ++j)
            {
                represent.append(" ").append(board[i][j]).append("  ");
            }
            represent.append("\n");
        }
        return represent.toString();
    }

    public int dimension()
    {
        return n;
    }
    //this method returs the hamming value of the board - number of tiles that are out of their place
    public int hamming()
    {
        int sum = 0;
        int k = 1;
        for(int i = 0; i < n; ++i)
        {
            for(int j = 0; j < n; ++j)
            {
                if(board[i][j] != 0 && board[i][j]!=k) sum++;
                k++;
            }
        }
        return sum;
    }
//this method returns manhattan value of the board -
// length of the path (moving in integer x and y coordinates)
// from the tile to its goal placement
    public int manhattan()
    {
        int k = 1;
        int sum = 0;
        for(int i = 0; i < n; ++i)
        {
            for(int j = 0; j < n; ++j)
            {
                if(board[i][j] != 0 && board[i][j] != k)
                {
                    //
                    int goal_x = (board[i][j]+n-1)%n;
                    int goal_y = (board[i][j]-1)/n;
                    int dx = Math.abs(j - goal_x);
                    int dy = Math.abs(i - goal_y);
                    sum+=(dx+dy);
                }
                k++;
            }
        }
        return sum;
    }

    public boolean isGoal()
    {
        int k = 1;
        for(int i = 0; i < n; ++i)
        {
            for(int j = 0; j < n; ++j)
            {
                if(board[i][j] != k && board[i][j]!=0) return false;
                k++;
            }
        }
        return true;
    }

    public boolean equals(Object y)
    {
        if(y == null) return false;
        return this.toString().equals(y.toString());
    }

    //all boards reachable in one move from this board
    public Iterable<Board> neighbors()
    {
        int x = -1, y = -1;
        ArrayList<Board> nghbrs = new ArrayList<>(); // ArrayList is iterable,
                                                     // so it can be used in this method
        //find 0 tile
        for(int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if(board[i][j] == 0){
                    x = j;
                    y = i;
                } } }
        if(x == 0)
        {

            Board nghbr1 = new Board(board);
            nghbr1.board[y][x] = board[y][x+1];
            nghbr1.board[y][x+1] = 0;
            nghbrs.add(nghbr1);
        }
        if(y == 0)
        {


            Board nghbr2 = new Board(board);
            nghbr2.board[y][x] = board[y+1][x];
            nghbr2.board[y+1][x] = 0;
            nghbrs.add(nghbr2);

        }
        if(x == n-1)
        {


            Board nghbr7 = new Board(board);
            nghbr7.board[y][x] = board[y][x-1];
            nghbr7.board[y][x-1] = 0;
            nghbrs.add(nghbr7);

        }
        if(y==n-1)
        {

            Board nghbr8 = new Board(board);
            nghbr8.board[y][x] = board[y-1][x];
            nghbr8.board[y-1][x] = 0;
            nghbrs.add(nghbr8);
        }
        if(x > 0 && x < n-1)
        {

            Board nghbr3 = new Board(board);
            nghbr3.board[y][x] = board[y][x+1];
            nghbr3.board[y][x+1] = 0;
            nghbrs.add(nghbr3);



            Board nghbr4 = new Board(board);
            nghbr4.board[y][x] = board[y][x-1];
            nghbr4.board[y][x-1] = 0;
            nghbrs.add(nghbr4);


        }
        if(y > 0 && y < n-1)
        {


            Board nghbr5 = new Board(board);
            nghbr5.board[y][x] = board[y+1][x];
            nghbr5.board[y+1][x] = 0;
            nghbrs.add(nghbr5);


            Board nghbr6 = new Board(board);
            nghbr6.board[y][x] = board[y-1][x];
            nghbr6.board[y-1][x] = 0;
            nghbrs.add(nghbr6);

        }

        return nghbrs;
    }

    // board in which any pair of tiles is swapped (excluding pair with '0' tile)
    // (in this case [[0][1] , [0][0]] or [[n-1][n-1], [n-1][n-2]],
    // which ensures no ArrayIndexOutOfBoundsException as the initial dimension is >2
    public Board twin()
    {
        int[][]b2 = new int[n][n];
        for(int i = 0; i < n; ++i)
            System.arraycopy(board[i], 0, b2[i], 0, n);
        if(board[0][0] == 0 || board[0][1] == 0) {
            b2[n-1][n-1] = board[n-1][n-2];
            b2[n-1][n-2] = board[n-1][n-1];
        }
        else {
            b2[0][0] = this.board[0][1];
            b2[0][1] = this.board[0][0];
        }
        Board twn = new Board(b2);
        return twn;
    }
}
