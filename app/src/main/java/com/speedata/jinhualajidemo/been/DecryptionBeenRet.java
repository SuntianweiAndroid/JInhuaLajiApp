package com.speedata.jinhualajidemo.been;

public class DecryptionBeenRet {

    /**
     * code : 1
     * mesg : Success
     * batchId : 456132_1563185641
     * content : {"batchId":"456132_1563185641","content":[18811158171],"htId":"456132","phone":"18811158171","secretKey":"6d2a37dcb5834dabb1a9e43d90a43881","time":1563185641}
     */

    private String code;
    private String mesg;
    private String batchId;
    private String content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DecryptionBeenRet{" +
                "code='" + code + '\'' +
                ", mesg='" + mesg + '\'' +
                ", batchId='" + batchId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
