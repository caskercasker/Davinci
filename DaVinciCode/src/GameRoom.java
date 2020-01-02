import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class GameRoom extends JPanel {
	Image back;
	GameRoom(){
		setLayout(null); //기본 레이아웃 무시
		back = Toolkit.getDefaultToolkit().getImage("image\\game_bg.jpg");

	}

	@Override
	protected void paintComponent(Graphics g) { //스킨 입힐 때 , 백그라운드에 사용
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}

}
