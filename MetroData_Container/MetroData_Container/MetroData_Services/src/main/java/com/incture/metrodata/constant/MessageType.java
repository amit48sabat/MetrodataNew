package com.incture.metrodata.constant;

public enum MessageType {
  NOTIFICATION("Notification"),FEED("Feed"),INCIDENT("Incident");
	
  String value;
  private MessageType(String value){
	 this.value = value;
  }
  
  public String getValue(){
	  return this.value;
  }
}
