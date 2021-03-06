package com.wing.socialcontact.service.wx.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SYSCONFIG
 * 
 * @author zengmin
 * @date 2017-04-17 15:04:05
 * @version 1.0
 */
@Table(name = "SYSCONFIG")
public class Sysconfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**  */
	@Id
	private Long id;

	/**  */
	private Date addtime;

	/**  */
	private Boolean deletestatus;

	/**  */
	private String address;

	/**  */
	private Integer bigheight;

	/**  */
	private Integer bigwidth;

	/**  */
	private String closereason;

	/**  */
	private String codestat;

	/**  */
	private Integer complaintTime;

	/**  */
	private Integer consumptionratio;

	/**  */
	private String copyright;

	/**  */
	private String creditrule;

	/**  */
	private Boolean deposit;

	/**  */
	private String description;

	/**  */
	private Boolean emailenable;

	/**  */
	private String emailhost;

	/**  */
	private Integer emailport;

	/**  */
	private String emailpws;

	/**  */
	private String emailtest;

	/**  */
	private String emailuser;

	/**  */
	private String emailusername;

	/**  */
	private Integer everyindentlimit;

	/**  */
	private Boolean gold;

	/**  */
	private Integer goldmarketvalue;

	/**  */
	private Boolean groupbuy;

	/**  */
	private String hotsearch;

	/**  */
	private Integer imagefilesize;

	/**  */
	private String imagesavetype;

	/**  */
	private String imagesuffix;

	/**  */
	private Integer indentcomment;

	/**  */
	private Boolean integral;

	/**  */
	private Integer integralrate;

	/**  */
	private Boolean integralstore;

	/**  */
	private String keywords;

	/**  */
	private Integer memberdaylogin;

	/**  */
	private Integer memberregister;

	/**  */
	private Integer middleheight;

	/**  */
	private Integer middlewidth;

	/**  */
	private Boolean securitycodeconsult;

	/**  */
	private Boolean securitycodelogin;

	/**  */
	private Boolean securitycoderegister;

	/**  */
	private String securitycodetype;

	/**  */
	private String shareCode;

	/**  */
	private Integer smallheight;

	/**  */
	private Integer smallwidth;

	/**  */
	private Boolean smsenbale;

	/**  */
	private String smspassword;

	/**  */
	private String smstest;

	/**  */
	private String smsurl;

	/**  */
	private String smsusername;

	/**  */
	private Boolean storeAllow;

	/**  */
	private String storePayment;

	/**  */
	private String syslanguage;

	/**  */
	private String templates;

	/**  */
	private String title;

	/**  */
	private String uploadfilepath;

	/**  */
	private String userCreditrule;

	/**  */
	private Boolean visitorconsult;

	/**  */
	private Boolean voucher;

	/**  */
	private String websitename;

	/**  */
	private Boolean websitestate;

	/**  */
	private Integer ztcPrice;

	/**  */
	private Boolean ztcStatus;

	/**  */
	private Long goodsimageId;

	/**  */
	private Long membericonId;

	/**  */
	private Long storeimageId;

	/**  */
	private Long websitelogoId;

	/**  */
	private Long applogoId;

	/**  */
	private Integer domainAllowCount;

	/**  */
	private Boolean secondDomainOpen;

	/**  */
	private String sysDomain;

	/**  */
	private Boolean qqLogin;

	/**  */
	private String qqLoginId;

	/**  */
	private String qqLoginKey;

	/**  */
	private String qqDomainCode;

	/**  */
	private String sinaDomainCode;

	/**  */
	private Boolean sinaLogin;

	/**  */
	private String sinaLoginId;

	/**  */
	private String sinaLoginKey;

	/**  */
	private String imagewebserver;

	/**  */
	private Date luceneUpdate;

	/**  */
	private Integer alipayFenrun;

	/**  */
	private Integer balanceFenrun;

	/**  */
	private Integer autoOrderConfirm;

	/**  */
	private Integer autoOrderNotice;

	/**  */
	private Integer bargainMaximum;

	/**  */
	private Double bargainRebate;

	/**  */
	private String bargainState;

	/**  */
	private Integer bargainStatus;

	/**  */
	private String bargainTitle;

	/**  */
	private String serviceQqList;

	/**  */
	private String serviceTelphoneList;

	/**  */
	private Integer sysDeliveryMaximum;

	/**  */
	private Boolean ucBbs;

	/**  */
	private String kuaidiId;

	/**  */
	private String ucApi;

	/**  */
	private String ucAppid;

	/**  */
	private String ucDatabase;

	/**  */
	private String ucDatabasePort;

	/**  */
	private String ucDatabasePws;

	/**  */
	private String ucDatabaseUrl;

	/**  */
	private String ucDatabaseUsername;

	/**  */
	private String ucIp;

	/**  */
	private String ucKey;

	/**  */
	private String ucTablePreffix;

	/**  */
	private String currencyCode;

	/**  */
	private Integer bargainValidity;

	/**  */
	private Integer deliveryAmount;

	/**  */
	private Integer deliveryStatus;

	/**  */
	private String deliveryTitle;

	/**  */
	private String websitecss;

	/**  */
	private Integer combinAmount;

	/**  */
	private Integer combinCount;

	/**  */
	private Integer ztcGoodsView;

	/**  */
	private Integer autoOrderEvaluate;

	/**  */
	private Integer autoOrderReturn;

	/**  */
	private Boolean weixinStore;

	/**  */
	private Integer weixinAmount;

	/**  */
	private Integer configPaymentType;

	/**  */
	private String weixinAccount;

	/** ???????????????appid */
	private String weixinAppid;

	/** ???????????????appsecret */
	private String weixinAppsecret;

	/** ???????????????appid */
	private String weixinAppidQyh;

	/** ???????????????appsecret */
	private String weixinAppsecretQyh;

	/** ???????????????appNo */
	private String weixinAppno;

	/**  */
	private String weixinToken;

	/**  */
	private String weixinWelecomeContent;

	/**  */
	private Long storeWeixinLogoId;

	/**  */
	private Long weixinQrImgId;

	/**  */
	private Integer vipIntegral;

	/**  */
	private Double taxRateValue;

	/**  */
	private Date addTaxRateTime;

	/**  */
	private Double oneRates;

	/**  */
	private Double twoRates;

	/**  */
	private Double threeRates;

	/**  */
	private String pointsServants;

	/**  */
	private Boolean returnRates;

	/**  */
	private Boolean centCommission;

	/** ??????????????? */
	private Byte commissionswitch;

	/** ????????????????????????????????? */
	private Double commissionrate;

	/** ios????????????????????? */
	private Boolean iosgoldrechargeswitch;

	/** ?????????????????? */
	private Date integralBeginTime;

	/** ?????????????????? */
	private Date integralEndTime;

	/** ???????????? */
	private Integer integralMultiple;

	/** ?????????????????? */
	private Integer autoOrderCancel;

	/** ??????????????????????????? */
	private Integer autoOrderReceipttocompleted;

	/**  */
	private Double platformToBusinessFee;

	/**  */
	private Double revpayToPlatformFee;

	/**  */
	private Double revpayToPlatformFeeBank;

	/**  */
	private Double sellerFreight;

	/** ????????????????????? */
	private Boolean offlineswitch;

	/**  */
	private Double revpayToPlatformFeeOnline;

	/**  */
	private Double freightRate;

	/** ???????????????????????????????????? */
	private Boolean isStorePayTaxes;

	/** ??????????????? */
	private Double commissionTaxes;

	/** ????????????????????? */
	private Double payTaxes;

	/** ??????????????????????????????????????????????????? */
	private Double revpayToPlatformFeeBankLimit;

	/**  */
	private Boolean pushenale;

	/**  */
	private String msgswitch;

	private String webSite;
	
	/** ???????????? */
	private String message_switch;
	
	public Sysconfig() {
	}

	
	/**
	 * ??????
	 */
	public Long getId() {
		return id;
	}

	/**
	 * ??????
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * ??????
	 */
	public Date getAddtime() {
		return addtime;
	}

	/**
	 * ??????
	 */
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	/**
	 * ??????
	 */
	public Boolean getDeletestatus() {
		return deletestatus;
	}

	/**
	 * ??????
	 */
	public void setDeletestatus(Boolean deletestatus) {
		this.deletestatus = deletestatus;
	}

	/**
	 * ??????
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * ??????
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * ??????
	 */
	public Integer getBigheight() {
		return bigheight;
	}

	/**
	 * ??????
	 */
	public void setBigheight(Integer bigheight) {
		this.bigheight = bigheight;
	}

	/**
	 * ??????
	 */
	public Integer getBigwidth() {
		return bigwidth;
	}

	/**
	 * ??????
	 */
	public void setBigwidth(Integer bigwidth) {
		this.bigwidth = bigwidth;
	}

	/**
	 * ??????
	 */
	public String getClosereason() {
		return closereason;
	}

	/**
	 * ??????
	 */
	public void setClosereason(String closereason) {
		this.closereason = closereason;
	}

	/**
	 * ??????
	 */
	public String getCodestat() {
		return codestat;
	}

	/**
	 * ??????
	 */
	public void setCodestat(String codestat) {
		this.codestat = codestat;
	}

	/**
	 * ??????
	 */
	public Integer getComplaintTime() {
		return complaintTime;
	}

	/**
	 * ??????
	 */
	public void setComplaintTime(Integer complaintTime) {
		this.complaintTime = complaintTime;
	}

	/**
	 * ??????
	 */
	public Integer getConsumptionratio() {
		return consumptionratio;
	}

	/**
	 * ??????
	 */
	public void setConsumptionratio(Integer consumptionratio) {
		this.consumptionratio = consumptionratio;
	}

	/**
	 * ??????
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * ??????
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	/**
	 * ??????
	 */
	public String getCreditrule() {
		return creditrule;
	}

	/**
	 * ??????
	 */
	public void setCreditrule(String creditrule) {
		this.creditrule = creditrule;
	}

	/**
	 * ??????
	 */
	public Boolean getDeposit() {
		return deposit;
	}

	/**
	 * ??????
	 */
	public void setDeposit(Boolean deposit) {
		this.deposit = deposit;
	}

	/**
	 * ??????
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * ??????
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * ??????
	 */
	public Boolean getEmailenable() {
		return emailenable;
	}

	/**
	 * ??????
	 */
	public void setEmailenable(Boolean emailenable) {
		this.emailenable = emailenable;
	}

	/**
	 * ??????
	 */
	public String getEmailhost() {
		return emailhost;
	}

	/**
	 * ??????
	 */
	public void setEmailhost(String emailhost) {
		this.emailhost = emailhost;
	}

	/**
	 * ??????
	 */
	public Integer getEmailport() {
		return emailport;
	}

	/**
	 * ??????
	 */
	public void setEmailport(Integer emailport) {
		this.emailport = emailport;
	}

	/**
	 * ??????
	 */
	public String getEmailpws() {
		return emailpws;
	}

	/**
	 * ??????
	 */
	public void setEmailpws(String emailpws) {
		this.emailpws = emailpws;
	}

	/**
	 * ??????
	 */
	public String getEmailtest() {
		return emailtest;
	}

	/**
	 * ??????
	 */
	public void setEmailtest(String emailtest) {
		this.emailtest = emailtest;
	}

	/**
	 * ??????
	 */
	public String getEmailuser() {
		return emailuser;
	}

	/**
	 * ??????
	 */
	public void setEmailuser(String emailuser) {
		this.emailuser = emailuser;
	}

	/**
	 * ??????
	 */
	public String getEmailusername() {
		return emailusername;
	}

	/**
	 * ??????
	 */
	public void setEmailusername(String emailusername) {
		this.emailusername = emailusername;
	}

	/**
	 * ??????
	 */
	public Integer getEveryindentlimit() {
		return everyindentlimit;
	}

	/**
	 * ??????
	 */
	public void setEveryindentlimit(Integer everyindentlimit) {
		this.everyindentlimit = everyindentlimit;
	}

	/**
	 * ??????
	 */
	public Boolean getGold() {
		return gold;
	}

	/**
	 * ??????
	 */
	public void setGold(Boolean gold) {
		this.gold = gold;
	}

	/**
	 * ??????
	 */
	public Integer getGoldmarketvalue() {
		return goldmarketvalue;
	}

	/**
	 * ??????
	 */
	public void setGoldmarketvalue(Integer goldmarketvalue) {
		this.goldmarketvalue = goldmarketvalue;
	}

	/**
	 * ??????
	 */
	public Boolean getGroupbuy() {
		return groupbuy;
	}

	/**
	 * ??????
	 */
	public void setGroupbuy(Boolean groupbuy) {
		this.groupbuy = groupbuy;
	}

	/**
	 * ??????
	 */
	public String getHotsearch() {
		return hotsearch;
	}

	/**
	 * ??????
	 */
	public void setHotsearch(String hotsearch) {
		this.hotsearch = hotsearch;
	}

	/**
	 * ??????
	 */
	public Integer getImagefilesize() {
		return imagefilesize;
	}

	/**
	 * ??????
	 */
	public void setImagefilesize(Integer imagefilesize) {
		this.imagefilesize = imagefilesize;
	}

	/**
	 * ??????
	 */
	public String getImagesavetype() {
		return imagesavetype;
	}

	/**
	 * ??????
	 */
	public void setImagesavetype(String imagesavetype) {
		this.imagesavetype = imagesavetype;
	}

	/**
	 * ??????
	 */
	public String getImagesuffix() {
		return imagesuffix;
	}

	/**
	 * ??????
	 */
	public void setImagesuffix(String imagesuffix) {
		this.imagesuffix = imagesuffix;
	}

	/**
	 * ??????
	 */
	public Integer getIndentcomment() {
		return indentcomment;
	}

	/**
	 * ??????
	 */
	public void setIndentcomment(Integer indentcomment) {
		this.indentcomment = indentcomment;
	}

	/**
	 * ??????
	 */
	public Boolean getIntegral() {
		return integral;
	}

	/**
	 * ??????
	 */
	public void setIntegral(Boolean integral) {
		this.integral = integral;
	}

	/**
	 * ??????
	 */
	public Integer getIntegralrate() {
		return integralrate;
	}

	/**
	 * ??????
	 */
	public void setIntegralrate(Integer integralrate) {
		this.integralrate = integralrate;
	}

	/**
	 * ??????
	 */
	public Boolean getIntegralstore() {
		return integralstore;
	}

	/**
	 * ??????
	 */
	public void setIntegralstore(Boolean integralstore) {
		this.integralstore = integralstore;
	}

	/**
	 * ??????
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * ??????
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * ??????
	 */
	public Integer getMemberdaylogin() {
		return memberdaylogin;
	}

	/**
	 * ??????
	 */
	public void setMemberdaylogin(Integer memberdaylogin) {
		this.memberdaylogin = memberdaylogin;
	}

	/**
	 * ??????
	 */
	public Integer getMemberregister() {
		return memberregister;
	}

	/**
	 * ??????
	 */
	public void setMemberregister(Integer memberregister) {
		this.memberregister = memberregister;
	}

	/**
	 * ??????
	 */
	public Integer getMiddleheight() {
		return middleheight;
	}

	/**
	 * ??????
	 */
	public void setMiddleheight(Integer middleheight) {
		this.middleheight = middleheight;
	}

	/**
	 * ??????
	 */
	public Integer getMiddlewidth() {
		return middlewidth;
	}

	/**
	 * ??????
	 */
	public void setMiddlewidth(Integer middlewidth) {
		this.middlewidth = middlewidth;
	}

	/**
	 * ??????
	 */
	public Boolean getSecuritycodeconsult() {
		return securitycodeconsult;
	}

	/**
	 * ??????
	 */
	public void setSecuritycodeconsult(Boolean securitycodeconsult) {
		this.securitycodeconsult = securitycodeconsult;
	}

	/**
	 * ??????
	 */
	public Boolean getSecuritycodelogin() {
		return securitycodelogin;
	}

	/**
	 * ??????
	 */
	public void setSecuritycodelogin(Boolean securitycodelogin) {
		this.securitycodelogin = securitycodelogin;
	}

	/**
	 * ??????
	 */
	public Boolean getSecuritycoderegister() {
		return securitycoderegister;
	}

	/**
	 * ??????
	 */
	public void setSecuritycoderegister(Boolean securitycoderegister) {
		this.securitycoderegister = securitycoderegister;
	}

	/**
	 * ??????
	 */
	public String getSecuritycodetype() {
		return securitycodetype;
	}

	/**
	 * ??????
	 */
	public void setSecuritycodetype(String securitycodetype) {
		this.securitycodetype = securitycodetype;
	}

	/**
	 * ??????
	 */
	public String getShareCode() {
		return shareCode;
	}

	/**
	 * ??????
	 */
	public void setShareCode(String shareCode) {
		this.shareCode = shareCode;
	}

	/**
	 * ??????
	 */
	public Integer getSmallheight() {
		return smallheight;
	}

	/**
	 * ??????
	 */
	public void setSmallheight(Integer smallheight) {
		this.smallheight = smallheight;
	}

	/**
	 * ??????
	 */
	public Integer getSmallwidth() {
		return smallwidth;
	}

	/**
	 * ??????
	 */
	public void setSmallwidth(Integer smallwidth) {
		this.smallwidth = smallwidth;
	}

	/**
	 * ??????
	 */
	public Boolean getSmsenbale() {
		return smsenbale;
	}

	/**
	 * ??????
	 */
	public void setSmsenbale(Boolean smsenbale) {
		this.smsenbale = smsenbale;
	}

	/**
	 * ??????
	 */
	public String getSmspassword() {
		return smspassword;
	}

	/**
	 * ??????
	 */
	public void setSmspassword(String smspassword) {
		this.smspassword = smspassword;
	}

	/**
	 * ??????
	 */
	public String getSmstest() {
		return smstest;
	}

	/**
	 * ??????
	 */
	public void setSmstest(String smstest) {
		this.smstest = smstest;
	}

	/**
	 * ??????
	 */
	public String getSmsurl() {
		return smsurl;
	}

	/**
	 * ??????
	 */
	public void setSmsurl(String smsurl) {
		this.smsurl = smsurl;
	}

	/**
	 * ??????
	 */
	public String getSmsusername() {
		return smsusername;
	}

	/**
	 * ??????
	 */
	public void setSmsusername(String smsusername) {
		this.smsusername = smsusername;
	}

	/**
	 * ??????
	 */
	public Boolean getStoreAllow() {
		return storeAllow;
	}

	/**
	 * ??????
	 */
	public void setStoreAllow(Boolean storeAllow) {
		this.storeAllow = storeAllow;
	}

	/**
	 * ??????
	 */
	public String getStorePayment() {
		return storePayment;
	}

	/**
	 * ??????
	 */
	public void setStorePayment(String storePayment) {
		this.storePayment = storePayment;
	}

	/**
	 * ??????
	 */
	public String getSyslanguage() {
		return syslanguage;
	}

	/**
	 * ??????
	 */
	public void setSyslanguage(String syslanguage) {
		this.syslanguage = syslanguage;
	}

	/**
	 * ??????
	 */
	public String getTemplates() {
		return templates;
	}

	/**
	 * ??????
	 */
	public void setTemplates(String templates) {
		this.templates = templates;
	}

	/**
	 * ??????
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * ??????
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * ??????
	 */
	public String getUploadfilepath() {
		return uploadfilepath;
	}

	/**
	 * ??????
	 */
	public void setUploadfilepath(String uploadfilepath) {
		this.uploadfilepath = uploadfilepath;
	}

	/**
	 * ??????
	 */
	public String getUserCreditrule() {
		return userCreditrule;
	}

	/**
	 * ??????
	 */
	public void setUserCreditrule(String userCreditrule) {
		this.userCreditrule = userCreditrule;
	}

	/**
	 * ??????
	 */
	public Boolean getVisitorconsult() {
		return visitorconsult;
	}

	/**
	 * ??????
	 */
	public void setVisitorconsult(Boolean visitorconsult) {
		this.visitorconsult = visitorconsult;
	}

	/**
	 * ??????
	 */
	public Boolean getVoucher() {
		return voucher;
	}

	/**
	 * ??????
	 */
	public void setVoucher(Boolean voucher) {
		this.voucher = voucher;
	}

	/**
	 * ??????
	 */
	public String getWebsitename() {
		return websitename;
	}

	/**
	 * ??????
	 */
	public void setWebsitename(String websitename) {
		this.websitename = websitename;
	}

	/**
	 * ??????
	 */
	public Boolean getWebsitestate() {
		return websitestate;
	}

	/**
	 * ??????
	 */
	public void setWebsitestate(Boolean websitestate) {
		this.websitestate = websitestate;
	}

	/**
	 * ??????
	 */
	public Integer getZtcPrice() {
		return ztcPrice;
	}

	/**
	 * ??????
	 */
	public void setZtcPrice(Integer ztcPrice) {
		this.ztcPrice = ztcPrice;
	}

	/**
	 * ??????
	 */
	public Boolean getZtcStatus() {
		return ztcStatus;
	}

	/**
	 * ??????
	 */
	public void setZtcStatus(Boolean ztcStatus) {
		this.ztcStatus = ztcStatus;
	}

	/**
	 * ??????
	 */
	public Long getGoodsimageId() {
		return goodsimageId;
	}

	/**
	 * ??????
	 */
	public void setGoodsimageId(Long goodsimageId) {
		this.goodsimageId = goodsimageId;
	}

	/**
	 * ??????
	 */
	public Long getMembericonId() {
		return membericonId;
	}

	/**
	 * ??????
	 */
	public void setMembericonId(Long membericonId) {
		this.membericonId = membericonId;
	}

	/**
	 * ??????
	 */
	public Long getStoreimageId() {
		return storeimageId;
	}

	/**
	 * ??????
	 */
	public void setStoreimageId(Long storeimageId) {
		this.storeimageId = storeimageId;
	}

	/**
	 * ??????
	 */
	public Long getWebsitelogoId() {
		return websitelogoId;
	}

	/**
	 * ??????
	 */
	public void setWebsitelogoId(Long websitelogoId) {
		this.websitelogoId = websitelogoId;
	}

	/**
	 * ??????
	 */
	public Long getApplogoId() {
		return applogoId;
	}

	/**
	 * ??????
	 */
	public void setApplogoId(Long applogoId) {
		this.applogoId = applogoId;
	}

	/**
	 * ??????
	 */
	public Integer getDomainAllowCount() {
		return domainAllowCount;
	}

	/**
	 * ??????
	 */
	public void setDomainAllowCount(Integer domainAllowCount) {
		this.domainAllowCount = domainAllowCount;
	}

	/**
	 * ??????
	 */
	public Boolean getSecondDomainOpen() {
		return secondDomainOpen;
	}

	/**
	 * ??????
	 */
	public void setSecondDomainOpen(Boolean secondDomainOpen) {
		this.secondDomainOpen = secondDomainOpen;
	}

	/**
	 * ??????
	 */
	public String getSysDomain() {
		return sysDomain;
	}

	/**
	 * ??????
	 */
	public void setSysDomain(String sysDomain) {
		this.sysDomain = sysDomain;
	}

	/**
	 * ??????
	 */
	public Boolean getQqLogin() {
		return qqLogin;
	}

	/**
	 * ??????
	 */
	public void setQqLogin(Boolean qqLogin) {
		this.qqLogin = qqLogin;
	}

	/**
	 * ??????
	 */
	public String getQqLoginId() {
		return qqLoginId;
	}

	/**
	 * ??????
	 */
	public void setQqLoginId(String qqLoginId) {
		this.qqLoginId = qqLoginId;
	}

	/**
	 * ??????
	 */
	public String getQqLoginKey() {
		return qqLoginKey;
	}

	/**
	 * ??????
	 */
	public void setQqLoginKey(String qqLoginKey) {
		this.qqLoginKey = qqLoginKey;
	}

	/**
	 * ??????
	 */
	public String getQqDomainCode() {
		return qqDomainCode;
	}

	/**
	 * ??????
	 */
	public void setQqDomainCode(String qqDomainCode) {
		this.qqDomainCode = qqDomainCode;
	}

	/**
	 * ??????
	 */
	public String getSinaDomainCode() {
		return sinaDomainCode;
	}

	/**
	 * ??????
	 */
	public void setSinaDomainCode(String sinaDomainCode) {
		this.sinaDomainCode = sinaDomainCode;
	}

	/**
	 * ??????
	 */
	public Boolean getSinaLogin() {
		return sinaLogin;
	}

	/**
	 * ??????
	 */
	public void setSinaLogin(Boolean sinaLogin) {
		this.sinaLogin = sinaLogin;
	}

	/**
	 * ??????
	 */
	public String getSinaLoginId() {
		return sinaLoginId;
	}

	/**
	 * ??????
	 */
	public void setSinaLoginId(String sinaLoginId) {
		this.sinaLoginId = sinaLoginId;
	}

	/**
	 * ??????
	 */
	public String getSinaLoginKey() {
		return sinaLoginKey;
	}

	/**
	 * ??????
	 */
	public void setSinaLoginKey(String sinaLoginKey) {
		this.sinaLoginKey = sinaLoginKey;
	}

	/**
	 * ??????
	 */
	public String getImagewebserver() {
		return imagewebserver;
	}

	/**
	 * ??????
	 */
	public void setImagewebserver(String imagewebserver) {
		this.imagewebserver = imagewebserver;
	}

	/**
	 * ??????
	 */
	public Date getLuceneUpdate() {
		return luceneUpdate;
	}

	/**
	 * ??????
	 */
	public void setLuceneUpdate(Date luceneUpdate) {
		this.luceneUpdate = luceneUpdate;
	}

	/**
	 * ??????
	 */
	public Integer getAlipayFenrun() {
		return alipayFenrun;
	}

	/**
	 * ??????
	 */
	public void setAlipayFenrun(Integer alipayFenrun) {
		this.alipayFenrun = alipayFenrun;
	}

	/**
	 * ??????
	 */
	public Integer getBalanceFenrun() {
		return balanceFenrun;
	}

	/**
	 * ??????
	 */
	public void setBalanceFenrun(Integer balanceFenrun) {
		this.balanceFenrun = balanceFenrun;
	}

	/**
	 * ??????
	 */
	public Integer getAutoOrderConfirm() {
		return autoOrderConfirm;
	}

	/**
	 * ??????
	 */
	public void setAutoOrderConfirm(Integer autoOrderConfirm) {
		this.autoOrderConfirm = autoOrderConfirm;
	}

	/**
	 * ??????
	 */
	public Integer getAutoOrderNotice() {
		return autoOrderNotice;
	}

	/**
	 * ??????
	 */
	public void setAutoOrderNotice(Integer autoOrderNotice) {
		this.autoOrderNotice = autoOrderNotice;
	}

	/**
	 * ??????
	 */
	public Integer getBargainMaximum() {
		return bargainMaximum;
	}

	/**
	 * ??????
	 */
	public void setBargainMaximum(Integer bargainMaximum) {
		this.bargainMaximum = bargainMaximum;
	}

	/**
	 * ??????
	 */
	public Double getBargainRebate() {
		return bargainRebate;
	}

	/**
	 * ??????
	 */
	public void setBargainRebate(Double bargainRebate) {
		this.bargainRebate = bargainRebate;
	}

	/**
	 * ??????
	 */
	public String getBargainState() {
		return bargainState;
	}

	/**
	 * ??????
	 */
	public void setBargainState(String bargainState) {
		this.bargainState = bargainState;
	}

	/**
	 * ??????
	 */
	public Integer getBargainStatus() {
		return bargainStatus;
	}

	/**
	 * ??????
	 */
	public void setBargainStatus(Integer bargainStatus) {
		this.bargainStatus = bargainStatus;
	}

	/**
	 * ??????
	 */
	public String getBargainTitle() {
		return bargainTitle;
	}

	/**
	 * ??????
	 */
	public void setBargainTitle(String bargainTitle) {
		this.bargainTitle = bargainTitle;
	}

	/**
	 * ??????
	 */
	public String getServiceQqList() {
		return serviceQqList;
	}

	/**
	 * ??????
	 */
	public void setServiceQqList(String serviceQqList) {
		this.serviceQqList = serviceQqList;
	}

	/**
	 * ??????
	 */
	public String getServiceTelphoneList() {
		return serviceTelphoneList;
	}

	/**
	 * ??????
	 */
	public void setServiceTelphoneList(String serviceTelphoneList) {
		this.serviceTelphoneList = serviceTelphoneList;
	}

	/**
	 * ??????
	 */
	public Integer getSysDeliveryMaximum() {
		return sysDeliveryMaximum;
	}

	/**
	 * ??????
	 */
	public void setSysDeliveryMaximum(Integer sysDeliveryMaximum) {
		this.sysDeliveryMaximum = sysDeliveryMaximum;
	}

	/**
	 * ??????
	 */
	public Boolean getUcBbs() {
		return ucBbs;
	}

	/**
	 * ??????
	 */
	public void setUcBbs(Boolean ucBbs) {
		this.ucBbs = ucBbs;
	}

	/**
	 * ??????
	 */
	public String getKuaidiId() {
		return kuaidiId;
	}

	/**
	 * ??????
	 */
	public void setKuaidiId(String kuaidiId) {
		this.kuaidiId = kuaidiId;
	}

	/**
	 * ??????
	 */
	public String getUcApi() {
		return ucApi;
	}

	/**
	 * ??????
	 */
	public void setUcApi(String ucApi) {
		this.ucApi = ucApi;
	}

	/**
	 * ??????
	 */
	public String getUcAppid() {
		return ucAppid;
	}

	/**
	 * ??????
	 */
	public void setUcAppid(String ucAppid) {
		this.ucAppid = ucAppid;
	}

	/**
	 * ??????
	 */
	public String getUcDatabase() {
		return ucDatabase;
	}

	/**
	 * ??????
	 */
	public void setUcDatabase(String ucDatabase) {
		this.ucDatabase = ucDatabase;
	}

	/**
	 * ??????
	 */
	public String getUcDatabasePort() {
		return ucDatabasePort;
	}

	/**
	 * ??????
	 */
	public void setUcDatabasePort(String ucDatabasePort) {
		this.ucDatabasePort = ucDatabasePort;
	}

	/**
	 * ??????
	 */
	public String getUcDatabasePws() {
		return ucDatabasePws;
	}

	/**
	 * ??????
	 */
	public void setUcDatabasePws(String ucDatabasePws) {
		this.ucDatabasePws = ucDatabasePws;
	}

	/**
	 * ??????
	 */
	public String getUcDatabaseUrl() {
		return ucDatabaseUrl;
	}

	/**
	 * ??????
	 */
	public void setUcDatabaseUrl(String ucDatabaseUrl) {
		this.ucDatabaseUrl = ucDatabaseUrl;
	}

	/**
	 * ??????
	 */
	public String getUcDatabaseUsername() {
		return ucDatabaseUsername;
	}

	/**
	 * ??????
	 */
	public void setUcDatabaseUsername(String ucDatabaseUsername) {
		this.ucDatabaseUsername = ucDatabaseUsername;
	}

	/**
	 * ??????
	 */
	public String getUcIp() {
		return ucIp;
	}

	/**
	 * ??????
	 */
	public void setUcIp(String ucIp) {
		this.ucIp = ucIp;
	}

	/**
	 * ??????
	 */
	public String getUcKey() {
		return ucKey;
	}

	/**
	 * ??????
	 */
	public void setUcKey(String ucKey) {
		this.ucKey = ucKey;
	}

	/**
	 * ??????
	 */
	public String getUcTablePreffix() {
		return ucTablePreffix;
	}

	/**
	 * ??????
	 */
	public void setUcTablePreffix(String ucTablePreffix) {
		this.ucTablePreffix = ucTablePreffix;
	}

	/**
	 * ??????
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * ??????
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * ??????
	 */
	public Integer getBargainValidity() {
		return bargainValidity;
	}

	/**
	 * ??????
	 */
	public void setBargainValidity(Integer bargainValidity) {
		this.bargainValidity = bargainValidity;
	}

	/**
	 * ??????
	 */
	public Integer getDeliveryAmount() {
		return deliveryAmount;
	}

	/**
	 * ??????
	 */
	public void setDeliveryAmount(Integer deliveryAmount) {
		this.deliveryAmount = deliveryAmount;
	}

	/**
	 * ??????
	 */
	public Integer getDeliveryStatus() {
		return deliveryStatus;
	}

	/**
	 * ??????
	 */
	public void setDeliveryStatus(Integer deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	/**
	 * ??????
	 */
	public String getDeliveryTitle() {
		return deliveryTitle;
	}

	/**
	 * ??????
	 */
	public void setDeliveryTitle(String deliveryTitle) {
		this.deliveryTitle = deliveryTitle;
	}

	/**
	 * ??????
	 */
	public String getWebsitecss() {
		return websitecss;
	}

	/**
	 * ??????
	 */
	public void setWebsitecss(String websitecss) {
		this.websitecss = websitecss;
	}

	/**
	 * ??????
	 */
	public Integer getCombinAmount() {
		return combinAmount;
	}

	/**
	 * ??????
	 */
	public void setCombinAmount(Integer combinAmount) {
		this.combinAmount = combinAmount;
	}

	/**
	 * ??????
	 */
	public Integer getCombinCount() {
		return combinCount;
	}

	/**
	 * ??????
	 */
	public void setCombinCount(Integer combinCount) {
		this.combinCount = combinCount;
	}

	/**
	 * ??????
	 */
	public Integer getZtcGoodsView() {
		return ztcGoodsView;
	}

	/**
	 * ??????
	 */
	public void setZtcGoodsView(Integer ztcGoodsView) {
		this.ztcGoodsView = ztcGoodsView;
	}

	/**
	 * ??????
	 */
	public Integer getAutoOrderEvaluate() {
		return autoOrderEvaluate;
	}

	/**
	 * ??????
	 */
	public void setAutoOrderEvaluate(Integer autoOrderEvaluate) {
		this.autoOrderEvaluate = autoOrderEvaluate;
	}

	/**
	 * ??????
	 */
	public Integer getAutoOrderReturn() {
		return autoOrderReturn;
	}

	/**
	 * ??????
	 */
	public void setAutoOrderReturn(Integer autoOrderReturn) {
		this.autoOrderReturn = autoOrderReturn;
	}

	/**
	 * ??????
	 */
	public Boolean getWeixinStore() {
		return weixinStore;
	}

	/**
	 * ??????
	 */
	public void setWeixinStore(Boolean weixinStore) {
		this.weixinStore = weixinStore;
	}

	/**
	 * ??????appno
	 */
	public Integer getWeixinAmount() {
		return weixinAmount;
	}

	/**
	 * ??????
	 */
	public void setWeixinAmount(Integer weixinAmount) {
		this.weixinAmount = weixinAmount;
	}

	/**
	 * ??????
	 */
	public Integer getConfigPaymentType() {
		return configPaymentType;
	}

	/**
	 * ??????
	 */
	public void setConfigPaymentType(Integer configPaymentType) {
		this.configPaymentType = configPaymentType;
	}

	/**
	 * ??????
	 */
	public String getWeixinAccount() {
		return weixinAccount;
	}

	/**
	 * ??????
	 */
	public void setWeixinAccount(String weixinAccount) {
		this.weixinAccount = weixinAccount;
	}

	/**
	 * ?????????????????????appid
	 */
	public String getWeixinAppid() {
		return weixinAppid;
	}

	/**
	 * ??????
	 */
	public void setWeixinAppid(String weixinAppid) {
		this.weixinAppid = weixinAppid;
	}

	/**
	 * ???????????????????????????appsecret
	 */
	public String getWeixinAppsecret() {
		return weixinAppsecret;
	}

	/**
	 * ??????
	 */
	public void setWeixinAppsecret(String weixinAppsecret) {
		this.weixinAppsecret = weixinAppsecret;
	}

	/**
	 * ??????
	 */
	public String getWeixinToken() {
		return weixinToken;
	}

	/**
	 * ??????
	 */
	public void setWeixinToken(String weixinToken) {
		this.weixinToken = weixinToken;
	}

	/**
	 * ??????
	 */
	public String getWeixinWelecomeContent() {
		return weixinWelecomeContent;
	}

	/**
	 * ??????
	 */
	public void setWeixinWelecomeContent(String weixinWelecomeContent) {
		this.weixinWelecomeContent = weixinWelecomeContent;
	}

	/**
	 * ??????
	 */
	public Long getStoreWeixinLogoId() {
		return storeWeixinLogoId;
	}

	/**
	 * ??????
	 */
	public void setStoreWeixinLogoId(Long storeWeixinLogoId) {
		this.storeWeixinLogoId = storeWeixinLogoId;
	}

	/**
	 * ??????
	 */
	public Long getWeixinQrImgId() {
		return weixinQrImgId;
	}

	/**
	 * ??????
	 */
	public void setWeixinQrImgId(Long weixinQrImgId) {
		this.weixinQrImgId = weixinQrImgId;
	}

	/**
	 * ??????
	 */
	public Integer getVipIntegral() {
		return vipIntegral;
	}

	/**
	 * ??????
	 */
	public void setVipIntegral(Integer vipIntegral) {
		this.vipIntegral = vipIntegral;
	}

	/**
	 * ??????
	 */
	public Double getTaxRateValue() {
		return taxRateValue;
	}

	/**
	 * ??????
	 */
	public void setTaxRateValue(Double taxRateValue) {
		this.taxRateValue = taxRateValue;
	}

	/**
	 * ??????
	 */
	public Date getAddTaxRateTime() {
		return addTaxRateTime;
	}

	/**
	 * ??????
	 */
	public void setAddTaxRateTime(Date addTaxRateTime) {
		this.addTaxRateTime = addTaxRateTime;
	}

	/**
	 * ??????
	 */
	public Double getOneRates() {
		return oneRates;
	}

	/**
	 * ??????
	 */
	public void setOneRates(Double oneRates) {
		this.oneRates = oneRates;
	}

	/**
	 * ??????
	 */
	public Double getTwoRates() {
		return twoRates;
	}

	/**
	 * ??????
	 */
	public void setTwoRates(Double twoRates) {
		this.twoRates = twoRates;
	}

	/**
	 * ??????
	 */
	public Double getThreeRates() {
		return threeRates;
	}

	/**
	 * ??????
	 */
	public void setThreeRates(Double threeRates) {
		this.threeRates = threeRates;
	}

	/**
	 * ??????
	 */
	public String getPointsServants() {
		return pointsServants;
	}

	/**
	 * ??????
	 */
	public void setPointsServants(String pointsServants) {
		this.pointsServants = pointsServants;
	}

	/**
	 * ??????
	 */
	public Boolean getReturnRates() {
		return returnRates;
	}

	/**
	 * ??????
	 */
	public void setReturnRates(Boolean returnRates) {
		this.returnRates = returnRates;
	}

	/**
	 * ??????
	 */
	public Boolean getCentCommission() {
		return centCommission;
	}

	/**
	 * ??????
	 */
	public void setCentCommission(Boolean centCommission) {
		this.centCommission = centCommission;
	}

	/**
	 * ?????????????????????
	 */
	public Byte getCommissionswitch() {
		return commissionswitch;
	}

	/**
	 * ?????????????????????
	 */
	public void setCommissionswitch(Byte commissionswitch) {
		this.commissionswitch = commissionswitch;
	}

	/**
	 * ???????????????????????????????????????
	 */
	public Double getCommissionrate() {
		return commissionrate;
	}

	/**
	 * ???????????????????????????????????????
	 */
	public void setCommissionrate(Double commissionrate) {
		this.commissionrate = commissionrate;
	}

	/**
	 * ??????ios?????????????????????
	 */
	public Boolean getIosgoldrechargeswitch() {
		return iosgoldrechargeswitch;
	}

	/**
	 * ??????ios?????????????????????
	 */
	public void setIosgoldrechargeswitch(Boolean iosgoldrechargeswitch) {
		this.iosgoldrechargeswitch = iosgoldrechargeswitch;
	}

	/**
	 * ????????????????????????
	 */
	public Date getIntegralBeginTime() {
		return integralBeginTime;
	}

	/**
	 * ????????????????????????
	 */
	public void setIntegralBeginTime(Date integralBeginTime) {
		this.integralBeginTime = integralBeginTime;
	}

	/**
	 * ????????????????????????
	 */
	public Date getIntegralEndTime() {
		return integralEndTime;
	}

	/**
	 * ????????????????????????
	 */
	public void setIntegralEndTime(Date integralEndTime) {
		this.integralEndTime = integralEndTime;
	}

	/**
	 * ??????????????????
	 */
	public Integer getIntegralMultiple() {
		return integralMultiple;
	}

	/**
	 * ??????????????????
	 */
	public void setIntegralMultiple(Integer integralMultiple) {
		this.integralMultiple = integralMultiple;
	}

	/**
	 * ????????????????????????
	 */
	public Integer getAutoOrderCancel() {
		return autoOrderCancel;
	}

	/**
	 * ????????????????????????
	 */
	public void setAutoOrderCancel(Integer autoOrderCancel) {
		this.autoOrderCancel = autoOrderCancel;
	}

	/**
	 * ?????????????????????????????????
	 */
	public Integer getAutoOrderReceipttocompleted() {
		return autoOrderReceipttocompleted;
	}

	/**
	 * ?????????????????????????????????
	 */
	public void setAutoOrderReceipttocompleted(Integer autoOrderReceipttocompleted) {
		this.autoOrderReceipttocompleted = autoOrderReceipttocompleted;
	}

	/**
	 * ??????
	 */
	public Double getPlatformToBusinessFee() {
		return platformToBusinessFee;
	}

	/**
	 * ??????
	 */
	public void setPlatformToBusinessFee(Double platformToBusinessFee) {
		this.platformToBusinessFee = platformToBusinessFee;
	}

	/**
	 * ??????
	 */
	public Double getRevpayToPlatformFee() {
		return revpayToPlatformFee;
	}

	/**
	 * ??????
	 */
	public void setRevpayToPlatformFee(Double revpayToPlatformFee) {
		this.revpayToPlatformFee = revpayToPlatformFee;
	}

	/**
	 * ??????
	 */
	public Double getRevpayToPlatformFeeBank() {
		return revpayToPlatformFeeBank;
	}

	/**
	 * ??????
	 */
	public void setRevpayToPlatformFeeBank(Double revpayToPlatformFeeBank) {
		this.revpayToPlatformFeeBank = revpayToPlatformFeeBank;
	}

	/**
	 * ??????
	 */
	public Double getSellerFreight() {
		return sellerFreight;
	}

	/**
	 * ??????
	 */
	public void setSellerFreight(Double sellerFreight) {
		this.sellerFreight = sellerFreight;
	}

	/**
	 * ???????????????????????????
	 */
	public Boolean getOfflineswitch() {
		return offlineswitch;
	}

	/**
	 * ???????????????????????????
	 */
	public void setOfflineswitch(Boolean offlineswitch) {
		this.offlineswitch = offlineswitch;
	}

	/**
	 * ??????
	 */
	public Double getRevpayToPlatformFeeOnline() {
		return revpayToPlatformFeeOnline;
	}

	/**
	 * ??????
	 */
	public void setRevpayToPlatformFeeOnline(Double revpayToPlatformFeeOnline) {
		this.revpayToPlatformFeeOnline = revpayToPlatformFeeOnline;
	}

	/**
	 * ??????
	 */
	public Double getFreightRate() {
		return freightRate;
	}

	/**
	 * ??????
	 */
	public void setFreightRate(Double freightRate) {
		this.freightRate = freightRate;
	}

	/**
	 * ??????????????????????????????????????????
	 */
	public Boolean getIsStorePayTaxes() {
		return isStorePayTaxes;
	}

	/**
	 * ??????????????????????????????????????????
	 */
	public void setIsStorePayTaxes(Boolean isStorePayTaxes) {
		this.isStorePayTaxes = isStorePayTaxes;
	}

	/**
	 * ?????????????????????
	 */
	public Double getCommissionTaxes() {
		return commissionTaxes;
	}

	/**
	 * ?????????????????????
	 */
	public void setCommissionTaxes(Double commissionTaxes) {
		this.commissionTaxes = commissionTaxes;
	}

	/**
	 * ???????????????????????????
	 */
	public Double getPayTaxes() {
		return payTaxes;
	}

	/**
	 * ???????????????????????????
	 */
	public void setPayTaxes(Double payTaxes) {
		this.payTaxes = payTaxes;
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 */
	public Double getRevpayToPlatformFeeBankLimit() {
		return revpayToPlatformFeeBankLimit;
	}

	/**
	 * ?????????????????????????????????????????????????????????
	 */
	public void setRevpayToPlatformFeeBankLimit(Double revpayToPlatformFeeBankLimit) {
		this.revpayToPlatformFeeBankLimit = revpayToPlatformFeeBankLimit;
	}

	/**
	 * ??????
	 */
	public Boolean getPushenale() {
		return pushenale;
	}

	/**
	 * ??????
	 */
	public void setPushenale(Boolean pushenale) {
		this.pushenale = pushenale;
	}

	/**
	 * ??????
	 */
	public String getMsgswitch() {
		return msgswitch;
	}

	/**
	 * ??????
	 */
	public void setMsgswitch(String msgswitch) {
		this.msgswitch = msgswitch;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	/** ?????????????????????appid */
	public String getWeixinAppidQyh() {
		return weixinAppidQyh;
	}

	public void setWeixinAppidQyh(String weixinAppidQyh) {
		this.weixinAppidQyh = weixinAppidQyh;
	}

	/** ?????????????????????appsecret */
	public String getWeixinAppsecretQyh() {
		return weixinAppsecretQyh;
	}

	public void setWeixinAppsecretQyh(String weixinAppsecretQyh) {
		this.weixinAppsecretQyh = weixinAppsecretQyh;
	}

	/** ?????????????????????appno */
	public String getWeixinAppno() {
		return weixinAppno;
	}

	public void setWeixinAppno(String weixinAppno) {
		this.weixinAppno = weixinAppno;
	}


	public String getMessage_switch()
	{
		return message_switch;
	}


	public void setMessage_switch(String message_switch)
	{
		this.message_switch = message_switch;
	}

}
