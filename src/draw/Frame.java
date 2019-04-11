package draw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JPanel buttonPanel;
	public JPanel canvasPanel;
	public BufferedImage image;
	private JButton b1, b2, b3;
	int xx, yy;

	public Frame() {
		buttonPanel = new JPanel();
		b1 = new JButton("test1");
		b2 = new JButton("test2");
		b3 = new JButton("test3");

		b1.setPreferredSize(new Dimension(80, 30));
		b2.setPreferredSize(new Dimension(80, 30));
		b3.setPreferredSize(new Dimension(80, 30));

		buttonPanel.add(b1);
		buttonPanel.add(b2);
		buttonPanel.add(b3);
		buttonPanel.setBackground(Color.white);
		// this.add(buttonPanel, BorderLayout.NORTH);

		canvasPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(image, 0, 0, null);
			}
		};
		this.add(canvasPanel, BorderLayout.CENTER);
		this.setBackground(Color.WHITE);
		this.setSize(900, 600);
		this.setTitle("CG-drawer");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setVisible(true);
	}

	public void updateImage(BufferedImage i) {
		image = i;
		canvasPanel.repaint();
	}

}
