package com.sist.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;


//전체 사이즈 : 1024 X 768
public class GameRoom extends JPanel implements ActionListener {
	private static final Component btn_ready_i = null;

	Image back;
	Image player1,player2; //플레이어 이미지 파일
	Image imgBuf;
	Image imgFixed;
	ImageIcon b1;

	JTextPane chatHistory;
	JTextField chatInput;
	JLabel gameMessage;
	JPanel mainPage;
	JLabel avatar_1, avatar_2; //플레이어 이미지 파일이 올라갈 레이블
	JButton[] bt = new JButton[24];
	JButton[] pl1 = new JButton[12]; // player 1 덱에 올라갈 버튼
	JButton[] pl2 = new JButton[12]; // player 2 덱에 올라갈 버튼

	public static int su[] = new int[24];

	GameRoom(){
		setLayout(null); //기본 레이아웃 무시
		chatHistory = new JTextPane();
		chatInput = new JTextField();
		gameMessage = new JLabel();
		mainPage = new JPanel();
		int space = 55;
		boolean bCheck = false;

		back = Toolkit.getDefaultToolkit().getImage("images/gameBackground.jpg");
		player1 = Toolkit.getDefaultToolkit().getImage("images/Avatar/_11.jpg");
		player2 = Toolkit.getDefaultToolkit().getImage("images/Avatar/_33.jpg");

		avatar_1 = new JLabel(new ImageIcon(player1.getScaledInstance(90, 120, Image.SCALE_SMOOTH)));
		avatar_2 = new JLabel(new ImageIcon(player2.getScaledInstance(90, 120, Image.SCALE_SMOOTH)));


		for (int i=0; i<su.length; i++) { //스택틱 배열인 su에 중복되지 않은 난수를 넣음.
			bCheck = true;
			while(bCheck) {
				bCheck=false;
				int rand = (int)(Math.random()*24);
				for(int j=0; j<i; j++) {
					if(su[j] == rand) {
						bCheck=true;
						break;
					}
				}
				su[i]=rand;
			}
		}

		// su = {23,1,4,5,6,67,3,4,3,2,2....
		for (int k=0; k<su.length;k++) {
			if(su[k]<12) {
			//imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_"+su[k]+".png");
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_back.png");
			imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
			bt[k] = new JButton(new ImageIcon(imgFixed));
			}else {
			//imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_"+(su[k]-12)+".png");
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_back.png");
			imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
			bt[k] = new JButton(new ImageIcon(imgFixed));

			}
			if(k==0) {
				bt[k].setBounds(30,260,45,65);
			}else if(k>0 && k<12) {
				bt[k].setBounds(30+space,260,45,65);
				space += 55;
			}else if(k==12) {
				bt[k].setBounds(30,350,45,65);
				space = 55;
			}else if(k>=13 && k<24) {
				bt[k].setBounds(30+space,350,45,65);
				space += 55;
			}
			add(bt[k]);
			bt[k].addActionListener(this);
		}

		pl1[0] = new JButton(new ImageIcon());
		pl1[0].setBounds(500, 470, 45, 65);
		add(pl1[0]);



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


		setLayout(null); //기본 레이아웃 무시

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
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==bt[0]) {
			System.out.println("버튼 들어옴");
			if(su[0]>12)
				su[0] = su[0]-12;
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_"+su[0]+".png");
			//imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_back.png");
			imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
			pl1[0].setIcon(new ImageIcon(imgFixed));
		}
	}

}

