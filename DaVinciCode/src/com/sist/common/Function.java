package com.sist.common;
/*
 * 	네트워크 (CS - Client Server) => 서버를 거쳐서 클라이언트가 변경
 * 		서버 => 클라이언트의 요청을 받아서 => 응답 (명령)
 * 		클라이언트 => 요청 => 응답 받아서 클라이언트 프로그램에 출력
 *
 * 		1) 클라이언트마다 통신이 따로 만들어져야 한다.
 * 						========
 * 						쓰레드 => 통신만 담당(클라이 언트마다 하나)
 * 		2) 클라이언트 자체에서 처리하지 않고 => 항상 서버를 통해서
 *		3) 스트림을 이용한 프로그램
 *			=====
 *			전송 => 바이트 스트림 => OutputStream
 *			수신 => 문자스트림     => BufferedReader
 */
public class Function {
	//MY 화면 변환.
	//로그인
	public static final int LOGIN = 100;		// 목록 출력 명령
	public static final int MYLOG = 110;		//대기방으로 이동
	//방만듬
	public static final int MAKEROOM = 200;		//방정보를 출력
	public static final int ROOMIN = 210;		//방에 들어가라
	public static final int ROOMOUT = 220;
	public static final int ROOMADD = 230;
	public static final int MYROOMOUT = 240;
	public static final int WAITUPDATE = 250;
	public static final int POSCHANGE = 260;
	public static final int WAITCHAT = 270;
	public static final int ROOMCHAT = 280;

	public static final int KANG = 300;
	public static final int INVITE = 310;
	public static final int INVITE_YES = 320;
	public static final int INVITE_NO = 330;
	public static final int GRCHAT = 290;
	public static final int GAMEREADY = 340;
	public static final int GAMESTART = 350;
	public static final int GAMESTARTADD = 360;

	public static final int GAMESTARTNEW = 500;
	public static final int TURNSET = 510;
	public static final int TURNCHANGE = 515;
	public static final int DECKCHOOSE = 525;
	public static final int GUESSDECKSTART = 530;
	public static final int GUESSNUMBER = 535;
	public static final int GO_OR_STOP = 540;
	public static final int INGAMETURNCHANGE = 550;

	public static final int DUMMYCHOOSE = 520;
	public static final int GAMEEND = 570;
	public static final int GAMERESET = 600;

			;
	public static final int SRCHAT = 400;
	public static final int EXIT = 900;		//
	public static final int MYEXIT = 910;	//나가는 사람 게임에서 나감.

	/*	Server
	 * 	Function.LOGIN	==> id|pwd|sex|loc	==> waitVC 저장
	 *
	 *  Client
	 *  Function.LOGIN  ==> id|pwd|sex|loc	==> table에 출력
	 */

}