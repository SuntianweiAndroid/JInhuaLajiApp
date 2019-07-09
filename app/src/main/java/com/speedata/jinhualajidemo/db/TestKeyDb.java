package com.speedata.jinhualajidemo.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class TestKeyDb {
    @Id(autoincrement = false)
    private String id;
    private int version;
    private String keyType;
    private String pubkeyId;
    private String pubKey;
    @Generated(hash = 311190128)
    public TestKeyDb(String id, int version, String keyType, String pubkeyId,
            String pubKey) {
        this.id = id;
        this.version = version;
        this.keyType = keyType;
        this.pubkeyId = pubkeyId;
        this.pubKey = pubKey;
    }
    @Generated(hash = 615733729)
    public TestKeyDb() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getVersion() {
        return this.version;
    }
    public void setVersion(int version) {
        this.version = version;
    }
    public String getKeyType() {
        return this.keyType;
    }
    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }
    public String getPubkeyId() {
        return this.pubkeyId;
    }
    public void setPubkeyId(String pubkeyId) {
        this.pubkeyId = pubkeyId;
    }
    public String getPubKey() {
        return this.pubKey;
    }
    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }
   
   

}
