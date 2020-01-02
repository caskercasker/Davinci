import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;


//전체 사이즈 : 1024 X 768
public class WaitRoom extends JPanel {
	
	//채팅창 화면 선언
	JTextPane chatHistory;  // textArea , 여러줄의 텍스트 아바타 올리기가 가능
	JTextField chatInput;	
	//레디 버튼 선언
	JButton btn_ready_1,btn_ready_2;
	//캐릭터 
	//private Image player1Image = new ImageIcon(MainForm.class.getResource("../images/avartar1.jpg")).getImage();
	
	WaitRoom(){
		setLayout(null); 
		
		// 생성
		chatHistory = new JTextPane(); //변수명 변경 : tp => chatHistory
		chatInput = new JTextField(); //변수명 변경 : tf => chatInput
		btn_ready_1 = new JButton("Ready");
		btn_ready_2 = new JButton("Ready");

		// 우측 패널
		//JPanel p2 = new JPanel();
		//p2.setBounds(700, 0, 324, 768); 
		
		JScrollPane chatRm = new JScrollPane(chatHistory); // 스크롤 가능하게 변경. 변수명 변경: js3 => chatRm
		chatRm.setBounds(705, 10, 300, 680); // 이전 설정 : (615, 15, 380, 300);
		chatInput.setBounds(705, 695, 300, 30); 		

		//add(p2);
		add(chatRm);
		add(chatInput);		

		// 좌측패널 : 700 * 768  
		//JPanel p1 = new JPanel();
		//p1.setBounds(0, 0, 700, 768);  
		//add(p1);
		//p1.add(btn_ready_1);
		//p1.add(btn_ready_2);
		
		btn_ready_1.setBounds(150, 550, 150, 40);
		add(btn_ready_1);
		
		btn_ready_2.setBounds(400, 550, 150, 40);
		add(btn_ready_2);
		

		
	}

	
}

