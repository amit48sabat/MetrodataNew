package com.incture.metrodata.service;

import com.google.maps.model.LatLng;
import com.incture.metrodata.dto.ResponseDto;

public interface OptimizedRouteServiceLocal {
ResponseDto optimizedRoute(String tripId, LatLng userLatLong);
}
