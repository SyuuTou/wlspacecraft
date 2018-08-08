package com.wl.spacecraft.dto.responsedto;

import java.math.BigDecimal;

public class TransactionInfoOutputDto {
    private Integer userId;
    private String  txHash;
    /**
     * 充提币类型，1充币，2提币
     */
    private Integer type;
    /**
     * 交易类型说明
     */
    private String typeNote;
    /**
     * 交易金额
     */
    private BigDecimal ogChange;

    public String getTypeNote() {
        return typeNote;
    }

    public void setTypeNote(String typeNote) {
        this.typeNote = typeNote;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getOgChange() {
        return ogChange;
    }

    public void setOgChange(BigDecimal ogChange) {
        this.ogChange = ogChange;
    }

    @Override
    public String toString() {
        return "TransactionInfoOutputDto{" +
                "userId=" + userId +
                ", txHash='" + txHash + '\'' +
                ", type=" + type +
                ", typeNote='" + typeNote + '\'' +
                ", ogChange=" + ogChange +
                '}';
    }
}
