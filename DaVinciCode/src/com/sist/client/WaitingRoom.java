package com.sist.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;

import javax.swing.table.*;

public class WaitingRoom extends JPanel implements ActionListener, MouseListener{
	JTable table1, table2;
	DefaultTableModel model1, model2;
	JButton b1, b2;

	JTextArea chatHistory; // 채팅 내용
	JTextField chatInput; // 채팅 입력 창

	JLabel la = new JLabel();

	WaitingRoom() {

		// Room list
		la.setIcon(new ImageIcon(getImageSizeChange(new ImageIcon("c:\\image\\file.jpg"), 380, 150)));
		String[] col1 = { "방 이름", "참가인원", "상태" };
		String[][] row1 = new String[0][3];
		model1 = new DefaultTableModel(row1, col1);
		table1 = new JTable(model1);
		JScrollPane js1 = new JScrollPane(table1);

		// User list
		String[] col2 = { "ID", "캐릭터", "상태" };
		String[][] row2 = new String[0][4];
		model2 = new DefaultTableModel(row2, col2);
		table2 = new JTable(model2);
		JScrollPane js2 = new JScrollPane(table2);

		// Room list & User list
		setLayout(null);
		js1.setBounds(10, 10, 680, 400);
		js2.setBounds(10, 420, 680, 150);

		// Buttons
		b1 = new JButton("게임방 만들기");
		b2 = new JButton("게임 종료");

		JPanel p = new JPanel();
		p.add(b1);
		p.add(b2);
		p.setBounds(10, 600, 680, 100);
		
		// Chatting
		chatHistory = new JTextArea();	//채팅 기록
		chatInput = new JTextField();	//채팅 인풋
		
		JScrollPane chatRm = new JScrollPane(chatHistory);
		chatRm.setBounds(705, 10, 300, 680);
		chatInput.setBounds(705, 695, 300, 30);
		chatHistory.setEnabled(false);
		chatInput.addActionListener(this);

		
		add(p);
		add(js1);
		add(js2);
		add(chatRm);
		add(chatInput);
	}

	public Image getImageSizeChange(ImageIcon icon, int width, int height) {
		Image img = icon.getImage();
		Image change = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return change;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Chat
		String msg= chatInput.getText();
		if(e.getSource()==chatInput) {
			chatHistory.append(msg+"\n");
			if(msg ==null || msg.length()==0) {
					JOptionPane.showMessageDialog(null,"대화할 내용을 입력하세요","채팅창 경고",JOptionPane.WARNING_MESSAGE);
			}else 
			chatInput.setText("");
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
