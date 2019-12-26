import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class MainForm extends JFrame implements ActionListener{//ActionLister 인터페이스
	Login login = new Login();
	WaitRoom wr = new WaitRoom();
	GameRoom gr = new GameRoom();
	Avatar ava = new Avatar();
	CardLayout card = new CardLayout();



	MainForm()	{
		setLayout(card);
		add("LOGIN",login);
		add("AVARTAR",ava);
		add("WR",wr);
		add("GAME",gr);
		//add("LOGIN",login);



		setSize(1024,768);
		setVisible(true); //윈도우를 보여라.
		login.b1.addActionListener(this);
	}
	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			JFrame.setDefaultLookAndFeelDecorated(true);
		}catch(Exception e) {}
		MainForm mf = new MainForm();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==login.b1) {
			card.show(getContentPane(), "WR");
		}

	}
}
