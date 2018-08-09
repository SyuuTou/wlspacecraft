package com.wl.spacecraft.dto.responsedto;

public class CoinToAccountOutputDto {
    /**
     * 提币成功与否的标志位
     * true 提币申请成功
     * false 提币申请不成功
     */
    private  boolean result;

    private String phone;
    /**
     * 用户的og总数
     */
    private Integer  amount;
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

    @Override
    public String toString() {
        return "CoinToAccountOutputDto{" +
                "result=" + result +
                ", phone='" + phone + '\'' +
                ", amount=" + amount +
                '}';
    }
}
