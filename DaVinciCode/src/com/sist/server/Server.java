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
		String id,name,img,pos;
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
						name = st.nextToken();
						img = st.nextToken();
						pos = "대기실";


						messageAll(Function.LOGIN+"|"+id+"|"+name+"|"+pos);
						//img+"|"+
						waitVc.add(this);
						messageTo(Function.MYLOG+"|"+id);
						for(Client user:waitVc) {
							messageTo(Function.LOGIN+"|"+user.id+"|"+user.name+"|"+user.pos);
						}

						//개설된 방 정보 전송
						for(Room room :roomVc) {
							messageTo(Function.MAKEROOM+"|"+room.roomName+"|"+room.roomState+"|"+room.current+"/"+room.maxcount);
						}
						break;
					}
					case Function.WAITCHAT:{

						messageAll(Function.WAITCHAT+"|["+name+"]"+st.nextToken());
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
						room.userVc.add(this);


						roomVc.add(room);
						pos = room.roomName;

						messageAll(Function.MAKEROOM+"|"+room.roomName+"|"+room.roomState+"|"+room.current+"/"+room.maxcount);
						break;
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