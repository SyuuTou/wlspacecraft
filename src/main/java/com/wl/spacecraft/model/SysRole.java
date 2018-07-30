package com.wl.spacecraft.model;

import javax.persistence.*;

@Table(name = "sys_role")
public class SysRole {
    /**
     * 角色id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 是否可用 0：可用  1：不可用
     */
    private String useable;

    /**
     * 获取角色id
     *
     * @return id - 角色id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置角色id
     *
     * @param id 角色id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取角色名称
     *
     * @return name - 角色名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置角色名称
     *
     * @param name 角色名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取是否可用 0：可用  1：不可用
     *
     * @return useable - 是否可用 0：可用  1：不可用
     */
    public String getUseable() {
        return useable;
    }

    /**
     * 设置是否可用 0：可用  1：不可用
     *
     * @param useable 是否可用 0：可用  1：不可用
     */
    public void setUseable(String useable) {
        this.useable = useable;
    }
}