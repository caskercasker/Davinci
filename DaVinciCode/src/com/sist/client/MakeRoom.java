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

public class MakeRoom extends JDialog {
	JLabel la1,la2,la3;
	JTextField tf;
	JComboBox box;
	JPasswordField pf;
	JRadioButton rb1;
	JButton b1,b2;
	public MakeRoom() {
		la1 = new JLabel("방이름",JLabel.RIGHT);
		la2 = new JLabel("상태",JLabel.RIGHT);
		la3 = new JLabel("인원",JLabel.RIGHT);


		tf = new JTextField();
		pf = new JPasswordField();
		rb1 = new JRadioButton("공개");

		ButtonGroup bg = new ButtonGroup();
		bg.add(rb1);
	
		// 선택
		rb1.setSelected(true);

		b1 = new JButton("방만들기");
		b2 = new JButton("취소");

		box = new JComboBox();
		box.addItem(2+"명");



		//배치
		setLayout(null);
		la1.setBounds(10, 15, 50, 30);
		tf.setBounds(65, 15, 150, 30); //방 이름 입력

		la2.setBounds(10, 50, 50, 30);
		rb1.setBounds(65, 50, 70, 30);
			
		la3.setBounds(10, 85, 50, 30);
		box.setBounds(65, 85, 150, 30);

		JPanel p=new JPanel();
		p.add(b1);
		p.add(b2);

		p.setBounds(10, 130, 205, 35);
		add(la1); add(tf);
		add(la2); add(rb1);
		add(la3); add(box);
		add(p);
		//setSize(255, 255);
		setBounds(448,156, 255, 220);
		//setVisible(true);

	

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MakeRoom();
	}


}