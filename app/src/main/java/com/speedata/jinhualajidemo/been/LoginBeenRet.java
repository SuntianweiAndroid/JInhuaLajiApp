package com.speedata.jinhualajidemo.been;

public class LoginBeenRet {


    /**
     * code : 1
     * mesg : Successs
     * batchID : sdfadfa
     * content : 132132
     */

    private String code;
    private String mesg;
    private String batchID;
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

    public String getBatchID() {
        return batchID;
    }

    public void setBatchID(String batchID) {
        this.batchID = batchID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "LoginBeenRet{" +
                "code='" + code + '\'' +
                ", mesg='" + mesg + '\'' +
                ", batchID='" + batchID + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
