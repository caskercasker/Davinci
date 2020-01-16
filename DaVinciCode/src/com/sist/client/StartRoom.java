package com.sist.client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
public class StartRoom extends JPanel{

   Image back;
   JPanel[] pans=new JPanel[2];
   JTextField[] ids=new JTextField[2];
   JTextArea ta=new JTextArea();
   JTextField tf=new JTextField();
   JButton b1,b2,b3,b4; //b1게임준비, b2 게임 시작, b3 나가기 b4 강퇴하기 임시
//   GameView games=new GameView();

   boolean[] sw=new boolean[6];

   public StartRoom()
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
	   pans[0].add("Center",new JLabel(new ImageIcon(getImageSizeChange(new ImageIcon("c:\\image\\def.png"), 160, 199))));
	   ids[0].setBounds(130, 400, 100, 20);

	   pans[1].setBounds(420, 200, 160,199 );
	   pans[1].setLayout(new BorderLayout());
	   pans[1].add("Center", new JLabel(new ImageIcon(getImageSizeChange(new ImageIcon("c:\\image\\def.png"), 160, 199))));
	   ids[1].setBounds(420, 400, 100, 20);

	   for(int i=0;i<2;i++)
	   {
		   add(pans[i]);
		   add(ids[i]);
	   }

	   JScrollPane js=new JScrollPane(ta);
	   js.setBounds(705, 10, 300, 680);
	   add(js);

	   tf.setBounds(705, 695, 300, 30);
	   add(tf);

	   b1=new JButton("준비");
	   b2=new JButton("시작");
	   b3=new JButton("나가기");
	   b4=new JButton("강퇴");


	   JPanel p=new JPanel();
	   p.setLayout(new GridLayout(1,4,4,5));
	   p.add(b1);p.add(b2);p.add(b3); p.add(b4);
	   p.setBounds(130, 450, 450, 50);
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