import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;


//��ü ������ : 1024 X 768
public class WaitRoom extends JPanel {
	
	//ä��â ȭ�� ����
	JTextPane chatHistory;  // textArea , �������� �ؽ�Ʈ �ƹ�Ÿ �ø��Ⱑ ����
	JTextField chatInput;	
	//���� ��ư ����
	JButton btn_ready_1,btn_ready_2;
	//ĳ���� 
	//private Image player1Image = new ImageIcon(MainForm.class.getResource("../images/avartar1.jpg")).getImage();
	
	WaitRoom(){
		setLayout(null); 
		
		// ����
		chatHistory = new JTextPane(); //������ ���� : tp => chatHistory
		chatInput = new JTextField(); //������ ���� : tf => chatInput
		btn_ready_1 = new JButton("Ready");
		btn_ready_2 = new JButton("Ready");

		// ���� �г�
		//JPanel p2 = new JPanel();
		//p2.setBounds(700, 0, 324, 768); 
		
		JScrollPane chatRm = new JScrollPane(chatHistory); // ��ũ�� �����ϰ� ����. ������ ����: js3 => chatRm
		chatRm.setBounds(705, 10, 300, 680); // ���� ���� : (615, 15, 380, 300);
		chatInput.setBounds(705, 695, 300, 30); 		

		//add(p2);
		add(chatRm);
		add(chatInput);		

		// �����г� : 700 * 768  
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

