package com.speedata.jinhualajidemo.been;

public class EncryptionLoginBeen {


    /**
     * batchId : sdfadfa
     * content : {"pwd":"c4ca4238a0b923820dcc509a6f75849b"}
     * htId : htid
     * phone : 18811158171
     * secretKey : icm_jh
     * time : 1563274242912
     */

    private String batchId;
    private ContentBean content;
    private String htId;
    private String phone;
    private String secretKey;
    private long time;

    public EncryptionLoginBeen(String batchId, ContentBean content, String htId, String phone, String secretKey, long time) {
        this.batchId = batchId;
        this.content = content;
        this.htId = htId;
        this.phone = phone;
        this.secretKey = secretKey;
        this.time = time;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static class ContentBean {
        /**
         * pwd : c4ca4238a0b923820dcc509a6f75849b
         */

        private String pwd;

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }
    }
}
