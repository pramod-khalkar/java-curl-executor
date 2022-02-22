package io.github.curl;

import java.io.File;
import java.net.URI;

/**
 * @author : Pramod Khalkar
 * @since : 22/02/22, Tue
 * description: This file belongs to java-curl-executor
 **/
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
