import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Solver {
	private int[][] puzzle;			
	private boolean solved = false;
	private Sudoku listerner;
		
	public Solver() {
		puzzle = new int[9][9];
	}
	
	// register a listener to this Solver 
	public void bindListerner(Sudoku listerner) {
		this.listerner = listerner;
	}
		
	// start the recursive procedure and report the result
	public void generateSolution() {
		if (solve(puzzle)){    // solves in place
			solved = true;
			listerner.stateHasChanged();		// notify the view to update
			printGrid(puzzle);
		}
		else {
            System.out.println("NONE");
		}
	}

	// Take a partially filled grid and try to fill all free cells
	// in order to find a solution
	private boolean solve(int[][] cells) {
        // find next free cell (x, y)
		// if there's none, a solution is found
		Point pt = new Point();
		if(!findNextFreeCell(cells, pt)) {
			return true;
		}		
		
		// a free cell is found, try to assign a safe value
		int x = (int)pt.getX();
		int y = (int)pt.getY();
		
        // consider number 1 to 9
		for (int val = 1; val <= 9; ++val) {
			// a safe number is found
			if (isSafe(x, y, val, cells)) {
                cells[x][y] = val;	// tentative assignment
                if (solve(cells))
                    return true;		// successful return
                cells[x][y] = 0; 	// failure, mark this cell to free again
            }
        }
        return false;	// no safe number for this cell, has to backtrack
    }


	private boolean findNextFreeCell(int[][] cells, Point pt) {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(cells[i][j] == 0) {
					pt.setLocation(i, j);
					return true;
				}
			}
		}
		return false;
	}

	
	// To be safe at (i, j), val must:
	// have no duplication in each row, col and subgrid
	public boolean isSafe(int i, int j, int val, int[][] cells) {
		for (int k = 0; k < 9; ++k)  // check col
            if (val == cells[k][j])
                return false;

        for (int k = 0; k < 9; ++k) // check row
            if (val == cells[i][k])
                return false;

        //subgrid index starts with 0, 3, 6 
        int boxRowOffset = (i / 3) * 3;
        int boxColOffset = (j / 3) * 3;
        for (int k = 0; k < 3; ++k) // box
            for (int m = 0; m < 3; ++m)
                if (val == cells[boxRowOffset+k][boxColOffset+m])
                    return false;

        return true; // no violations, so it's legal
	}


	public int[][] getPuzzle() {
		return puzzle;
	}


	public void setPuzzle(int[][] puzzle) {
		this.puzzle = puzzle;
		listerner.stateHasChanged();
	}
	
	public void setPuzzleCell(int x, int y, int val) {
		puzzle[x][y] = val;
	}

	
	// load puzzle from file
	public void setPuzzle(String filepath) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filepath));
			String line = "";
			int lineNo = 0;
			while((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				for(int i = 0; i < 9; i++) {
					puzzle[lineNo][i] = Integer.parseInt(tokens[i]);
				}
				lineNo++;
			}
			listerner.stateHasChanged();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void resetPuzzle() {
		puzzle = new int[9][9];
		listerner.stateHasChanged();
	}
	
	public int[][] getSolution() {
		if(solved) {
			return puzzle;
		} else {
			return null;
		}
	}
		
	static void printGrid(int[][] solution) {
        for (int i = 0; i < 9; ++i) {
            if (i % 3 == 0)
                System.out.println(" -----------------------");
            for (int j = 0; j < 9; ++j) {
                if (j % 3 == 0) System.out.print("| ");
                System.out.print(solution[i][j] == 0
                                 ? " "
                                 : Integer.toString(solution[i][j]));

                System.out.print(' ');
            }
            System.out.println("|");
        }
        System.out.println(" -----------------------");
    }

}
