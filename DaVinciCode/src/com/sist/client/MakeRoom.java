package com.sist.client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MakeRoom extends JFrame implements ActionListener{

	JOptionPane jp;
	JDialog dia;
	JTextField roomNameInput;	
	JButton cancleBtn;
	JButton confirmBtn;
	JLabel nameRequest;
	
	MakeRoom(){
		
		setLayout(null);
		this.setTitle("방 만들기"); 		
		setSize(400, 200); // 윈도우창 사이즈 설정
		setVisible(true); // 윈도우를 보여라.
		setResizable(false); // 창 크기 변경 불가능하게
		setLocationRelativeTo(null); // 창이 정 중앙에 뜨게
		//setDefaultCloseOperation(this.EXIT_ON_CLOSE); // 창 종료 시 게임 종료되도록 (그렇지 않으면 게임 꺼도 계속 돌아감...)
		
		nameRequest = new JLabel("방 이름을 입력해주세요.");
		roomNameInput = new JTextField();
		cancleBtn = new JButton("취소");
		confirmBtn = new JButton("확인");		
		
		nameRequest.setBounds(80,20,240,30);
		roomNameInput.setBounds(80,60,240,30); 
		cancleBtn.setBounds(80,110,110,30); 
		confirmBtn.setBounds(210,110,110,30); 
		
		add(roomNameInput);
		add(cancleBtn);
		add(confirmBtn);
		add(nameRequest);
		
		cancleBtn.addActionListener(this);
		confirmBtn.addActionListener(this);
		roomNameInput.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==cancleBtn) {
			dispose();
		}			
		else if(e.getSource()==confirmBtn) {
			String roomName= roomNameInput.getText();
			if(roomName ==null || roomName.length()==0) {
				JOptionPane.showMessageDialog(null,"방 이름을 입력하세요.","방 만들기 실패",JOptionPane.WARNING_MESSAGE);
			}
			else {
				dispose();
			}
			
		}
			
	}

}
