package com.wl.spacecraft.dto.responsedto;

public class UserInfoOutputDto {
    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 积分总数
     */
    private Integer amount;

    /**
     * 今日已获取og数量
     */
    private Integer limit;

    public String getPhone() {
        return phone;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "UserInfoOutputDto{" +
                "phone='" + phone + '\'' +
                ", amount=" + amount +
                ", limit=" + limit +
                '}';
    }
}
