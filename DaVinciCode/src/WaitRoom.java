import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

public class WaitRoom extends JPanel {
	JTable table1, table2;
	DefaultTableModel model1, model2;
	
	JTextPane tp;  // textArea , 여러줄의 텍스트 아바타 올리기가 가능
	JTextField tf;
	
	JButton btn_ready_p1,btn_ready_p2;
	
	WaitRoom(){
		String[] col1 = {"방이름","공개/비공개","인원",};
		String[][] row1 = new String[0][3];

		model1 = new DefaultTableModel(row1, col1);
		table1 = new JTable(model1);
		JScrollPane js1 = new JScrollPane(table1);
		//

		String[] col2 = {"ID","이름","성별","위치"};
		String[][] row2 = new String[0][4];
		model2 = new DefaultTableModel(row2, col2);
		table2 = new JTable(model2);
		JScrollPane js2 = new JScrollPane(table2);

		setLayout(null);
		js1.setBounds(10, 15, 600, 400);
		js2.setBounds(10,420, 600, 290);

		tp = new JTextPane();
		tf = new JTextField();
		btn_ready_p1 = new JButton("Ready");
		btn_ready_p2 = new JButton("Ready");

		//tp.setBounds(615, 15, 380, 300);
		JScrollPane js3 = new JScrollPane(tp);
		js3.setBounds(615, 15, 380, 300);

		tf.setBounds(615, 420, 380, 30);

		add(tf);

		JPanel p = new JPanel();

		//p.setLa
		p.setLayout(new GridLayout(3,2,5,5));
		p.add(btn_ready_p1);
		p.add(btn_ready_p2);

		p.setBounds(615, 460, 380, 240);

		add(p);
		add(js3);
		add(js1);
		add(js2);
	}
}

