import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class GameRoom extends JPanel {
	Image back;
	GameRoom(){
		setLayout(null); //�⺻ ���̾ƿ� ����
		back = Toolkit.getDefaultToolkit().getImage("c:\\image\\gameground.jpg");

	}

	@Override
	protected void paintComponent(Graphics g) { //��Ų ���� �� , ��׶��忡 ���
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}

}
