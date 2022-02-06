package com.github.curl;

import java.util.Map;

/**
 * Date: 05/02/22
 * Time: 11:19 am
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
class CurlReqModel {
    private String url;
    private String method;
    private Map<String, String> headers;
    private String dataType;
    private Data data;
    private BasicAuth basicAuth;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public BasicAuth getBasicAuth() {
        return basicAuth;
    }

    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }

    static class BasicAuth {
        private String user;
        private String password;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    static class Data {
        private Object files;
        private Object ascii;

        public Object getFiles() {
            return files;
        }

        public void setFiles(Object files) {
            this.files = files;
        }

        public Object getAscii() {
            return ascii;
        }

        public void setAscii(Object ascii) {
            this.ascii = ascii;
        }
    }
}
