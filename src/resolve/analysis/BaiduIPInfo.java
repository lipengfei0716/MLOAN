package resolve.analysis;

	/*{
	    "errNum": 0,
	    "errMsg": "success",
	    "retData": {
	        "ip": "117.89.35.58", //IP地址
	        "country": "中国", //国家 
	        "province": "江苏", //省份 国外的默认值为none
	        "city": "南京", //城市  国外的默认值为none
	        "district": "鼓楼",// 地区 国外的默认值为none
	        "carrier": "中国电信" //运营商  特殊IP显示为未知
	    }
	}*/
public class BaiduIPInfo {
	
	private int errNum;
	private String errMsg;
	
	private RetData retData;
	
	
	
	public String getIP() {
		return retData.ip;
	}
	
	public String getCountry() {
		return retData.country;
	}
	
	public String getProvince() {
		return retData.province;
	}
	
	public String getCity() {
		return retData.city;
	}
	
	public String getDistrict() {
		return retData.district;
	}
	
	public String getCarrier() {
		return retData.carrier;
	}
	
	public String getErrMsg() {
		return errMsg;
	}
	public int getErrNum() {
		return errNum;
	}

}


class RetData {
	String ip;
	String country;
	String province;
	String city;
	String district;
	String carrier;
}
