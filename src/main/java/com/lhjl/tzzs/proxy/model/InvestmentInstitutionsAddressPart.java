package com.lhjl.tzzs.proxy.model;

import java.math.BigDecimal;
import javax.persistence.*;

@Table(name = "investment_institutions_address_part")
public class InvestmentInstitutionsAddressPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 机构id
     */
    @Column(name = "investment_institution_id")
    private Integer investmentInstitutionId;

    /**
     * 分部名称
     */
    private String name;

    /**
     * 所在城市
     */
    private String town;

    /**
     * 详细地址
     */
    @Column(name = "detail_address")
    private String detailAddress;

    /**
     * 电话国家码
     */
    @Column(name = "phone_country_code")
    private String phoneCountryCode;

    /**
     * 电话区号
     */
    @Column(name = "phone_district_code")
    private String phoneDistrictCode;

    /**
     * 电话号（不包含，国家码，区号）
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * 分部独立邮箱
     */
    @Column(name = "part_email")
    private String partEmail;

    private BigDecimal longitude;

    private BigDecimal latitude;

    /**
     * 删除标志:0代表有效；1代表无效
     */
    private Integer yn;

    /**
     * 权重
     */
    private Integer weight;
    /**
     * 排序
     */
    @Transient
    private Integer sort;
    
    /**
     * 主体类型
     */
    @Transient 
    private Integer subjectType;
    

    public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取机构id
     *
     * @return investment_institution_id - 机构id
     */
    public Integer getInvestmentInstitutionId() {
        return investmentInstitutionId;
    }

    /**
     * 设置机构id
     *
     * @param investmentInstitutionId 机构id
     */
    public void setInvestmentInstitutionId(Integer investmentInstitutionId) {
        this.investmentInstitutionId = investmentInstitutionId;
    }

    /**
     * 获取分部名称
     *
     * @return name - 分部名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置分部名称
     *
     * @param name 分部名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取所在城市
     *
     * @return town - 所在城市
     */
    public String getTown() {
        return town;
    }

    /**
     * 设置所在城市
     *
     * @param town 所在城市
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * 获取详细地址
     *
     * @return detail_address - 详细地址
     */
    public String getDetailAddress() {
        return detailAddress;
    }

    /**
     * 设置详细地址
     *
     * @param detailAddress 详细地址
     */
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    /**
     * 获取电话国家码
     *
     * @return phone_country_code - 电话国家码
     */
    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    /**
     * 设置电话国家码
     *
     * @param phoneCountryCode 电话国家码
     */
    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    /**
     * 获取电话区号
     *
     * @return phone_district_code - 电话区号
     */
    public String getPhoneDistrictCode() {
        return phoneDistrictCode;
    }

    /**
     * 设置电话区号
     *
     * @param phoneDistrictCode 电话区号
     */
    public void setPhoneDistrictCode(String phoneDistrictCode) {
        this.phoneDistrictCode = phoneDistrictCode;
    }

    /**
     * 获取电话号（不包含，国家码，区号）
     *
     * @return phone_number - 电话号（不包含，国家码，区号）
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 设置电话号（不包含，国家码，区号）
     *
     * @param phoneNumber 电话号（不包含，国家码，区号）
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 获取分部独立邮箱
     *
     * @return part_email - 分部独立邮箱
     */
    public String getPartEmail() {
        return partEmail;
    }

    /**
     * 设置分部独立邮箱
     *
     * @param partEmail 分部独立邮箱
     */
    public void setPartEmail(String partEmail) {
        this.partEmail = partEmail;
    }

    /**
     * @return longitude
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * @return latitude
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * 获取删除标志:0代表有效；1代表无效
     *
     * @return yn - 删除标志:0代表有效；1代表无效
     */
    public Integer getYn() {
        return yn;
    }

    /**
     * 设置删除标志:0代表有效；1代表无效
     *
     * @param yn 删除标志:0代表有效；1代表无效
     */
    public void setYn(Integer yn) {
        this.yn = yn;
    }

    /**
     * 获取权重
     *
     * @return weight - 权重
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * 设置权重
     *
     * @param weight 权重
     */
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

	@Override
	public String toString() {
		return "InvestmentInstitutionsAddressPart [id=" + id + ", investmentInstitutionId=" + investmentInstitutionId
				+ ", name=" + name + ", town=" + town + ", detailAddress=" + detailAddress + ", phoneCountryCode="
				+ phoneCountryCode + ", phoneDistrictCode=" + phoneDistrictCode + ", phoneNumber=" + phoneNumber
				+ ", partEmail=" + partEmail + ", longitude=" + longitude + ", latitude=" + latitude + ", yn=" + yn
				+ ", weight=" + weight + ", sort=" + sort + "]";
	}
    
}