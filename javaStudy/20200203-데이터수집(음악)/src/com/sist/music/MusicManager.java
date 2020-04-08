package com.sist.music;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


//DAO,VO´Ù ±¸ÃàÇÏ°í ³ª¼­ µ¥ÀÌÅÍ¸¦ °¡Á®¿Ã ÁØºñ¸¦ ÇÏ´Â Å¬·¡½º
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
		
			// ¸ÅÀÏ »õ·Î¿î Á¤º¸ °¡Á®¿À°Ô ÇÏ´Â ÄÚµù
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			System.out.println(sdf.format(date));        // ³¯Â¥°¡ Àß ³ª¿À°í ÀÖ´ÂÁö È®ÀÎÇØº¸ÀÚ
			int rank=1 ;      // ·©Å© ~ »óÅÂ¸¦ ºĞ¸®ÇÏ·Á »ç¿ë
			// ÇÏ³ª ±Ü¾î¿Ã ¶§ ¸¶´Ù ÇÏ³ª¾¿ ÀúÀåÇØº¸ÀÚ ( ¿À¶óÅ¬ÀúÀå¼Óµµ°¡ ³Ê¹« »¡¶ó¼­ Ä¿³Ø¼Ç¿¡ ¹®Á¦°¡ »ı±äµí!)
			MusicDAO dao=new MusicDAO();
			for(int i=1;i<=4;i++)
			{ 
				try {
					Document doc=Jsoup.connect("https://www.genie.co.kr/chart/top200?ditc=D&ymd="+sdf.format(date)+"&hh=12&rtm=Y&pg="+i).get();  // ÀÌ ÁÖ¼Ò¿¡ µé¾î°¡¼­ °ªÀ» °¡Á®¿Í¶ó
					//System.out.println(doc.toString());  // HTML ¼Ò½º ºÒ·¯¿À±â
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
						System.out.println("¼øÀ§: "+rank);
						System.out.println("Á¦¸ñ: "+title.get(j).text());
						System.out.println("°¡¼ö: "+singer.get(j).text());
						System.out.println("¾Ù¹ü: "+album.get(j).text());
						System.out.println("ÀÌ¹ÌÁö: "+poster.get(j).attr("src"));
						//System.out.println("±âÅ¸: "+temp.get(j).text());
						String s=temp.get(j).text();
						String idcrement="";
						String state=""; 
						/*rank-none>< -- À¯ÁöµÊ
							rank-up>3<  -- 3µî ¿Ã¶ó°¨
							rank-down>4< -- 4µî ³»·Á°¨
						*/
	
						//20ÇÏ°­
						String ss=s.replaceAll("[^°¡-ÆR]", "");  //ÇÑ±ÛÁ¤±Ô½ÄÀ» Á¦¿ÜÇÏ°í °ø¹éÀ¸·Î ¹Ù²ã¶ó = ¼ıÀÚ¸¦ Áö¿ö¶ó
						if(ss.equals("»ó½Â"))
						{
							idcrement=s.replaceAll("[^0-9]", "");  //¼ıÀÚ¸¦ Á¦¿ÜÇÏ°í °ø¹éÀ¸·Î ¹Ù²ã¶ó
							state=ss;
						}else if(ss.equals("ÇÏ°­"))
						{
							idcrement=s.replaceAll("[^0-9]", "");
							state=ss;
						}else
						{
							idcrement="0";
							state="À¯Áö";
						}
						System.out.println("µîÆø: "+state);
						System.out.println("º¯°æ°ª: "+idcrement);
						System.out.println("µ¿¿µ»ó Å°: "+youtubeKeyData(title.get(j).text()));
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
						rank++;   // 50¾¿ 4¹ÙÄû µ¹¾Æ¾ß µÊ
					}
			//		Elements idcrement=doc.select("tr.list td.info a.title");
			//		Elements state=doc.select("tr.list td.info a.title");
				}catch( Exception ex) {}
			}
			
		}catch(Exception ex) {}
		return list;
	}
	//YouTUbe¿¡¼­ ¹» °¡Á®¿Â´Ù
	public String youtubeKeyData(String title)
	{
		String key="";
		try 
		{
			//  / watch?v=wpUiN5hBnyc
			Document doc=Jsoup.connect("https://www.youtube.com/results?search_query="+title).get();
			Pattern p=Pattern.compile("/watch\\?v=[^°¡-ÆR]+")  ;  // ±âÈ£°¡ ³ª¿Ã¶§´Â ¿ª½½·¯½¬ 2°³ ºÙ¿©¾ß µÈ´Ù  $ ^ . ?   |  
			Matcher m=p.matcher(doc.toString());
			//Ã£±â
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
