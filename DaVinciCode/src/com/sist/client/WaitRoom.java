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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;



//전체 사이즈 : 1024 X 768
public class WaitRoom extends JPanel implements ActionListener {
	
	private static final Color Color = null;
	private static final String WHITE = null;
	Image back;
	JTextArea chatHistory; 
	JTextField chatInput;	
	JButton btn_ready_1,btn_ready_2;
	JLabel msg1,msg2,msg3,showMyID,showOtherID;
	
	Image host;
	ImageIcon ava1,ava2;
	JLabel ava1Box,ava2Box,hostBox;
	Font f1,f2,f3;
	int count=0;
	// 임시방편으로 count<-- 방장(0),상대방(1) 가정
	// count=1 넣으면 상대방(1)로 플레이가능
	
	public static int buffer;
	

	WaitRoom(){
		setLayout(null); 
		back = Toolkit.getDefaultToolkit().getImage("images/gameBackground.jpg");
		
		chatHistory = new JTextArea();
		chatInput = new JTextField();
		btn_ready_1 = new JButton("Start");
		btn_ready_2 = new JButton("Ready");
		System.out.println("=======================");
		System.out.println();
		// Message on top
		
		btn_ready_1.setEnabled(false);
		// 임시방편으로 게임룸에 가려면 이 코드에 주석 넣어주세요.
			
		
		f1 = new Font("돋움", Font.BOLD, 15);
		f2 = new Font("돋움", Font.BOLD, 15);
		f3 = new Font("돋움", Font.BOLD, 15);
		showMyID = new JLabel("Monariza1");
		
		if(count==0)
		{
			btn_ready_2.setEnabled(true);
			// Ready버튼 비활성화
			msg1 = new JLabel("상대방의 준비를 기다리고 있습니다.");
			msg1.setFont(f2);
			msg1.setBounds(190, 100, 500, 20);
			add(msg1);
			showOtherID = new JLabel("접속 대기 중");
		}
		
		if(count==1)
		{
			msg2 = new JLabel("게임할 준비가 되었다면 Ready 버튼을 눌러주세요.");
			msg2.setFont(f1);
			msg2.setBounds(190, 100, 500, 20);
			add(msg2);
			showOtherID = new JLabel("Aziranom2");
		}
		
		// Chatting room on right 
		JScrollPane chatRm = new JScrollPane(chatHistory); 
		chatRm.setBounds(705, 10, 300, 680); 
		chatInput.setBounds(705, 695, 300, 30); 		
		chatHistory.setEnabled(false);
		add(chatRm);
		add(chatInput);		
		// 채팅 레디버튼 이벤트 등록 
		chatInput.addActionListener(this);		
		btn_ready_2.addActionListener(this);
		
		// Show ID 
		showMyID.setBounds(130, 220, 200, 40);
		add(showMyID);
		
		showOtherID.setBounds(420, 220, 200, 40);
		add(showOtherID);
		
		// Show avatar image
		ava1 = new ImageIcon("images/Avatar/_"+buffer+buffer+"jpg");
		ava1Box = new JLabel(ava1);
		ava1Box.setBounds(130, 280, 160, 199);
		add(ava1Box);
		
		ava2 = new ImageIcon("images/Avatar/_22.jpg");
		ava2Box = new JLabel(ava2);
		ava2Box.setBounds(420, 280, 160, 199);
		add(ava2Box);
		
		host = Toolkit.getDefaultToolkit().getImage("C:\\host.jpg");
		hostBox = new JLabel(new ImageIcon(host.getScaledInstance(55,30, Image.SCALE_SMOOTH)));
		hostBox.setBounds(120, 270, 55, 30);
		add(hostBox);
		
		// Ready button on bottom 
		btn_ready_1.setBounds(130, 520, 160, 40);
		add(btn_ready_1);
		
		btn_ready_2.setBounds(420, 520, 160, 40);
		add(btn_ready_2);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btn_ready_2) {
			msg2 = new JLabel("게임할 준비가 되었다면 Ready 버튼을 눌러주세요.");
			msg2.setFont(f1);
			msg2.setBounds(190, 100, 500, 20);
			add(msg2);
			showOtherID = new JLabel("Aziranom2");
			
			
		}
		
				if(count==0) {
					btn_ready_1.setEnabled(true);
					msg3 = new JLabel("Start버튼을 누르면 게임이 시작됩니다.");
					msg3.setFont(f3);
					msg3.setBounds(190, 100, 500, 20);
					add(msg3);
					if(count==1) {
						btn_ready_2.setEnabled(false);
					}
					// 상대방이 스타트 버튼을 누르면 방장에게 msg3메세지가 출력되고
					// 방장에게만 스타트버튼이 활성화됨
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

