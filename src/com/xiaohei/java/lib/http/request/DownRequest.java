package com.xiaohei.java.lib.http.request;


import com.xiaohei.java.lib.http.util.Method;



public class DownRequest extends Request {
	private long start;
	private long end;
	{
		setAccept("image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
		method = Method.GET;
		setDown(true);
	}

	@Override
	public Request setPath(String path) {
		// TODO Auto-generated method stub
		setReferer(path);
		return super.setPath(path);
	}

	public DownRequest setStartAndEnd(long startPos, long endPos) {
		start = startPos;
		end = endPos;
		setRange("bytes=" + startPos + "-" + endPos);
		return this;
	}

	@Override
	public DownRequest setMethod(Method method) {
		// TODO Auto-generated method stub
		return this;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

}
