package com.wl.spacecraft.dto.responsedto;

import com.wl.spacecraft.model.ConfigOgPrice;

import java.math.BigDecimal;

/**
 * 充值信息输出dto
 */
public class RechargeInfoOutputDto {
    /**
     * 最小充值金额
     */
    private BigDecimal minRechargeAmount;
    /**
     * 微信客服1
     */
    private String wechatNum1;
    /**
     * 微信客服2
     */
    private String wechatNum2;
    /**
     *Eth同OG兑换比例
     */
    private ConfigOgPrice configOgPrice;

    public BigDecimal getMinRechargeAmount() {
        return minRechargeAmount;
    }

    public void setMinRechargeAmount(BigDecimal minRechargeAmount) {
        this.minRechargeAmount = minRechargeAmount;
    }

    public String getWechatNum1() {
        return wechatNum1;
    }

    public void setWechatNum1(String wechatNum1) {
        this.wechatNum1 = wechatNum1;
    }

    public String getWechatNum2() {
        return wechatNum2;
    }

    public void setWechatNum2(String wechatNum2) {
        this.wechatNum2 = wechatNum2;
    }

    public ConfigOgPrice getConfigOgPrice() {
        return configOgPrice;
    }

    public void setConfigOgPrice(ConfigOgPrice configOgPrice) {
        this.configOgPrice = configOgPrice;
    }

    @Override
    public String toString() {
        return "ChargeInfoOutputDto{" +
                "minRechargeAmount=" + minRechargeAmount +
                ", wechatNum1='" + wechatNum1 + '\'' +
                ", wechatNum2='" + wechatNum2 + '\'' +
                ", configOgPrice=" + configOgPrice +
                '}';
    }
}
