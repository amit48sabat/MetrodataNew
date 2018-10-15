package com.incture.metrodata.service;

import java.util.List;

import com.google.maps.model.LatLng;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.ResponseDto;

public interface OptimizedRouteServiceLocal {
   ResponseDto optimizedRoute(List<DeliveryHeaderDTO> dtos, LatLng userLatLong);
}
