package com.xiaohei.java.lib.http.task;


import com.xiaohei.java.lib.http.request.Request;
import com.xiaohei.java.lib.http.response.Response;


public abstract class ATask {
	protected Request mRequest;

	public void setRequest(Request mRequest) {
		this.mRequest = mRequest;
	}

	public abstract Response connection();
}
