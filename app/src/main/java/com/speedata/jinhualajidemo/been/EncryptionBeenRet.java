package com.speedata.jinhualajidemo.been;

public class EncryptionBeenRet {


    /**
     * code : 1
     * mesg : Success
     * batchId : 456132_1563185641
     * content : 3lM4YAuLt+Qd21NAIB9MwFm7gpjeoY/7N/ZtW4FSEnAdANixvuYOQFeiyIyDrZj5g9eOS1OB59uYI2CaJWRghVZy4FBJbc/4cXDAz/lwrRft38tlZ0t3q2arNoIKuXCG++HC+6ro7GjG0fV6/Sft9CEP1Iyyg4LTXvplaj7zpVLDcOxx7ICwt48Gd4hL7DHpUs7oJiqHTSsZ16lOv0JK7A==
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
        return "EncryptionBeenRet{" +
                "code='" + code + '\'' +
                ", mesg='" + mesg + '\'' +
                ", batchId='" + batchId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
