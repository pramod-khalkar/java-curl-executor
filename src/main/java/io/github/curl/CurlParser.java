package io.github.curl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Date: 03/02/22
 * Time: 10:52 pm
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
final class CurlParser {
    private static final Logger log = Logger.getLogger(CurlParser.class.getSimpleName());
    private final String[] boolOptions = new String[] {
            "#", "progress-bar", "-", "next", "0", "http1.0", "http1.1", "http2",
            "no-npn", "no-alpn", "1", "tlsv1", "2", "sslv2", "3", "sslv3", "4", "ipv4", "6", "ipv6",
            "a", "append", "anyauth", "B", "use-ascii", "basic", "compressed", "create-dirs",
            "crlf", "digest", "disable-eprt", "disable-epsv", "environment", "cert-status",
            "false-start", "f", "fail", "ftp-create-dirs", "ftp-pasv", "ftp-skip-pasv-ip",
            "ftp-pret", "ftp-ssl-ccc", "ftp-ssl-control", "g", "globoff", "G", "get",
            "ignore-content-length", "i", "include", "I", "head", "j", "junk-session-cookies",
            "J", "remote-header-name", "k", "insecure", "l", "list-only", "L", "location",
            "location-trusted", "metalink", "n", "netrc", "N", "no-buffer", "netrc-file",
            "netrc-optional", "negotiate", "no-keepalive", "no-sessionid", "ntlm", "O",
            "remote-name", "oauth2-bearer", "p", "proxy-tunnel", "path-as-is", "post301", "post302",
            "post303", "proxy-anyauth", "proxy-basic", "proxy-digest", "proxy-negotiate",
            "proxy-ntlm", "q", "raw", "remote-name-all", "s", "silent", "sasl-ir", "S", "show-error",
            "ssl", "ssl-reqd", "ssl-allow-beast", "ssl-no-revoke", "socks5-gssapi-nec", "tcp-nodelay",
            "tlsv1.0", "tlsv1.1", "tlsv1.2", "tr-encoding", "trace-time", "v", "verbose", "xattr",
            "h", "help", "M", "manual", "V", "version"
    };

    private String input;
    private int cursor = 0;

    private CurlParser(String inputCurlCmd) {
        this.input = inputCurlCmd;
    }

    static CurlExecution parse(String cmd) throws Exception {
        CurlParser instance = new CurlParser(cmd);
        Map<String, List<String>> parsed = instance.parseCommand();
        if (parsed.get("_") == null || parsed.get("_").size() == 0 || !parsed.get("_").get(0).equals("curl")) {
            throw new RuntimeException(String.format("Not a curl command %s", cmd));
        }
        CurlReqModel httpRequest = instance.extractHttpRequest(parsed);
        return new CurlExecution(httpRequest);
    }

    private Map<String, List<String>> parseCommand() throws Exception {
        if (this.input == null) {
            throw new Exception("Provided command shouldn't be null");
        }
        if (this.input.trim().length() == 0) {
            throw new Exception(String.format("%s is not valid", this.input));
        }
        if (!input.trim().startsWith("curl")) {
            throw new Exception(String.format("command %s should start with \"curl\"", this.input));
        }
        Map<String, List<String>> result = new HashMap<>();
        result.put("_", new ArrayList<>());
        input = input.trim();
        if (input.length() > 2 && (input.charAt(0) == '$' || input.charAt(0) == '#') && isWhiteSpace(input.charAt(1))) {
            input = input.substring(1).trim();
        }
        for (; cursor < input.length(); cursor++) {
            skipWhiteSpace();
            if (input.charAt(cursor) == '-') {
                //Parsing for flag settings
                if (cursor < input.length() - 1 && input.charAt(cursor + 1) == '-') {
                    //for long flag settings e.g. --header
                    cursor += 2; // skip leading dashes
                    String flagName = moveToNextString('=');
                    if (!result.containsKey(flagName)) {
                        result.put(flagName, new ArrayList<>());
                    }
                    if (isBoolFlagPresent(flagName)) {
                        addOrUpdateResult(result, flagName, "true");
                    } else {
                        addOrUpdateResult(result, flagName, moveToNextString());
                    }
                } else {
                    cursor++; // skip leading hyphen
                    while (cursor < input.length() && !isWhiteSpace(input.charAt(cursor))) {
                        char flagName = input.charAt(cursor);
                        if (!result.containsKey(String.valueOf(flagName))) {
                            result.put(String.valueOf(flagName), new ArrayList<>());
                        }
                        cursor++; // skip the flag name
                        if (isBoolFlagPresent(String.valueOf(flagName))) {
                            addOrUpdateResult(result, String.valueOf(flagName), "true");
                        } else {
                            addOrUpdateResult(result, String.valueOf(flagName), moveToNextString());
                        }
                    }
                }
            } else {
                //un-flagged parameters e.g. curl and url
                addOrUpdateResult(result, "_", moveToNextString());
            }
        }
        return result;
    }

    private boolean isWhiteSpace(Character charValue) {
        return charValue == ' ' || charValue == '\t' || charValue == '\n' || charValue == '\r';
    }

    private void skipWhiteSpace() {
        for (; cursor < input.length(); cursor++) {
            while (input.charAt(cursor) == '\\' && (cursor < input.length() - 1 && isWhiteSpace(input.charAt(cursor + 1)))) {
                cursor++;
            }
            if (!isWhiteSpace(input.charAt(cursor))) {
                break;
            }
        }
    }

    private void addOrUpdateResult(Map<String, List<String>> map, String key, String value) {
        List<String> list;
        if (map.containsKey(key)) {
            list = map.get(key);
            list.add(value);
            map.put(key, list);
            return;
        }
        list = new ArrayList<>();
        list.add(value);
        map.put(key, list);
    }

    private boolean isBoolFlagPresent(String flagName) {
        return Arrays.asList(boolOptions).contains(flagName);
    }

    private String moveToNextString() {
        return moveToNextString(null);
    }

    private String moveToNextString(Character endChar) {
        skipWhiteSpace();
        String str = "";
        char quoteCh = ' ';
        boolean quoted = false;
        boolean escaped = false;
        boolean quoteDS = false;
        for (; cursor < input.length(); cursor++) {
            if (quoted) {
                if (input.charAt(cursor) == quoteCh && !escaped && input.charAt(cursor - 1) != '\\') {
                    quoted = false;
                    continue;
                }
            }
            if (!quoted) {
                if (!escaped) {
                    if (isWhiteSpace(input.charAt(cursor))) {
                        return str;
                    }
                    if (input.charAt(cursor) == '\"' || input.charAt(cursor) == '\'') {
                        quoted = true;
                        quoteCh = input.charAt(cursor);
                        if ((str + quoteCh).equals("$'")) {
                            quoteDS = true;
                            str = "";
                        }
                        cursor++;
                    }
                    if (endChar != null && input.charAt(cursor) == endChar) {
                        cursor++;
                        return str;
                    }
                }
            }
            if (!escaped && !quoteDS && input.charAt(cursor) == '\\') {
                escaped = true;
                //skip the backslash unless the next character is $
                if (!(cursor < input.length() - 1) && input.charAt(cursor + 1) == '$') {
                    continue;
                }
            }
            str += input.charAt(cursor);
            escaped = false;
        }
        return str;
    }

    private CurlReqModel extractHttpRequest(Map<String, List<String>> cmd) {
        CurlReqModel curlReqModel = new CurlReqModel();
        if (cmd.containsKey("url") && cmd.get("url").size() > 0) {
            curlReqModel.setUrl(cmd.get("url").get(0));
        } else if (cmd.get("_").size() > 1) {
            curlReqModel.setUrl(cmd.get("_").get(1));
        }
        if (!curlReqModel.getUrl().contains("http")) {
            curlReqModel.setUrl("http://" + curlReqModel.getUrl());
        }
        // TODO: 05/02/22 need to clarify
        if (cmd.containsKey("H") || cmd.containsKey("header")) {
            //Map<String, String> headers = new HashMap<>();
            if (cmd.get("H") != null) {
                cmd.get("H").forEach(h -> parseHeader(h, curlReqModel));
            }
            if (cmd.get("header") != null) {
                cmd.get("header").forEach(h -> parseHeader(h, curlReqModel));
            }
        }
        if (cmd.containsKey("I") || cmd.containsKey("head")) {
            curlReqModel.setMethod("HEAD");
        }
        if (cmd.containsKey("request") && cmd.get("request").size() > 0) {
            curlReqModel.setMethod(cmd.get("request").get(0).toUpperCase());
        } else if (cmd.containsKey("X") && cmd.get("X").size() > 0) {
            curlReqModel.setMethod(cmd.get("X").get(cmd.get("X").size() - 1).toLowerCase(Locale.ROOT));
        } else if (cmd.containsKey("data-binary") && cmd.get("data-binary").size() > 0) {
            curlReqModel.setMethod("POST");
            curlReqModel.setDataType("raw");
        }

        if (cmd.containsKey("connect-timeout")) {
            List<String> values = cmd.get("connect-timeout");
            if (values != null && values.size() > 0) {
                long timeoutValue = Helper.parseInt(values.get(values.size() - 1));
                curlReqModel.setConnectionTimeout(TimeUnit.SECONDS.toMillis(timeoutValue));
            }
        }

        if (cmd.containsKey("max-time")) {
            List<String> values = cmd.get("max-time");
            if (values != null && values.size() > 0) {
                long readTimeoutValue = Helper.parseInt(values.get(values.size() - 1));
                curlReqModel.setReadTimeout(TimeUnit.SECONDS.toMillis(readTimeoutValue));
            }
        }

        List<String> dataAscii = new ArrayList<>();
        List<CurlReqModel.FileData> dataFiles = new ArrayList<>();
        if (cmd.containsKey("d")) {
            loadData(curlReqModel, cmd.get("d"), dataFiles, dataAscii);
        }
        if (cmd.containsKey("data")) {
            loadData(curlReqModel, cmd.get("data"), dataFiles, dataAscii);
        }
        if (cmd.containsKey("data-binary")) {
            loadData(curlReqModel, cmd.get("data-binary"), dataFiles, dataAscii);
        }

        CurlReqModel.Data data = new CurlReqModel.Data();
        if (cmd.containsKey("form")) {
            if (curlReqModel.getHeaders() == null || !curlReqModel.getHeaders().containsKey("Content-Type")) {
                String boundary = Long.toHexString(System.currentTimeMillis());
                curlReqModel.addInHeader("Content-Type", "multipart/form-data;boundary=" + boundary);
            }
            data.setFormData(true);
            loadData(curlReqModel, cmd.get("form"), dataFiles, dataAscii,true);
        }
        if (dataAscii.size() > 0) {
            data.setAscii(dataAscii);
            curlReqModel.setData(data);
        }
        if (dataFiles.size() > 0) {
            data.setFiles(dataFiles);
            curlReqModel.setData(data);
        }

        String basicAuthSting = null;
        if (cmd.containsKey("user") && cmd.get("user").size() > 0) {
            basicAuthSting = cmd.get("user").get(cmd.get("user").size() - 1);
        } else if (cmd.containsKey("u") && cmd.get("u").size() > 0) {
            basicAuthSting = cmd.get("u").get(cmd.get("u").size() - 1);
        }
        if (basicAuthSting != null) {
            int basicAuthSplit = basicAuthSting.indexOf(":");
            if (basicAuthSplit > -1) {
                curlReqModel.setBasicAuth(basicAuthSting.substring(0, basicAuthSplit), basicAuthSting.substring(basicAuthSplit + 1));
            } else {
                curlReqModel.setBasicAuth(basicAuthSting, "<password>");
            }
        }
        if (curlReqModel.getMethod() == null || curlReqModel.getMethod().equals("")) {
            curlReqModel.setMethod("GET");
        }
        return curlReqModel;
    }

    private void loadData(CurlReqModel httpRequest,
                          List<String> data,
                          List<CurlReqModel.FileData> dataFiles,
                          List<String> dataAscii){
        loadData(httpRequest, data, dataFiles, dataAscii,false);
    }

    private void loadData(CurlReqModel httpRequest,
                          List<String> data,
                          List<CurlReqModel.FileData> dataFiles,
                          List<String> dataAscii, boolean isFormDataWithKey) {
        if (httpRequest.getMethod() == null || httpRequest.getMethod().equals("")) {
            httpRequest.setMethod("POST");
        }
        if (httpRequest.getHeaders() == null || !httpRequest.getHeaders().containsKey("Content-Type")) {
            httpRequest.addInHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        for (String data_ : data) {
            if (isFormDataWithKey || (data_.length() > 0 && data_.charAt(0) == '@')) {
                if (isFormDataWithKey) {
                    String[] keyValue = data_.split("=");
                    CurlReqModel.FileData fData;
                    if (keyValue.length == 2) {
                        fData = new CurlReqModel.FileData(keyValue[0], keyValue[1].substring(1));
                    } else {
                        fData = new CurlReqModel.FileData("file", keyValue[0].substring(1));
                    }
                    dataFiles.add(fData);
                } else {
                    dataFiles.add(new CurlReqModel.FileData(null, data_.substring(1)));
                }
            } else {
                dataAscii.add(data_);
            }
        }
    }

    private void parseHeader(String header, CurlReqModel curlReqModel) {
        if (header != null && header.contains(":")) {
            String[] headerSplit = header.split(":");
            curlReqModel.addInHeader(headerSplit[0], headerSplit[1]);
        }
    }
}
