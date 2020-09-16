package com.xiaohei.java.lib.http.request;

import java.io.InputStream;
import java.net.URL;

public class URLUploadRequest extends UploadRequest {
	private URL mUrl;

	public URLUploadRequest(URL url) {
		// TODO Auto-generated constructor stub
		mUrl = url;
	}

	public URLUploadRequest(String url) {
		// TODO Auto-generated constructor stub
		try {
			mUrl = new URL(url);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		if(mUrl==null)
			return null;
		try {
			return mUrl.openConnection().getInputStream();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
