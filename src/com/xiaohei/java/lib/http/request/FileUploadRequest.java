package com.xiaohei.java.lib.http.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class FileUploadRequest extends UploadRequest {
	private File mFile;

	public FileUploadRequest(File file) {
		// TODO Auto-generated constructor stub
		mFile = file;
	}

	public FileUploadRequest(String file) {
		// TODO Auto-generated constructor stub
		this(new File(file));
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		if (mFile == null)
			return null;
		try {
			return new FileInputStream(mFile);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
