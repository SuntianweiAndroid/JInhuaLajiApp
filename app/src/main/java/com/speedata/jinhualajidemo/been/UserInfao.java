package com.speedata.jinhualajidemo.been;

public class UserInfao {

    /**
     * phone : 18811158171
     * cardId : JH100678876
     * credit : 0
     * point : 0
     */

    private String phone;
    private String cardId;
    private String credit;
    private String point;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "UserInfao{" +
                "phone='" + phone + '\'' +
                ", cardId='" + cardId + '\'' +
                ", credit='" + credit + '\'' +
                ", point='" + point + '\'' +
                '}';
    }
}