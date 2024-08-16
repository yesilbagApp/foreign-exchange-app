package com.openpayd.foreign.exchange.service.impl;

import com.openpayd.foreign.exchange.service.HttpClientWrapper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public class HttpClientWrapperImpl implements HttpClientWrapper {
    private final HttpClient httpClient;

    public HttpClientWrapperImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}

