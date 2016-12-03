import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Solver {
	private int[][] puzzle;
	
	private boolean solved = false;
	private Sudoku listerner;
		
	public Solver() {
		puzzle = new int[9][9];
	}
	
	public void bindListerner(Sudoku listerner) {
		this.listerner = listerner;
	}
		
	public void generateSolution() {
		if (solve(0,0,puzzle)){    // solves in place
			solved = true;
			listerner.stateHasChanged();
			printGrid(puzzle);
		}
		else {
            System.out.println("NONE");
		}
	}

	private boolean solve(int i, int j, int[][] cells) {
        if (i == 9) {
            i = 0;
            if (++j == 9)
                return true;
        }
        if (cells[i][j] != 0)  // skip filled cells
            return solve(i+1,j,cells);

        for (int val = 1; val <= 9; ++val) {
            if (valid(i,j,val,cells)) {
                cells[i][j] = val;
                if (solve(i+1,j,cells))
                    return true;
            }
        }
        cells[i][j] = 0; // reset on backtrack
        return false;
    }


	public boolean valid(int i, int j, int val, int[][] cells) {
		for (int k = 0; k < 9; ++k)  // row
            if (val == cells[k][j])
                return false;

        for (int k = 0; k < 9; ++k) // col
            if (val == cells[i][k])
                return false;

        int boxRowOffset = (i / 3)*3;
        int boxColOffset = (j / 3)*3;
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

	public void setPuzzle(String filepath) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filepath));
			String line = "";
			int lineNo = 0;
			ArrayList<String> lines= new ArrayList<String>();
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
