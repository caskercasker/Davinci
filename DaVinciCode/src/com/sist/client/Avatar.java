package com.sist.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Avatar extends JPanel{
	ImageIcon b1,b2,b3,b4,sb1,sb2,sb3,sb4;
	JButton b5;
	Image back;
	JLabel msg1;
	Font f1;
	 
	Avatar(){
		setLayout(null); 
		back = Toolkit.getDefaultToolkit().getImage("images/gameBackground.jpg");
		Image image;
		
		b1=new ImageIcon("images/Avatar/_11.jpg");
		sb1=new ImageIcon("images/Avatar/_111.jpg");
		b2=new ImageIcon("images/Avatar/_22.jpg");
		sb2=new ImageIcon("images/Avatar/_222.jpg");
		b3=new ImageIcon("images/Avatar/_33.jpg");
		sb3=new ImageIcon("images/Avatar/_333.jpg");
		b4=new ImageIcon("images/Avatar/_44.jpg");
		sb4=new ImageIcon("images/Avatar/_444.jpg");
		b5=new JButton("결정");
		f1 = new Font("돋움", Font.BOLD, 20);
		msg1 = new JLabel("원하는 캐릭터를 고르세요.");
		
		msg1.setFont(f1);
		msg1.setBounds(380, 100, 500, 20);
		add(msg1);
		
		ButtonGroup g=new ButtonGroup();
		JRadioButton p1Icon = new JRadioButton(b1);
		JRadioButton p2Icon = new JRadioButton(b2);
		JRadioButton p3Icon = new JRadioButton(b3);
		JRadioButton p4Icon = new JRadioButton(b4);
		
		p1Icon.setBorderPainted(true);
		p1Icon.setSelectedIcon(sb1);
		p2Icon.setBorderPainted(true);
		p2Icon.setSelectedIcon(sb2);
		p3Icon.setBorderPainted(true);
		p3Icon.setSelectedIcon(sb3);
		p4Icon.setBorderPainted(true);
		p4Icon.setSelectedIcon(sb4);
	
		g.add(p1Icon);
		g.add(p2Icon);
		g.add(p3Icon);
		g.add(p4Icon);
		
				
		add(p1Icon);
		p1Icon.setBounds( 90, 190, 160, 199);
		p1Icon.setOpaque(false);
		add(p2Icon);
		p2Icon.setBounds( 303, 190, 160, 199);
		p2Icon.setOpaque(false);
		add(p3Icon);
		p3Icon.setBounds( 516, 190, 160, 199);
		p3Icon.setOpaque(false);
		add(p4Icon);
		p4Icon.setBounds( 729, 190, 160, 199);
		p4Icon.setOpaque(false);
		add(b5);
		b5.setBounds( 450, 550, 100, 50);
		b5.setOpaque(false);
	}


	@Override
	protected void paintComponent(Graphics g) { 
		g.drawImage(back, 0, 0, getWidth(), getHeight(), this);
	}

}

