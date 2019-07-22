package com.speedata.jinhualajidemo.been;

public class DecryptionBeen {


    /**
     * secretKey : 6d2a37dcb5834dabb1a9e43d90a43881
     * htId : 456132
     * phone : 18811158171
     * batchId : 456132_1563185641
     * time : 1563185641
     * content : 3lM4YAuLt+Qd21NAIB9MwFm7gpjeoY/7N/ZtW4FSEnAdANixvuYOQFeiyIyDrZj5g9eOS1OB59uYI2CaJWRghVZy4FBJbc/4cXDAz/lwrRft38tlZ0t3q2arNoIKuXCG++HC+6ro7GjG0fV6/Sft9CEP1Iyyg4LTXvplaj7zpVLDcOxx7ICwt48Gd4hL7DHpUs7oJiqHTSsZ16lOv0JK7A==
     */

    private String secretKey;
    private String htId;
    private String phone;
    private String batchId;
    private long time;
    private String content;

    public DecryptionBeen(String secretKey, String htId, String phone, String batchId, long time, String content) {
        this.secretKey = secretKey;
        this.htId = htId;
        this.phone = phone;
        this.batchId = batchId;
        this.time = time;
        this.content = content;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getHtId() {
        return htId;
    }

    public void setHtId(String htId) {
        this.htId = htId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DecryptionBeen{" +
                "secretKey='" + secretKey + '\'' +
                ", htId='" + htId + '\'' +
                ", phone='" + phone + '\'' +
                ", batchId='" + batchId + '\'' +
                ", time=" + time +
                ", content='" + content + '\'' +
                '}';
    }
}
