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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.Border;


//전체 사이즈 : 1024 X 768
public class GameRoom extends JPanel implements ActionListener {
	private static final Component btn_ready_i = null;
	public static double su[] = new double[24]; //고정된 난수 배열
	//공용 데이터 공통으로 쓰는 화면
	Image back;
	JButton[] dummy = new JButton[24]; // 처음에 올라가는 카드 더미
	JTextArea chatHistory;		
	JTextField chatInput;		
	JLabel gameMessage;
	JPanel mainPage;
	JLabel timeLabel;
	CountUpProgressBar cdpb; //timer Reset되는 방법 추가 및 경우의 수 넣기

	//세팅 값  및 버퍼들
	Image imgBuf;
	Image imgFixed;
	ImageIcon b1;
	Border border = BorderFactory.createLineBorder(Color.RED, 5);  //선택된 카드를 구분하기 위한 보더 설정값 (두께 5에 빨간색)
	Border borderEmpty = BorderFactory.createLineBorder(new Color(0,0,0,0),2); //
	RotatedIcon ri; // 공개 혹 미공개 된 덱을 구분하기 위한 이미지 돌리는 값.
	int set = 1; //player 구분 테스트용

	//Player1 용 데이터
	Image player1;
	JLabel avatar_1;
	JLabel[] play1 = new JLabel[12]; // player 1 덱이 올라갈 레이블
	Image[] buf = new Image[12]; //정렬된 이미지 출력용 이미지 배열
	double[] temp = {12,12,12,12,12,12,12,12,12,12,12,12}; // player 1 Deck 의 정렬된 숫자값을 가지고 있는 배열
	ArrayList<Double> tail = new ArrayList<Double>(); //난수 정렬용 리스트

	//Players2용 데이터
	Image player2;
	JLabel avatar_2; //플레이어 이미지 파일이 올라갈 레이블
	JLabel[] play2 = new JLabel[12]; // player 2 덱이 올라갈 레이블
	Image[] buf2 = new Image[12];
	double[] temp2 = {12,12,12,12,12,12,12,12,12,12,12,12}; //players 2 Deck 의 정렬된 숫자값을 가지고 있는 배열
	ArrayList<Double> tail2 = new ArrayList<Double>();

	GameRoom(){
		setLayout(null); //기본 레이아웃 무시
		chatHistory = new JTextArea ();
		chatInput = new JTextField();
		gameMessage = new JLabel();
		mainPage = new JPanel();
		cdpb = new CountUpProgressBar();

		cdpb.setBounds(600, 580, 100, 50);
		add(cdpb);

		int space = 55;

		back = Toolkit.getDefaultToolkit().getImage("images/gameBackground.jpg");
		player1 = Toolkit.getDefaultToolkit().getImage("images/Avatar/_11.jpg");
		player2 = Toolkit.getDefaultToolkit().getImage("images/Avatar/_33.jpg");

		avatar_1 = new JLabel(new ImageIcon(player1.getScaledInstance(90, 120, Image.SCALE_SMOOTH)));
		avatar_2 = new JLabel(new ImageIcon(player2.getScaledInstance(90, 120, Image.SCALE_SMOOTH)));

		getRand(su.length); //난수 static su[]배열에 삽입.

		// su = {23,1,4,5,6,67,3,4,3,2,2....
		for (int k=0; k<su.length;k++) {
			if(su[k]<12) {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_"+su[k]+".png");
			//imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_back.png");
			imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
			dummy[k] = new JButton(new ImageIcon(imgFixed));
			}else {
				su[k] = su[k]-12+0.5;
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_"+su[k]+".png");
			//imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_back.png");
			imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
			dummy[k] = new JButton(new ImageIcon(imgFixed));

			}
			if(k==0) {
				dummy[k].setBounds(30,260,45,65);
			}else if(k>0 && k<12) {
				dummy[k].setBounds(30+space,260,45,65);
				space += 55;
			}else if(k==12) {
				dummy[k].setBounds(30,350,45,65);
				space = 55;
			}else if(k>=13 && k<24) {
				dummy[k].setBounds(30+space,350,45,65);
				space += 55;
			}
			add(dummy[k]);
			dummy[k].addActionListener(this);
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
		chatHistory.setEnabled(false);
		
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
		
		// 채팅 이벤트 등록 
		chatInput.addActionListener(this);


	}
	@Override
	protected void paintComponent(Graphics g) { //스킨 입힐 때 , 백그라운드에 사용
		// 실제 동작하는 화면은 paint를 활용
		//super.paintComponent(g);
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// Chat
		String msg= chatInput.getText();
		if(e.getSource()==chatInput) {
			chatHistory.append(msg+"\n");
			if(msg ==null || msg.length()==0) {
				JOptionPane.showMessageDialog(null,"대화할 내용을 입력하세요","채팅창 경고",JOptionPane.WARNING_MESSAGE);
			}else 
				chatInput.setText("");
			}
		// Card
		for(int j=0; j<24; j++) {
			if(e.getSource()==dummy[j]) {
				if(set == 1) {
					dummy[j].setVisible(false); //기존 버튼 이미지 날리기
					if(su[j]>12) //블랙 화이트 구분하기 위한 숫자 변환
						su[j] = su[j]-12+0.5;
					tail.add(su[j]);
					Collections.sort(tail); // 리스트 정렬
						for(int k=0; k<tail.size();k++) {
							int l =0;
							buf[k] = setCardImage(tail.get(k));
							if(tail.size()==1 )
								l=5;
							else if(tail.size()==2)
								l=4;
							else if (tail.size()==3)
								l=3;
							else if (tail.size()==4)
								l=2;
							else if (tail.size()==5)
								l=1;
							else if( tail.size()>=6)
								l=0;
							temp[l+k] = tail.get(k);
							play1[l+k].setIcon(new ImageIcon(buf[k]));
							play1[l+k].setOpaque(true);
							play1[l+k].setBorder(borderEmpty);
//							ri = new RotatedIcon(new ImageIcon(buf[k]),RotatedIcon.Rotate.UPSIDE_DOWN);
//							play1[l+k].setIcon(ri);

						}
						int count = 0;
						for (int i=0; i<12; i++) {
							if(su[j]==temp[i]) {
								count =i;
								play1[count].setBorder(border);
								break;
							}
						}
						set = 0;
				}else if (set ==0) {
					dummy[j].setVisible(false); //기존 버튼 이미지 날리기
					if(su[j]>12) //블랙 화이트 구분하기 위한 숫자 변환
						su[j] = su[j]-12+0.5;
					tail2.add(su[j]);
					Collections.sort(tail2); // 리스트 정렬
						for(int k=0; k<tail2.size();k++) {
							int l =0;
							buf2[k] = setCardImage(tail2.get(k));
							if(tail2.size()==1 )
								l=5;
							else if(tail2.size()==2)
								l=4;
							else if (tail2.size()==3)
								l=3;
							else if (tail2.size()==4)
								l=2;
							else if (tail2.size()==5)
								l=1;
							else if( tail2.size()>=6)
								l=0;
							temp2[l+k] = tail2.get(k);
							play2[l+k].setIcon(new ImageIcon(buf2[k]));
							play2[l+k].setOpaque(true);
							play2[l+k].setBorder(borderEmpty);
//							ri = new RotatedIcon(new ImageIcon(buf2[k]),RotatedIcon.Rotate.UPSIDE_DOWN);
//							play2[l+k].setIcon(ri);

						}
						int count = 0;
						for (int i=0; i<12; i++) {
							if(su[j]==temp2[i]) {
								count =i;
								play2[count].setBorder(border);
								break;
							}
						}
						set = 1;
				}
			}
		}
	}


	public Image setCardImage(double a) {
		if(set==1)
			if(a%1.0!=0) {
				imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_"+a+".png");
			}else {
				imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_"+a+".png");
			}
		else if(set==0)
			if(a%1.0!=0) {
				imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_back.png");
			}else {
				imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_back.png");
			}

		imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
		return imgFixed;
	}

	public void getRand(int a) {
		boolean bCheck = false;
		for (int i=0; i<a; i++) { //스택틱 배열인 su에 중복되지 않은 난수를 넣음.
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
	}

}

