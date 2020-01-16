package com.sist.server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import com.sist.common.Function;
public class Server implements Runnable{
	//연결 => 접속 => ServerSocket
	//각 클라이언트마다 통신담당 (Thread) => Socket <=> Socket
	//1.서버 가동
	private ServerSocket ss;
	private final int PORT = 8888;
	//접속자 저장 공간

	private Vector<Client> waitVc = new Vector<Client>();
	private Vector<Room>	roomVc = new Vector<Room>();
	public Server() {
		try {
			ss = new ServerSocket(PORT); //bind,listen
			System.out.println("Server Start .......");
		}catch (Exception ex) {}
	}
	//접속시 처리
	@Override
	public void run() {
		try {
			while(true) {
				//접속을 했다면 클라이언트의 정보 수집 => IP,PORT(Socket);
				Socket s = ss.accept();
				//s(클라이언트의 정보 (ip,port) => Thread로 전송 (각자마다 통신을 할 수 있다)
				Client client = new Client(s);
				client.start();
			}
		}catch(Exception ex) {}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server = new Server();
		new Thread(server).start();
	}
	//통신을 담당하는 부분 (각 클라이언트마다 따로 작업을 한다)
	class Client extends Thread{
		String id,img_name,img_source,pos;
		//int img;
		//pos => 방위치
		//통신
		Socket s; //통신장비
		//보내기
		OutputStream out;
		//받기
		BufferedReader in;

		public Client(Socket s) {
			try {
				this.s = s;
				out = s.getOutputStream(); //클라이언트의 저장위치 => 일어갈수 있게 만든다.
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				//inputStreamReader( 필터스트림) => byte -> 2bteㄹ 변환
			}catch (Exception ex) {}
		}
		//반복을 제거 => 메소드
		// 서버에서 => 전송
		// 개인적으로 전송

		//클라이언트와 통신
		@Override
		public void run() {
			try {
				//100|hong|홍길동|남자\n
				while(true) {
					String msg = in.readLine();
					StringTokenizer st = new StringTokenizer(msg,"|");
					int protocol = Integer.parseInt(st.nextToken());
					switch(protocol) {
					case Function.LOGIN:{
						id=st.nextToken();
						img_name = st.nextToken();
						img_source = st.nextToken();
						pos = "대기실";



						messageAll(Function.LOGIN+"|"+id+"|"+img_name+"|"+pos);
						//img+"|"+
						waitVc.add(this);
						messageTo(Function.MYLOG+"|"+id);
						for(Client user:waitVc) {
							messageTo(Function.LOGIN+"|"+user.id+"|"+user.img_name+"|"+user.pos);
						}

						//개설된 방 정보 전송
						for(Room room :roomVc) {
							messageTo(Function.MAKEROOM+"|"+room.roomName+"|"+room.current+"/"+room.maxcount+"|"+room.roomState);
						}
						break;
					}
					case Function.WAITCHAT:{

						messageAll(Function.WAITCHAT+"|["+img_name+"]"+st.nextToken());
						break;
					}
					case Function.EXIT:{
						String mid = id;
						for(int i=0; i<waitVc.size();i++) {
							Client user=waitVc.get(i);
							if(mid.equals(user.id)) {
								//윈도우 종료
								messageTo(Function.MYEXIT+"|");
								//vector 제거
								waitVc.remove(i);
								//닫기  (통신종료)
								in.close();
								out.close();
								break;
							}
						}
						//전체 메세지 => 나가는 유저를 테이블에서 삭제
						messageAll(Function.EXIT+"|"+mid);
						break;
					}
					case Function.MAKEROOM:{
						/*
						 * Function.makeRoom
						 *
						 */
						Room room = new Room(st.nextToken(), st.nextToken(), st.nextToken()	, Integer.parseInt(st.nextToken()));
						//String roomName, String roomState, String roomPwd, int maxcount)
						room.userVc.add(this);


						roomVc.add(room);
						pos = room.roomName;

						messageAll(Function.MAKEROOM+"|"+room.roomName+"|"+room.current+"/"+room.maxcount+"|"+pos);
						//방에 들어가게 만든다.
						messageTo(Function.ROOMIN+"|"+room.roomName+"|"+id+"|"+img_name+"|"+img_source); //이미지 파일 명 떄문에 성별과 아바타 번호를 받는다.



						//대기실
						   messageAll(Function.POSCHANGE+"|"+id+"|"+pos);
						break;
					}
					case Function.ROOMIN:{
						//Function.ROOMIN+"|"+rn
						String rn = st.nextToken();
						/*
						 * 	1. 방이름을 받는다.
						 * 	2. 방을 찾는다 (roomVc)
						 *  3. pos.current 를 변경
						 *  ===================
						 *  = 방에 있는 사람 처리 => ROOMADD
						 *  	1. 방에 입장하는 사람의 정보를 정보 전송  (id,avata..)
						 *    	2. 입장메시지 전송
						 *  = 방에 들어가는 사람 처리 =>
						 *  	1. 방에 들어가라 => ROOMIN
						 *  	2. 방에 있는 사람들에게 정보를 보내준다.
						 *  = 대기실 변경
						 *  	인원수가 변경 => 메시지 전송
						 *
						 */
						for(Room room:roomVc) {
							if(rn.equals(room.roomName)) { //방찾기
								pos=room.roomName;
								room.current++;

								for(Client user:room.userVc) { //uservc들어있는 첫번쨰 값이 true 니까 두번째 공간에다가 userVC에 들어있는 값을 넣어라.
									user.messageTo(Function.ROOMADD+"|"
											+id+"|"+img_name+"|"+img_source);
									user.messageTo(Function.ROOMCHAT+"|[알림 ☞]"+id+"님이 입장하셨습니다.");
								}
								//본인처리
								room.userVc.add(this);
								messageTo(Function.ROOMIN+"|"+room.roomName+"|"+id+"|"+img_name+"|"+img_source);

								for(Client user:room.userVc) {
									if(!id.equals(user.id)) {
										messageTo(Function.ROOMADD+"|"
												+user.id+"|"+user.img_name+"|"+user.img_source);
									}
								//대기실 갱신
								messageAll(Function.WAITUPDATE+"|"+room.roomName+"|"+room.current+"|"+room.maxcount+"|"+id+"|"+pos);
								}
							}
						}
						break;
					}
					case Function.ROOMCHAT:{
						   String rn = st.nextToken();
						   String strMsg = st.nextToken();
						   for(Room room:roomVc) {
							   if(rn.equals(room.roomName)) {
								   for(Client user:room.userVc) {
									   user.messageTo(Function.ROOMCHAT+"|["+id+"."+img_name+"]"+strMsg);
								   }
							   }
						   }
						   break;
					   }
					   case Function.ROOMOUT:{
						   //방찾기
						   String rn=st.nextToken();
						   for(int i=0; i<roomVc.size(); i++) {
							   Room room=roomVc.get(i);
							   if(rn.equals(room.roomName)) {
								   pos = "대기실";
								   room.current--;

								   //방에 남아 있는 사람
								   for(Client user:room.userVc) {
									   if(!user.id.equals(id)) {
										   user.messageTo(Function.ROOMOUT+"|"+id);
										   user.messageTo(Function.ROOMCHAT+"|[알림☞]"+id+"."+img_name+"님이 퇴장하셨습니다");
									   }
								   }
								   //실제 나가는 사람
								   for(int j=0; j<room.userVc.size();j++) {
									   Client user = room.userVc.get(j);
									   if(id.equals(user.id)) {
										   //userVc에서 제거
										   room.userVc.remove(j);
										   messageTo(Function.MYROOMOUT+"|");
										   break;
									   }
								   }
								   //대기실
								   messageAll(Function.WAITUPDATE+"|"+room.roomName+"|"+room.current+"|"+room.maxcount+"|"+id+"|"+pos);
								   if(room.current==0) {

									   roomVc.remove(i);
									   break;
								   }
							   }
						   }
						  //messageTo(Function.MYROOMOUT+"|");
						 // break;
					   }

					}
				}
			}catch(Exception ex) {}
		}

		//반복을 제거 => 메소드
		// 서버에서 => 전송
		// 개인적으로 전송
		public synchronized void messageTo(String msg) {
			try {
				out.write((msg+"\n").getBytes());
				//readLine() => 끝나는 시점 =(\n)
			}catch(Exception ex) {}
		}
		// 전체적으로 전송
		public synchronized void messageAll(String msg) {
			try {
				for(Client user:waitVc) {
					user.messageTo(msg);
				}
			}catch(Exception ex) {}
		}
	}
}
