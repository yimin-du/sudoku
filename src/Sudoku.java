import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class Sudoku extends JFrame{

	private Solver solver;			
	private Controller controller;

	// UI control (sizes, colors and fonts)
	public static final int CELL_SIZE = 80;   
	public static final int CANVAS_WIDTH  = CELL_SIZE * 9;
	public static final int CANVAS_HEIGHT = CELL_SIZE * 9;

	// Board width/height in pixels
	public static final Color CELL_BGCOLOR = new Color(0x92, 0xEE, 0xF2);
	public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

	// The game board composes of 9x9 JTextcells,
	private Container cp;		// top content panel
	private JPanel[][] panels;		// each element is a subgrid
	private Cell[][] cells;	// each cell is a JTextField

	public Sudoku() {
		solver = new Solver();
		controller = new Controller();
		controller.setSolver(solver);
		controller.setSudoku(this);

		solver.bindListerner(this);
		cp = getContentPane();
		cp.setLayout(new GridLayout(3, 3));  // 9x9 GridLayout

		panels = new JPanel[3][3];
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				panels[y][x] = new JPanel(new GridLayout(3, 3));
				panels[y][x].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				cp.add(panels[y][x]);
			}
		}

		cells = new Cell[9][9];
		for (int y = 0; y < 9; y++) {
			for (int x = 0; x < 9; x++) {
				cells[y][x] = new Cell(y, x);
				cells[y][x].addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Cell cellSelected = (Cell) e.getSource();
						int xpos = cellSelected.x();
						int ypos = cellSelected.y();
						int val = Integer.parseInt(cellSelected.getText());
						if(!solver.valid(xpos, ypos, val, solver.getPuzzle())) {
							JOptionPane.showMessageDialog(Sudoku.this, "Invalid input");
							cellSelected.setText("");
							return;
						}
						
						System.out.println("x: " + cellSelected.x() + ", y: " + cellSelected.y());	
						solver.setPuzzleCell(xpos, ypos, val);

					}
					
				});

//				cells[y][x].getDocument().putProperty("owner", cells[y][x]);
//				cells[y][x].getDocument().addDocumentListener(new DocumentListener() {
//			
//					@Override
//					public void removeUpdate(DocumentEvent e) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void insertUpdate(DocumentEvent e) {
//
//						Cell cellSelected = (Cell) e.getDocument().getProperty("owner");
//						System.out.println(cellSelected.getText());
//						int xpos = cellSelected.x();
//						int ypos = cellSelected.y();
//						int val = Integer.parseInt(cellSelected.getText());
//						if(!solver.valid(xpos, ypos, val, solver.getPuzzle())) {
//							JOptionPane.showMessageDialog(Sudoku.this, "Invalid input");
//							cellSelected.setText("");
//						}
//						
//						System.out.println("x: " + cellSelected.x() + ", y: " + cellSelected.y());	
//						solver.setPuzzleCell(xpos, ypos, val);
//					}
//					
//					@Override
//					public void changedUpdate(DocumentEvent e) {
//						
//					}
//				});
				panels[y / 3][x / 3].add(cells[y][x]);
			}
		}

		// create menubars
		JMenuBar menubar = new JMenuBar();
		menubar.setOpaque(true);
		menubar.setBackground(new Color(154, 165, 127));
		menubar.setPreferredSize(new Dimension(200, 30));

		//Build File menu.
		JMenu controlMenu = new JMenu("Controls");

		// menu items under File
		JMenuItem newPuzzleMenuItem = new JMenuItem("Load Puzzle");
		newPuzzleMenuItem.addActionListener(controller);
		controlMenu.add(newPuzzleMenuItem);	

		JMenuItem createPuzzleMenuItem = new JMenuItem("Create Puzzle");
		createPuzzleMenuItem.addActionListener(controller);
		controlMenu.add(createPuzzleMenuItem);	


		JMenuItem solveMenuItem = new JMenuItem("Solve It");
		solveMenuItem.addActionListener(controller);
		controlMenu.add(solveMenuItem);		


		menubar.add(controlMenu);
		setJMenuBar(menubar);
		// Set the size of the content-pane and pack all the components
		//  under this container.
		cp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		cp.setBackground(Color.WHITE);
		pack();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
		setTitle("Sudoku");
		setVisible(true);

	}


	// update view when model changes
	public void stateHasChanged() {
		updateGrid(solver.getPuzzle());
	}

	public void updateGrid(int[][] grid) {
		for (int row = 0; row < 9; ++row) {
			for (int col = 0; col < 9; ++col) {
				String num = Integer.toString(grid[row][col]);
				if(grid[row][col] == 0)	num = "";
				cells[row][col].setText(num);     // set to empty string
				cells[row][col].setEditable(false);

				cells[row][col].setBackground(CELL_BGCOLOR);

				// Beautify all the cells
				cells[row][col].setHorizontalAlignment(JTextField.CENTER);
				cells[row][col].setFont(FONT_NUMBERS);
			}
		}

	}

	public void setEditable(boolean enable) {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(enable) {
					cells[i][j].setEditable(true);
				} else {
					cells[i][j].setEditable(false);
				}
			}
		}
	}
	
		
	public static void main(String[] args) {
		// Use System Look and Feel
		try { 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
		}
		catch (Exception ex) { 
			ex.printStackTrace(); 
		}
		new Sudoku();
	}
}