package com.xiaohei.java.lib.http.listen;

import java.io.File;

public interface ProgressListen {
	void progress(float progress);
	void end(File file);
}
