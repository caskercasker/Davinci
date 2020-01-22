package com.sist.server;
import java.util.Vector;

public class GameRoom {
	String roomName;
	int current;
	int maxCount;
	Vector<Server.Client> gamerVc = new Vector<Server.Client>();
	public GameRoom(String roomName) {
		this.roomName = roomName;
		this.current = current;
		this.maxCount = maxCount;
	}


}