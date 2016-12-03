import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;

public class Cell extends JTextField{
	private int x, y;
	public static final Color CELL_BGCOLOR = new Color(0x92, 0xEE, 0xF2);
	public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
		setText("");
		setEditable(false);
		setBackground(CELL_BGCOLOR);

		setHorizontalAlignment(JTextField.CENTER);
		setFont(FONT_NUMBERS);
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
}
