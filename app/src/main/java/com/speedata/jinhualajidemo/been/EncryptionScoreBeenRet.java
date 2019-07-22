package com.speedata.jinhualajidemo.been;

public class EncryptionScoreBeenRet {


    /**
     * batchId : sdfadfa
     * content : {"cardID":"JH100678876","credit":3,"img":"image","type":1,"htCode":"value"}
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

    public EncryptionScoreBeenRet(String batchId, ContentBean content, String htId, String phone, String secretKey, long time) {
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
         * cardID : JH100678876
         * credit : 3
         * img : image
         * type : 1
         * htCode : value
         */

        private String cardId;
        private int credit;
        private String img;
        private int type;
        private String htCode;

        public ContentBean(String cardId, int credit, String img, int type, String htCode) {
            this.cardId = cardId;
            this.credit = credit;
            this.img = img;
            this.type = type;
            this.htCode = htCode;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public int getCredit() {
            return credit;
        }

        public void setCredit(int credit) {
            this.credit = credit;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getHtCode() {
            return htCode;
        }

        public void setHtCode(String htCode) {
            this.htCode = htCode;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "cardId='" + cardId + '\'' +
                    ", credit=" + credit +
                    ", img='" + img + '\'' +
                    ", type=" + type +
                    ", htCode='" + htCode + '\'' +
                    '}';
        }
    }
}
