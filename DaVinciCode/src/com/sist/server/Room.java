package com.sist.server;
import java.util.Vector;

public class Room {
	String roomName,roomState,roomPwd;
	int current,maxcount;
	int ready;
	int gameTurn;

	Vector<Server.Client> userVc = new Vector<Server.Client>();
	public Room(String roomName, String roomState, String roomPwd, int maxcount) {
		this.roomName = roomName;
		this.roomState = roomState;
		this.roomPwd = roomPwd;
		this.maxcount = maxcount;
		current = 1;
		ready = 0;


	}


}