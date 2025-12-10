package pe.confianza.preevaluadorapi.util;


import java.util.List;

import org.apache.commons.lang.StringUtils;

public interface Util {
	
	public static String getDefaultString(Object objValue){
		try {
			if(objValue!=null && StringUtils.isNotBlank(objValue.toString()))
				return objValue.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		return null;
	}
	
	public static Integer getDefaultNullInt(Object objValue){
		try {
			if(objValue!=null && StringUtils.isNotBlank(objValue.toString()))
				return Integer.valueOf(objValue.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer getDefaultNullZeroInt(Object objValue){
		try {
			if(objValue!=null && StringUtils.isNotBlank(objValue.toString()) && 0!=Integer.valueOf(objValue.toString()))
				return Integer.valueOf(objValue.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Double getDefaultNullZeroDouble(Object objValue){
		try {
			if(objValue!=null && StringUtils.isNotBlank(objValue.toString()) && 0!=Double.valueOf(objValue.toString()))
				return Double.valueOf(objValue.toString());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer getValidValuesInt(Object objValue, int ...values) {
		try {
			if(values.length > Parameter.ZERO) {
				boolean isValid = false;
				for (int i = 0; i < values.length; i++) {
					if(values[i] == Integer.valueOf(objValue.toString())) {
						isValid = true;
					}
				}
				if(isValid) {
					return Integer.valueOf(objValue.toString());
				}else {
					return null;
				}
			}
			return Integer.valueOf(objValue.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Integer getValidValuesMinAndMaxInt(Object objValue, Double minValue, Double maxValue) {
		try {
			if(null != minValue && null != maxValue) {
				if(minValue <= Integer.valueOf(objValue.toString()) && Integer.valueOf(objValue.toString()) <= maxValue) {
					return Integer.valueOf(objValue.toString());
				}else {
					return null;
				}
			}else if(null != minValue) {
				if(minValue <= Integer.valueOf(objValue.toString())) {
					return Integer.valueOf(objValue.toString());
				}else {
					return null;
				}
			}else if(null != maxValue) {
				if(Integer.valueOf(objValue.toString()) <= maxValue) {
					return Integer.valueOf(objValue.toString());
				}else {
					return null;
				}
			}else {				
				return Integer.valueOf(objValue.toString());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getValidValuesString(Object objValue, int minTamanio, int maxTamanio, String ...values) {
		try {
			if(null != values) {
				boolean isValid = false;
				for (int i = 0; i < values.length; i++) {
					if(values[i].equals(objValue.toString().trim())) {
						isValid = true;
					}
				}
				if(isValid) {
					return objValue.toString();
				}else {
					return null;
				}
			}else if(minTamanio <= objValue.toString().trim().length() && objValue.toString().trim().length() <= maxTamanio) {
				return objValue.toString();
			}else {
				return null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}

