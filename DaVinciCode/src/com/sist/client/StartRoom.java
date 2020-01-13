package com.sist.client;
import java.util.EventObject;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



//전체 사이즈 : 1024 X 768
public class StartRoom extends JPanel implements ActionListener {
	
	private static final Color Color = null;
	private static final String WHITE = null;
	Image back;
	JTextArea chatHistory; 
	JTextField chatInput;	
	JButton btn_ready_1,btn_ready_2;
	JLabel showMyID,showOtherID,gameMessage;
	
	Image host;
	ImageIcon ava1,ava2;
	JLabel ava1Box,ava2Box,hostBox;
	Font f1,f2,f3;
	int count=0;
	// 임시방편으로 count<-- 방장(0),상대방(1) 가정
	// count=1 넣으면 상대방(1)로 플레이가능
	
	public static int buffer;
	

	StartRoom(){
		setLayout(null); 
		back = Toolkit.getDefaultToolkit().getImage("images/gameBackground.jpg");
		
		chatHistory = new JTextArea();
		chatInput = new JTextField();
		btn_ready_1 = new JButton("Start");
		btn_ready_2 = new JButton("Ready");
		gameMessage = new JLabel("상대방의 접속을 기다리고 있습니다", SwingConstants.CENTER);
		showMyID = new JLabel("Monariza1", SwingConstants.CENTER);
		showOtherID = new JLabel("접속 대기 중", SwingConstants.CENTER);
		
		System.out.println("=======================");
		System.out.println();
		// Message on top
		
		btn_ready_1.setEnabled(false);
		// 임시방편으로 게임룸에 가려면  이 코드에 주석 넣고 int count==0
		
		if(count==0)
		{
			btn_ready_2.setEnabled(false);
			// Ready버튼 비활성화			
			// Show avatar1 image
			ava1 = new ImageIcon("images/Avatar/_"+buffer+buffer+"jpg");
			ava1Box = new JLabel(ava1);
			ava1Box.setBounds(130, 200, 160, 199);
			add(ava1Box);
		}
		
		if(count==1)
		{
			messageByPlyer(1);
			showOtherID = new JLabel("Aziranom2",SwingConstants.CENTER);
			
			// Show avatar2 image			
			ava2 = new ImageIcon("images/Avatar/_22.jpg");
			ava2Box = new JLabel(ava2);
			ava2Box.setBounds(420, 200, 160, 199);
			add(ava2Box);
			if(count==0)
			{
				messageByPlyer(2);
			}
		}
		
		gameMessage.setBounds(10,645,690,80);
		gameMessage.setBackground(Color.white);
		gameMessage.setOpaque(true);
		
		// Chatting room on right 
		JScrollPane chatRm = new JScrollPane(chatHistory); 
		chatRm.setBounds(705, 10, 300, 680); 
		chatInput.setBounds(705, 695, 300, 30); 		
		chatHistory.setEnabled(false);
		add(chatRm);
		add(chatInput);
		gameMessage.setFont(new Font("Serif", Font.BOLD, 20));
		add(gameMessage);
		
		// 채팅 레디버튼 이벤트 등록 
		chatInput.addActionListener(this);		
		btn_ready_2.addActionListener(this);
		
		// Show ID 
		showMyID.setBounds(130, 400, 100, 20);
		add(showMyID);
		showMyID.setBackground(Color.lightGray);
		showMyID.setOpaque(true);
		showOtherID.setBounds(420, 400, 100, 20);
		add(showOtherID);
		showOtherID.setBackground(Color.lightGray);
		showOtherID.setOpaque(true);
		
		//방장 아이콘 표시
		host = Toolkit.getDefaultToolkit().getImage("C:\\host.jpg");
		hostBox = new JLabel(new ImageIcon(host.getScaledInstance(55,30, Image.SCALE_SMOOTH)));
		hostBox.setBounds(120, 270, 55, 30);
		add(hostBox);
		
		// Ready button on bottom 
		btn_ready_1.setBounds(130, 450, 160, 40);
		add(btn_ready_1);
		
		btn_ready_2.setBounds(420, 450, 160, 40);
		add(btn_ready_2);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btn_ready_2) 
				{
				if(count==0) 
					{
					btn_ready_1.setEnabled(true);
					btn_ready_2.setEnabled(false);
					messageByPlyer(3);
					if(count==1) 
						{
						btn_ready_2.setEnabled(false);
						messageByPlyer(4);
						}
					// 상대방이 스타트 버튼을 누르면 방장에게 msg3메세지가 출력되고
					// 상대방의 레디버튼 비활성화 + 방장에게 스타트버튼이 활성화
					}
				}
				else if(e.getSource()==chatInput) {
					String msg= chatInput.getText();
					chatHistory.append(msg+"\n");
					if(msg ==null || msg.length()==0) {
						JOptionPane.showMessageDialog(null,"대화할 내용을 입력하세요","채팅창 경고",JOptionPane.WARNING_MESSAGE);
					}else {
						chatInput.setText("");
					}
				}
	}
	
	public void messageByPlyer(int a) {												//상황에 따른 메시지 출력용 메소드 (단 player1과 player2가 다르게 보여야 메시지를 구분하여 뿌리지 못한다.현재화면 한개)
		if(a==1) {
			gameMessage.setText("게임할 준비가 되었다면 Ready 버튼을 눌러주세요.");
		}else if (a==2) {
			gameMessage.setText("상대방의 준비를 기다리고 있습니다");	
		}else if (a==3) {
			gameMessage.setText("방장이 Start버튼을 누르면 게임이 시작됩니다.");
		}else if (a==4) {
			gameMessage.setText("Start버튼을 누르면 게임이 시작됩니다.");
		}else if (a==5) {
			gameMessage.setText("방장이 접속을 종료하여 방장이 되었습니다.");
		}
	}
}	

//private int clientOne, clientTwo;
//private String userOne, userTwo;
//
//private int divisionNN = 0;
//
//public void updateData(ServerData data) {
//
//	ServerData sndData = null;
//	int count = cp.getLogName().size();
//	switch (data.getState()) {
//
//	case ServerData.LOGIN:
//		stateMessage(data.getName() + "님이 접속하였습니다. 접속자수:" + (count + 1)					+ "\n");
//		divisionNN = count;
//		sndData = new ServerData(data.getName(), "하이", ServerData.LOGIN,
//				divisionNN);
//
//		if (count == 0) {
//			clientOne = 1;
//			userOne = data.getName();
//			
//		} else if (count == 1) {
//			clientTwo = 2;
//			userTwo = data.getName();
//		}
//		cp.addClient(st);
//		cp.broadCasting(sndData);
//		break;
//		
//	case ServerData.SENDDATA:
//		if (data.getDivision() == 11) {
//			stateMessage(userOne + "에게 데이터를 보냅니다.\n");
//			sndData = new ServerData(userOne, "One Client!",
//				ServerData.SENDDATA, 11);
//			cp.addClient(st);
//			cp.broadCasting(sndData);
//			break;
//		} else if (data.getDivision() == 12) {
//			stateMessage(userTwo + "에게 데이터를 보냅니다.\n");
//			sndData = new ServerData(userTwo, "Two Client!",
//					ServerData.SENDDATA, 12);
//			cp.addClient(st);
//			cp.broadCasting(sndData);
//			break;
//		} else if (data.getDivision() == 13) {
//			stateMessage(userThree + "에게 데이터를 보냅니다.\n");
//			sndData = new ServerData(userThree, "Three Client!",
//					ServerData.SENDDATA, data.getDivision());
//
//			cp.broadCasting(sndData);
//			break;
//		}
//		break;
//}
//https://jsieun73.tistory.com/72
//네트워크 도입 후 코드작성 예시

