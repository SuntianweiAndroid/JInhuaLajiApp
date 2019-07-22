package com.speedata.jinhualajidemo.been;

public class NetResultBeen {
    /**
     * code : 1
     * mesg : 成功！
     * batchId : sdfadfa
     * content : 4x817oiyYUibjAv+2Ye/4xe7KCufcQt8OLrCrcw6oUlFw1IO6pKv+wKtbCv92hyzTnK1geerVyp2k7tx3r0rlmaztM0B/EFUZfJNZUDDaWWnoZ4+ts7o1uJcNyA9uZ4050GnZ6KdQ/kzuSwtlRBk8ur3NfXcyrBNvQ6LT6to/YU=
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
        return "NetResultBeen{" +
                "code='" + code + '\'' +
                ", mesg='" + mesg + '\'' +
                ", batchId='" + batchId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
