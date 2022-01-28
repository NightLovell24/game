package com.archers.controller.net;

public interface Connection {

	String receiveData();
	
	void dispatchData(String data);
	
	
}
