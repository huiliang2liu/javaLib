package com.xiaohei.java.lib.http.down;

import com.xiaohei.java.lib.http.response.Response;



 interface Listener {
     void success(Response response, DownEntity entity);
}
