package com.sist.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {
	private Connection conn;//����Ŭ ���� => Socket
	private PreparedStatement ps; // BufferedReader, OutputStream
	private final String URL="jdbc:oracle:thin:@211.238.142.192:1521:XE"; // �п����� ������ ��
	//private final String URL="jdbc:oracle:thin:@localhost:1521:XE"; // ������ ������ ��
	// ����Ŭ ���� �ּ�
	// 1. ����̹� ��� => �ѹ����� => ������
	public MemberDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	// 2. ����
	public void getConnection() {
		try {
			conn = DriverManager.getConnection(URL,"hr","happy");
			//conn hr/happy
		}catch(Exception ex)	{}
	}
	// 3. ��������
	public void disConnection() {
		try {
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
			// exit
		}catch(Exception ex) {}
	}
	// 4. ���ó��
	public String isLogin(String id, String pwd) {
		String result = "";
		try {
				//����
			getConnection();
			//����Ŭ�� ��û
			String sql = "SELECT COUNT(*) FROM memberInfo WHERE id=?"; //id�� �� ��ΰ�?
			ps = conn.prepareStatement(sql); //����Ŭ�� ����
			//?�� ���� ä���.
			ps.setString(1, id);
			//�����û
			ResultSet rs = ps.executeQuery();
			rs.next(); //���� ��� ��ġ�� Ŀ���� �̵��Ѵ�.
			int count = rs.getInt(1); //���ڳ� ���ڳ� ��¥�Ŀ� ���� ������ ���� ������(number,varchar,date)
			rs.close();

			if(count==0) {
				result="NOID";
			}else {
				// ��û
				sql = "SELECT * FROM memberInfo WHERE id=?";
				ps = conn.prepareStatement(sql); //����
				ps.setString(1, id); //���� ���� ?�� ���� ä���.
				rs = ps.executeQuery();
				rs.next();
				//���� �޴´�.
				String mid = rs.getString(1);
				String mpwd = rs.getString(2);
				String name = rs.getString(3);
				String img_source = rs.getString(4);
				//String img_source = rs.getString(5);
				rs.close();

				if(mpwd.equals(pwd)) {
					//�α���
					result=mid+"|"+name+"|"+img_source;
				}else {
					//��й�ȣ�� Ʋ����
					result ="NOPWD";
				}

				/*		String name = "";
				 * 		String sex = "";
				 * 		String id = "";
				 * 		int age=10;
				 * 		sql = "INSERT INTO member VALUES('"+name+"','"+sex+"',
				 * 					'"+id+"',"+age+")"; //���õ� �� Statement
				 * 		sql = "INSERT INTo member VALUE (?,?,?,?)"; ������ ����� �ΰ� ���� ���߿� ?�� ���� ä�� �ִ´�. //prepared Statement
				 */
			}

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		finally {
			//���� ����
			disConnection();
		}
		return result;
	}
}
