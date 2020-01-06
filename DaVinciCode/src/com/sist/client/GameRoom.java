package com.sist.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;


//전체 사이즈 : 1024 X 768
public class GameRoom extends JPanel {
	private static final Component btn_ready_i = null;

	Image back;
	Image before,after;

	JTextPane chatHistory;
	JTextField chatInput;
	JLabel gameMessage;
	JPanel mainPage;
	JLabel avatar_1;
	JLabel avatar_2;
	JButton[] bt = new JButton[24];

	GameRoom(){
		setLayout(null); //기본 레이아웃 무시
		back = Toolkit.getDefaultToolkit().getImage("images\\gameBackground.jpg");

		chatHistory = new JTextPane();
		chatInput = new JTextField();
		gameMessage = new JLabel();
		mainPage = new JPanel();
		int space = 55;

		avatar_1 = new JLabel(new ImageIcon("images\\Avatar\\_11.jpg"));
		avatar_2 = new JLabel(new ImageIcon("images\\Avatar\\_33.jpg"));
		for(int i=0; i<24; i++)
		{
			bt[i] = new JButton(""+i);
		}

		for(int i=0; i<24; i++)
		{

			if(i==0)
				bt[i].setBounds(30, 260, 45, 65);
			else if (i>=1 && i<=11){

				bt[i].setBounds(30 + space, 260, 45, 65);
				space += 55;

			}
			else if(i == 12) {
				bt[i].setBounds(30, 350, 45, 65);
				space = 55;
			}
			else if(i>=13 && i<24) {
				bt[i].setBounds(30 + space, 350, 45, 65);
				space += 55;
			}
			add(bt[i]);
		}

		JScrollPane chatRm = new JScrollPane(chatHistory); 


		chatRm.setBounds(705, 10, 300, 680);
		chatInput.setBounds(705, 695, 300, 30);

		gameMessage.setBounds(10,645,690,80);
		gameMessage.setBackground(Color.white);
		gameMessage.setOpaque(true);

		mainPage.setBounds(10, 10, 690, 625);
		mainPage.setBackground(Color.gray);
		mainPage.setOpaque(false);

		avatar_1.setBounds(50, 50, 90, 120);
		avatar_2.setBounds(50, 470, 90, 120);

		add(chatRm);
		add(chatInput);
		add(gameMessage);
		add(mainPage);
		add(avatar_1);
		add(avatar_2);

	}
	@Override
	protected void paintComponent(Graphics g) { //스킨 입힐 때 , 백그라운드에 사용
		// 실제 동작하는 화면은 paint를 활용
		//super.paintComponent(g);
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}

}

