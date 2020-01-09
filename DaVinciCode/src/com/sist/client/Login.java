package com.sist.client;

//Window 와 관련된 클래스
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
/*
 * JFrame //창 최소화,최대화 닫기 창.
 *
 * JPanel 단독 디스플레이 불가능// 내부의 창..  					로그인창
 *
 * 		패널을 4개 다 올려두고, 맨위로 올릴것을 정하는 방식으로 해야 다른 페이지에 저장된 데이터가 날아가지 않음.
 *
 *
 * JWindow //no title bar,,시작 로딩 창
 *
 *
 * JDialog // 닫기버튼만 존재하는 설정화면 같은 형태 , eclipse = file-new-project
 * 			// 다른 일 진행 불가능...
 * 			ex> 방만들기..
 *
 * 			다른 작업이 가능한 방법도 있음(쪽지보내기),
 *
 * extends //단일상속 명령어
 *
 *
 *
 *	윈도우 창 1개의 패널 여러개
 *	패널에다가 내부 화면 구성.. (버튼클릭과 같은 이벤트 처리)
 *
 *	패널 구성방식 디자인  	1. Card layout
 *					2. 탭
 *					3. Splitpan
 *
 *	internal frame 엑셀 ,쪽지창 띄우기, 1대1채팅창 띄우기
 *
 *
 *
 */
import javax.swing.JTextField;

public class Login extends JPanel{// 해당블록은 선언부, 구현이 시작된 부분이 아님..

	JLabel la1, la2;
	JTextField tf;
	JPasswordField pf;
	JButton b1,b2;

	Image back;
	//초기값 설정, 초기화 블록 , 호출을 따로 안해도 자동으로 선언 , 생성자		ex> 자동로그인
	//Login login = new Login(); 생성자로 기능을 하기 위해서 리턴형이 없어야함 ex> void 붙이면 일반 메소드가 됨.
	Login(){
		setLayout(null); //기본 레이아웃 무시
		back = Toolkit.getDefaultToolkit().getImage("images/loginBackground.jpg");
		la1 = new JLabel("ID",JLabel.LEFT);
		//la1.setForeground(Color.white);
		//la1.setFont(new Font("Serif", Font.BOLD, 48));

		la1.setFont(new Font("Setif", Font.BOLD, 15));
		la2 = new JLabel("PassWord",JLabel.LEFT);
		//la2.setForeground(Color.white);
		la2.setFont(new Font("Setif", Font.BOLD, 15));
		tf = new JTextField();
		pf = new JPasswordField();

		b1 = new JButton("로그인");
		//b2 = new JButton("취소");
		//배치
		//la1.set;

		//la1.text
		la1.setBounds(80,220,80,30);
		tf.setBounds(160, 220, 150, 30);
		la1.setBackground(Color.black);


		la2.setBounds(80,255,80,30);
		pf.setBounds(160,255, 150, 30);

		//b1.setBounds(390,400,110,30);
		//b2.setBounds(515,400,110,30);

		JPanel p = new JPanel();
		//p.setBounds(390, 405, 235, 30);
		//p.add(b1);
		//p.add(b2);
		//p.setBounds(80, 310, 235, 30);
		p.setOpaque(false); //투명도

		add(b1);
		b1.setBounds(80,310,230,35);

		add(la1);
		add(la2);
		add(tf);
		add(pf);
		add(p);


	} //블록안에서만 구현

	@Override
	protected void paintComponent(Graphics g) { //스킨 입힐 때 , 백그라운드에 사용
		// 실제 동작하는 화면은 paint를 활용
		//super.paintComponent(g);
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}

}

