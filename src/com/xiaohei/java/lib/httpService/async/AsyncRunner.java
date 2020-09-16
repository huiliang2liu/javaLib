package com.xiaohei.java.lib.httpService.async;


import com.xiaohei.java.lib.httpService.HttpService;

public interface AsyncRunner {
    void closeAll();

    void closed(HttpService.ClientHandler clientHandler);

    void exec(HttpService.ClientHandler code);
}
