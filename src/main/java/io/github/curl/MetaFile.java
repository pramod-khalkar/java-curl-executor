package io.github.curl;

import java.io.File;
import java.net.URI;

/**
 * Date: 20/02/22
 * Time: 3:14 PM
 * This file is project specific to java-curl-executor
 * Author: Pramod Khalkar
 */
class MetaFile extends File {
    private CurlReqModel.FileData metaData;

    public MetaFile(String pathname, CurlReqModel.FileData metaData) {
        super(pathname);
        this.metaData = metaData;
    }

    public MetaFile(String parent, String child, CurlReqModel.FileData metaData) {
        super(parent, child);
        this.metaData = metaData;
    }

    public MetaFile(File parent, String child, CurlReqModel.FileData metaData) {
        super(parent, child);
        this.metaData = metaData;
    }

    public MetaFile(URI uri, CurlReqModel.FileData metaData) {
        super(uri);
        this.metaData = metaData;
    }

    public String getFormKey() {
        return metaData.getFormKey() != null ? metaData.getFormKey() : "file";
    }
}
