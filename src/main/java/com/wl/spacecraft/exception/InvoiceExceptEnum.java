package com.wl.spacecraft.exception;

public enum InvoiceExceptEnum {

    SUCCESS("0000","开票成功"),

    // 订单相关错误
    NOT_FOUND("35_1000","未查询到相关数据"),
    NOT_FOUND_NEC_ORDER_1("35_1001","订单不支持开具电子发票"),
    NOT_FOUND_NEC_ORDER_2("35_1002","NEC未查询到订单信息"),
    NOT_FOUND_INNIT("35_1003","未查询到本地订单，订单未初始化"),

    // 入参校验错误
    PARAMS_ERROR("35_2000", "入参不合法"),
    PARAMS_ERROR_1("35_2001", "用户信息不合法"),
    PARAMS_ERROR_2("35_2002", "用户填写发票抬头不合法"),
    PARAMS_ERROR_3("35_2003", "发票提取码不合法"),
    PARAMS_ERROR_4("35_2004", "邮箱不合法"),
    PARAMS_ERROR_5("35_2005", "邮箱不合法"),

    // 开具发票，金税错误
    APPLE_INVOICE("35_3000","开票参数错误"),
    APPLE_DOWN_INVOICE("35_3100","开票下载错误"),
    INVOICE_BUILT_ERROR("35_3200","发票定义异常"),
    APPLE_INVOICE_ERROR("35_3300","金税系统异常"),


    // 小票开票校验错误
    NOT_ALLOW_INVOICE("35_34000","小票暂不支持开票"),
    NOT_CURR_APPLY_P("35_34001","非当前开票人"),
    NOT_ALLOW_APPLY_STORE("35_34002","当前门店不支持开票"),
    NOT_ALLOW_APPLY_VENDOR("35_34003","当前商家不支持开票"),
    NOT_ALLOW_SECOND_APPLY("35_34004", "发票不允许二次开具"),

    // 消息错误
    SEND_MSG_ERROR("35_35000","发送消息失败"),

    // 初始发票信息失败
    INIT_INVOICE_ERROR("35_36000","初始化发票失败"),
    INIT_INVOICE_ERROR_1("35_36001","更新发票失败订单信息失败"),
    INIT_INVOICE_ERROR_2("35_36002","更新公司抬头失败"),

    UN_KNOWN("-35_1000", "未知错误");


    private String code;

    private String name;

    InvoiceExceptEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 根据code查询枚举对象
     * @param code code
     * @return InvoiceExceptEnum
     */
    public static InvoiceExceptEnum fromCode(String code){
        if (null == code || code.length() <= 0) {
            return null;
        }
        for (InvoiceExceptEnum invoiceExceptEnum : values()) {
            if (code.equals(invoiceExceptEnum.getCode())) {
                return invoiceExceptEnum;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
