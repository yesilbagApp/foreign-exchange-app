package com.openpayd.foreign.exchange.service;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface HttpClientWrapper {
    HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException;
}

