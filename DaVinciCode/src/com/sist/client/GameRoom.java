package com.sist.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;


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
	JLabel[] play1 = new JLabel[12]; // player 1 덱이 올라갈 레이블
	JLabel[] play2 = new JLabel[12]; // player 2 덱이 올라갈 레이블
	Image[] buf = new Image[12]; //정렬된 이미지 출력용 이미지 배열

	Border border = BorderFactory.createLineBorder(Color.RED, 5);
	Border borderEmpty = BorderFactory.createLineBorder(new Color(0,0,0,0),2);

	RotatedIcon ri;
	public static double su[] = new double[24]; //고정된 난수 배열
	double[] temp = {12,12,12,12,12,12,12,12,12,12,12,12};
	int bufArray[] = new int [12];

	ArrayList<Double> tail = new ArrayList<Double>(); //난수 정렬용 리스트

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
				//System.out.print(su[i]+" ");
			}
			System.out.print(su[i]+" ");
		}
		// su = {23,1,4,5,6,67,3,4,3,2,2....
		for (int k=0; k<su.length;k++) {
			if(su[k]<12) {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_"+su[k]+".png");
			//imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_back.png");
			imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
			bt[k] = new JButton(new ImageIcon(imgFixed));
			}else {
				su[k] = su[k]-12+0.5;
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_"+su[k]+".png");
			//imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_back.png");
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
		space = 0;
		for (int i=0; i<12; i++) {
			if(i<6) {
				play1[i] = new JLabel(new ImageIcon());
				play1[i].setBounds(250+space, 470, 45, 65);
				space += 55;
				play1[i].setBackground(Color.white);
				play1[i].setOpaque(false);
			}else {
				if(i==6) {
					space=0;
				}
				play1[i] = new JLabel(new ImageIcon());
				play1[i].setBounds(250+space, 540, 45, 65);
				space += 55;
				play1[i].setBackground(Color.white);
				play1[i].setOpaque(false);
			}
			add(play1[i]);
		}
		space =0;
		for (int i=0; i<12; i++) {
			if(i<6) {
				play2[i] = new JLabel(new ImageIcon());
				play2[i].setBounds(250+space, 50, 45, 65);
				space += 55;
				play2[i].setBackground(Color.white);
				play2[i].setOpaque(false);
			}else {
				if(i==6) {
					space=0;
				}
				play2[i] = new JLabel(new ImageIcon());
				play2[i].setBounds(250+space, 120, 45, 65);
				space += 55;
				play2[i].setBackground(Color.white);
				play2[i].setOpaque(false);
			}
			add(play2[i]);
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
		for(int j=0; j<24; j++) {
			if(e.getSource()==bt[j]) {
				bt[j].setVisible(false); //기존 버튼 이미지 날리기
				if(su[j]>12) //블랙 화이트 구분하기 위한 숫자 변환
					su[j] = su[j]-12+0.5;

				tail.add(su[j]);
				Collections.sort(tail); // 리스트 정렬

				for(int k=0; k<tail.size();k++) {
					int l =0;
					buf[k] = setCardImage(tail.get(k));
					if(tail.size()==1 || tail.size()==2)
						l=5;
					else if(tail.size()==3 || tail.size()==4)
						l=4;
					else if (tail.size()==5 || tail.size()==6)
						l=3;
					else if (tail.size()==7 || tail.size()==8)
						l=2;
					else if (tail.size()==9 || tail.size()==10)
						l=1;
					else if( tail.size()==11 || tail.size()==12)
						l=0;
					temp[l+k] = tail.get(k);
					play1[l+k].setIcon(new ImageIcon(buf[k]));
					play1[l+k].setOpaque(true);
					play1[l+k].setBorder(borderEmpty);
					ri = new RotatedIcon(new ImageIcon(buf[k]),RotatedIcon.Rotate.UPSIDE_DOWN);
					play1[l+k].setIcon(ri);

				}
				int count = 0;
				for (int i=0; i<12; i++) {
					if(su[j]==temp[i]) {
						count =i;
						break;
					}
				}
				play1[count].setBorder(border);
			}

				// 숫자에 해당하는 imgbuf파일을 만듬.
				//12칸의 배열에 정렬된 값을 넣는다.
				//play1[j].setIcon(new ImageIcon(imgFixed));
		}
	}


	public Image setCardImage(double a) {
		if(a%1.0!=0) {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_"+a+".png");
		}else {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_"+a+".png");
		}
		imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
		return imgFixed;
	}

}

