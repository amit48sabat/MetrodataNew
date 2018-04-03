package com.incture.metrodata.constant;

public enum MessageType {
  NOTIFICATION("notification"),FEED("feed"),INCIDENT("incident");
	
  String value;
  private MessageType(String value){
	 this.value = value;
  }
  
  public String getValue(){
	  return this.value;
  }
}
