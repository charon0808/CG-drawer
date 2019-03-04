package draw;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame implements MouseListener{

	private JPanel p;
	private JButton b1,b2,b3;
	
	public Frame() {
		p=new JPanel();
		b1=new JButton("test1");
		b2=new JButton("test2");
		b3=new JButton("test3");
		p.add(b1);
		p.add(b2);
		p.add(b3);
		p.setBackground(Color.white);
		
		this.add(p);
		
		this.setSize(400,300);
		this.setTitle("CG-drawer");
		this.setLocation(700,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
