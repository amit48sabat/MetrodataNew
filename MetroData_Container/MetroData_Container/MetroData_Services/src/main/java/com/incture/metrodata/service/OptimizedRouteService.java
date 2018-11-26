package com.incture.metrodata.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.incture.metrodata.constant.DeliveryNoteStatus;
import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dao.TripDAO;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.dto.TripDetailsDTO;
import com.incture.metrodata.dto.opti.DistanceMatrixResponce;
import com.incture.metrodata.dto.opti.Element;
import com.incture.metrodata.dto.opti.Row;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.util.ServicesUtil;

@Service("optimizedRouteService")
@Transactional
public class OptimizedRouteService implements OptimizedRouteServiceLocal {

	@Autowired
	Environment environment;
	
	@Autowired
	TripDAO tripDao;
	
	/*@Value("${distance.matrix.api.key}")
	String distanceMatrixApiKey;//="AIzaSyC8YW1oFL25n8ki3XELODzxvhIWzjU-5EA";
*/
	private String baseUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=matrix";

	@Override
	public ResponseDto optimizedRoute(String tripId, LatLng userLatLong) {
		ResponseDto responseDto = new ResponseDto();
		try {
			
			// find the trip and get the list of delivery notes
			List<DeliveryHeaderDTO> dtos = getDeliveryNoteListOfTrip(tripId);
			
			if(ServicesUtil.isEmpty(dtos))
			   throw new Exception("No delivery note to remaining.");
			
			String distanceMatrixApiKey = environment.getProperty("distance.matrix.api.key"); 
			List<LatLng> originslist = new ArrayList<>();
			originslist.add(userLatLong);
			String userLatLngEncoded = new EncodedPolyline(originslist).getEncodedPath();
			String origin = "&origins=enc:"+userLatLngEncoded+":";
			String destinationsEncoded =  encodedLatLngList(dtos);
			String destinations = "&destinations=enc:"+destinationsEncoded+":";
			String key = "&key=" + distanceMatrixApiKey;
			URL obj = new URL(baseUrl+origin+destinations+key);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				
				if(ServicesUtil.isEmpty(in)){
					throw new Exception("Error: route can not be optimized at this time. Please try again.");
				}
				
				DistanceMatrixResponce res = new Gson().fromJson(response.toString(), DistanceMatrixResponce.class);
				
				attachTimeAndDistWeight(dtos,res);
				
				Collections.sort(dtos, (e1,e2)->{
				    return e1.getTimeWeight().compareTo(e2.getTimeWeight());
				});
				
				responseDto.setStatus(true);
				responseDto.setCode(200);
				responseDto.setData(dtos);
				responseDto.setMessage(Message.SUCCESS.getValue());
				
			} else {
				System.out.println();
				responseDto.setStatus(false);
				responseDto.setCode(417);
				responseDto.setMessage(Message.FAILED + " : " + "route optimization request not worked");
			}
		} catch (Exception e) {
			responseDto.setStatus(false);
			responseDto.setCode(500);
			responseDto.setMessage(Message.FAILED + " : " + e.toString());
			e.printStackTrace();
		}
		return responseDto;
	}

	private List<DeliveryHeaderDTO> getDeliveryNoteListOfTrip(String tripId) throws ExecutionFault {
		TripDetailsDTO tripDto = new TripDetailsDTO();
		tripDto.setTripId(tripId);
		tripDto = tripDao.findById(tripDto);
		
		List<DeliveryHeaderDTO> dnToProcess = new ArrayList<DeliveryHeaderDTO>();
		if(!ServicesUtil.isEmpty(tripDto) && !ServicesUtil.isEmpty(tripDto.getDeliveryHeader())){
			for(DeliveryHeaderDTO dto : tripDto.getDeliveryHeader()){
				
				if(dto.getStatus().equals(DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue()))
					dnToProcess.add(dto);
			}
		}
		
		return dnToProcess;
	}

	private void attachTimeAndDistWeight(List<DeliveryHeaderDTO> dtos, DistanceMatrixResponce res) {
		 List<Row> rowList = res.getRows();
		 List<Element> elementList = rowList.get(0).getElements();
		 
		 for(int i=0;i<dtos.size();i++){
			 DeliveryHeaderDTO dto = dtos.get(i);
			 Element element = elementList.get(i);
			 dto.setDistWeight(element.getDistance().getValue());
			 dto.setTimeWeight(element.getDuration().getValue());
		 }
	}

	private String encodedLatLngList(List<DeliveryHeaderDTO> dtos) {
		List<LatLng> list = new ArrayList<>();
		int i=1;
		for (DeliveryHeaderDTO dto : dtos) {
			
			if(i>23)
				break;
			i++;
			list.add(new LatLng(dto.getLatitude(), dto.getLongitude()));
		}
		return new EncodedPolyline(list).getEncodedPath();
	}

}
