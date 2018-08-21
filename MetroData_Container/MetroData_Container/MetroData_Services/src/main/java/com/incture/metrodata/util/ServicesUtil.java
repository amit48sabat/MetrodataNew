package com.incture.metrodata.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.incture.metrodata.dto.ResponseDto;
import com.incture.metrodata.exceptions.InvalidInputFault;

public class ServicesUtil {

	public static final String NOT_APPLICABLE = "N/A";
	public static final String SPECIAL_CHAR = "∅";
	public static SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	public static boolean isEmpty(Object[] objs) {
		if (objs == null || objs.length == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Object o) {
		if (o == null || (o instanceof Long && (Long)o == 0L)) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Collection<?> o) {
		if (o == null || o.isEmpty()) {
			return true;
		}
		return false;
	}

	public static int nullHandler(Collection<?> o) {
		if (o == null || o.isEmpty()) {
			return 0;
		} else
			return o.size();
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(StringBuffer sb) {
		if (sb == null || sb.length() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(StringBuilder sb) {
		if (sb == null || sb.length() == 0) {
			return true;
		}
		return false;
	}

	public static String getCSV(Object... objs) {
		if (!isEmpty(objs)) {
			if (objs[0] instanceof Collection<?>) {
				return getCSVArr(((Collection<?>) objs[0]).toArray());
			} else {
				return getCSVArr(objs);
			}

		} else {
			return "";
		}
	}

	private static String getCSVArr(Object[] objs) {
		if (!isEmpty(objs)) {
			StringBuffer sb = new StringBuffer();
			for (Object obj : objs) {
				sb.append(',');
				if (obj instanceof Field) {
					sb.append(extractFieldName((Field) obj));
				} else {
					sb.append(extractStr(obj));
				}
			}
			sb.deleteCharAt(0);
			return sb.toString();
		} else {
			return "";
		}
	}

	public static String extractStr(Object o) {
		return o == null ? "" : o.toString();
	}

	public static String extractFieldName(Field o) {
		return o == null ? "" : o.getName();
	}

	public static String buildNoRecordMessage(String queryName, Object... parameters) {
		StringBuffer sb = new StringBuffer("No Record found for query: ");
		sb.append(queryName);
		if (!isEmpty(parameters)) {
			sb.append(" for params:");
			sb.append(getCSV(parameters));
		}
		return sb.toString();
	}

	public static String appendLeadingChars(String input, char c, int finalSize) throws InvalidInputFault {
		StringBuffer strBuffer = new StringBuffer();
		if (input == null) {
			return null;
		}
		int paddingSize = finalSize - input.length();
		if (paddingSize < 0) {
			throw new InvalidInputFault(
					getCSV("Value passed is greater than count:" + input + " count is: " + finalSize));
		}
		while (paddingSize-- > 0) {
			strBuffer.append(c);
		}
		strBuffer.append(input);

		return strBuffer.toString();
	}

	public static String appendTrailingChars(String input, char c, int finalSize) throws InvalidInputFault {
		StringBuffer strBuffer = new StringBuffer();
		if (input == null) {
			input = "";
		}
		int paddingSize = finalSize - input.length();
		if (paddingSize < 0) {
			throw new InvalidInputFault(
					getCSV("Value passed is greater than count:" + input + " count is: " + finalSize));
		}
		strBuffer.append(input);
		while (paddingSize-- > 0) {
			strBuffer.append(c);
		}
		String result = strBuffer.toString();
		return result;
	}

	public static void enforceMandatory(String field, Object value) throws InvalidInputFault {
		if (ServicesUtil.isEmpty(value)) {
			String message = "Field=" + field + " can't be empty";
			throw new InvalidInputFault(message, null);
		}
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static BigDecimal nullHandler(BigDecimal b) {
		if (ServicesUtil.isEmpty(b)) {
			return BigDecimal.ZERO;
		}
		return b;
	}

	public static Boolean nullHandler(Boolean b) {
		if (ServicesUtil.isEmpty(b)) {
			return b = false;
		} else
			return b;
	}

	public static String getExceptionTrace(Exception ex) {
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}

	public static boolean isTrue(Boolean b) {
		if (!isEmpty(b) && b == true) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isFalse(Boolean b) {
		if (isEmpty(b) || b == false) {
			return true;
		} else {
			return false;
		}
	}

	public static BigDecimal min(BigDecimal a, BigDecimal b) {
		if (a.compareTo(b) >= 0) {
			return b;
		} else {
			return a;
		}
	}

	public static BigDecimal max(BigDecimal a, BigDecimal b) {
		if (a.compareTo(b) < 0) {
			return b;
		} else {
			return a;
		}
	}

	public static String formatDate(Date d, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}

	public static String formatDate(Date d) {
		return DEFAULT_DATE_FORMAT.format(d);
	}

	// removes all leading zeros in a string
	public static String removeLeadingZeros(String s) {
		s = s.replaceFirst("^0*", "");
		return s;
	}

	public static String replaceLast(String string, String substring, String replacement) {
		int index = string.lastIndexOf(substring);
		if (index == -1)
			return string;
		return string.substring(0, index) + replacement + string.substring(index + substring.length());
	}

	public static Date getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public static String timeConvert(long time) {
		long days = time / 24 / 60;
		long hours = time / 60 % 24; // since both are ints, you get an int
		long minutes = time % 60;
		return String.format("%02d : %02d : %02d", days, hours, minutes);
		// return time/24/60 + " : " + time/60%24 + " : " + time%60;
	}

	public static BigDecimal catchNull(BigDecimal value) {
		if (!ServicesUtil.isEmpty(value))
			return value;
		else
			return BigDecimal.ZERO;
	}

	public static BigDecimal catchNull(String value) {
		if (!ServicesUtil.isEmpty(value))
			return new BigDecimal(value);
		else
			return BigDecimal.ZERO;
	}
  
	
	public static Map<String, Double> getLatAndLong(String address, GeoApiContext context) throws Exception{
		/*GeoApiContext context = new GeoApiContext();
		context.setApiKey("AIzaSyB9tRmnyuWILfaL_aiP_iA5P6ltMRyVnxM")*/;
			GeocodingResult[] results =  GeocodingApi.geocode(context, address).await();
			Map<String, Double>  map = new HashMap<>();
		   try{
				map.put("lat", results[0].geometry.location.lat);
				map.put("lng", results[0].geometry.location.lng);
		   }catch (Exception e) {
			//throw new InvalidInputFault("Invalid Address");
		}
			//System.out.println(gson.toJson(results[0].geometry.location.lat));
	return map;		
	}
	
	public static ResponseDto getUnauthorizedResponseDto(){
		ResponseDto res = new ResponseDto();
		res.setCode(HttpStatus.SC_UNAUTHORIZED);
		res.setMessage("Unauthorzed request");
		res.setStatus(false);
		return res;
	}
	
	public static void setPagination(String firstResult, String maxResult){
		
		if (!ServicesUtil.isEmpty(firstResult) && !ServicesUtil.isEmpty(maxResult)) {
			PaginationUtil.FIRST_RESULT = Integer.parseInt(firstResult);
			PaginationUtil.MAX_RESULT = Integer.parseInt(maxResult);
		}else{
			PaginationUtil.FIRST_RESULT = 0;
			PaginationUtil.MAX_RESULT = 0;
		}
	}
	
	public static Date convertTime(long time){
	   return new Date(time);
	}
	
}