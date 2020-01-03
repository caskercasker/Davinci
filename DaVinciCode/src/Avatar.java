import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Avatar extends JPanel{
	JButton b1,b2,b3,b4,b5;
	Image back;
	Avatar(){
		setLayout(null); 
		back = Toolkit.getDefaultToolkit().getImage("c:\\image\\gameground.jpg");
		Image image;
		
		b1=new JButton(new ImageIcon("c:\\image\\_11.jpg"));
		ImageIcon pressedIcon1 = new ImageIcon("c:\\image\\_111.jpg");   //눌렀을때 이미지 변경 아직 미구현
		b2=new JButton(new ImageIcon("c:\\image\\_22.jpg"));
		ImageIcon pressedIcon2 = new ImageIcon("c:\\image\\");
		b3=new JButton(new ImageIcon("c:\\image\\_33.jpg"));
		ImageIcon pressedIcon3 = new ImageIcon("c:\\image\\");
		b4=new JButton(new ImageIcon("c:\\image\\_44.jpg"));
		ImageIcon pressedIcon4 = new ImageIcon("c:\\image\\");
		b5=new JButton("결정");
		
		
		
		add(b1);
		b1.setBounds( 90, 175, 160, 199);
		b1.setOpaque(false);
		add(b2);
		b2.setBounds( 303, 175, 160, 199);
		b2.setOpaque(false);
		add(b3);
		b3.setBounds( 516, 175, 160, 199);
		b3.setOpaque(false);
		add(b4);
		b4.setBounds( 729, 175, 160, 199);
		b4.setOpaque(false);
		add(b5);
		b5.setBounds( 450, 550, 100, 50);
		b5.setOpaque(false);
	}

	


	@Override
	protected void paintComponent(Graphics g) { 
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}

}

