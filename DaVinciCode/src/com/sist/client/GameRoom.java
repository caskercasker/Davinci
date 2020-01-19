package com.sist.client;

import java.awt.BorderLayout;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;


//전체 사이즈 : 1024 X 768
public class GameRoom extends JPanel implements ActionListener, MouseListener {
	private static final Component btn_ready_i = null;
	public static double su[] = new double[24]; //고정된 난수 배열
	//공용 데이터 공통으로 쓰는 화면
	Image back;							//배경이미지
	JButton[] dummy = new JButton[24]; // 처음에 올라가는 카드 더미
	JButton confirmGameEnd;		//게임 종료 버튼
	JTextArea chatHistory;		//채팅 내용
	JTextField chatInput;		//채팅 입력 창
	JLabel gameMessage;			//게임 메시지 띄우는 레이블
	int space = 55;				//고정된 간격값
	JOptionPane pane;

	boolean[] sw = new boolean[2];
	JPanel[] pans=new JPanel[6];
	JTextField[] ids=new JTextField[6];
	
	boolean deckChooseEnd=false;
	boolean deckSizeCheck=false;
	boolean dummyChooseCheck=false;



	boolean gameEndMessage = false;  //true일 경우 게임이 끝났음을 체크
	boolean dummyClickTurn= false;	//본격적인 게임 시작시 dummy 클릭 비활성화를 위한 flag
	boolean gameStart = false;		//게임 시작임을 알리는 flag
	
	int[] gameReadyCheck = {0,0}; //

	Object[] numbers = {"0", "1", "2", "3","4","5","6","7","8","9","10","11"};		//숫자를 고르기 위해 JOptionPane 에 출력될 숫자
	Object[] goOrStop = {"Yes","No"};												//숫자를 계속 맞출것인지 넘길것인지를 표시하는 옵션
	public static double choose;													//플에이어가 고른 숫자가 들어가는 값 0,1,2,3,4,5,6,7,8,9,10,11
	public static int option;														//숫자를 계속 맞출것인지 넘길것인지에 대한 리턴 값

	//세팅 값  및 버퍼들
	Image imgBuf;
	Image imgFixed;
	ImageIcon b1;
	Border border = BorderFactory.createLineBorder(Color.RED, 5);  						//선택된 카드를 구분하기 위한 보더 설정값 (두께 5에 빨간색)
	Border borderEmpty = BorderFactory.createLineBorder(new Color(0,0,0,0),2); 			//현재 들어오지 않은 카드를 설정하기 위한 보더 설정값


	int messageToPlayers = 0;
	int playerTurn;
	//Player1 용 데이터
	Image player1;																		//player1 이미지
	JLabel avatar_1;																	//player1 이미지 파일이 올라갈 레이블
	JLabel[] play1 = new JLabel[12]; 													//player1 덱이 올라갈 레이블
	Image[] imageBuf1 = new Image[12]; 													//player1의 정렬된 이미지 출력용 이미지 배열
	double[] temp = {12,12,12,12,12,12,12,12,12,12,12,12};							    // player1 Deck 의 정렬된 숫자값을 가지고 있는 배열
	public ArrayList<Double> tail = new ArrayList<Double>(); 							//player1 난수 정렬용 리스트
	int count = 0; 																		//현재 들어온 값 저장.Player1
	int gameEnd1 = 100;			//게임 승리조건 초기화(Player1이 패배할 경우)

	//Players2용 데이터
	Image player2;																		 ////player2 이미지
	JLabel avatar_2;																	 //player2 이미지 파일이 올라갈 레이블
	JLabel[] play2 = new JLabel[12];													 // player2 덱이 올라갈 레이블
	Image[] imageBuf2 = new Image[12];													//player2의 정렬된 이미지 출력용 이미지 배열
	double[] temp2 = {12,12,12,12,12,12,12,12,12,12,12,12};								 //players2 Deck 의 정렬된 숫자값을 가지고 있는 배열
	public ArrayList<Double> tail2 = new ArrayList<Double>();
	int count2 = 0; 																	//현재 들어온 값 저장.Player2
	int gameEnd2 = 1000;		//게임 승리조건 초기화 (Player2가 패배할 경우)


	
	GameRoom(){
		setLayout(null); 																//기본 레이아웃 무시

		pane = new JOptionPane();

//		dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		chatHistory = new JTextArea();													//채팅 기록
		chatInput = new JTextField();													//채팅 인풋
		gameMessage = new JLabel("카드 4장을 골라주세요", SwingConstants.CENTER); 			//메시지 초기값
		gameMessage.setFont(new Font("Serif", Font.BOLD, 20));

		//게임 종료 확인 버튼 설정 (게임이 종료시에만 드러난다)
		confirmGameEnd = new JButton("종료 ");
		confirmGameEnd.setFont(new Font ("Verdana",Font.BOLD,35));
		confirmGameEnd.setBounds(250,400,100,80);
		confirmGameEnd.setEnabled(false); //단순 비활성화
		confirmGameEnd.setVisible(false);
		//confirmGameEnd.addActionListener(this);

		//기본 이미지 설정 (추후에 대기방에서 데이터를 받아와야함.
		back = Toolkit.getDefaultToolkit().getImage("images/gameBackground.jpg");

		for(int i=0;i<2;i++)
		   {
			   pans[i]=new JPanel();
			   pans[i].setBackground(Color.black);
			   ids[i]=new JTextField();
			   ids[i].setEditable(false);
		   }
		   setLayout(null);


		   pans[0].setBounds(50, 470, 90, 120);
		   pans[0].setLayout(new BorderLayout());
		   pans[0].add("Center",new JLabel(new ImageIcon(getImageSizeChange(new ImageIcon("images/blank.png"), 160, 199))));
		   ids[0].setBounds(50, 595, 90, 20);

		   pans[1].setBounds(50, 50, 90, 120);
		   pans[1].setLayout(new BorderLayout());
		   pans[1].add("Center", new JLabel(new ImageIcon(getImageSizeChange(new ImageIcon("images/blank.png"), 160, 199))));
		   ids[1].setBounds(50, 175, 90, 20);

		   for(int i=0;i<2;i++)
		   {
			   add(pans[i]);
			   add(ids[i]);
		   }

		//getRand(su.length); 																			//난수 static su[]배열에 삽입.



		//발생한 난수에 따라 Dummy가 배치됨
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
			//dummy[k].addActionListener(this);
		}

		space = 0; //간격값 초기화

		//player 1의 레이블 배치
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
			//play1[i].addMouseListener(this);
		}

		space =0;
		//player2의 레이블 배치
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
			//play2[i].addMouseListener(this);
		}


		JScrollPane chatRm = new JScrollPane(chatHistory);
		chatRm.setBounds(705, 10, 300, 680);
		chatInput.setBounds(705, 695, 300, 30);
		chatHistory.setEditable(false);
		chatInput.addActionListener(this);
		chatRm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); //horizontal scroll 생기지 않도록
		chatHistory.setLineWrap(true); //아주 긴 내용 입력 시 자동으로 줄바뀜되도록

		//안내메시지 위치값 설정
		gameMessage.setBounds(10,645,690,80);
		gameMessage.setBackground(Color.white);
		gameMessage.setOpaque(true);

		add(chatRm);
		add(chatInput);
		add(gameMessage);
		add(confirmGameEnd);

		//선턴을  랜덤으로 정함.

//		int a = (int)(Math.random()*2);
//		System.out.println(a);
//		if(a==0) {
//			playerTurn =0;
//			System.out.println("나 선턴");
//			avatar_1.setBorder(border);
//			avatar_2.setBorder(borderEmpty);
//		}else if(a ==1) {
//			playerTurn =1;
//			System.out.println("상대방 선턴");
//			avatar_2.setBorder(border);
//			avatar_1.setBorder(borderEmpty);
//
//		}


	}

	@Override
	protected void paintComponent(Graphics g) { //스킨 입힐 때 , 백그라운드에 사용
		// 실제 동작하는 화면은 paint를 활용
		//super.paintComponent(g);
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	public Image setCardImage(double a) {											// 버튼클릭 혹은 입력한 숫자값에 해당하는 이미지를 세팅하기 위한 메소
			if(a%1.0!=0) {
				imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_"+a+".png");
			}else {
				imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_"+a+".png");
			}

		imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
		return imgFixed;
	}
	
	public Image setEnemyCardImage (double a) {
		if(a%1.0!=0) {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_back.png");
		}else {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_back.png");
		}

	imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
	return imgFixed;
	}

	public Image changeCardImage(double a) {										//플에이어 기준 없이 입력된 카드를 (필요 없을지도...
		if(a%1.0!=0) {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_"+a+".png");
		}else {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_"+a+".png");
		}
		imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);

		return imgFixed;
	}

	public Image reverseCardImage(double a) {
		if(a%1.0!=0) {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile_rev/w_tile_rev_"+a+".png");
		}else {
			imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile_rev/b_tile_rev_"+a+".png");
		}
		imgFixed = imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);

		return imgFixed;
	}

	public void messageStart(boolean b) {											//게임 시작만 알리는 전용 메소드
		if(b==true) {
			gameMessage.setText(convertToMultiline("게임을 시작합니다\n 카드를 한장 골라주세요("+playerTurn+")의 차례"));
		}

	}

	public void messageByPlyer(int a) {												//상황에 따른 메시지 출력용 메소드 (단 player1과 player2가 다르게 보여야 메시지를 구분하여 뿌리지 못한다.현재화면 한개)
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

	public void getRand(int a) {														//스택틱 배열인 su에 중복되지 않은 난수를 넣는 메소드
		boolean bCheck = false;
		for (int i=0; i<a; i++) {
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
	public void mouseClicked(MouseEvent e) {										//숫자 맞추기에 들어가는 메소드
	
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

	public boolean gameEndCheck()  {									//게임 종료조건 -> 비공개에서 공개로 된 카드 숫자와 , 현재 플레이어가 가진 카드수가 같다면 종료.
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
		if(gameEnd1-tail.size()==0) {
			messageByPlyer(9);
			System.out.println("종료 ");
			confirmGameEnd.setEnabled(true); //단순 비활성화
			confirmGameEnd.setVisible(true);
			gameEndMessage = true;

		}else if(gameEnd2-tail2.size()==0) {
			messageByPlyer(8);
			System.out.println("종료 ");
			confirmGameEnd.setEnabled(true); //단순 비활성화
			confirmGameEnd.setVisible(true);
			gameEndMessage = true;

		}
		System.out.println("gmeEnd1 = " + gameEnd1);
		System.out.println("tailSize = " + tail.size());
		System.out.println("gmeEnd2 = " + gameEnd2);
		System.out.println("tail2Size = " + tail2.size());
		return gameEndMessage;
	}

	//enable disable은 잘못된 입력값을 방지하기 위한 메소드
	public void enableLabel_1(int a) {
		for(int i=0; i<a; i++) {
			play1[i].addMouseListener(this);
		}
	}

	public void enableLabel_2(int a) {
		for(int i=0; i<a; i++) {
			play2[i].addMouseListener(this);
		}

	}
	public void disableLabel_1(int a) {
		for(int i=0; i<a; i++) {
			play1[i].removeMouseListener(this);
		}
	}

	public void disableLabel_2(int a) {
		for(int i=0; i<a; i++) {
			play2[i].removeMouseListener(this);
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

	public static String convertToMultiline(String orig)			//힌트 메시지 창에 출력되는 문자열을 나누기 위해 html 로 세팅
	{
	    return "<html>" + orig.replaceAll("\n", "<br>");
	}
	public Image getImageSizeChange(ImageIcon icon,int width,int height)
	   {
	   	Image img=icon.getImage();
	   	Image change=img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	   	return change;
	   }
}

