package com.sist.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {
	private Connection conn;//오라클 연결 => Socket
	private PreparedStatement ps; // BufferedReader, OutputStream
	private final String URL="jdbc:oracle:thin:@211.238.142.192:1521:XE";
	// 오라클 연결 주소
	// 1. 드라이버 등록 => 한번수행 => 생성자
	public MemberDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	// 2. 연결
	public void getConnection() {
		try {
			conn = DriverManager.getConnection(URL,"hr","happy");
			//conn hr/happy
		}catch(Exception ex)	{}
	}
	// 3. 연결해제
	public void disConnection() {
		try {
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
			// exit
		}catch(Exception ex) {}
	}
	// 4. 기능처리
	public String isLogin(String id, String pwd) {
		String result = "";
		try {
				//연결
			getConnection();
			//오라클에 요청
			String sql = "SELECT COUNT(*) FROM memberInfo WHERE id=?"; //id가 총 몇개인가?
			ps = conn.prepareStatement(sql); //오라클로 전송
			//?에 값을 채운다.
			ps.setString(1, id);
			//실행요청
			ResultSet rs = ps.executeQuery();
			rs.next(); //실제 출력 위치에 커서를 이동한다.
			int count = rs.getInt(1); //숫자냐 문자냐 날짜냐에 따라 데이터 형을 맞춰줌(number,varchar,date)
			rs.close();

			if(count==0) {
				result="NOID";
			}else {
				// 요청
				sql = "SELECT * FROM memberInfo WHERE id=?";
				ps = conn.prepareStatement(sql); //전송
				ps.setString(1, id); //실행 전에 ?에 값을 채운다.
				rs = ps.executeQuery();
				rs.next();
				//값을 받는다.
				String mid = rs.getString(1);
				String mpwd = rs.getString(2);
				String name = rs.getString(3);
				String img = rs.getString(4);

				rs.close();

				if(mpwd.equals(pwd)) {
					//로그인
					result=mid+"|"+name+"|"+img;
				}else {
					//비밀번호가 틀리다
					result ="NOPWD";
				}

				/*		String name = "";
				 * 		String sex = "";
				 * 		String id = "";
				 * 		int age=10;
				 * 		sql = "INSERT INTO member VALUES('"+name+"','"+sex+"',
				 * 					'"+id+"',"+age+")"; //세팅된 값 Statement
				 * 		sql = "INSERT INTo member VALUE (?,?,?,?)"; 구문을 만들어 두고 값은 나중에 ?에 값을 채워 넣는다. //prepared Statement
				 */
			}

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		finally {
			//연결 해제
			disConnection();
		}
		return result;
	}
}
