package com.github.curl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Date: 05/02/22
 * Time: 5:02 pm
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
class CurlExecution {
    private final CurlReqModel req;

    public CurlExecution(CurlReqModel req) {
        this.req = req;
    }

    CurlResModel execute() throws IOException {
        try {
            if (req.getHeaders() != null
                    && req.getHeaders().size() == 0
                    && req.getBasicAuth() == null
                    && req.getData() != null
                    && req.getData().getAscii() == null
                    && req.getData().getFiles() == null) {
                return simpleRequest();
            } else {
                return complexRequest();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private CurlResModel simpleRequest() throws IOException {
        return execution(req.getMethod(), req.getUrl(), Collections.emptyMap());
    }

    // TODO: 05/02/22 need to improve for complex command
    private CurlResModel complexRequest() throws IOException {
        return execution(req.getMethod(), req.getUrl(), req.getHeaders());
    }

    private CurlResModel execution(String method, String strUrl, Map<String, String> headers) throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.toUpperCase(Locale.ENGLISH));
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (headers != null) {
            headers.forEach(connection::setRequestProperty);
        }
        String response;
        try {
            response = readFromStream(connection.getInputStream());
            return new CurlResModel(connection.getResponseCode(), response, true);
        } catch (IOException ex) {
            if (connection.getErrorStream() == null) {
                return new CurlResModel(502, ex.getMessage(), false);
            } else {
                response = readFromStream(connection.getErrorStream());
                return new CurlResModel(connection.getResponseCode(), response, false);
            }
        }
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String response;
        while ((response = bufferedReader.readLine()) != null) {
            sb.append(response);
        }
        bufferedReader.close();
        return sb.toString();
    }
}
