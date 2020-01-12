package com.sist.client;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

public class MainForm extends JFrame implements ActionListener { // ActionLister 인터페이스
	Login login = new Login();
	StartRoom sr = new StartRoom(); // 1/12 이름 변경 : WaitRoom → StartRoom 
	GameRoom gr = new GameRoom();
	Avatar ava = new Avatar();
	WaitingRoom wr = new WaitingRoom(); // 1/12 신규 생성 : WaitingRoom 
	CardLayout card = new CardLayout();

	MainForm() {
		this.setTitle("The Da Vinci Code Game"); // 타이틀에 게임제목 노출
		setLayout(card);
		add("WR", wr );
		add("GAME", gr);
		add("SR", sr);
		add("LOGIN", login);
		add("AVARTAR", ava);

		setSize(1024, 768); // 윈도우창 사이즈 설정
		setVisible(true); // 윈도우를 보여라.
		setResizable(false); // 창 크기 변경 불가능하게
		setLocationRelativeTo(null); // 창이 정 중앙에 뜨게
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 종료 시 게임 종료되도록 (그렇지 않으면 게임 꺼도 계속 돌아감...)

		login.b1.addActionListener(this);
		sr.btn_ready_1.addActionListener(this);
		ava.b5.addActionListener(this);
		ava.p1Icon.addActionListener(this);
		ava.p2Icon.addActionListener(this);
		ava.p3Icon.addActionListener(this);
		ava.p4Icon.addActionListener(this);
		gr.confirmGameEnd.addActionListener(this);

	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
		}
		MainForm mf = new MainForm();
		//mf.ava.setNowSelected(1);
		mf.sr.buffer=1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == login.b1) {
			card.show(getContentPane(), "AVARTAR");
		}
		if (e.getSource() == ava.b5) {
			card.show(getContentPane(), "WR");
		}else if(e.getSource() == ava.p1Icon) {
			setAvatar(1);			
		}else if(e.getSource() == ava.p2Icon){
			setAvatar(2);
		}else if(e.getSource() == ava.p3Icon) {
			setAvatar(3);
		}else if(e.getSource() == ava.p4Icon) {
			setAvatar(4);
		}	
		if (e.getSource() == sr.btn_ready_1) {
			card.show(getContentPane(), "GR");
		}
		if (e.getSource() == gr.confirmGameEnd) {
			card.show(getContentPane(),"GR");
		}

	}
	public void setAvatar(int a) {
		ava.setNowSelected(a);
		sr.buffer= ava.getNowSelected();
		//System.out.println(sr.buffer);
		sr.ava1 = new ImageIcon("images/Avatar/_"+sr.buffer+sr.buffer+".jpg");
		sr.ava1Box = new JLabel(sr.ava1);
		sr.ava1Box.setBounds(130, 280, 160, 199);
		sr.add(sr.ava1Box);
	}
	
}
