package io.github.curl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
class CurlReqModel {
    private String url;
    private String method;
    private Map<String, String> headers;
    private String dataType;
    private Data data;
    private BasicAuth basicAuth;
    private Long connectionTimeout;
    private Long readTimeout;

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

    public void addInHeader(String key, String value) {
        if (this.headers != null) {
            this.headers.put(key, value);
        } else {
            this.headers = new HashMap<>();
            this.headers.put(key, value);
        }
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

    public void setBasicAuth(String user, String password) {
        this.basicAuth = new BasicAuth(user, password);
    }

    public Long getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Long getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Long readTimeout) {
        this.readTimeout = readTimeout;
    }

    static class BasicAuth {
        private String user;
        private String password;

        public BasicAuth(String user, String password) {
            this.user = user;
            this.password = password;
        }

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

        public boolean isPresent() {
            return this.user != null && this.password != null;
        }

        public String getJointUserPass() {
            return String.format("%s:%s", this.user, this.password);
        }
    }

    static class Data {
        private Object files;
        private Object ascii;
        private Boolean isFormData;

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

        public void setFormData(Boolean formData) {
            isFormData = formData;
        }

        public Boolean isFormData() {
            return isFormData;
        }
    }

    static class FileData {
        private final String formKey;
        private final String absPath;

        FileData(String formKey, String absPath) {
            this.formKey = formKey;
            this.absPath = absPath;
        }

        public String getAbsPath() {
            return absPath;
        }

        public String getFormKey() {
            return formKey;
        }
    }
}
