package com.xiaohei.java.lib.http.request;


import com.xiaohei.java.lib.http.util.Method;

import java.io.OutputStream;


public class FlowRequest extends Request {
	private String mFlow;
	{
		setMethod(Method.POST);
	}

	public FlowRequest(String flow) {
		// TODO Auto-generated constructor stub
		mFlow = flow;

	}

	@Override
	public void report(OutputStream os) {
		// TODO Auto-generated method stub
		super.report(os);
		if (mFlow == null)
			return;
		try {
			os.write(mFlow.getBytes());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
