package com.wl.spacecraft.dto.responsedto;

public class GameOverOutputDto {

    /**
     * 金币获取情况
     * true成功
     * false失败
     */
    private boolean result;

    private String phone;
    /**
     * 拥有的og数量
     */
    private Integer amount;
    /**
     * 今日已获取og数量
     */
    private Integer limit;

    public boolean isResult() {
        return result;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getAmount() {
        return amount;
    }


    public void setResult(boolean result) {
        this.result = result;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "GameOverOutputDto{" +
                "result=" + result +
                ", phone='" + phone + '\'' +
                ", amount=" + amount +
                ", limit=" + limit +
                '}';
    }
}
