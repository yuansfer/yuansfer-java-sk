package com.yuanex.payment.request.online;

import org.apache.commons.lang.StringUtils;

import com.yuanex.payment.enums.CurrencyEnums;
import com.yuanex.payment.enums.TerminalEnums;
import com.yuanex.payment.enums.VendorEnums;
import com.yuanex.payment.exception.YuanpayException;
import com.yuanex.payment.request.ParamValidator;
import com.yuanex.payment.request.RequestConstants;
import com.yuanex.payment.request.YuanpayRequest;
import com.yuanex.payment.response.online.OnlineSecurepayResponse;
import com.yuanex.payment.utils.JSONUtils;

import net.sf.json.JSONObject;

/**
 * @author zhoukai
 */
public class OnlineSecurepayRequest extends YuanpayRequest<OnlineSecurepayResponse> {
	 
	private String amount;						//美金金额
	private String rmbAmount;					//人民币金额
	private String reference;					//商户支付流水号
	private String currency;					//币种
	private String vendor;						//渠道
	private String terminal;					//客户端类型 包括 ONLINE，WAP
	private String description;					//订单描述，会展示在收银台页面
	private String note;						//备注信息，会原样返回
	private Integer timeout;					//过期时间
	private String ipnUrl;						//异步回调地址
	private String callbackUrl;					//同步回调地址
	private String goodsInfo;					//商品信息，要求json格式
	
	//信用卡相关
	private String creditType;
	private Integer paymentCount;
	private Integer frequency;
	
	
	public String getAmount() {
		return amount;
	}
	public OnlineSecurepayRequest setAmount(String amount) {
		this.amount = amount;
		return this;
	}
	public String getRmbAmount() {
		return rmbAmount;
	}
	public OnlineSecurepayRequest setRmbAmount(String rmbAmount) {
		this.rmbAmount = rmbAmount;
		return this;
	}
	public String getCurrency() {
		return currency;
	}
	public OnlineSecurepayRequest setCurrency(String currency) {
		this.currency = currency;
		return this;
	}
	public String getVendor() {
		return vendor;
	}
	public OnlineSecurepayRequest setVendor(String vendor) {
		this.vendor = vendor;
		return this;
	}
	public String getIpnUrl() {
		return ipnUrl;
	}
	public OnlineSecurepayRequest setIpnUrl(String ipnUrl) {
		this.ipnUrl = ipnUrl;
		return this;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public OnlineSecurepayRequest setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
		return this;
	}
	public String getTerminal() {
		return terminal;
	}
	public OnlineSecurepayRequest setTerminal(String terminal) {
		this.terminal = terminal;
		return this;
	}
	public String getDescription() {
		return description;
	}
	public OnlineSecurepayRequest setDescription(String description) {
		this.description = description;
		return this;
	}
	public String getNote() {
		return note;
	}
	public OnlineSecurepayRequest setNote(String note) {
		this.note = note;
		return this;
	}
	public Integer getTimeout() {
		return timeout;
	}
	public OnlineSecurepayRequest setTimeout(Integer timeout) {
		this.timeout = timeout;
		return this;
	}
	public String getGoodsInfo() {
		return goodsInfo;
	}
	public OnlineSecurepayRequest setGoodsInfo(String goodsInfo) {
		this.goodsInfo = goodsInfo;
		return this;
	}
	
	public String getReference() {
		return reference;
	}
	public OnlineSecurepayRequest setReference(String reference) {
		this.reference = reference;
		return this;
	}
	
	public String getCreditType() {
		return creditType;
	}
	public OnlineSecurepayRequest setCreditType(String creditType) {
		this.creditType = creditType;
		return this;
	}
	public Integer getPaymentCount() {
		return paymentCount;
	}
	public OnlineSecurepayRequest setPaymentCount(Integer paymentCount) {
		this.paymentCount = paymentCount;
		return this;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public OnlineSecurepayRequest setFrequency(Integer frequency) {
		this.frequency = frequency;
		return this;
	}
	
	//数据校验
	@Override
	protected void dataValidate() {
		//金额校验
		if (StringUtils.isEmpty(this.amount) && StringUtils.isEmpty(this.rmbAmount)) {
			throw new YuanpayException("amount and rmbAmount cannnot be null at the same time.");
		} else if (StringUtils.isNotEmpty(this.amount) && StringUtils.isNotEmpty(this.rmbAmount)) {
			throw new YuanpayException("amount and rmbAmount can't exist at the same time.");
		} else if (StringUtils.isNotEmpty(this.amount)) {
			ParamValidator.amountValidate("amount", this.amount);
		} else {
			ParamValidator.amountValidate("rmbAmount", this.rmbAmount);
		}
		
		
		if (StringUtils.isEmpty(this.reference)) {
			throw new YuanpayException("reference missing");
		}
		
		//币种校验
		if (StringUtils.isEmpty(this.currency)) {
			throw new YuanpayException("currency missing.");
		}
		if (!CurrencyEnums.USD.getValue().equals(this.currency)) {
			throw new YuanpayException("only USD is supported yet.");
		}
		
		//vendor校验
		if (StringUtils.isEmpty(this.vendor)) {
			throw new YuanpayException("vendor missing.");
		}
		boolean vendorFlag = VendorEnums.containValidate(this.vendor);
		if (!vendorFlag) {
			throw new YuanpayException("data error: vendor.");
		}
		
		//terminal校验
		if (StringUtils.isEmpty(this.terminal)) {
			throw new YuanpayException("terminal missing");
		}
		boolean terminalFlag = TerminalEnums.containValidate(this.terminal);
		if (!terminalFlag) {
			throw new YuanpayException("data error:terminal");
		}
		
		//description,note校验
		if (StringUtils.isNotEmpty(this.description)) {
			if (this.description.length() > 100) {
				throw new YuanpayException("description is too big");
			}
		}
		if (StringUtils.isNotEmpty(this.note)) {
			if (this.note.length() > 100) {
				throw new YuanpayException("note is too big");
			}
		}
		//goodsInfo校验
		if (StringUtils.isNotEmpty(this.goodsInfo)) {
			boolean jsonFlag = JSONUtils.isStringJsonArrFormat(this.goodsInfo);
			if (!jsonFlag) {
				throw new YuanpayException("goodsInfo should be json array format");
			}
		}
		
	}

	@Override
	protected String getAPIUrl(String env) {
		String urlPrefix = getUrlPrefix(env);
		String url = urlPrefix + RequestConstants.ONLINE_SECUREPAY;
		return url;
	}
	
	
	
	@Override
	public OnlineSecurepayResponse convertResponse(String ret) {
		OnlineSecurepayResponse response = new OnlineSecurepayResponse();
		JSONObject json = JSONObject.fromObject(ret);
		if (null != json.get("result")) {
			response.setResult(json.getJSONObject("result"));
		}
		response.setRetCode(json.getString("ret_code"));
		response.setRetMsg(json.getString("ret_msg"));
		return response;
	}
	
}