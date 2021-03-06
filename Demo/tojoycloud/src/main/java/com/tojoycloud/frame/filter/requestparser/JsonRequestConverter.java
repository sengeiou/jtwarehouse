package com.tojoycloud.frame.filter.requestparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tojoycloud.frame.common.ReportUtil;
import com.tojoycloud.frame.report.base.BaseReport;
import com.tojoycloud.frame.report.base.ResponseCode;

public class JsonRequestConverter extends AbstractHttpMessageConverter<BaseReport>
{
	public static final String CONTENTTYPE = "application";
	public static final String SUBTYPE = "x-json";

	public JsonRequestConverter()
	{
		super(new MediaType(CONTENTTYPE, SUBTYPE, Charset.forName("UTF-8")));
	}

	@Override
	protected BaseReport readInternal(Class<? extends BaseReport> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException
	{
		InputStream in = inputMessage.getBody();
		String strRequestReport = ReportUtil.getRequestStr(in);
		if (StringUtils.isBlank(strRequestReport))
		{
			logger.error("empty request");
			return null;
		}
		else
		{
			logger.info(strRequestReport);
			return JSON.parseObject(strRequestReport, clazz);
		}
	}

	@Override
	protected void writeInternal(BaseReport t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException
	{
		BaseReport responseReport = (BaseReport) t;
		String response = JSON.toJSONString(responseReport, SerializerFeature.DisableCircularReferenceDetect);
		logger.info(response);
		byte[] bResponseToClient = new byte[0];
		if (StringUtils.isBlank(response))
		{
			responseReport = new BaseReport(ResponseCode.Error);
			responseReport.setResponseTips("Null response string");
		}
		byte[] bufCompressed = ReportUtil.compressData(response.getBytes("utf-8")); // ??????
		bResponseToClient = ReportUtil.ecrypt(bufCompressed); // ??????
		OutputStream out = outputMessage.getBody();
		if (bResponseToClient != null)
			out.write(bResponseToClient);
		out.flush();
		out.close(); // ???????????????
	}

	@Override
	protected boolean supports(Class<?> clazz)
	{
		return true;
	}

}
