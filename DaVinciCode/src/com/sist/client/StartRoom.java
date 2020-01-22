package com.sist.client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
public class StartRoom extends JPanel{

   JPanel[] pans=new JPanel[2];
   JTextField[] ids=new JTextField[2];

   JButton b1,b3; //b1게임준비, b2 게임 시작, b3 나가기 b4 강퇴하기 임시
   JTextArea chatHistory; // 채팅 내용
   JTextField chatInput; // 채팅 입력 창

   Image back;


   boolean[] sw=new boolean[6];

   public StartRoom()
   {
	   // Player info - Avatar, id
	   setLayout(null);
	   back = Toolkit.getDefaultToolkit().getImage("images/gameBackground.jpg");
	   for(int i=0;i<2;i++)
	   {
		   pans[i]=new JPanel();
		   pans[i].setBackground(Color.black);
		   ids[i]=new JTextField();
		   ids[i].setEditable(false);
	   }
	   setLayout(null);
	   pans[0].setBounds(130, 200, 160,199 );
	   pans[0].setLayout(new BorderLayout());
	   pans[0].add("Center",new JLabel(new ImageIcon(getImageSizeChange(new ImageIcon("c:\\image\\def.png"), 160, 199))));
	   ids[0].setBounds(160, 400, 100, 20);
	   ids[0].setHorizontalAlignment(JTextField.CENTER);

	   pans[1].setBounds(420, 200, 160,199 );
	   pans[1].setLayout(new BorderLayout());
	   pans[1].add("Center", new JLabel(new ImageIcon(getImageSizeChange(new ImageIcon("c:\\image\\def.png"), 160, 199))));
	   ids[1].setBounds(450, 400, 100, 20);
	   ids[1].setHorizontalAlignment(JTextField.CENTER);
	   for(int i=0;i<2;i++)
	   {
		   add(pans[i]);
		   add(ids[i]);
	   }


	   // Buttons
	   b1=new JButton("준비");
	   b3=new JButton("나가기");

	 
	   JPanel p=new JPanel();
	   p.setLayout(new GridLayout(1,4,4,5));
	   p.setBounds(130, 450, 450, 50);




	   // Chatting
	   chatHistory = new JTextArea(); // 채팅 기록
	   chatInput = new JTextField(); // 채팅 인풋
	   JScrollPane chatRm = new JScrollPane(chatHistory);
	   chatRm.setBounds(705, 10, 300, 680);
	   chatInput.setBounds(705, 695, 300, 30);
	   // 채팅창 세부 옵션
	   chatHistory.setEditable(false);
	   chatRm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // horizontal scroll 생기지 않도록
	   chatHistory.setLineWrap(true); // 아주 긴 내용 입력 시 자동으로 줄바뀜되도록

	   p.add(b1);p.add(b3);


	   add(p);
	   add(chatRm);
	   add(chatInput);
 }

   public Image getImageSizeChange(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image change = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return change;
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}
}
