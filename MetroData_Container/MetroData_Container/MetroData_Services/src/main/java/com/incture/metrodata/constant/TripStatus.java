package com.incture.metrodata.constant;

public enum TripStatus {
	
	TRIP_STATUS_CREATED("trip_created"),
	TRIP_STATUS_DRIVER_ASSIGNED("trip_driver_assigned"),
	TRIP_STATUS_STARTED("trip_started"),
	TRIP_STATUS_COMPLETED("trip_completed"),
	TRIP_STATUS_CANCELLED("trip_cancelled");
  String value;
  
  private TripStatus(String value){
	  this.value = value;
  }
  
  public String getValue(){
	 return this.value;
  }
}

