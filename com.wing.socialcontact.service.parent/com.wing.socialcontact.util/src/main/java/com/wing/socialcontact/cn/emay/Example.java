package com.wing.socialcontact.cn.emay;

import java.util.HashMap;
import java.util.Map;

import com.wing.socialcontact.cn.emay.eucp.inter.framework.dto.CustomSmsIdAndMobile;
import com.wing.socialcontact.cn.emay.eucp.inter.framework.dto.CustomSmsIdAndMobileAndContent;
import com.wing.socialcontact.cn.emay.eucp.inter.framework.dto.PersonalityParams;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.request.BalanceRequest;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.request.MoRequest;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.request.ReportRequest;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.request.SmsBatchOnlyRequest;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.request.SmsBatchRequest;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.request.SmsPersonalityAllRequest;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.request.SmsPersonalityRequest;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.request.SmsSingleRequest;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.response.BalanceResponse;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.response.MoResponse;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.response.ReportResponse;
import com.wing.socialcontact.cn.emay.eucp.inter.http.v1.dto.response.SmsResponse;
import com.wing.socialcontact.cn.emay.util.AES;
import com.wing.socialcontact.cn.emay.util.GZIPUtils;
import com.wing.socialcontact.cn.emay.util.JsonHelper;
import com.wing.socialcontact.cn.emay.util.http.EmayHttpClient;
import com.wing.socialcontact.cn.emay.util.http.EmayHttpRequestBytes;
import com.wing.socialcontact.cn.emay.util.http.EmayHttpResponseBytes;
import com.wing.socialcontact.cn.emay.util.http.EmayHttpResponseBytesPraser;
import com.wing.socialcontact.cn.emay.util.http.EmayHttpResultCode;
import com.wing.socialcontact.util.ApplicationPath;

public class Example {
	
	private static final String EMAY_APPID = ApplicationPath.getParameter("EMAY_APPID");
	private static final String EMAY_HOST = ApplicationPath.getParameter("EMAY_HOST");
	private static final String EMAY_APPSECRET = ApplicationPath.getParameter("EMAY_APPSECRET");

	public static void main(String[] args) {
		// appId
		String appId = EMAY_APPID;//???????????????????????????????????? ??????
		// ??????
		String secretKey = EMAY_APPSECRET;//???????????????????????????????????? ??????
		// ????????????
		String host = EMAY_HOST;//?????????????????????
		// ????????????
		String algorithm = "AES/ECB/PKCS5Padding";
		// ??????
		String encode = "UTF-8";
		// ????????????
		boolean isGizp = false;
		
		// ????????????
		getBalance(appId,secretKey,host,algorithm,isGizp,encode);
		// ??????????????????
		getReport(appId,secretKey,host,algorithm,isGizp,encode);
		// ????????????
		getMo(appId,secretKey,host,algorithm,isGizp,encode);
		// ??????????????????
		setSingleSms(appId,secretKey,host,algorithm,"?????????????????????????????????????????????", null, null, "13760262786",isGizp,encode);
		// ??????????????????[????????????SMSID]
		setBatchSms(appId,secretKey,host,algorithm,"?????????????????????????????????????????????", null, new CustomSmsIdAndMobile[]{new CustomSmsIdAndMobile("1", "18001000000"),new CustomSmsIdAndMobile("2", "18001000001")},isGizp,encode);
		// ??????????????????[????????????SMSID]
		setBatchOnlySms(appId,secretKey,host,algorithm,"?????????????????????????????????????????????", null, new String[]{"18001000000","18001000001"},isGizp,encode);
		// ??????????????????
		setPersonalitySms(appId,secretKey,host,algorithm, null, new CustomSmsIdAndMobileAndContent[]{new CustomSmsIdAndMobileAndContent("1", "18001000000","?????????????????????????????????????????????"),new CustomSmsIdAndMobileAndContent("2", "18001000001","????????????????????????????????????????????????")},isGizp,encode);
		// ?????????????????????
		setPersonalityAllSms(appId, secretKey, host, algorithm, new PersonalityParams[]{new PersonalityParams("101", "18001000000", "????????????", "01", null),new PersonalityParams("102", "18001000001", "????????????1", "02", "2017-12-01 11:00:00")}, isGizp, encode);
	}
	
	/**
	 * ????????????
	 * @param isGzip ????????????
	 */
	private static void getBalance(String appId,String secretKey,String host,String algorithm,boolean isGzip,String encode) {
		System.out.println("=============begin getBalance==================");
		BalanceRequest pamars = new BalanceRequest();
		ResultModel result = request(appId,secretKey,algorithm,pamars, "http://" + host + "/inter/getBalance",isGzip,encode);
		System.out.println("result code :" + result.getCode());
		if("SUCCESS".equals(result.getCode())){
			BalanceResponse response = JsonHelper.fromJson(BalanceResponse.class, result.getResult());
			if (response != null) {
				System.out.println("result data : " + response.getBalance());
			}
		}
		System.out.println("=============end getBalance==================");
	}

	/**
	 * ??????????????????
	 * @param isGzip ????????????
	 */
	private static void getReport(String appId,String secretKey,String host,String algorithm,boolean isGzip,String encode) {
		System.out.println("=============begin getReport==================");
		ReportRequest pamars = new ReportRequest();
		ResultModel result = request(appId,secretKey,algorithm,pamars, "http://" + host + "/inter/getReport",isGzip,encode);
		System.out.println("result code :" + result.getCode());
		if("SUCCESS".equals(result.getCode())){
			ReportResponse[] response = JsonHelper.fromJson(ReportResponse[].class, result.getResult());
			if (response != null) {
				for (ReportResponse d : response) {
					System.out.println("result data : " + d.getExtendedCode() + "," + d.getMobile() + "," + d.getCustomSmsId() + "," + d.getSmsId() + "," + d.getState() + "," + d.getDesc()
							+ "," + d.getSubmitTime() + "," + d.getReceiveTime());
				}
			}
		}
		System.out.println("=============end getReport==================");
	}
	
	/**
	 * ????????????
	 * @param isGzip ????????????
	 */
	private static void getMo(String appId,String secretKey,String host,String algorithm,boolean isGzip,String encode) {
		System.out.println("=============begin getMo==================");
		MoRequest pamars = new MoRequest();
		ResultModel result = request(appId,secretKey,algorithm,pamars, "http://" + host + "/inter/getMo",isGzip,encode);
		System.out.println("result code :" + result.getCode());
		if("SUCCESS".equals(result.getCode())){
			MoResponse[] response = JsonHelper.fromJson(MoResponse[].class, result.getResult());
			if (response != null) {
				for (MoResponse d : response) {
					System.out.println("result data:" + d.getContent()+ "," + d.getExtendedCode() + "," + d.getMobile() + "," + d.getMoTime());
				}
			}
		}
		System.out.println("=============end getMo==================");
	}
	
	/**
	 * ??????????????????
	 * @param isGzip ????????????
	 */
	private static void setSingleSms(String appId,String secretKey,String host,String algorithm,String content, String customSmsId, String extendCode, String mobile,boolean isGzip,String encode) {
		System.out.println("=============begin setSingleSms==================");
		SmsSingleRequest pamars = new SmsSingleRequest();
		pamars.setContent(content);
		pamars.setCustomSmsId(customSmsId);
		pamars.setExtendedCode(extendCode);
		pamars.setMobile(mobile);
		ResultModel result = request(appId,secretKey,algorithm,pamars, "http://" + host + "/inter/sendSingleSMS",isGzip,encode);
		System.out.println("result code :" + result.getCode());
		if("SUCCESS".equals(result.getCode())){
			SmsResponse response = JsonHelper.fromJson(SmsResponse.class, result.getResult());
			if (response != null) {
				System.out.println("data : " + response.getMobile() + "," + response.getSmsId() + "," + response.getCustomSmsId());
			}
		}
		System.out.println("=============end setSingleSms==================");
	}
	
	/**
	 * ??????????????????
	 * @param isGzip ????????????
	 */
	private static void setBatchOnlySms(String appId,String secretKey,String host,String algorithm,String content, String extendCode, String[] mobiles,boolean isGzip,String encode) {
		System.out.println("=============begin setBatchOnlySms==================");
		SmsBatchOnlyRequest pamars = new SmsBatchOnlyRequest();
		pamars.setMobiles(mobiles);
		pamars.setExtendedCode(extendCode);
		pamars.setContent(content);
		ResultModel result = request(appId,secretKey,algorithm,pamars, "http://" + host + "/inter/sendBatchOnlySMS",isGzip,encode);
		System.out.println("result code :" + result.getCode());
		if("SUCCESS".equals(result.getCode())){
			SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
			if (response != null) {
				for (SmsResponse d : response) {
					System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
				}
			}
		}
		System.out.println("=============end setBatchOnlySms==================");
	}
	
	/**
	 * ??????????????????
	 * @param isGzip ????????????
	 */
	private static void setBatchSms(String appId,String secretKey,String host,String algorithm,String content, String extendCode, CustomSmsIdAndMobile[] customSmsIdAndMobiles,boolean isGzip,String encode) {
		System.out.println("=============begin setBatchSms==================");
		SmsBatchRequest pamars = new SmsBatchRequest();
		pamars.setSmses(customSmsIdAndMobiles);
		pamars.setExtendedCode(extendCode);
		pamars.setContent(content);
		ResultModel result = request(appId,secretKey,algorithm,pamars, "http://" + host + "/inter/sendBatchSMS",isGzip,encode);
		System.out.println("result code :" + result.getCode());
		if("SUCCESS".equals(result.getCode())){
			SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
			if (response != null) {
				for (SmsResponse d : response) {
					System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
				}
			}
		}
		System.out.println("=============end setBatchSms==================");
	}
	
	/**
	 * ??????????????????
	 * @param isGzip ????????????
	 */
	private static void setPersonalitySms(String appId,String secretKey,String host,String algorithm,String extendCode, CustomSmsIdAndMobileAndContent[] customSmsIdAndMobileAndContents,boolean isGzip,String encode) {
		System.out.println("=============begin setPersonalitySms==================");
		SmsPersonalityRequest pamars = new SmsPersonalityRequest();
		pamars.setSmses(customSmsIdAndMobileAndContents);
		pamars.setExtendedCode(extendCode);
		ResultModel result = request(appId,secretKey,algorithm,pamars, "http://" + host + "/inter/sendPersonalitySMS",isGzip ,encode);
		System.out.println("result code :" + result.getCode());
		if("SUCCESS".equals(result.getCode())){
			SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
			if (response != null) {
				for (SmsResponse d : response) {
					System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
				}
			}
		}
		System.out.println("=============end setPersonalitySms==================");
	}
	
	/**
	 * ??????????????????
	 * @param isGzip ????????????
	 */
	private static void setPersonalityAllSms(String appId,String secretKey,String host,String algorithm,PersonalityParams[] customSmsIdAndMobileAndContents,boolean isGzip,String encode) {
		System.out.println("=============begin setPersonalityAllSms==================");
		SmsPersonalityAllRequest pamars = new SmsPersonalityAllRequest();
		pamars.setSmses(customSmsIdAndMobileAndContents);
		ResultModel result = request(appId,secretKey,algorithm,pamars, "http://" + host + "/inter/sendPersonalityAllSMS",isGzip ,encode);
		System.out.println("result code :" + result.getCode());
		if("SUCCESS".equals(result.getCode())){
			SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
			if (response != null) {
				for (SmsResponse d : response) {
					System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
				}
			}
		}
		System.out.println("=============end setPersonalityAllSms==================");
	}
	
	/**
	 * ??????????????????
	 */
	public static ResultModel request(String appId,String secretKey,String algorithm,Object content, String url,final boolean isGzip,String encode) {
		Map<String, String> headers = new HashMap<String, String>();
		EmayHttpRequestBytes request = null;
		try {
			headers.put("appId", appId);
			headers.put("encode", encode);
			String requestJson = JsonHelper.toJsonString(content);
			System.out.println("result json: " + requestJson);
			byte[] bytes = requestJson.getBytes(encode);
			System.out.println("request data size : " + bytes.length);
			if (isGzip) {
				headers.put("gzip", "on");
				bytes = GZIPUtils.compress(bytes);
				System.out.println("request data size [com]: " + bytes.length);
			}
			byte[] parambytes = AES.encrypt(bytes, secretKey.getBytes(), algorithm);
			System.out.println("request data size [en] : " + parambytes.length);
			request = new EmayHttpRequestBytes(url, encode, "POST", headers, null, parambytes);
		} catch (Exception e) {
			System.out.println("????????????");
			e.printStackTrace();
		}
		EmayHttpClient client = new EmayHttpClient();
		String code = null;
		String result = null;
		try {
			EmayHttpResponseBytes res = client.service(request, new EmayHttpResponseBytesPraser());
			if(res == null){
				System.out.println("??????????????????");
				return new ResultModel(code, result);
			}
			if (res.getResultCode().equals(EmayHttpResultCode.SUCCESS)) {
				if (res.getHttpCode() == 200) {
					code = res.getHeaders().get("result");
					if (code.equals("SUCCESS")) {
						byte[] data = res.getResultBytes();
						System.out.println("response data size [en and com] : " + data.length);
						data = AES.decrypt(data, secretKey.getBytes(), algorithm);
						if (isGzip) {
							data = GZIPUtils.decompress(data);
						}
						System.out.println("response data size : " + data.length);
						result = new String(data, encode);
						System.out.println("response json: " + result);
					}
				} else {
					System.out.println("??????????????????,?????????:" + res.getHttpCode());
				}
			} else {
				System.out.println("????????????????????????:" + res.getResultCode().getCode());
			}
		} catch (Exception e) {
			System.out.println("????????????");
			e.printStackTrace();
		}
		ResultModel re = new ResultModel(code, result);
		return re;
	}

}

