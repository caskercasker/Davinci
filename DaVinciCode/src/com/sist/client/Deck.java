package com.sist.client;

public class Deck {
	public static int size = 24; //더미 배열의 총 사이
	char black = 'B';
	char white = 'W';
	boolean visible; // 덱이 보여지는지 안보여지는지 여부
	int[] number = new int[size];  //24개의 숫자가 랜덤으로 들어가 있는 배열 
	
	
	public Deck() {
		int su[] = new int[size];
		boolean bCheck = false;
		for (int i=0; i<size; i++) {
			bCheck = true;
			while(bCheck) {
				bCheck = false;
				int rand = (int)(Math.random()*24);
				for (int j=0; j<i; j++) {
					if(su[j] == rand) {
						bCheck =true;
						break;
					}
				}
				su[i]=rand;
			}
		}
	}
}

