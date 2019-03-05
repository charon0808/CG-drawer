package draw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {

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
		buttonPanel.add(b1);
		buttonPanel.add(b2);
		buttonPanel.add(b3);
		buttonPanel.setBackground(Color.white);
		this.add(buttonPanel, BorderLayout.NORTH);

		canvasPanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(image, 0, 0, null);
			}
		};
		this.add(canvasPanel, BorderLayout.CENTER);

		this.setSize(900, 600);
		this.setTitle("CG-drawer");
		this.setLocation(700, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setVisible(true);
	}

	public void updateImage(BufferedImage i) {
		image = i;
		canvasPanel.repaint();
	}

}
