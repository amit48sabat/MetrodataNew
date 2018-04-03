package com.incture.metrodata.constant;

public enum Message {
  SUCCESS("Success"),FAILED("Failed")
  ,NO_RECORD_FOUND("No record found");
  String value;
  private Message(String value){
	 this.value = value;
  }
  
  public String getValue(){
	  return this.value;
  }
}
