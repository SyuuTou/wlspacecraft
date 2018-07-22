package com.wl.spacecraft.dto.responsedto;

public class OgObtainRankOutputDto {
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

    public Integer getOgObtain() {
        return ogObtain;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setOgObtain(Integer ogObtain) {
        this.ogObtain = ogObtain;
    }

    @Override
    public String toString() {
        return "OgObtainRankOutputDto{" +
                "phone='" + phone + '\'' +
                ", ogObtain=" + ogObtain +
                '}';
    }
}
