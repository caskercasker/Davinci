package com.sist.client;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class StartRoom1 extends JPanel{
	
   Image back;
   JPanel[] pans=new JPanel[2];
   JTextField[] ids=new JTextField[2];
//   JTextArea ta=new JTextArea();
//   JTextField tf=new JTextField();
   JButton b1,b2;
//   GameView games=new GameView();
   
   boolean[] sw=new boolean[6];
   
   public StartRoom1()
   {
	   setLayout(null);
	   back = Toolkit.getDefaultToolkit().getImage("images/gameBackground.jpg");
	   for(int i=0;i<2;i++)
	   {
		   pans[i]=new JPanel();
		   pans[i].setBackground(Color.black);
		   ids[i]=new JTextField();
		   ids[i].setEnabled(false);
	   }
	   setLayout(null);
	   pans[0].setBounds(130, 200, 160,199 );
	   pans[0].setLayout(new BorderLayout());
	   pans[0].add("Center",new JLabel(new ImageIcon(getImageSizeChange(new ImageIcon("c:\\image\\def.png"), 150, 120))));
	   ids[0].setBounds(130, 400, 100, 20);
	   
	   pans[1].setBounds(420, 200, 160,199 );
	   pans[1].setLayout(new BorderLayout());
	   pans[1].add("Center",new JLabel(new ImageIcon(getImageSizeChange(new ImageIcon("c:\\image\\def.png"), 150, 120))));
	   ids[1].setBounds(420, 400, 100, 20);
	   
	   for(int i=0;i<2;i++)
	   {
		   add(pans[i]);
		   add(ids[i]);
	   }
	   
		/*
		 * JScrollPane js=new JScrollPane(ta); js.setBounds(705, 10, 300, 680); add(js);
		 * 
		 * tf.setBounds(705, 695, 300, 30); add(tf);
		 */
	   
	   b1=new JButton("게임준비");
	   b2=new JButton("게임시작");
	   
	   JPanel p=new JPanel();
	   p.setLayout(new GridLayout(1,2,5,5));
	   p.add(b1);p.add(b2);
	   p.setBounds(235, 450, 240, 50);
	   add(p);
   }
   public Image getImageSizeChange(ImageIcon icon,int width,int height)
   {
   	Image img=icon.getImage();
   	Image change=img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
   	return change;
   }
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}
}