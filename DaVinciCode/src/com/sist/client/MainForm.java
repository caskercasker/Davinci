package com.sist.client;

import java.awt.CardLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class MainForm extends JFrame implements ActionListener, Runnable, MouseListener { // ActionLister 인터페이스
	Login login = new Login();
	StartRoom sr = new StartRoom(); // 1/12 이름 변경 : WaitRoom → StartRoom
	GameRoom gr = new GameRoom();
	Avatar ava = new Avatar();
	WaitingRoom wr = new WaitingRoom(); // 1/12 신규 생성 : WaitingRoom
	CardLayout card = new CardLayout();
	MakeRoom mr = new MakeRoom();
	//서버 연결과 관련된 라이브러리
	Socket s; //서버 연결
	OutputStream out;// 서버로 데이터 전송 (요청)
	BufferedReader in; //서버에서 응답한 데이터를 받는다.

	String myRoom;
	MainForm() {
		this.setTitle("The Da Vinci Code Game"); // 타이틀에 게임제목 노출
		setLayout(card);
		add("GAME", gr);
		add("LOGIN", login);
		add("SR", sr);
		add("AVARTAR", ava);
		add("WR", wr );
		




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
		wr.b1.addActionListener(this); //방만들기
		wr.b2.addActionListener(this); //나가기

		mr.b1.addActionListener(this);	// 실제방만들기
		mr.b2.addActionListener(this);  // 방만들기 취소

		wr.table1.addMouseListener(this);
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
				System.out.println("dfdfd");
				connection(result);
				System.out.println("asdasd");
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

		if (e.getSource() == wr.b1) {
			mr.tf.setText("");
			mr.rb1.setSelected(true);
			mr.box.setSelectedIndex(0);
			mr.la4.setVisible(false);
			mr.pf.setVisible(false);
			mr.pf.setText("");
			mr.tf.requestFocus();
			mr.setVisible(true);

		}else if(e.getSource() ==wr.b2) {
			try {
				out.write((Function.EXIT+"|\n").getBytes());
				/*
				 * 나가기 => 요청
				 * 		  ===
				 * 			처리 ==> 서버
				 * 			결과출력 ==> 클라이언트
				 */

			}catch(Exception ex) {}
		}else if(e.getSource() == mr.b1) {
			//1. 방이름
			String rn = mr.tf.getText();
			if(rn.length() <1) {
				JOptionPane.showMessageDialog(this, "방 이름을 입력하세요");
				mr.tf.requestFocus();
				return;
			}
			System.out.println(wr.model1.getRowCount());
			for (int i=0; i<wr.model1.getRowCount(); i++) {
				String roomName = wr.model1.getValueAt(i, 0).toString();
				if(rn.equals(roomName)) {
					JOptionPane.showMessageDialog(this, "이미 존재하는 방입니다./n다시 입력하세요");
					mr.tf.setText("");
					mr.tf.requestFocus();
					return;
				}
			}

			//공개 비공개
			String rs =""; //상태
			String rp =""; //비밀번호
			if(mr.rb1.isSelected()) {
				rs="공개";
				rp=" ";
			}else {
				rs="비공개";
				rs=String.valueOf(mr.pf.getPassword());
			}

			//인원
			int inwon = mr.box.getSelectedIndex()+2;

			//서버로 전송
			try {
				out.write((Function.MAKEROOM+"|"+rn+"|"+rs+"|"+rp+"|"+inwon+"\n").getBytes());
			}catch(Exception ex) {}
				mr.setVisible(false);
		}else if(e.getSource() == mr.b2) {
			mr.setVisible(false);
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
							st.nextToken(),	 //캐릭터 이름
							st.nextToken(), //아바타 숫자
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
						// 스크롤이 최하단으로 자동으로 내려가게 설정
						int sc = wr.chatHistory.getText().length();
						wr.chatHistory.setCaretPosition(sc);
						break;
					}
					case Function.EXIT: { //남아 있는 사람
						String id =st.nextToken();
						for(int i=0; i< wr.model2.getRowCount();i++) {
							String mid = wr.model2.getValueAt(i,0).toString();
							// (String)wr.model2.getVaoueAt(i,0)
							if(mid.equals(id)){
								wr.model2.removeRow(i);
								break;
							}
						}
						break;
					}
					case Function.MYEXIT:{ //실제 나가는 사람 처리

						dispose(); //메모리 회수
						System.exit(0); //프로그램 종료

					}
					case Function.MAKEROOM:{
						String[] data = {st.nextToken(), //방이름
										st.nextToken(),  //상태 (공개/비공개)
										st.nextToken()}; //인원 1/6
						wr.model1.addRow(data);
						break;
					}
					case Function.ROOMIN:{
						/*
						 * Function.ROOMIN+"|"+room.roomName+"|"+id+"|"+img_name+"|"+img_source
						 *
						 */
						 myRoom = st.nextToken();
						 String id = st.nextToken();
						 String img_name = st.nextToken();
						 String img_source = st.nextToken();
						 Image img ;
						 String temp ="";
//						 if(sex.equals("남자")) {
//							 temp ="m"+avatar; //m1.png, m2.png ...
//						 }else {
//							 temp = "w"+avatar; //w1.png, w2.png ...
//						 }
						 card.show(getContentPane(), "SR"); //스타팅룸으로 들어감
						if(sr.sw[0]==false) {
								sr.sw[0] = true;
								sr.ava1Box.removeAll();
								System.out.println(img_source);
								img = Toolkit.getDefaultToolkit().getImage(""+img_source);
								sr.ava1.setImage(img);
								sr.ava1Box = new JLabel(sr.ava1);
								sr.showMyID.setText(id);

						}else {
								sr.sw[1] = true;
								sr.ava2Box.removeAll();
								System.out.println(img_source);
								img = Toolkit.getDefaultToolkit().getImage(""+img_source);
								sr.ava2.setImage(img);
								sr.ava2Box = new JLabel(sr.ava2);
								sr.showOtherID.setText(id);
						}


//						 for(int i=0; i<6; i++) { //비어있다면 false 들어가고 True로 바꾸는 boolean 형 배열
//							 if(gr.sw[i]==false) {
//								 gr.sw[i] = true;
//								 gr.pans[i].removeAll(); //label 위에 labe을 올릴수 없기 때문에 이미 올라가있는 label 을 지움
//								 gr.pans[i].setLayout(new BorderLayout());
//								 gr.pans[i].add("Center",new JLabel(new ImageIcon(gr.getImageSizeChange(new ImageIcon("c:\\image\\"+temp+".png"), 150, 120))));
//								 gr.pans[i].validate(); //재배치 remove 와 쌍으로 작동한다.
//								 gr.ids[i].setText(id);
//								 break;
//							 }

						break;
					}
					case Function.ROOMADD:{
						 String id = st.nextToken();
						 String img_name = st.nextToken();
						 String img_source = st.nextToken();

//						 String temp ="";
//						 if(sex.equals("남자")) {
//							 temp ="m"+avatar; //m1.png, m2.png ...
//						 }else {
//							 temp = "w"+avatar; //w1.png, w2.png ...
//						 }

						 if(sr.sw[0]==false) {
								sr.sw[0] = true;
								sr.ava1Box.removeAll();
								System.out.println(img_source);
								sr.ava1 = new ImageIcon(img_source);
								sr.ava1Box = new JLabel(sr.ava1);
								sr.showMyID.setText(id);

						}else {
							sr.sw[1] = true;
							sr.ava2Box.removeAll();
								System.out.println(img_source);
								sr.ava2 = new ImageIcon(img_source);
								sr.ava2Box = new JLabel(sr.ava2);
								sr.showMyID.setText(id);
							}

						break;
					}
					case Function.ROOMCHAT:{
						sr.chatHistory.append(st.nextToken()+"\n");
						// 스크롤이 최하단으로 자동으로 내려가게 설정
						int sc = gr.chatHistory.getText().length();
						gr.chatHistory.setCaretPosition(sc);
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
		sr.ava1Box.setBounds(130, 200, 160, 199);
		sr.add(sr.ava1Box);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == wr.table1) {
			if(e.getClickCount()==2) {//더블클릭
				//방이름
				int row = wr.table1.getSelectedRow(); //값 비교를 위해 해당 row의 값들을 하나한 가져와서 비교함.
				String rn=wr.model1.getValueAt(row, 0).toString();
				String inwon=wr.model1.getValueAt(row,2).toString();
				//String state=wr.model1.getValueAt(row,1).toString();
				StringTokenizer st = new StringTokenizer(inwon,"/");

				//1/5
				int no1 = Integer.parseInt(st.nextToken()); //1
				int no2 = Integer.parseInt(st.nextToken()); //5
				if(no1==no2) {
					//방에 들어갈 수 없다.
					JOptionPane.showMessageDialog(this, "이미 방 인원이 찼습니다\n 다른방을 선택하세요");
				}else {
					try {
						out.write((Function.ROOMIN+"|"+rn+"\n").getBytes());
					}catch(Exception ex) {}
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


}

