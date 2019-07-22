package com.speedata.jinhualajidemo.been;

public class UploadScoreBeenRet {


    /**
     * code : 1
     * mesg : 成功！
     * batchId : sdfadfa3
     * content :
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
        return "UploadScoreBeenRet{" +
                "code='" + code + '\'' +
                ", mesg='" + mesg + '\'' +
                ", batchId='" + batchId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
