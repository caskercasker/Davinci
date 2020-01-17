package com.sist.client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Image;
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

	Image img,img2;
	String myRoom;
	MainForm() {
		this.setTitle("The Da Vinci Code Game"); // 타이틀에 게임제목 노출
		setLayout(card);
		add("LOGIN", login);
		add("GAME", gr);
		add("SR", sr);
		add("AVATAR", ava);
		add("WR", wr );


		setSize(1024, 768); // 윈도우창 사이즈 설정
		setVisible(true); // 윈도우를 보여라.
		setResizable(false); // 창 크기 변경 불가능하게
		setLocationRelativeTo(null); // 창이 정 중앙에 뜨게
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 종료 시 게임 종료되도록 (그렇지 않으면 게임 꺼도 계속 돌아감...)

		login.b1.addActionListener(this);
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

		sr.b1.addActionListener(this); //준비
		sr.b2.addActionListener(this); //시작
		sr.b3.addActionListener(this); //나가기
		sr.tf.addActionListener(this);
	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
		}
		MainForm mf = new MainForm();
		//mf.ava.setNowSelected(1);
		//mf.sr.buffer=1;
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

			// 서버로 전송
			try {
				out.write((Function.WAITCHAT + "|" + msg + "\n").getBytes());
			} catch (Exception ex) {
			}

			wr.chatInput.setText("");
		}


		if (e.getSource() == ava.b5) {
			card.show(getContentPane(), "WR");
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
		else if (e.getSource() == sr.tf) {
			String msg=sr.tf.getText();
			if(msg.length()<1)
				return;
			try
			{ // 서버로 값보냄												방이름
				out.write((Function.ROOMCHAT+"|"+myRoom+"|"+msg+"\n").getBytes());
			}catch(Exception ex){}
			sr.tf.setText("");
		}


		else if(e.getSource() == sr.b1) {
			try {
				out.write((Function.GAMEREADY+"|"+myRoom+"\n").getBytes());
			}catch (Exception ex) {}
		}
		else if(e.getSource() == sr.b2) {
			System.out.println("게임 시작하십쇼(클라)");
			try {
				out.write((Function.GAMESTART +"|"+myRoom+"\n").getBytes());
			}catch(Exception ex) {}
		}

		else if(e.getSource()==sr.b3) // 겜방 나가기.
		{
			try
			{
				out.write((Function.ROOMOUT+"|"+myRoom+"\n").getBytes());
			}catch(Exception ex) {}
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
						wr.model1.addRow(data); //대기방에 방목록
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
						 //Image img ;
						 String temp ="";
//						 if(sex.equals("남자")) {
//							 temp ="m"+avatar; //m1.png, m2.png ...
//						 }else {
//							 temp = "w"+avatar; //w1.png, w2.png ...
//						 }
						card.show(getContentPane(), "SR"); //스타팅룸으로 들어감

						 for(int i=0;i<2;i++){
							{
								if(sr.sw[i]==false)
								{
									sr.sw[i]=true;
									sr.pans[i].removeAll();  // 라벨을 지워야 새로운 라벨을 올릴 수 있다
									sr.pans[i].setLayout(new BorderLayout());
									sr.pans[i].add("Center",new JLabel(new ImageIcon(sr.getImageSizeChange(new ImageIcon(img_source), 160, 199))));
									sr.pans[i].validate();  // 재배치 remove-validate
									sr.ids[i].setText(id);
									break;
								}
							}
						 }
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
						 for(int i=0;i<2;i++){
							{
								if(sr.sw[i]==false)
								{
									sr.sw[i]=true;
									sr.pans[i].removeAll();  // 라벨을 지워야 새로운 라벨을 올릴 수 있다
									sr.pans[i].setLayout(new BorderLayout());
									sr.pans[i].add("Center",new JLabel(new ImageIcon(sr.getImageSizeChange(new ImageIcon(img_source), 160, 199))));
									sr.pans[i].validate();  // 재배치 remove-validate
									sr.ids[i].setText(id);
									break;
								}
							}
						 }
						 break;
					}
					case Function.ROOMCHAT:{
						sr.ta.append(st.nextToken()+"\n");
						// 스크롤이 최하단으로 자동으로 내려가게 설정
						int sc = sr.ta.getText().length();
						sr.ta.setCaretPosition(sc);

						break;
					}
					case Function.WAITUPDATE:
					{
						//messageAll(Function.WAITUPDATE+"|"+room.roomName+"|"+room.current+"|"+room.maxcount+"|"+id+"|"+pos);
						String rn=st.nextToken();
						String current=st.nextToken();
						String maxcount=st.nextToken();
						String id=st.nextToken();
						String pos=st.nextToken();


						// t1에서 방을 찾기
						for(int i=0; i<wr.model1.getRowCount(); i++)
						{
							String roomName=wr.model1.getValueAt(i, 0).toString();
							if(rn.equals(roomName))
							{
								if(Integer.parseInt(current)==0)
								{
										wr.model1.removeRow(i);
								}
								else
								{
										wr.model1.setValueAt(current+"/"+maxcount, i, 1);

								}
								break;
							}
						}
						//접속자 목록 변경
						for( int i=0; i<wr.model2.getRowCount();i++)
						{
							String mid=wr.model2.getValueAt(i, 0).toString();
							if(mid.equals(id))
							{
								wr.model2.setValueAt(pos, i, 2);
							}
						}
						break;
					}
					case Function.POSCHANGE:
					{
						String id=st.nextToken();
						String pos=st.nextToken();
						//String bang=st.nextToken();
						for( int i=0; i<wr.model2.getRowCount();i++)
						{
							String mid=wr.model2.getValueAt(i, 0).toString();
							if(mid.equals(id))
							{
								wr.model2.setValueAt(pos, i, 2);
							}
						}
						break;
					}
					case Function.ROOMOUT:
					{/////
						String id=st.nextToken();
						for(int i=0;i<2;i++)
						{
							String mid=sr.ids[i].getText();
							if(id.equals(mid))  // 아이디가 같으면 제거해라
							{
								sr.sw[i]=false;
								sr.pans[i].removeAll();
								sr.pans[i].setLayout(new BorderLayout());
								sr.pans[i].add("Center",new JLabel(new ImageIcon(sr.getImageSizeChange(new ImageIcon("c:\\image\\def.png"), 160, 199))));
								sr.pans[i].validate();
								sr.ids[i].setText("");
								break;
							}
						}
						break;
					}
					case Function.MYROOMOUT:
					{
						// 초기화 ( 내가 빠져나가기 전에 )
						for(int i=0;i<2;i++)
						{
							sr.sw[i]=false;
							sr.pans[i].removeAll();
							sr.pans[i].setLayout(new BorderLayout());
							sr.pans[i].add("Center",new JLabel(new ImageIcon(sr.getImageSizeChange(new ImageIcon("c:\\image\\def.png"), 160, 199))));
							sr.pans[i].validate();
							sr.ids[i].setText("");
						}
						sr.ta.setText("");
						sr.tf.setText("");
						card.show(getContentPane(), "WR");  // 대기실로 이동해라
						break;
					}
					case Function.KANG:
					{
						String rn=st.nextToken();
						JOptionPane.showMessageDialog(this, rn+"방에서 강퇴되었습니다");
						out.write((Function.ROOMOUT+"|"+rn+"\n").getBytes());
						break;
					}
					case Function.GAMEREADY:{
						myRoom = st.nextToken();
						String id = st.nextToken();
						String img_source = st.nextToken();
						card.show(getContentPane(), "GAME");
						for(int i=0;i<2;i++){
							{
								if(gr.sw[i]==false)
								{
									gr.sw[i]=true;
									gr.pans[i].removeAll();  // 라벨을 지워야 새로운 라벨을 올릴 수 있다
									gr.pans[i].setLayout(new BorderLayout());
									gr.pans[i].add("Center",new JLabel(new ImageIcon(gr.getImageSizeChange(new ImageIcon(img_source), 160, 199))));
									gr.pans[i].validate();  // 재배치 remove-validate
									gr.ids[i].setText(id);
									break;
								}
							}
						 }
					 break;
					}

					case Function.GAMESTART:{
						 System.out.println(msg);
						 myRoom = st.nextToken();
						 System.out.println("메시지2");
						 String id = st.nextToken();
						 System.out.println("메시지3");
						 String img_source = st.nextToken();
						 System.out.println("메시지4");
						 card.show(getContentPane(), "GAME");
						 System.out.println("게임 카드 바꾸기");
						 for(int i=0;i<2;i++){
								{
									if(gr.sw[i]==false)
									{
										gr.sw[i]=true;
										gr.pans[i].removeAll();  // 라벨을 지워야 새로운 라벨을 올릴 수 있다
										gr.pans[i].setLayout(new BorderLayout());
										gr.pans[i].add("Center",new JLabel(new ImageIcon(gr.getImageSizeChange(new ImageIcon(img_source), 160, 199))));
										gr.pans[i].validate();  // 재배치 remove-validate
										gr.ids[i].setText(id);
										break;
									}
								}
							 }
						 break;
					}
				}
			}
		}catch(Exception ex) {}
	}

//	public void setAvatar(int a) {
//		ava.setNowSelected(a);
//		sr.buffer= ava.getNowSelected();
//		//System.out.println(sr.buffer);
//		sr.ava1 = new ImageIcon("images/Avatar/_"+sr.buffer+sr.buffer+".jpg");
//		sr.ava1Box = new JLabel(sr.ava1);
//		sr.ava1Box.setBounds(130, 200, 160, 199);
//		sr.add(sr.ava1Box);
//	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == wr.table1) {
			if(e.getClickCount()==2) {//더블클릭
				//방이름
				int row = wr.table1.getSelectedRow(); //값 비교를 위해 해당 row의 값들을 하나한 가져와서 비교함.
				String rn=wr.model1.getValueAt(row, 0).toString();
				String inwon=wr.model1.getValueAt(row,1).toString();
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

