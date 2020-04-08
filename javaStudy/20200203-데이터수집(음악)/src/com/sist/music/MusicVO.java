package com.sist.music;
/*
 * DESC  mno NUMBER,
    rank NUMBER,
    title VARCHAR2(500),
    singer VARCHAR2(200),
    album VARCHAR2(300),
    poster VARCHAR2(360), -- HTTP占쏙옙 260占싱삼옙占쏙옙 占십곤옙占쏙옙占쏙옙 占십는댐옙
    idcrement NUMBER,
    state CHAR(4),  -- 占쏙옙占쏙옙 占싹곤옙 
    key VARCHAR2(100)
 */
public class MusicVO {
		
		private int mno;  //占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
		private int rank;
		private String title;
		private String singer;
		private String album;
		private String poster;
		private int idcrement;
		private String state;
		private String key;     // 占쏙옙 占쌕뀐옙占� 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
		public int getMno() {
			return mno;
		}
		public void setMno(int mno) {
			this.mno = mno;
		}
		public int getRank() {
			return rank;
		}
		public void setRank(int rank) {
			this.rank = rank;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getSinger() {
			return singer;
		}
		public void setSinger(String singer) {
			this.singer = singer;
		}
		public String getAlbum() {
			return album;
		}
		public void setAlbum(String album) {
			this.album = album;
		}
		public String getPoster() {
			return poster;
		}
		public void setPoster(String poster) {
			this.poster = poster;
		}
		public int getIdcrement() {
			return idcrement;
		}
		public void setIdcrement(int idcrement) {
			this.idcrement = idcrement;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		
}
