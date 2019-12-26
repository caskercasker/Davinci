import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Avatar extends JPanel{
	Image back;
	Avatar(){
		setLayout(null); //기본 레이아웃 무시
		back = Toolkit.getDefaultToolkit().getImage("c:\\image\\gameground.jpg");

	}

	//JButton btn_choice;
	//java.awt.image.BufferedImage buttonIcon = ImageIO.read(new File("c:\\image\\avartar_4.jpg"));

	//BufferedImage buttonIcon = ImageIO.read(new File("buttonIconPath"));
	//button = new JButton(new ImageIcon(buttonIcon));


	@Override
	protected void paintComponent(Graphics g) { //스킨 입힐 때 , 백그라운드에 사용
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}

}
