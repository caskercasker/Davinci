package com.sist.music;

import java.sql.*;
import java.util.*;

public class MusicDAO {

		private Connection conn;
		private PreparedStatement ps;
		private final String URL="jdbc:oracle:thin:@localhost:1521:XE";
		// SELECT, DELETE,INSERT,UPDATE => 占쌘바울옙占쏙옙 占쌘듸옙, 占쏙옙占쏙옙클占쏙옙占쏙옙 占쌘듸옙
		 // SELECT * FROM emp WHERE ename LIKE '%A%' (X)
		// SELECT * FROM emp WHERE ename LIKE '%'||A||'%' (O)
		
		//1. 占쏙옙占쏙옙譴占� 占쏙옙占�
		public MusicDAO()
		{
			try
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				// 占쏙옙占시뤄옙占쏙옙 => 클占쏙옙占쏙옙占쏙옙 占싱몌옙占쏙옙 占싻억옙占� 占쌨몌옙 占쌀댐옙  (new占쏙옙占쏙옙 占쏙옙占쏙옙) => Spring => 占쌨소듸옙 호占쏙옙 占쏙옙占쏙옙占쏙옙
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
			//2. 占쏙옙占쏙옙클 占쏙옙占쏙옙
			public void getConnection()
			{
				try
				{
					conn=DriverManager.getConnection(URL,"hr","happy");
				}catch (Exception ex) {}
			}
			public void disConnection()
			{
				try
				{
						if(ps!=null)ps.close();
						if(conn!=null)conn.close();
				}catch (Exception ex) {}
			}
			//3. 占쏙옙占시놂옙占�
			public void musicInsert(MusicVO vo) // row 占쏙옙 占쏙옙 占쏙옙 占쏙옙載ｏ옙占�
			{
				try
				{
				
					getConnection();
					String sql="INSERT INTO music_genie  VALUES("
											+"(SELECT NVL(MAX(mno)+1,1) FROM music_genie),?,?,?,?,?,?,?,?) "; // 占쌘듸옙占쏙옙占쏙옙, 占십깍옙화
					ps=conn.prepareStatement(sql); // 占싱몌옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占� 占쏙옙占쏙옙 占쏙옙占쌩울옙 占쏙옙占쏙옙 채占쏙옙募占� 占실뱄옙
					
					
				
					ps.setInt(1, vo.getRank());
					ps.setString(2,vo.getTitle());    // setString占싱띰옙占� 占쌨소듸옙 占쏙옙체占쏙옙 '' 占쏙옙 占쌕울옙占쌍니깍옙 ?占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙표占쏙옙 占쏙옙占쏙옙占쏙옙 占십는댐옙
					ps.setString(3,vo.getSinger());
					ps.setString(4,vo.getAlbum());
					ps.setString(5,vo.getPoster());
					ps.setInt(6,vo.getIdcrement());
					ps.setString(7,vo.getState());
					ps.setString(8,vo.getKey());
					
					//占쏙옙占쏙옙占쏙옙 占쏙옙占� = commit
					ps.executeUpdate();
					
		
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				finally
				{
					disConnection();
				}
			}
		}
		

