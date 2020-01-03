import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;


//전체 사이즈 : 1024 X 768
public class WaitRoom extends JPanel {
	
	JTextPane chatHistory; 
	JTextField chatInput;	
	JButton btn_ready_1,btn_ready_2;
	JLabel msg1,msg2,showMyID,showOtherID;
	
	ImageIcon ava1,ava2;
	JLabel ava1Box,ava2Box;
	Font f1;
	Font f2;
	
	WaitRoom(){
		setLayout(null); 
		
		chatHistory = new JTextPane();
		chatInput = new JTextField();
		btn_ready_1 = new JButton("Ready");
		btn_ready_2 = new JButton("Ready");
		
		// Message on top 
		f1 = new Font("돋움", Font.BOLD, 15);
		f2 = new Font("돋움", Font.PLAIN, 12);
		msg1 = new JLabel("게임할 준비가 되었다면 Ready 버튼을 눌러주세요.");
		msg2 = new JLabel("(상대방도 Ready 버튼을 누르면 게임이 시작됩니다.)");
		showMyID = new JLabel("나(Monariza1)");
		showOtherID = new JLabel("상대방(Aziranom2)");
		
		msg1.setFont(f1);
		msg1.setBounds(190, 100, 500, 20);
		add(msg1);
		msg2.setFont(f2);
		msg2.setBounds(225, 120, 500, 20);
		add(msg2);
		
		// Chatting room on right 
		JScrollPane chatRm = new JScrollPane(chatHistory); // 스크롤 가능하게
		chatRm.setBounds(705, 10, 300, 680); 
		chatInput.setBounds(705, 695, 300, 30); 		

		add(chatRm);
		add(chatInput);		
		
		// Show ID 
		showMyID.setBounds(130, 220, 200, 40);
		add(showMyID);
		
		showOtherID.setBounds(420, 220, 200, 40);
		add(showOtherID);
		
		// Show avatar image
		ava1 = new ImageIcon("image\\Avatar\\_11.jpg");
		ava1Box = new JLabel(ava1);
		ava1Box.setBounds(130, 280, 160, 199);
		add(ava1Box);
		
		ava2 = new ImageIcon("image\\Avatar\\_22.jpg");
		ava2Box = new JLabel(ava2);
		ava2Box.setBounds(420, 280, 160, 199);
		add(ava2Box);
		
		// Ready button on bottom 
		btn_ready_1.setBounds(130, 520, 160, 40);
		add(btn_ready_1);
		
		btn_ready_2.setBounds(420, 520, 160, 40);
		add(btn_ready_2);
		
	}

	
}

