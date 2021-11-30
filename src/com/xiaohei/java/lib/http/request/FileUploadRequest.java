package com.xiaohei.java.lib.http.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class FileUploadRequest extends UploadRequest {
	private File mFile;

	public FileUploadRequest(File file) {
		mFile = file;
	}

	public FileUploadRequest(String file) {
		this(new File(file));
	}

	@Override
	public InputStream getInputStream() {
		if (mFile == null)
			return null;
		try {
			return new FileInputStream(mFile);
		} catch (Exception e) {
		}
		return null;
	}
}
