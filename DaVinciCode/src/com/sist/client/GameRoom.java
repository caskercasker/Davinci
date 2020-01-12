package com.sist.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;


//전체 사이즈 : 1024 X 768
public class GameRoom extends JPanel implements ActionListener, MouseListener {
	private static final Component btn_ready_i = null;
	public static double su[] = new double[24]; //고정된 난수 배열
	//공용 데이터 공통으로 쓰는 화면
	Image back;
	JButton[] dummy = new JButton[24]; // 처음에 올라가는 카드 더미
	JButton confirmGameEnd;
	JTextPane chatHistory;		//채팅 내용
	JTextField chatInput;		//채팅 입력 창
	JLabel gameMessage;
	JPanel mainPage;
	JLabel timeLabel;
	boolean gameStart = false;
	int gameEnd1 = 100;
	int gameEnd2 = 1000;
	boolean gameEndMessage = false;
	boolean dummyClickTurn= false;
	
	
	Object[] numbers = {"0", "1", "2", "3","4","5","6","7","8","9","10","11"};
	Object[] goOrStop = {"Yes","No"};
	CountUpProgressBar cdpb; //timer Reset되는 방법 추가 및 경우의 수 넣기
	public static double choose;
	public static int option;
	//세팅 값  및 버퍼들
	Image imgBuf;
	Image imgFixed;
	ImageIcon b1;
	Border border = BorderFactory.createLineBorder(Color.RED, 5);  //선택된 카드를 구분하기 위한 보더 설정값 (두께 5에 빨간색)
	Border borderEmpty = BorderFactory.createLineBorder(new Color(0,0,0,0),2); //
	RotatedIcon ri; // 공개 혹 미공개 된 덱을 구분하기 위한 이미지 돌리는 값.
	int playerTurn = 0; //player 구분 테스트용
	int messageToPlayers = 0;

	//Player1 용 데이터
	Image player1;
	JLabel avatar_1;
	JLabel[] play1 = new JLabel[12]; // player 1 덱이 올라갈 레이블
	Image[] imageBuf1 = new Image[12]; //정렬된 이미지 출력용 이미지 배열
	double[] temp = {12,12,12,12,12,12,12,12,12,12,12,12}; // player 1 Deck 의 정렬된 숫자값을 가지고 있는 배열
	public ArrayList<Double> tail = new ArrayList<Double>(); //난수 정렬용 리스트
	int[] reveal = {1,1,1,1,1,1,1,1,1,1,1,1};
	int count = 0; //현재 들어온 값 저장.
	//reveal = {1,1,1,1,1,1,1,1,1,1,1,1};

	//Players2용 데이터
	Image player2;
	JLabel avatar_2; //플레이어 이미지 파일이 올라갈 레이블
	JLabel[] play2 = new JLabel[12]; // player 2 덱이 올라갈 레이블
	Image[] imageBuf2 = new Image[12];
	double[] temp2 = {12,12,12,12,12,12,12,12,12,12,12,12}; //players 2 Deck 의 정렬된 숫자값을 가지고 있는 배열
	public ArrayList<Double> tail2 = new ArrayList<Double>();
	int[] reveal2 = {1,1,1,1,1,1,1,1,1,1,1,1};
	int count2 = 0;

	
	GameRoom(){
		setLayout(null); //기본 레이아웃 무시
		chatHistory = new JTextPane();
		chatInput = new JTextField();
		gameMessage = new JLabel("카드 4장을 골라주세요", SwingConstants.CENTER); //메시지 초기값
		gameMessage.setFont(new Font("Serif", Font.BOLD, 20));
		
		confirmGameEnd = new JButton("CONFIRM");
		confirmGameEnd.setFont(new Font ("Verdana",Font.BOLD,35));
		confirmGameEnd.setBounds(320,330,300,100);
		confirmGameEnd.setEnabled(false); //단순 비활성화
		confirmGameEnd.setVisible(false);
		//confirmGameEnd.setOpaque(true);
		confirmGameEnd.addActionListener(this);
		add(confirmGameEnd);
		
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
			play1[i].addMouseListener(this);
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
			play2[i].addMouseListener(this);
		}

		JScrollPane chatRm = new JScrollPane(chatHistory);

		
		
		
		chatRm.setBounds(705, 10, 300, 680);
		chatInput.setBounds(705, 695, 300, 30);
		
	
		//gameMessage.setText("카드 장을 골라주세요"); //고정 멘트
	
		gameMessage.setBounds(10,645,690,80);
		gameMessage.setBackground(Color.white);
		gameMessage.setOpaque(true);

		mainPage.setBounds(10, 10, 690, 625);
		mainPage.setBackground(Color.gray);
		mainPage.setOpaque(false);

		avatar_1.setBounds(50, 470, 90, 120);
		avatar_2.setBounds(50, 50, 90, 120);

		setLayout(null); //기본 레이아웃 무시

		add(chatRm);
		add(chatInput);
		add(gameMessage);
		add(mainPage);
		add(avatar_1);
		add(avatar_2);
		
		if(gameEnd1==tail.size()) {
			System.out.println("Player 패배");
			messageByPlyer(9);
		}else if (gameEnd2 == tail.size()) {
			System.out.println("상대방 승리");
			messageByPlyer(8);
		}
		
		
		
		
		
		///// 랜덤으로 턴을 정함.
		int a = (int)(Math.random()*2);
		System.out.println(a);
		if(a==0) {
			playerTurn =0; 
			System.out.println("나 선턴");
			avatar_1.setBorder(border);
			avatar_2.setBorder(borderEmpty);
		}else if(a ==1) {
			playerTurn =1;
			System.out.println("상대방 선턴");
			avatar_2.setBorder(border);
			avatar_1.setBorder(borderEmpty);
			
		}
		
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
		if(dummyClickTurn == false || (tail.size()>=4 || tail2.size()>=4)) {
			for(int j=0; j<24; j++) {
				if(e.getSource()==dummy[j]) {
					if(playerTurn == 0) {
						dummy[j].setVisible(false); //기존 버튼 이미지 날리기
						if(su[j]>12) //블랙 화이트 구분하기 위한 숫자 변환
							su[j] = su[j]-12+0.5;
						tail.add(su[j]);
						Collections.sort(tail); // 리스트 정렬
							for(int k=0; k<tail.size();k++) {
								imageBuf1[k] = setCardImage(tail.get(k));
								temp[k] = tail.get(k);
								play1[k].setIcon(new ImageIcon(imageBuf1[k]));
								play1[k].setOpaque( true);
								play1[k].setBorder(borderEmpty);
								if(tail.get(k)%0.5!=0) {
									double c = tail.get(k)-0.01;
									imageBuf1[k] = setCardImage(c);
									ri = new RotatedIcon(new ImageIcon(imageBuf1[k]),RotatedIcon.Rotate.UPSIDE_DOWN);
									play1[k].setBorder(borderEmpty);
									play1[k].setIcon(ri);
									//play1[k].setEnabled(false);
	
								}
							}
							for (int i=0; i<12; i++) {
								if(su[j]==temp[i]) {
									count =i;
									play1[count].setBorder(border);
									break;
								}
							}
							if((tail.size()<=4 || tail2.size()<=4) && dummyClickTurn ==false){
								turnChange();
								if(tail.size()!=0 || tail2.size()!=0) {
//									disableLabel_1(tail.size());
//									disableLabel_2(tail2.size());
								}							
							}							
							if(dummyClickTurn ==true) {
								disableDummy();
								System.out.println("내턴 상대방고르기");
								enableLabel_2(tail2.size());
//								disableLabel_1(tail.size());
								messageByPlyer(10);
							}
					}else if (playerTurn ==1) {
						dummy[j].setVisible(false); //기존 버튼 이미지 날리기
						if(su[j]>12) //블랙 화이트 구분하기 위한 숫자 변환
							su[j] = su[j]-12+0.5;
						tail2.add(su[j]);
						Collections.sort(tail2); // 리스트 정렬
							for(int k=0; k<tail2.size();k++) {
								imageBuf2[k] = setCardImage(tail2.get(k));
								temp2[k] = tail2.get(k);
								play2[k].setIcon(new ImageIcon(imageBuf2[k]));
								play2[k].setOpaque(true);
								play2[k].setBorder(borderEmpty);
								if(tail2.get(k)%0.5!=0) {
									double c = tail2.get(k)-0.01;
									//buf2[k] = setCardImage(c);
									play2[k].setIcon(new ImageIcon(changeCardImage(c)));
									play2[k].setBorder(borderEmpty);
									//play2[k].setEnabled(false);
									//play2[k].setIcon(ri);
								}
					
							}
							for (int i=0; i<12; i++) {
								if(su[j]==temp2[i]) {
									count2 =i;
									play2[count2].setBorder(border);
									break;
								}
							}
							if((tail.size()<=4 || tail2.size()<=4) && dummyClickTurn ==false) {
								turnChange();
								if(tail.size()!=0 || tail2.size()!=0) {
//									disableLabel_1(tail.size());
//									disableLabel_2(tail2.size());
								}
							}							
							if(dummyClickTurn ==true) {
								disableDummy();		
								System.out.println("상대턴 내꺼 고르기");
								enableLabel_1(tail.size());
//								disableLabel_2(tail2.size());
								messageByPlyer(10);
							}
					}
				}
			}	
		}
		if(tail.size()==4 && tail2.size()==4){
			gameStart=true;
			messageStart(gameStart);
			dummyClickTurn = true; //게임이 시작되었다. 
		}
	}



	public Image setCardImage(double a) {
		if(playerTurn==0)
			if(a%1.0!=0) {
				imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_"+a+".png");
			}else {
				imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_"+a+".png");
			}
		else if(playerTurn==1)
			if(a%1.0!=0) {
				imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_back.png");
			}else {
				imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_back.png");
			}

		imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
		return imgFixed;
	}
	
	public Image changeCardImage(double a) {
		if(a%1.0!=0) {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_"+a+".png");
		}else {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_"+a+".png");
		}
		imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
		
		return imgFixed;
	}
	
	public void messageStart(boolean b) {
		if(b==true) {
			gameMessage.setText(convertToMultiline("게임을 시작합니다\n 카드를 한장 골라주세요("+playerTurn+")의 차례"));
		}
		
	}
	
	public void messageByPlyer(int a) {
		if(a==1) {
			gameMessage.setText("카드를 한장 골라주세요");
		}else if (a==2) {
			gameMessage.setText("상대방이 카드를 고르고 있습니다.");
		}else if (a==3) {
			gameMessage.setText(convertToMultiline("상대방의 카드에서 1장을 선택해주세요.\n상대방의 카드 숫자는 무엇일까요?\n 01,2,3,4,5,6,7,8,9,10,11,"));
		}else if (a==4) {
			gameMessage.setText(convertToMultiline("틀렸습니다.새로 가져온 카드의 수가 공개되었습니다.\n 카드를 한장 골라주세요"));			
		}else if (a==5 ) {
			gameMessage.setText(convertToMultiline("맞았습니다.\n 한 번 숫자를 맞춰보실래요 ? Yes or No"));
		}else if (a==6) {
			gameMessage.setText("상대방이 카드를 선택중입니다.");
		}else if (a==7) {
			gameMessage.setText(convertToMultiline("상대방이 틀렸습니다.\n 상대방의 카드가 하나 공개되었습니다."));
		}else if (a==8) {
			gameMessage.setText("상대방의 모든 카드를 맞췄습니다. 승리 ");
		}else if (a==9) {
			gameMessage.setText("내 카드가 모두 다 공개되었습니다. ");
		}else if (a==10) {
			gameMessage.setText("상대방의 덱에서 카드하나를 더블클릭후 숫자를 맞춰 보세요");
		}
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
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		for(int i =0; i<12;i++) {
			if(e.getSource()==play2[i]) {
				if(e.getClickCount()==2) {
					choose = JOptionPane.showOptionDialog(null, "숫자를 고르세요","상대카드", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, numbers, numbers[0]);
					System.out.println("------");
					System.out.println(choose);
					System.out.println("------");
					System.out.println(temp2[i]);
					
					double tempValue = 0;
					if(temp2[i]%1.0!=0) {
						System.out.println("계산");
						tempValue = temp2[i] - 0.5;
						System.out.println(tempValue);
					}
					System.out.println(count);
					
					if(tempValue == choose || temp2[i] == choose) {
						System.out.println("맞음");
						play2[i].setIcon(new ImageIcon(changeCardImage(temp2[i])));
						play2[i].setBorder(borderEmpty);
						tail2.set(i, tail2.get(i)+0.01);
						gameEndCheck();
						if(gameEndMessage==true){
							break;
						}else if(gameEndMessage == false) {
							option = JOptionPane.showOptionDialog(null, "한 번더 숫자를 맞춰보실래요 ?","GoOrStop", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, goOrStop, goOrStop[0]);
							System.out.println(option); //맞으면 0 틀리면 1
							if (option ==0) {
								messageByPlyer(1);
								enableDummy();
//								disableLabel_1(tail.size());
//								disableLabel_2(tail2.size());
							}else if(option ==1) {
								turnChange();
								enableDummy();
								messageByPlyer(6);
//								disableLabel_1(tail.size());
//								disableLabel_2(tail2.size());
							}
						}
					}else {
						System.out.println("틀림");
						enableDummy();
//						disableLabel_1(tail.size());
//						disableLabel_2(tail2.size());
						tail.set(count, tail.get(count)+0.01);
						ri = new RotatedIcon(new ImageIcon(imageBuf1[count]),RotatedIcon.Rotate.UPSIDE_DOWN);
						play1[count].setBorder(borderEmpty);
						play1[count].setIcon(ri);
						messageByPlyer(4);
						turnChange();

					}
				}	
			}else if (e.getSource() == play1[i]) {
				if(e.getClickCount()==2) {
					choose = JOptionPane.showOptionDialog(null, "숫자를 고르세요","상대카드", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, numbers, numbers[0]);
					System.out.println("------");
					System.out.println(choose);
					System.out.println("------");
					System.out.println(temp[i]);
					
					double tempValue = 0;
					if(temp[i]%1.0!=0) {
						System.out.println("계산");
						tempValue = temp[i] - 0.5;
						System.out.println(tempValue);
					}
					System.out.println(count);
					
					if(tempValue == choose || temp[i] == choose) {
						System.out.println("맞음");
						
						ri = new RotatedIcon(new ImageIcon(imageBuf1[i]),RotatedIcon.Rotate.UPSIDE_DOWN);
						play1[i].setBorder(borderEmpty);
						play1[i].setIcon(ri);
						tail.set(i, tail.get(i)+0.01);
						
						gameEndCheck();
						if(gameEndMessage==true){
							break;
						}else if(gameEndMessage == false) {
							option = JOptionPane.showOptionDialog(null, "한 번더 숫자를 맞춰보실래요 ?","GoOrStop", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, goOrStop, goOrStop[0]);
							System.out.println(option); //맞으면 0 틀리면 1
							if (option ==0) {
								messageByPlyer(1);
								enableDummy();
//								disableLabel_1(tail.size());
//								disableLabel_2(tail2.size());
							}else if(option ==1) {
								turnChange();
								messageByPlyer(6);
								enableDummy();
//								disableLabel_1(tail.size());
//								disableLabel_2(tail2.size());
							}
						}
					}else {
						System.out.println("틀림");
						enableDummy();
//						disableLabel_1(tail.size());
//						disableLabel_2(tail2.size());
						tail2.set(count2, tail2.get(count2)+0.01);
						play2[count2].setIcon(new ImageIcon(changeCardImage(temp2[count2])));
						play2[count2].setBorder(borderEmpty);
						messageByPlyer(4);
						turnChange();

					}
				}
			}			
		}
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void turnChange() { //내가 1 상대가 0
		if(playerTurn==1) {
			playerTurn = 0; 
			avatar_1.setBorder(border);
			avatar_2.setBorder(borderEmpty);
			System.out.println("턴이 나에게로 옴");
		}else if(playerTurn ==0) {
			playerTurn =1;
			avatar_2.setBorder(border);
			avatar_1.setBorder(borderEmpty);
			System.out.println("턴이 상대방으로 감");
		}
	}
	
	public void gameEndCheck()  {
		gameEnd1 =0;
		gameEnd2 =0;

		for(int check=0; check<tail.size();check++) {
			if (tail.get(check)%0.5!=0) 
				gameEnd1 +=1;						
		}
		for (int cc = 0; cc<tail2.size(); cc++) {
			if (tail2.get(cc)%0.5!=0) 
			gameEnd2 +=1;
		}
		System.out.println("게임 체크함");
		if(gameEnd1 == tail.size()) {
			messageByPlyer(9);
			System.out.println("player 2 승리");
			confirmGameEnd.setEnabled(true); //단순 비활성화
			confirmGameEnd.setVisible(true);
			gameEndMessage = true;

		}else if(gameEnd2 == tail.size()) {
			messageByPlyer(8);
			System.out.println("player 1 승리");
			confirmGameEnd.setEnabled(true); //단순 비활성화
			confirmGameEnd.setVisible(true);
			gameEndMessage = true;

		}
		System.out.println("gmeEnd1 = " + gameEnd1);
		System.out.println("tailSize = " + tail.size());
		System.out.println("gmeEnd2 = " + gameEnd2);
		System.out.println("tail2Size = " + tail2.size());
	}
	
	public void enableLabel_1(int a) {
		for(int i=0; i<a; i++) {
			play1[i].setEnabled(true);
		}
	}
	
	public void enableLabel_2(int a) {
		for(int i=0; i<a; i++) {
			play2[i].setEnabled(true);
		}
		
	}
	public void disableLabel_1(int a) {
		for(int i=0; i<a; i++) {
			play1[i].setEnabled(false);
		}			
	}
	
	public void disableLabel_2(int a) {
		for(int i=0; i<a; i++) {
			play2[i].setEnabled(false);
		}
	}
	public void disableDummy() {
		for (int i=0; i<24; i++) {
				dummy[i].setEnabled(false);
				//dummy[i].setVisible(false);
			}
		
	}
	
	public void enableDummy() {
		for (int i=0; i<24; i++) {
			dummy[i].setEnabled(true);
			//dummy[i].setVisible(true);
		}
	}
	
	public static String convertToMultiline(String orig)
	{
	    return "<html>" + orig.replaceAll("\n", "<br>");
	}
}

