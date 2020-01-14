package com.sist.client;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.sist.common.Function;
import com.sist.dao.MemberDAO;

public class MainForm extends JFrame implements ActionListener, Runnable { // ActionLister 인터페이스
	Login login = new Login();
	StartRoom sr = new StartRoom(); // 1/12 이름 변경 : WaitRoom → StartRoom
	GameRoom gr = new GameRoom();
	Avatar ava = new Avatar();
	WaitingRoom wr = new WaitingRoom(); // 1/12 신규 생성 : WaitingRoom
	CardLayout card = new CardLayout();
	//서버 연결과 관련된 라이브러리
	Socket s; //서버 연결
	OutputStream out;// 서버로 데이터 전송 (요청)
	BufferedReader in; //서버에서 응답한 데이터를 받는다.

	MainForm() {
		this.setTitle("The Da Vinci Code Game"); // 타이틀에 게임제목 노출
		setLayout(card);
		add("LOGIN", login);
		add("AVARTAR", ava);
		add("WR", wr );
		add("GAME", gr);

		add("SR", sr);

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
		wr.chatInput.addActionListener(this);
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
		if(e.getSource()==login.b1) {
			String id = login.tf.getText();
			if(id.length()<1) {
				JOptionPane.showMessageDialog(this, "ID를 입력하세요");
				login.tf.requestFocus();
				return;

			}
			String pwd = String.valueOf(login.pf.getPassword());
			if(pwd.length()<1) {
				JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요");
				login.pf.requestFocus();
				return;
			}
			MemberDAO dao = new MemberDAO();
			String result = dao.isLogin(id, pwd);
			if(result.equals("NOID")) {
				JOptionPane.showMessageDialog(this, "ID가 존재하지 않습니다.");
				login.tf.setText("");
				login.pf.setText("");
				login.tf.requestFocus();
			}else if(result.equals("NOPWD")) {
				JOptionPane.showMessageDialog(this, "비밀번호가 틀립니다.");
				login.pf.setText("");
				login.pf.requestFocus();
			}else {
				connection(result);
			}
		}else if(e.getSource() == wr.chatInput) {
			// 입력된 문자열 읽기
			String msg = wr.chatInput.getText();
			if(msg.length()<1) {
				wr.chatInput.requestFocus();
				return;
			}

			//서버로 전송
			try {
				out.write((Function.WAITCHAT+"|"+msg+"\n").getBytes());
			}catch(Exception ex) {}

			wr.chatInput.setText("");
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
	public void connection(String userData) {
		try {
			s = new Socket("localhost",8888); //전화 걸기
			//송신/수신
			out=s.getOutputStream();
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));

			//로그인 데이터 보내기
			// 100|hong|홍길동|남자\n
			out.write((Function.LOGIN+"|"+userData+"\n").getBytes());
		}catch(Exception ex) {}
		//서버로부터 데이터를 읽기 시작
		new Thread(this).start();
	}

	//서버로부터 데이터를 수신하는 기능
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true) {
				// 100|hong|홍길동|남자|대기실\n
				String msg = in.readLine();
				StringTokenizer st = new StringTokenizer(msg,"|");
				int protocol = Integer.parseInt(st.nextToken());
				switch(protocol) {
					case Function.LOGIN:{
						String[] data = {
							st.nextToken(),  //아이디
							st.nextToken(),	 //이름
							st.nextToken(), //성별
							//st.nextToken()	//위치
						};
						wr.model2.addRow(data);
						break;
					}
					case Function.MYLOG:{
						String id = st.nextToken();
						setTitle(id);
						card.show(getContentPane(), "WR");
						break;
					}
					case Function.WAITCHAT:{
						wr.chatHistory.append(st.nextToken()+"\n");

						break;
					}

				}
			}
		}catch(Exception ex) {}
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

