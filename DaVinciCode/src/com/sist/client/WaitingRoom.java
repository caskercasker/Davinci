package com.sist.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class WaitingRoom extends JPanel implements ActionListener{
	JTable table1, table2;
	DefaultTableModel model1, model2;
	TableColumn column1, column2;
	JButton b1, b2;

	JTextArea chatHistory; // 채팅 내용
	JTextField chatInput; // 채팅 입력 창

	Image back;

	WaitingRoom() {
		setLayout(null);
		back = Toolkit.getDefaultToolkit().getImage("images/gameBackground.jpg");

		// Room list
		String[] col1 = { "방 이름", "참가인원", "상태" };
		String[][] row1 = new String[0][3];
		model1 = new DefaultTableModel(row1, col1) { //상속없이 오버라이딩 익명의 클래스

			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false; //편집이 안됨
			}

		};
		table1 = new JTable(model1);
		JScrollPane js1 = new JScrollPane(table1);
		// Table decoration
		table1.getTableHeader().setReorderingAllowed(false); //컬럼 이동 불가능하게
		table1.getTableHeader().setResizingAllowed(false); //컬럼 크기 조절 불가
		table1.getTableHeader().setBackground(Color.LIGHT_GRAY);
		table1.getTableHeader().setForeground(Color.white);
		table1.getTableHeader().setFont(new Font("돋움", Font.BOLD, 14));
		js1.getViewport().setBackground(Color.white);
		// Table cell width
		for (int i = 0; i < 3; i++) {
			column1 = table1.getColumnModel().getColumn(i);
			TableCellRenderer rend = column1.getCellRenderer();
			if (i == 0) {
				column1.setPreferredWidth(440);
			} else if (i == 1) {
				column1.setPreferredWidth(120);
			} else if (i == 2) {
				column1.setPreferredWidth(120);
			}
			column1.setCellRenderer(rend);

		}

		// User list
		String[] col2 = { "ID", "캐릭터", "상태" };
		String[][] row2 = new String[0][4];
		model2 = new DefaultTableModel(row2, col2){ //상속없이 오버라이딩 익명의 클래스
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false; //편집이 안됨
			}
		};
		table2 = new JTable(model2);
		JScrollPane js2 = new JScrollPane(table2);
		// Table decoration
		table2.getTableHeader().setReorderingAllowed(false); //컬럼 이동 불가능하게
		table2.getTableHeader().setResizingAllowed(false); //컬럼 크기 조절 불가
		table2.getTableHeader().setBackground(Color.LIGHT_GRAY);
		table2.getTableHeader().setForeground(Color.white);
		table2.getTableHeader().setFont(new Font("돋움", Font.BOLD, 14));
		js2.getViewport().setBackground(Color.white);
		// Table cell width
		for (int i = 0; i < 3; i++) {
			column2 = table2.getColumnModel().getColumn(i);
			TableCellRenderer rend = column2.getCellRenderer();
			if (i == 0) {
				column2.setPreferredWidth(440);
			} else if (i == 1) {
				column2.setPreferredWidth(120);
			} else if (i == 2) {
				column2.setPreferredWidth(120);
			}
			column2.setCellRenderer(rend);

		}

		// Room list & User list
		setLayout(null);
		js1.setBounds(10, 10, 680, 400);
		js2.setBounds(10, 420, 680, 200);

		// Buttons
		b1 = new JButton("게임방 만들기");
		b2 = new JButton("게임 종료");
		b1.setBounds(170, 650, 150, 40);
		b2.setBounds(370, 650, 150, 40);

		// Chatting
		chatHistory = new JTextArea();	//채팅 기록
		chatInput = new JTextField();	//채팅 인풋
		JScrollPane chatRm = new JScrollPane(chatHistory);
		chatRm.setBounds(705, 10, 300, 680);
		chatInput.setBounds(705, 695, 300, 30);
		// 채팅창 세부 옵션
		chatHistory.setEditable(false);
		chatRm.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); //horizontal scroll 생기지 않도록
		chatHistory.setLineWrap(true); //아주 긴 내용 입력 시 자동으로 줄바뀜되도록 

		add(b1);
		add(b2);
		add(js1);
		add(js2);
		add(chatRm);
		add(chatInput);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// Chat
		String msg= chatInput.getText();
		if(e.getSource()==chatInput) {
//			//chatHistory.append(msg+"\n");
//			if(msg ==null || msg.length()==0) {
//					JOptionPane.showMessageDialog(null,"대화할 내용을 입력하세요","채팅창 경고",JOptionPane.WARNING_MESSAGE);
//			}else
//			chatInput.setText("");
//		}
		}
		// 방만들기 버튼 클릭 시
		if(e.getSource()==b1) {


		}
		// 게임종료 버튼 클릭 시 종료시킴

		if(e.getSource()==b2) {

		}

	}


}
