import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class MainForm extends JFrame implements ActionListener { // ActionLister �������̽�
	Login login = new Login();
	WaitRoom wr = new WaitRoom();
	GameRoom gr = new GameRoom();
	Avatar ava = new Avatar();
	CardLayout card = new CardLayout();

	MainForm() {
		setLayout(card);
		add("LOGIN", login);
		add("WR", wr);
		add("AVARTAR", ava);
		add("GAME", gr);

		setSize(1024, 768); // ������â ������ ����
		setVisible(true); // �����츦 ������.
		setResizable(false); // â ũ�� ���� �Ұ����ϰ�
		setLocationRelativeTo(null); // â�� �� �߾ӿ� �߰� 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // â ���� �� ���� ����ǵ��� (�׷��� ������ ���� ���� ��� ���ư�...)
		
		login.b1.addActionListener(this);

	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
		}
		MainForm mf = new MainForm();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == login.b1) {
			card.show(getContentPane(), "WR");
		}

	}
}
