
package com.sist.client;
import java.awt.BorderLayout;
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
import java.util.Collections;
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
		add("WR", wr );

		setSize(1024, 768); // 윈도우창 사이즈 설정
		setVisible(true); // 윈도우를 보여라.
		setResizable(false); // 창 크기 변경 불가능하게
		setLocationRelativeTo(null); // 창이 정 중앙에 뜨게
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 종료 시 게임 종료되도록 (그렇지 않으면 게임 꺼도 계속 돌아감...)

		login.b1.addActionListener(this);
		wr.chatInput.addActionListener(this);
		wr.b1.addActionListener(this); //방만들기
		wr.b2.addActionListener(this); //나가기
		wr.table1.addMouseListener(this);
		mr.b1.addActionListener(this);	// 실제방만들기
		mr.b2.addActionListener(this);  // 방만들기 취소
		sr.b1.addActionListener(this); //준비
		sr.b2.addActionListener(this); //시작
		sr.b3.addActionListener(this); //나가기
		sr.chatInput.addActionListener(this);
		sr.b4.addActionListener(this); //강퇴
		gr.chatInput.addActionListener(this);
		gr.confirmGameEnd.addActionListener(this);


		for(int i=0; i<24; i++) {
			gr.dummy[i].addActionListener(this);
		}
//		for(int i=0; i<12; i++) {
//			gr.play1[i].addMouseListener(this);
//			gr.play2[i].addMouseListener(this);
//		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
		}
		MainForm mf = new MainForm();
	}

	// ActionEvent Starts
	@Override
	public void actionPerformed(ActionEvent e) {
		// [로그인] 로그인 버튼 - ID/PW 일치여부 판정
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

		}
		// [게임방] 게임끝컨펌
//		if (e.getSource() == gr.confirmGameEnd) {
//			card.show(getContentPane(),"GR");
//		}
		// [대기실] 방 만들기
		if (e.getSource() == wr.b1) {
			mr.tf.setText("");
			mr.rb1.setSelected(true);
			mr.box.setSelectedIndex(0);
			mr.la4.setVisible(false);
			mr.pf.setVisible(false);
			mr.pf.setText("");
			mr.tf.requestFocus();
			mr.setVisible(true);
		}
		// [대기실] 채팅입력창
		else if(e.getSource() == wr.chatInput) {
			// 입력된 문자열 읽기
			String msg = wr.chatInput.getText();
			if(msg.length()<1) {
				wr.chatInput.requestFocus();
				return;
			}
			// 서버로 전송
			try {
				out.write((Function.WAITCHAT+"|"+msg+"\n").getBytes());
			} catch (Exception ex) {}
			wr.chatInput.setText("");
		}
		// [대기실] 게임종료 버튼
		else if(e.getSource() ==wr.b2) {
			try {
				out.write((Function.EXIT+"|\n").getBytes());
			}catch(Exception ex) {}
		}
		// [방만들기 JDialog] 방만들기 버튼
		else if(e.getSource() == mr.b1) {
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
			}
			else {
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
		}
		// [방만들기 JDialog]  취소 버튼
		else if(e.getSource() == mr.b2) {
			mr.setVisible(false);
		}
		// [시작룸] 준비 버튼
		else if(e.getSource() == sr.b1) {
			System.out.println("버튼입");
			sr.b1.setEnabled(false);
			try {
				out.write((Function.GAMEREADY+"|"+myRoom+"\n").getBytes());
			}catch (Exception ex) {}
		}
		// [시작룸] 시작 버튼
//		else if(e.getSource() == sr.b2) {
//			System.out.println("게임 시작하십쇼(클라)");
//			try {
//				out.write((Function.GAMESTART +"|"+myRoom+"\n").getBytes());
//			}catch(Exception ex) {}
//		}
		// [시작룸] 나가기 버튼
		else if(e.getSource()==sr.b3)
		{
			try
			{
				out.write((Function.ROOMOUT+"|"+myRoom+"\n").getBytes());
			}catch(Exception ex) {}
		}
		// [시작룸] 채팅입력창
		else if (e.getSource() == sr.chatInput) {
			String msg = sr.chatInput.getText();
			if (msg.length() < 1) {
				sr.chatInput.requestFocus();
				return;
			}
			try {
				out.write((Function.SRCHAT+"|"+myRoom+"|"+msg+"\n").getBytes()); // out.write(); ==> 서버로 값이 넘어감
			} catch (Exception ex) {}
			sr.chatInput.setText(""); // 채팅입력창 비워준다
		}
		// [게임룸] 채팅입력창
		else if (e.getSource() == gr.chatInput) {
			String msg = gr.chatInput.getText();
			if (msg.length() < 1) {
				gr.chatInput.requestFocus();
				return;
			}
			try {
				out.write((Function.ROOMCHAT+"|"+myRoom+"|"+msg+"\n").getBytes()); // out.write(); ==> 서버로 값이 넘어감
				// 방 이름이 넘어가야 방에 있는 사람들에게만 채팅메시지 보낼 수 있음
				// 방 이름 중복되지 않게 해놨으니까 가능
				// 방 안에 userVc있으니까 방에 들어간 사람 찾을 수 있음!
				// 방 찾으려고 myroom이라고 전역변수 만들어놓았음
			} catch (Exception ex) {}
			gr.chatInput.setText(""); // 채팅입력창 비워준다
		}
		// 화면전환 (card.show) ==> 서버 통신 동작으로 변경 필요
		if (e.getSource() == sr.b1) {
			card.show(getContentPane(), "GR");
		}
		if (e.getSource() == gr.confirmGameEnd) {
			card.show(getContentPane(),"GR");
		}
		if(gr.dummyClickTurn == false || (gr.tail.size()>=4 || gr.tail2.size()>=4) ) {
			boolean c = (gr.tail.size()<=4 || gr.tail2.size()<=4);
			boolean d = (gr.tail.size()!=0 || gr.tail2.size()!=0);

			gr.dummyChooseCheck= (gr.tail.size()<=4 || gr.tail2.size() <= 4); //55 일때 false
			gr.deckSizeCheck = (gr.tail.size()!=0 || gr.tail2.size()!=0); //처음에는 false 이후에는 true

			for(int j=0; j<24; j++) {
				if(e.getSource() == gr.dummy[j]) {
		    		//String k = String.valueOf(j);
		    		try {
		    			out.write((Function.DUMMYCHOOSE+"|"+myRoom+"|"+j+"|"+gr.dummyClickTurn+"|"+gr.dummyChooseCheck
		    					+"|"+gr.deckSizeCheck+"\n").getBytes());
		    		}catch (Exception ex) {}
				}
			}
		}
		if(e.getSource()==gr.confirmGameEnd) {
			try {
    			out.write((Function.GAMERESET+"|"+myRoom+"\n").getBytes());
    		}catch (Exception ex) {}
		}
	}
	// ActionEvent Ends

	// userData와 connect
	public void connection(String userData) {
		try {
			s = new Socket("211.238.142.199",8888); //전화 걸기
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

	// 서버로부터 데이터를 수신하는 기능
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
						gr.chatHistory.append(st.nextToken()+"\n");
						// 스크롤이 최하단으로 자동으로 내려가게 설정
						int sc = gr.chatHistory.getText().length();
						gr.chatHistory.setCaretPosition(sc);
						break;
					}
					case Function.SRCHAT:{
						sr.chatHistory.append(st.nextToken()+"\n");
						// 스크롤이 최하단으로 자동으로 내려가게 설정
						int sc = sr.chatHistory.getText().length();
						sr.chatHistory.setCaretPosition(sc);
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
						sr.chatHistory.setText("");
						sr.chatInput.setText("");
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
					case Function.GAMESTART:{
//						 sr.b1.setEnabled(false);
//						 sr.b2.setEnabled(true);
						 //System.out.println(msg);
						 myRoom = st.nextToken();
						 String id = st.nextToken();
						 String img_source = st.nextToken();
						 card.show(getContentPane(), "GAME");
						 for(int j=0; j<12; j++) {
							 gr.play1[j].setVisible(true);
							 gr.play2[j].setVisible(true);
						 }
						 for(int i=0;i<2;i++){
								{
									if(gr.sw[i]==false)
									{
										gr.sw[i]=true;
										gr.pans[i].removeAll();  // 라벨을 지워야 새로운 라벨을 올릴 수 있다
										gr.pans[i].setLayout(new BorderLayout());
										gr.pans[i].add("Center",new JLabel(new ImageIcon(gr.getImageSizeChange(new ImageIcon(img_source), 90, 120))));
										gr.pans[i].validate();  // 재배치 remove-validate
										gr.ids[i].setText(id);
										break;
									}
								}
							 }
						 gr.getRand(gr.su.length);
						 break;
					}
					case Function.GAMESTARTNEW:{
						System.out.println("GAMESTARTNEW");
						System.out.println(msg);
						for (int i=0; i<24; i++) {
							gr.su[i] = Double.parseDouble(st.nextToken());
						}

						dummySet();
						break;
					}

					case Function.TURNSET:{
						System.out.println("TurnSet");
						System.out.println(msg);
						int gameturn = Integer.parseInt(st.nextToken());
						int playerTurn = Integer.parseInt(st.nextToken());
						if(gr.dummyClickTurn == false)
							message(gameturn,playerTurn,1);
						if(gr.dummyClickTurn == true) {
							try {
								Thread.sleep(2000);
							} catch (Exception e) {
								// TODO: handle exception
							}

							message (gameturn,playerTurn, 2);
						}
						if(playerTurn==1) {
							gr.ids[1].setBorder(gr.borderBlue);
						}else if(playerTurn==0) {
							gr.ids[0].setBorder(gr.borderBlue);
						}
						if(gameturn==0) {

							gr.pans[0].setBorder(gr.border);
							gr.pans[1].setBorder(gr.borderEmpty);
						}else if(gameturn ==1) {
							gr.pans[1].setBorder(gr.border);
							gr.pans[0].setBorder(gr.borderEmpty);
						}

						if (playerTurn!=gameturn) {
							gr.disableDummy();
							disableLabel_1(gr.tail.size());
							disableLabel_2(gr.tail2.size());

						}else if(playerTurn==gameturn) {
							gr.enableDummy();
							disableLabel_1(gr.tail.size());
							disableLabel_2(gr.tail2.size());

						}
						break;
					}
					case Function.DUMMYCHOOSE:{
						System.out.println("DUMMYCHOOSE");
						System.out.println(msg);
						int gameTurn = Integer.parseInt(st.nextToken());
						int playerTurn = Integer.parseInt(st.nextToken());
						int number = Integer.parseInt(st.nextToken());
						if(gr.dummyClickTurn == false) {
							message(gameTurn,playerTurn,1);
							disableLabel_1(gr.tail.size());
							disableLabel_2(gr.tail2.size());
						}else if(gr.dummyClickTurn == true) {
							message(gameTurn,playerTurn,2);
							disableLabel_1(gr.tail.size());
							disableLabel_2(gr.tail2.size());
						}


						if(gameTurn == 0) {
							gr.dummy[number].setVisible(false);
							if(gr.su[number]>12)													 //블랙 화이트 구분하기 위한 숫자 변환 12보다 큰수들은 white에 값
								gr.su[number] = gr.su[number]-12+0.5;										//같아진 블랙 화이트 카드를 0.5의 값으로 크기를 구분
							gr.tail.add(gr.su[number]);
							Collections.sort(gr.tail); 											// 리스트 정렬
								//카드를 가져온수만큼 player에 레이블을 뿌린다.
								for(int k=0; k<gr.tail.size();k++) {
									if(gameTurn == playerTurn)
										gr.imageBuf1[k] = gr.setCardImage(gr.tail.get(k));
									if(gameTurn != playerTurn)
										gr.imageBuf1[k] = gr.setEnemyCardImage(gr.tail.get(k));

									gr.temp[k] = gr.tail.get(k);
									gr.play1[k].setIcon(new ImageIcon(gr.imageBuf1[k]));
									gr.play1[k].setOpaque( true);
									gr.play1[k].setBorder(gr.borderEmpty);
									if(gr.tail.get(k)%0.5!=0) {										// 게임이 진행되면서 비공개 에서 공개된 값들을 구분하게 뿌려준다.
										if(gameTurn == playerTurn) {
											double c = gr.tail.get(k)-0.01;
											gr.play1[k].setBorder(gr.borderEmpty);
											gr.play1[k].setIcon(new ImageIcon(gr.reverseCardImage(c)));
										}
										if(gameTurn != playerTurn) {
											double c = gr.tail.get(k)-0.01;
											gr.play1[k].setBorder(gr.borderEmpty);
											gr.play1[k].setIcon(new ImageIcon(gr.changeCardImage(c)));
										}
									}
								}
								for(int i=0; i<12; i++) {
									if(gr.su[number] == gr.temp[i]) {
										gr.count=i;
										gr.play1[gr.count].setBorder(gr.border);
										break;
									}
								}
						}else if (gameTurn ==1) {
							gr.dummy[number].setVisible(false);
							if(gr.su[number]>12)
								gr.su[number] = gr.su[number]-12+0.5;
							gr.tail2.add(gr.su[number]);
							Collections.sort(gr.tail2);
								for(int k=0; k<gr.tail2.size();k++) {
									if(gameTurn == playerTurn)
										gr.imageBuf2[k] = gr.setCardImage(gr.tail2.get(k));
									if(gameTurn != playerTurn)
										gr.imageBuf2[k] = gr.setEnemyCardImage(gr.tail2.get(k));
									gr.temp2[k] = gr.tail2.get(k);
									gr.play2[k].setIcon(new ImageIcon(gr.imageBuf2[k]));
									gr.play2[k].setOpaque(true);
									gr.play2[k].setBorder(gr.borderEmpty);
									if(gr.tail2.get(k)%0.5!=0) {
										if(gameTurn == playerTurn) {
											double c = gr.tail2.get(k)-0.01;
											gr.play2[k].setBorder(gr.borderEmpty);
											gr.play2[k].setIcon(new ImageIcon(gr.reverseCardImage(c)));
										}
										if(gameTurn != playerTurn) {
											double c = gr.tail2.get(k)-0.01;
											gr.play2[k].setBorder(gr.borderEmpty);
											gr.play2[k].setIcon(new ImageIcon(gr.changeCardImage(c)));
										}
									}
								}
								for(int i=0; i<12; i++) {
									if(gr.su[number] == gr.temp2[i]) {
										gr.count2=i;
										gr.play2[gr.count2].setBorder(gr.border);
										break;
									}
								}

						}
						gr.deckSizeCheck = (gr.tail.size()!=0 || gr.tail2.size()!=0);
						gr.dummyChooseCheck= (gr.tail.size()<=4 || gr.tail2.size() <= 4);

						gr.deckChooseEnd = (gr.tail.size()==4 && gr.tail2.size()==4);

						if(gr.deckChooseEnd){											//4장씩 가져왔다면 본격적인 게임을 시작함을 알리기 위한 조건문
							gr.gameStart=true;
							gr.messageStart(gr.gameStart);
							gr.dummyClickTurn = true;
						}
						break;
					}
					case Function.DECKCHOOSE:{
						int gameTurn = Integer.parseInt(st.nextToken());
						int playerTurn = Integer.parseInt(st.nextToken());
						System.out.println("카드 고르기 비활성화 양쪽 다");
							disableLabel_1(gr.tail.size());
							disableLabel_2(gr.tail2.size());
						break;
					}

					case Function.GUESSDECKSTART:{
						int gameTurn = Integer.parseInt(st.nextToken());
						int playerTurn = Integer.parseInt(st.nextToken());
						gr.disableDummy();

						message(gameTurn,playerTurn,3);

						if(gameTurn==playerTurn) {
							if(playerTurn ==0) {
								System.out.println("플레이어1이 고를수있게 되었음");
								enableLabel_2(gr.tail2.size());
								disableLabel_1(gr.tail.size());
							}else if(playerTurn ==1) {
								System.out.println("플레이가 2가 고를수 있게 되었음");
								disableLabel_2(gr.tail2.size());
								enableLabel_1(gr.tail.size());
							}

						}else if(gameTurn!=playerTurn) {
							disableLabel_1(gr.tail.size());
							disableLabel_2(gr.tail2.size());
						}
						break;
					}
					case Function.GUESSNUMBER:{
						String rn = st.nextToken();
						int gameTurn = Integer.parseInt(st.nextToken());
						int playerTurn = Integer.parseInt(st.nextToken());
						int deckNumber = Integer.parseInt(st.nextToken());
						message(gameTurn,playerTurn,3);

						double numberChosen = Double.parseDouble(st.nextToken());
						if(gameTurn==0) {
							double tempValue = 0;											//값 변경 없이 비교를 위한 임시 변수(화이트 값은 0.5가 추가되었지만  사용자 입력은 0.5를 받지 않기 때문에 존재)
							if(gr.temp2[deckNumber]%1.0!=0) {
								System.out.println("계산");
								tempValue = gr.temp2[deckNumber] - 0.5;
								System.out.println(tempValue);
							}

							if(tempValue == numberChosen || gr.temp2[deckNumber] == numberChosen) {					//임시변수와 temp2[i]는 같은 값이지만 버튼 클릭스 블랙 화이트가 구분지어지기 때문에 두개를 비고하여야 함.
								System.out.println("맞음");
								messageOnDeckChoose(gameTurn,playerTurn, numberChosen,1);
								if(gameTurn == playerTurn) {
									gr.play2[deckNumber].setIcon(new ImageIcon(gr.changeCardImage(gr.temp2[deckNumber])));
									gr.play2[deckNumber].setBorder(gr.borderEmpty);
									gr.tail2.set(deckNumber, gr.tail2.get(deckNumber)+0.01);

									if(gr.gameEndCheck()==true){//마우스 클릭 입력을 받았기에 게임 종료 상태인지를 체크

										boolean pl2_Win = (gr.gameEnd1 == gr.tail.size());
										boolean pl1_Win = (gr.gameEnd2 == gr.tail2.size());
										gameEndMessage(gameTurn, playerTurn);
										try {
											out.write((Function.GAMEEND+"|"+myRoom+"|"+gameTurn+"|"+pl1_Win+"|"+pl2_Win+"\n").getBytes());
										}catch(Exception ex) {}
									}else if(gr.gameEndCheck() == false) {							//게임 종료 상태가 아니라면  턴을 이어갈것인지 종료할것인지를 체크
										gr.option = JOptionPane.showOptionDialog(null, "한 번더 숫자를 맞춰보실래요 ?","GoOrStop", JOptionPane.DEFAULT_OPTION,
												JOptionPane.DEFAULT_OPTION, null, gr.goOrStop, gr.goOrStop[0]);
										System.out.println(gr.option); //맞으면 0 틀리면 1
										try {
											out.write((Function.GO_OR_STOP+"|"+myRoom+"|"+gameTurn+"|"+playerTurn+"|"+gr.option+"\n").getBytes());
										}catch(Exception ex) {}
									}
								}else if (gameTurn != playerTurn) {
									gr.play2[deckNumber].setIcon(new ImageIcon(gr.reverseCardImage(gr.temp2[deckNumber])));
									gr.play2[deckNumber].setBorder(gr.borderEmpty);
									gr.tail2.set(deckNumber, gr.tail2.get(deckNumber)+0.01);
								}

							}else {
								messageOnDeckChoose(gameTurn,playerTurn, numberChosen,2);
								if(gameTurn == playerTurn) {
									System.out.println("틀림");
									gr.play1[gr.count].setBorder(gr.borderEmpty);
									gr.play1[gr.count].setIcon(new ImageIcon(gr.reverseCardImage(gr.temp[gr.count])));
									gr.tail.set(gr.count,gr.tail.get(gr.count)+0.01);

								}else if(gameTurn != playerTurn) {
									gr.play1[gr.count].setBorder(gr.borderEmpty);
									gr.play1[gr.count].setIcon(new ImageIcon(gr.changeCardImage(gr.temp[gr.count])));
									gr.tail.set(gr.count,gr.tail.get(gr.count)+0.01);
								}
								try {
									out.write((Function.INGAMETURNCHANGE+"|"+rn+"|"+gameTurn+"|"+playerTurn+"\n").getBytes());
								}catch(Exception ex) {}

							}
						}else if(gameTurn==1) {
							double tempValue = 0;											//값 변경 없이 비교를 위한 임시 변수(화이트 값은 0.5가 추가되었지만  사용자 입력은 0.5를 받지 않기 때문에 존재)
							if(gr.temp[deckNumber]%1.0!=0) {
								System.out.println("계산");
								tempValue = gr.temp[deckNumber] - 0.5;
								System.out.println(tempValue);
							}

							if(tempValue == numberChosen || gr.temp[deckNumber] == numberChosen) {					//임시변수와 temp2[i]는 같은 값이지만 버튼 클릭스 블랙 화이트가 구분지어지기 때문에 두개를 비고하여야 함.
								System.out.println("맞음");
								messageOnDeckChoose(gameTurn,playerTurn, numberChosen,1);
								if(gameTurn == playerTurn) {
									gr.play1[deckNumber].setIcon(new ImageIcon(gr.changeCardImage(gr.temp[deckNumber])));
									gr.play1[deckNumber].setBorder(gr.borderEmpty);
									gr.tail.set(deckNumber, gr.tail.get(deckNumber)+0.01);
									gr.gameEndCheck();												//마우스 클릭 입력을 받았기에 게임 종료 상태인지를 체크

									if(gr.gameEndCheck()==true){

										boolean pl2_Win = (gr.gameEnd1 == gr.tail.size());
										boolean pl1_Win = (gr.gameEnd2 == gr.tail2.size());
										gameEndMessage(gameTurn, playerTurn);
										try {
											out.write((Function.GAMEEND+"|"+myRoom+"|"+gameTurn+"|"+pl1_Win+"|"+pl2_Win+"\n").getBytes());
										}catch(Exception ex) {}
										break;
									}else if(gr.gameEndCheck() == false) {							//게임 종료 상태가 아니라면  턴을 이어갈것인지 종료할것인지를 체크
										gr.option = JOptionPane.showOptionDialog(null, "한 번더 숫자를 맞춰보실래요 ?","GoOrStop", JOptionPane.DEFAULT_OPTION,
												JOptionPane.DEFAULT_OPTION, null, gr.goOrStop, gr.goOrStop[0]);
										System.out.println(gr.option); //맞으면 0 틀리면 1
										int a =gr.option;
										try {
											out.write((Function.GO_OR_STOP+"|"+myRoom+"|"+gameTurn+"|"+playerTurn+"|"+a+"\n").getBytes());
										}catch(Exception ex) {}
									}
								}else if (gameTurn != playerTurn) {
									gr.play1[deckNumber].setIcon(new ImageIcon(gr.reverseCardImage(gr.temp[deckNumber])));
									gr.play1[deckNumber].setBorder(gr.borderEmpty);
									gr.tail.set(deckNumber, gr.tail.get(deckNumber)+0.01);
								}
							}else {
								messageOnDeckChoose(gameTurn,playerTurn, numberChosen,2);
								if(gameTurn == playerTurn) {
									System.out.println("틀림");
									gr.play2[gr.count2].setBorder(gr.borderEmpty);
									gr.play2[gr.count2].setIcon(new ImageIcon(gr.reverseCardImage(gr.temp2[gr.count2])));
									gr.tail2.set(gr.count2,gr.tail2.get(gr.count2)+0.01);

								}else if(gameTurn != playerTurn) {
									gr.play2[gr.count2].setBorder(gr.borderEmpty);
									gr.play2[gr.count2].setIcon(new ImageIcon(gr.changeCardImage(gr.temp2[gr.count2])));
									gr.tail2.set(gr.count2,gr.tail2.get(gr.count2)+0.01);
								}
								try {
									out.write((Function.INGAMETURNCHANGE+"|"+rn+"|"+gameTurn+"|"+playerTurn+"\n").getBytes());
								}catch(Exception ex) {}

							}

						}
						break;
					}
					case Function.GO_OR_STOP:{
						System.out.println("go_or_stop Client");
						System.out.println(msg);
						String rn = st.nextToken();
						int gameTurn = Integer.parseInt(st.nextToken());
						int playerTurn = Integer.parseInt(st.nextToken());
						int option = Integer.parseInt(st.nextToken());
						System.out.println("옵션은 0 으로 go를 선택했다 "+option);

						if (option ==1) {
							System.out.println("Stop 메시지 ㄱㄱ");
							try {
								out.write((Function.INGAMETURNCHANGE+"|"+rn+"|"+gameTurn+"|"+playerTurn+"\n").getBytes());
							}catch(Exception ex) {}
						}else if(option == 0){
							message(gameTurn,playerTurn,3);
							System.out.println("레이블 활성화");
//							if(playerTurn==gameTurn) {
//								if(playerTurn==0) {
//									enableLabel_2(gr.tail2.size());
//									disableLabel_1(gr.tail.size());
//								}else if(playerTurn==1) {
//									enableLabel_1(gr.tail.size());
//									disableLabel_2(gr.tail2.size());
//
//								}
//							}else if(playerTurn!=gameTurn) {
//								disableLabel_1(gr.tail.size());
//								disableLabel_2(gr.tail2.size());
//							}
						}
						break;

					}
					case Function.GAMEEND:{
						int gameEndTurn = Integer.parseInt(st.nextToken());
						int playerTurn = Integer.parseInt(st.nextToken());
						String id = st.nextToken();

						if(gameEndTurn==playerTurn) {
							gr.confirmGameEnd.setEnabled(true);
							gr.confirmGameEnd.setVisible(true);
							JOptionPane.showConfirmDialog(this, id+"님이 승리하셨습니다 ", "게임종료 ",JOptionPane.OK_CANCEL_OPTION);
						}else if (gameEndTurn!=playerTurn) {
							gr.confirmGameEnd.setEnabled(true);
							gr.confirmGameEnd.setVisible(true);
							JOptionPane.showConfirmDialog(this, id+"님이 패하셨습니다 ", "게임종료 ",JOptionPane.OK_CANCEL_OPTION);
						}
						break;
					}
					case Function.GAMERESET:{
						String rn = st.nextToken();
						myRoom = rn;
						gr.sw[0]=false;
						gr.sw[1]=false;
						gr.deckChooseEnd=false;
						gr.deckSizeCheck=false;
						gr.dummyChooseCheck=false;
						gr.gameEndMessage = false;
						gr.dummyClickTurn= false ;
						gr.gameStart = false;
						for(int i=0; i<12; i++){
							gr.play1[i].removeAll();
							gr.play2[i].removeAll();

							gr.play1[i].setIcon(new ImageIcon("images/blank.png"));
							gr.play2[i].setIcon(new ImageIcon("images/blank.png"));
							gr.play1[i].setBorder(gr.borderEmpty);
							gr.play2[i].setBorder(gr.borderEmpty);
							gr.temp[i] =12;
							gr.temp2[i]=12;
						}
						for(int i=0; i<24; i++) {
							gr.dummy[i].removeAll();
							gr.dummy[i].setVisible(true);
						}
						sr.b1.setEnabled(true);
						gr.count = 0;
						gr.count2 = 0;
						gr.gameEnd1 = 10000;
						gr.gameEnd2 = 10000;
						gr.confirmGameEnd.setVisible(false);
						gr.confirmGameEnd.setEnabled(false);
						gr.tail.clear();
						gr.tail2.clear();
						gr.chatHistory.setText("");
						gr.chatInput.setText("");
						gr.gameMessage.setText("");

						card.show(getContentPane(), "SR");
						break;
					}
				}
			}
		}catch(Exception ex) {}
	}

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

		for (int i=0; i<12;i++) {
			if(e.getSource() == gr.play2[i]) {
				if(e.getClickCount() ==2) {
					gr.choose = JOptionPane.showOptionDialog(null, "숫자를 고르세요","상대카드", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, gr.numbers, gr.numbers[0]);
					try {
						out.write((Function.GUESSNUMBER+"|"+myRoom+"|"+i+"|"+gr.choose+"\n").getBytes());
					}catch(Exception ex) {}

				}

			}else if (e.getSource() == gr.play1[i]) {
				if(e.getClickCount() ==2) {
					gr.choose = JOptionPane.showOptionDialog(null, "숫자를 고르세요","상대카드", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, gr.numbers, gr.numbers[0]);
					try {
						out.write((Function.GUESSNUMBER+"|"+myRoom+"|"+i+"|"+gr.choose+"\n").getBytes());
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
	public void dummySet () {
		for (int k=0; k<gr.su.length;k++) {

			if(gr.su[k]<12) {
			gr.imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_back.png");
			//imgBuf = Toolkit.getDefaultToolkit().getImage("images/b_tile/b_tile_back.png");
			gr.imgFixed = gr.imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
			//gr.dummy[k] = new JButton(new ImageIcon(gr.imgFixed));
			gr.dummy[k].setIcon(new ImageIcon(gr.imgFixed));
			}else {
				gr.su[k] = gr.su[k]-12+0.5;
			gr.imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_back.png");
			//imgBuf = Toolkit.getDefaultToolkit().getImage("images/w_tile/w_tile_back.png");
			gr.imgFixed = gr.imgBuf.getScaledInstance(220, 190, Image.SCALE_SMOOTH);
			//gr.dummy[k] = new JButton(new ImageIcon(gr.imgFixed));
			gr.dummy[k].setIcon(new ImageIcon(gr.imgFixed));
			}
			//add(gr.dummy[k]);
			//"images/w_tile/w_tile_"+gr.su[k]+".png");
		}
	}

	public void enableLabel_1(int a) {
		System.out.println("enableLalbe1_In");
		System.out.println("덱사이즈"+a);
		for(int i=0; i<a; i++) {
			//gr.play1[i].setFocusable(true);
			gr.play1[i].addMouseListener(this);
		}
	}

	public void enableLabel_2(int a) {
		System.out.println("enableLalbe2_In");
		System.out.println("덱사이즈"+a);
		for(int i=0; i<a; i++) {
			//gr.play2[i].setFocusable(true);
			gr.play2[i].addMouseListener(this);
		}

	}
	public void disableLabel_1(int a) {
		System.out.println("DisableLalbe1_In");
		System.out.println("덱사이즈"+a);
		for(int i=0; i<a; i++) {
			//gr.play1[i].setFocusable(false);
			gr.play1[i].removeMouseListener(this);
		}
	}

	public void disableLabel_2(int a) {
		System.out.println("DisableLalbe2_In");
		System.out.println("덱사이즈"+a);
		for(int i=0; i<a; i++) {
			//gr.play2[i].setFocusable(false);
			gr.play2[i].removeMouseListener(this);
		}
	}
	public void disableDummy() {
		for (int i=0; i<24; i++) {
				gr.dummy[i].setEnabled(false);
				//dummy[i].setVisible(false);
			}

	}
	public void message (int gameturn, int playerturn, int msgNumber) {
		if(msgNumber==1) {
			if(gameturn ==0) {
				if(gameturn == playerturn) {
					gr.gameMessage.setText("카드를 한장 골라주세요");
				}else if(gameturn!=playerturn) {
					gr.gameMessage.setText("상대방이 카드를 고르고 있습니다.");
				}
			}else if(gameturn ==1) {
				if(gameturn == playerturn) {
					gr.gameMessage.setText("카드를 한장 골라주세요");
				}else if(gameturn!=playerturn) {
					gr.gameMessage.setText("상대방이 카드를 고르고 있습니다.");
				}
			}
		}else if (msgNumber==2) {
			if(gameturn ==0) {
				if(gameturn == playerturn) {
					gr.gameMessage.setText("추가 카드를 한장 골라주세요");
				}else if(gameturn!=playerturn) {
					gr.gameMessage.setText("상대방이 추가 카드를 선택중입니다.");
				}
			}else if(gameturn ==1) {
				if(gameturn == playerturn) {
					gr.gameMessage.setText("추가 카드를 한장 골라주세요");
				}else if(gameturn!=playerturn) {
					gr.gameMessage.setText("상대방이 추가 카드를 선택중입니다.");
				}
			}
		}else if (msgNumber ==3) {
			if(gameturn ==0){
				if(gameturn == playerturn) {
					gr.gameMessage.setText(gr.convertToMultiline("상대방의 카드에서 1장을 선택, 숫자는 무엇일까요?\n 01,2,3,4,5,6,7,8,9,10,11,"));
				}else if(gameturn!=playerturn) {
					gr.gameMessage.setText("상대방이 카드를 추측중입니다.");
				}
			}else if(gameturn ==1) {
				if(gameturn == playerturn) {
					gr.gameMessage.setText(gr.convertToMultiline("상대방의 카드에서 1장을 선택, 숫자는 무엇일까요?\n 01,2,3,4,5,6,7,8,9,10,11,"));
				}else if(gameturn!=playerturn) {
					gr.gameMessage.setText("상대방이 카드를 추측중입니다.");
				}
			}
		}
	}

	public void messageOnDeckChoose(int gameturn, int playerturn, double c,int msgNumber) {
		if(msgNumber==1) {
			if(gameturn ==0) {
				if(gameturn == playerturn) {
					gr.gameMessage.setText("맞았습니다.");
				}else if(gameturn!=playerturn) {
					gr.gameMessage.setText(gr.convertToMultiline("상대방이"+c+"라고 추측했습니다.\n 상대가 맞췄습니다."));
				}
			}else if(gameturn ==1) {
				if(gameturn == playerturn) {
					gr.gameMessage.setText("맞았습니다.");
				}else if(gameturn!=playerturn) {
					gr.gameMessage.setText(gr.convertToMultiline("상대방이"+c+"라고 추측했습니다.\n 상대가 맞췄습니다."));
				}
			}
		}else if (msgNumber==2) {
			if(gameturn ==0) {
				if(gameturn == playerturn) {
					gr.gameMessage.setText("틀렸습니다.가져온 카드가 공개되었습니다.");
				}else if(gameturn!=playerturn) {
					gr.gameMessage.setText(gr.convertToMultiline("상대방이"+c+"라고 추측\n 상대가 틀렸습니다."));
				}
			}else if(gameturn ==1) {
				if(gameturn == playerturn) {
					gr.gameMessage.setText("틀렸습니다.가져온 카드가 공개되었습니다.");
				}else if(gameturn!=playerturn) {
					gr.gameMessage.setText(gr.convertToMultiline("상대방이"+c+"라고 추측했습니다.\n 상대가 틀렸습니다."));
				}
			}
		}
	}
	public void gameEndMessage(int gameturn, int playerturn) {
		if(gameturn ==0) {
			if(gameturn == playerturn) {
				gr.gameMessage.setText("당신이 승리하였습니다. ");
			}else if(gameturn!=playerturn) {
				gr.gameMessage.setText("당신이 패배하였습니다. ");
			}
		}else if(gameturn ==1) {
			if(gameturn == playerturn) {
				gr.gameMessage.setText("당신이 승리하였습니다.");
			}else if(gameturn!=playerturn) {
				gr.gameMessage.setText("당신이 패배하였습니다.");
			}
		}
	}

}


