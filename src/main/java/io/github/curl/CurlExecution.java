package io.github.curl;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    void execute(CurlCallBack curlCallBack) {
        Thread newThread = new Thread(() -> {
            CurlResModel result = execute();
            result.response(curlCallBack);
        });
        newThread.start();
    }

    void execute(CurlCallBackStream curlCallBack) {
        Thread newThread = new Thread(() -> {
            CurlResModel result = execute();
            result.streamResponse(curlCallBack);
        });
        newThread.start();
    }

    CurlResModel execute() {
        try {
            if (this.req.getBasicAuth() != null && this.req.getBasicAuth().isPresent()) {
                this.req.addInHeader("Authorization",
                        String.format("Basic %s",
                                Base64.getEncoder().encodeToString(this.req.getBasicAuth().getJointUserPass().getBytes(UTF_8))));
            }
            return execution(req);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new CurlResModel(-1, createInputStream(ex));
        }
    }

    private CurlResModel execution(CurlReqModel reqModel) throws IOException {
        URL url = new URL(reqModel.getUrl());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(reqModel.getMethod().toUpperCase(Locale.ENGLISH));
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (reqModel.getHeaders() != null) {
            reqModel.getHeaders().forEach(connection::setRequestProperty);
        }
        if (reqModel.getConnectionTimeout() != null) {
            connection.setConnectTimeout(reqModel.getConnectionTimeout().intValue());
        }
        if (reqModel.getReadTimeout() != null) {
            connection.setReadTimeout(reqModel.getReadTimeout().intValue());
        }
        if (reqModel.getData() != null) {
            if (reqModel.getData().getAscii() != null) {
                String data;
                if (reqModel.getData().getAscii() instanceof List) {
                    List<Object> list = (List<Object>) reqModel.getData().getAscii();
                    data = list.stream().map(Object::toString).collect(Collectors.joining(""));
                } else {
                    data = (String) reqModel.getData().getAscii();
                }
                writeToStream(connection.getOutputStream(), data);
            } else if (reqModel.getData().getFiles() != null) {
                List<MetaFile> files = prepareFiles((List<CurlReqModel.FileData>) reqModel.getData().getFiles());
                if (files.size() > 0) {
                    String boundary = null;
                    if (reqModel.getData().isFormData()) {
                        boundary = reqModel.getHeaders().get("Content-Type").split("=")[1];
                    }
                    writeToStream(files, connection.getOutputStream(), boundary);
                } else {
                    throw new RuntimeException(String.format("Not able to locate files %s", files));
                }
            }
        }

        try {
            return new CurlResModel(connection.getResponseCode(), connection.getInputStream());
        } catch (IOException ex) {
            if (connection.getErrorStream() == null) {
                InputStream in = createInputStream(ex);
                return new CurlResModel(-1, in);
            } else {
                return new CurlResModel(connection.getResponseCode(), connection.getErrorStream());
            }
        }
    }

    private List<MetaFile> prepareFiles(List<CurlReqModel.FileData> fData) {
        List<MetaFile> presentFiles = new ArrayList<MetaFile>();
        for (CurlReqModel.FileData fileD : fData) {
            MetaFile file = new MetaFile(fileD.getAbsPath(), fileD);
            if (file.exists()) {
                presentFiles.add(file);
            }
        }
        return presentFiles;
    }

    private void writeToStream(List<MetaFile> files, OutputStream out, String boundary) throws IOException {
        String towHyphens = "--";
        String end = "\r\n";
        DataOutputStream dos = new DataOutputStream(out);
        for (MetaFile file : files) {
            dos.writeBytes(towHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"" + file.getFormKey()
                    + "\"; filename=\"" + file.getName() + "\"" + end);
            dos.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + end);
            dos.writeBytes("Content-Transfer-Encoding: binary" + end);
            dos.writeBytes(end);
            FileInputStream fis = new FileInputStream(file);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, length);
            }
            dos.writeBytes(end);
            fis.close();
        }
        dos.writeBytes(towHyphens + boundary + towHyphens + end);
        dos.flush();
    }

    private void writeToStream(OutputStream os, String data) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os, UTF_8);
        osw.write(data);
        osw.flush();
        osw.close();
        os.close();
    }

    static String readFromStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String response;
        while ((response = bufferedReader.readLine()) != null) {
            sb.append(response);
        }
        bufferedReader.close();
        return sb.toString();
    }

    private InputStream createInputStream(Exception ex) {
        return new ByteArrayInputStream(ex.getMessage().getBytes(UTF_8));
    }

    private InputStream createInputStream(String data) {
        return new ByteArrayInputStream(data.getBytes(UTF_8));
    }
}
