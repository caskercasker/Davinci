package com.sist.client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class MakeRoom extends JDialog implements ActionListener{
	JLabel la1,la2,la3,la4;
	JTextField tf;
	JComboBox box;
	JPasswordField pf;
	JRadioButton rb1,rb2;
	JButton b1,b2;
	public MakeRoom() {
		la1 = new JLabel("방이름",JLabel.RIGHT);
		la2 = new JLabel("상태",JLabel.RIGHT);
		la3 = new JLabel("인원",JLabel.RIGHT);
		la4 = new JLabel("비밀번호");

		tf = new JTextField();
		pf = new JPasswordField();
		rb1 = new JRadioButton("공개");
		rb2 = new JRadioButton("비공개");
		ButtonGroup bg = new ButtonGroup();
		bg.add(rb1);
		bg.add(rb2);
		// 선택
		rb1.setSelected(true);
		la4.setVisible(false);
		pf.setVisible(false);

		b1 = new JButton("방만들기");
		b2 = new JButton("취소");

		box = new JComboBox();
		box.addItem(2+"명");



		//배치
		setLayout(null);
		la1.setBounds(10, 15, 50, 30);
		tf.setBounds(65, 15, 150, 30);

		la2.setBounds(10, 50, 50, 30);
		rb1.setBounds(65, 50, 70, 30);
		rb2.setBounds(140, 50, 70, 30);

		la4.setBounds(65, 85, 55, 30);
		pf.setBounds(120, 85, 95, 30);

		la3.setBounds(10, 120, 50, 30);
		box.setBounds(65, 120, 150, 30);

		JPanel p=new JPanel();
		p.add(b1);
		p.add(b2);

		p.setBounds(10, 160, 205, 35);
		add(la1); add(tf);
		add(la2); add(rb1); add(rb2);
		add(la3); add(box);
		add(la4); add(pf);
		add(p);
		//setSize(255, 255);
		setBounds(448,156, 255, 255);
		//setVisible(true);

		rb1.addActionListener(this);
		rb2.addActionListener(this);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MakeRoom();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == rb1) {
			la4.setVisible(false);
			pf.setVisible(false);
			pf.setText("");

		}else if(e.getSource() == rb2) {
			la4.setVisible(true);
			pf.setVisible(true);
			pf.setText("");
			pf.requestFocus();
		}
	}

}
