import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Controller implements ActionListener{
	
	private Solver solver;
	private Sudoku sudoku;
	
	public void setSolver(Solver solver) {
		this.solver = solver;
	}
	
	public void setSudoku(Sudoku sudoku) {
		this.sudoku = sudoku;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Load Puzzle")) {
			System.out.println("new puzzle");
			openFile();
		} else if(e.getActionCommand().equals("Solve It")) {
			System.out.println("solve it");
			sudoku.setEditable(false);
			solver.generateSolution();
		} else if(e.getActionCommand().equals("Create Puzzle")) {
			System.out.println("create puzzle");
			solver.resetPuzzle();
			sudoku.setEditable(true);
		} 
	}
	
	private void openFile() {
		// choose file from current directory
		File workingDirectory = new File(System.getProperty("user.dir"));
		JFileChooser fc = new JFileChooser(workingDirectory);
		int returnVal = fc.showOpenDialog(fc);

		if (returnVal == JFileChooser.APPROVE_OPTION) {	// user click open
			File file = fc.getSelectedFile();
			String filepath = file.getPath();
			solver.setPuzzle(filepath);
			System.out.println("Opening: " + file.getName() + ".");
		} else {		// user click cancel
			System.out.println("Open command cancelled by user.");
		}


	}

}
