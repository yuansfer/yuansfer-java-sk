package com.yuanex.payment.request.dataSearch;

import org.apache.commons.lang.StringUtils;

import com.yuanex.payment.exception.YuanpayException;
import com.yuanex.payment.request.ParamValidator;
import com.yuanex.payment.request.RequestConstants;
import com.yuanex.payment.request.YuanpayRequest;
import com.yuanex.payment.response.offline.SettleListResponse;

import net.sf.json.JSONObject;

public class SettleListRequest extends YuanpayRequest<SettleListResponse> {

	private String startDate;
	private String endDate;
	
	public String getStartDate() {
		return startDate;
	}

	public SettleListRequest setStartDate(String startDate) {
		this.startDate = startDate;
		return this;
	}

	public String getEndDate() {
		return endDate;
	}

	public SettleListRequest setEndDate(String endDate) {
		this.endDate = endDate;
		return this;
	}

	@Override
	protected void dataValidate() {
		if (StringUtils.isEmpty(this.startDate)) {
			throw new YuanpayException("startDate missing");
		}
		if (StringUtils.isEmpty(this.endDate)) {
			throw new YuanpayException("endDate missing");
		}
		
		try {
			ParamValidator.dateValidate("startDate", this.startDate);
			
			ParamValidator.dateValidate("endDate", this.endDate);
			
			if (Integer.valueOf(this.endDate) < Integer.valueOf(this.startDate)) {
				throw new RuntimeException("endDate should be later than startDate");
			}
		} catch (Exception e) {
			throw new YuanpayException(e.getMessage());
		}
	}

	@Override
	protected String getAPIUrl(String env) {
		String urlPrefix = getUrlPrefix(env);
		String url = urlPrefix + RequestConstants.SETTLE_LIST;
		return url;
	}

	@Override
	public SettleListResponse convertResponse(String ret) {
		SettleListResponse response = new SettleListResponse();
		JSONObject json = JSONObject.fromObject(ret);
		response.setRetCode(json.getString("ret_code"));
		response.setRetMsg(json.getString("ret_msg"));
		
		if (null != json.get("settlements")) {
			response.setSettlements(json.getJSONArray("settlements"));
		}
		if (null != json.get("size")) {
			response.setSize(json.getInt("size"));
		}
		return response;
	}

	
}