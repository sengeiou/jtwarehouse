package com.tojoy.business.common.report;

public class ErrorResponseReport extends ResponseReport
{

	private static final long serialVersionUID = 1L;

	public ErrorResponseReport()
	{
		super();
	}

	public ErrorResponseReport(Byte responseCode)
	{
		super(responseCode);
	}
}
