package com.sist.music;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


//DAO,VO�� �����ϰ� ���� �����͸� ������ �غ� �ϴ� Ŭ����
public class MusicManager {
	public ArrayList <MusicVO> musicListData()
	{
		ArrayList<MusicVO> list=new ArrayList<MusicVO>();
		try
		{
		/*
		 * private String title;
		private String singer;
		private String album;
		private String poster;
		private int idcrement;
		private String state;
		private String key; 	
		 */
		
			// ���� ���ο� ���� �������� �ϴ� �ڵ�
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			System.out.println(sdf.format(date));        // ��¥�� �� ������ �ִ��� Ȯ���غ���
			int rank=1 ;      // ��ũ ~ ���¸� �и��Ϸ� ���
			// �ϳ� �ܾ�� �� ���� �ϳ��� �����غ��� ( ����Ŭ����ӵ��� �ʹ� ���� Ŀ�ؼǿ� ������ �����!)
			MusicDAO dao=new MusicDAO();
			for(int i=1;i<=4;i++)
			{ 
				try {
					Document doc=Jsoup.connect("https://www.genie.co.kr/chart/top200?ditc=D&ymd="+sdf.format(date)+"&hh=12&rtm=Y&pg="+i).get();  // �� �ּҿ� ���� ���� �����Ͷ�
					//System.out.println(doc.toString());  // HTML �ҽ� �ҷ�����
					/*
					 * <tr class="list">
					 * </tr>  -- 0
					 * <tr class="list">
					 * </tr>  -- 1
					 */
					Elements title=doc.select("tr.list td.info a.title");
					Elements singer=doc.select("tr.list td.info a.artist");
					Elements album=doc.select("tr.list td.info a.albumtitle");
					Elements poster=doc.select("tr.list td a.cover img");
					Elements temp=doc.select("tr.list span.rank");
					
					for(int j=0; j<title.size();j++)
					{
						System.out.println("����: "+rank);
						System.out.println("����: "+title.get(j).text());
						System.out.println("����: "+singer.get(j).text());
						System.out.println("�ٹ�: "+album.get(j).text());
						System.out.println("�̹���: "+poster.get(j).attr("src"));
						//System.out.println("��Ÿ: "+temp.get(j).text());
						String s=temp.get(j).text();
						String idcrement="";
						String state=""; 
						/*rank-none>< -- ������
							rank-up>3<  -- 3�� �ö�
							rank-down>4< -- 4�� ������
						*/
	
						//20�ϰ�
						String ss=s.replaceAll("[^��-�R]", "");  //�ѱ����Խ��� �����ϰ� �������� �ٲ�� = ���ڸ� ������
						if(ss.equals("���"))
						{
							idcrement=s.replaceAll("[^0-9]", "");  //���ڸ� �����ϰ� �������� �ٲ��
							state=ss;
						}else if(ss.equals("�ϰ�"))
						{
							idcrement=s.replaceAll("[^0-9]", "");
							state=ss;
						}else
						{
							idcrement="0";
							state="����";
						}
						System.out.println("����: "+state);
						System.out.println("���氪: "+idcrement);
						System.out.println("������ Ű: "+youtubeKeyData(title.get(j).text()));
						System.out.println("==========================");
						MusicVO vo=new MusicVO();
						vo.setRank(rank);
						vo.setTitle(title.get(j).text());
						vo.setSinger(singer.get(j).text());
						vo.setAlbum(album.get(j).text());
						vo.setPoster(poster.get(j).attr("src"));
						vo.setState(state);
						vo.setIdcrement(Integer.parseInt(idcrement));
						vo.setKey(youtubeKeyData(title.get(j).text()));
						
						dao.musicInsert(vo);
						list.add(vo);
						rank++;   // 50�� 4���� ���ƾ� ��
					}
			//		Elements idcrement=doc.select("tr.list td.info a.title");
			//		Elements state=doc.select("tr.list td.info a.title");
				}catch( Exception ex) {}
			}
			
		}catch(Exception ex) {}
		return list;
	}
	//YouTUbe���� �� �����´�
	public String youtubeKeyData(String title)
	{
		String key="";
		try 
		{
			//  / watch?v=wpUiN5hBnyc
			Document doc=Jsoup.connect("https://www.youtube.com/results?search_query="+title).get();
			Pattern p=Pattern.compile("/watch\\?v=[^��-�R]+")  ;  // ��ȣ�� ���ö��� �������� 2�� �ٿ��� �ȴ�  $ ^ . ?   |  
			Matcher m=p.matcher(doc.toString());
			//ã��
			while(m.find())
			{
				String temp=m.group();
				key=temp.substring(temp.indexOf("=")+1,temp.indexOf("\""));
				break;
				//System.out.println(temp);
			}
		}catch (Exception ex){}
		return key;
	}
	public static void main(String args[])
	{
		MusicManager m=new MusicManager();
		MusicDAO dao=new MusicDAO();
		ArrayList<MusicVO> list =m.musicListData();
		/*
		 * for(MusicVO vo:list) { dao.musicInsert(vo); }
		 */
		System.out.println("Oracle save  data End....");
	}
}
