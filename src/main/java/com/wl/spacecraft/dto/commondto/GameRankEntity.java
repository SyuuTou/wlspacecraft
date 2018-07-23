package com.wl.spacecraft.dto.commondto;

public class GameRankEntity {
    /**
     * 手机号
     */
    private String phone;
    /**
     * Og币获取数目
     */
    private Integer ogObtain;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getOgObtain() {
        return ogObtain;
    }

    public void setOgObtain(Integer ogObtain) {
        this.ogObtain = ogObtain;
    }

    @Override
    public String toString() {
        return "GameRankEntity{" +
                "phone='" + phone + '\'' +
                ", ogObtain=" + ogObtain +
                '}';
    }
}
