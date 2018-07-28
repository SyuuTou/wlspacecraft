package com.wl.spacecraft.model;

import javax.persistence.*;

@Table(name = "sys_main")
public class SysMain {
    /**
     * 码表类别
     */
    @Id
    private String main100;

    /**
     * 码表代码值
     */
    @Id
    private String main102;

    /**
     * 码表类别名称
     */
    private String main101;

    /**
     * 码表代码名称
     */
    private String main103;

    /**
     * 获取码表类别
     *
     * @return main100 - 码表类别
     */
    public String getMain100() {
        return main100;
    }

    /**
     * 设置码表类别
     *
     * @param main100 码表类别
     */
    public void setMain100(String main100) {
        this.main100 = main100;
    }

    /**
     * 获取码表代码值
     *
     * @return main102 - 码表代码值
     */
    public String getMain102() {
        return main102;
    }

    /**
     * 设置码表代码值
     *
     * @param main102 码表代码值
     */
    public void setMain102(String main102) {
        this.main102 = main102;
    }

    /**
     * 获取码表类别名称
     *
     * @return main101 - 码表类别名称
     */
    public String getMain101() {
        return main101;
    }

    /**
     * 设置码表类别名称
     *
     * @param main101 码表类别名称
     */
    public void setMain101(String main101) {
        this.main101 = main101;
    }

    /**
     * 获取码表代码名称
     *
     * @return main103 - 码表代码名称
     */
    public String getMain103() {
        return main103;
    }

    /**
     * 设置码表代码名称
     *
     * @param main103 码表代码名称
     */
    public void setMain103(String main103) {
        this.main103 = main103;
    }
}