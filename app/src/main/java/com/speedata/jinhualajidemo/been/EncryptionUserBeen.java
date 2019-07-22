package com.speedata.jinhualajidemo.been;

public class EncryptionUserBeen {


    /**
     * batchId : sdfadfa
     * content : {"card":"JH100678876","cardTpye":3}
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

    public EncryptionUserBeen(String batchId, ContentBean content, String htId, String phone, String secretKey, long time) {
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
         * card : JH100678876
         * cardTpye : 3
         */

        private String card;
        private int cardType;

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public int getCardType() {
            return cardType;
        }

        public void setCardType(int cardType) {
            this.cardType = cardType;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "card='" + card + '\'' +
                    ", cardType=" + cardType +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "EncryptionUserBeen{" +
                "batchId='" + batchId + '\'' +
                ", content=" + content +
                ", htId='" + htId + '\'' +
                ", phone='" + phone + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", time=" + time +
                '}';
    }
}
