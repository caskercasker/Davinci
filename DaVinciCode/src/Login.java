//Window �� ���õ� Ŭ����
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
 * JFrame //â �ּ�ȭ,�ִ�ȭ �ݱ� â.
 *
 * JPanel �ܵ� ���÷��� �Ұ���// ������ â..  					�α���â
 *
 * 		�г��� 4�� �� �÷��ΰ�, ������ �ø����� ���ϴ� ������� �ؾ� �ٸ� �������� ����� �����Ͱ� ���ư��� ����.
 *
 *
 * JWindow //no title bar,,���� �ε� â
 *
 *
 * JDialog // �ݱ��ư�� �����ϴ� ����ȭ�� ���� ���� , eclipse = file-new-project
 * 			// �ٸ� �� ���� �Ұ���...
 * 			ex> �游���..
 *
 * 			�ٸ� �۾��� ������ ����� ����(����������),
 *
 * extends //���ϻ�� ��ɾ�
 *
 *
 *
 *	������ â 1���� �г� ������
 *	�гο��ٰ� ���� ȭ�� ����.. (��ưŬ���� ���� �̺�Ʈ ó��)
 *
 *	�г� ������� ������  	1. Card layout
 *					2. ��
 *					3. Splitpan
 *
 *	internal frame ���� ,����â ����, 1��1ä��â ����
 *
 *
 *
 */
import javax.swing.JTextField;

public class Login extends JPanel{// �ش����� �����, ������ ���۵� �κ��� �ƴ�..

	JLabel la1, la2;
	JTextField tf;
	JPasswordField pf;
	JButton b1,b2;

	Image back;
	//�ʱⰪ ����, �ʱ�ȭ ��� , ȣ���� ���� ���ص� �ڵ����� ���� , ������		ex> �ڵ��α���
	//Login login = new Login(); �����ڷ� ����� �ϱ� ���ؼ� �������� ������� ex> void ���̸� �Ϲ� �޼ҵ尡 ��.
	Login(){
		setLayout(null); //�⺻ ���̾ƿ� ����
		back = Toolkit.getDefaultToolkit().getImage("image\\login_bg.jpg");
		la1 = new JLabel("ID",JLabel.LEFT);
		//la1.setForeground(Color.white);
		//la1.setFont(new Font("Serif", Font.BOLD, 48));

		la1.setFont(new Font("Setif", Font.BOLD, 15));
		la2 = new JLabel("PassWord",JLabel.LEFT);
		//la2.setForeground(Color.white);
		la2.setFont(new Font("Setif", Font.BOLD, 15));
		tf = new JTextField();
		pf = new JPasswordField();

		b1 = new JButton("�α���");
		b2 = new JButton("���");
		//��ġ
		//la1.set;

		//la1.text
		la1.setBounds(80,230,80,30);
		tf.setBounds(160, 230, 150, 30);
		la1.setBackground(Color.black);


		la2.setBounds(80,265,80,30);
		pf.setBounds(160,265, 150, 30);

		//b1.setBounds(390,400,110,30);
		//b2.setBounds(515,400,110,30);

		JPanel p = new JPanel();
		//p.setBounds(390, 405, 235, 30);
		p.add(b1);
		p.add(b2);
		p.setBounds(80, 310, 235, 30);
		p.setOpaque(false); //����

		add(la1);
		add(la2);
		add(tf);
		add(pf);
		add(p);

//		b1.setBounds(390,400,110,30);
//		b2.setBounds(515,400,110,30);
//		add(b1);
//		add(b2);


	} //��Ͼȿ����� ����

	@Override
	protected void paintComponent(Graphics g) { //��Ų ���� �� , ��׶��忡 ���
		// ���� �����ϴ� ȭ���� paint�� Ȱ��
		// TODO Auto-generated method stub
		//super.paintComponent(g);
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}

}

