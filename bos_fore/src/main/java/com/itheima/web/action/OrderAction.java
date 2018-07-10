package com.itheima.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;
import com.itheima.bos.utils.Constants;
import com.itheima.bos.web.action.BaseAction;
import com.itheima.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/order")
public class OrderAction extends BaseAction<Order> {

	@Override
	protected Specification<Order> buildSpecification() {
		return null;
	}

	// 接收区域数据
	private String sendAreaInfo;
	private String recAreaInfo;

	public String getSendAreaInfo() {
		return sendAreaInfo;
	}

	public void setSendAreaInfo(String sendAreaInfo) {
		this.sendAreaInfo = sendAreaInfo;
	}

	public String getRecAreaInfo() {
		return recAreaInfo;
	}

	public void setRecAreaInfo(String recAreaInfo) {
		this.recAreaInfo = recAreaInfo;
	}

	/**
	 * 下订单
	 * 
	 * @throws Exception
	 */
	@Action("saveOrder")
	public void saveOrder() throws Exception {
		// 1.得到页面的数据
		Order order = this.getModel();

		// System.out.println(order);

		// 判断客户是否已经登录
		// 从session取出登录用户
		Customer cust = (Customer) ActionContext.getContext().getSession().get("customer");
		if (cust == null) {
			// 没有登录
			result.put("success", false);
			result.put("msg", "下单前请先登录");
			writeJson(result);
			return;
		}

		try {
			// 设置客户ID
			order.setCustomerId(cust.getId());

			// 2.处理寄件人区域和收件人区域
			// 2.1 封装寄件人区域
			if (StringUtils.isNoneBlank(sendAreaInfo)) {
				String[] sendAreaArray = sendAreaInfo.split("/");

				// 封装Area对象
				Area sendArea = new Area();
				if (sendAreaArray != null && sendAreaArray.length >= 1) {
					sendArea.setProvince(sendAreaArray[0]);
				}
				if (sendAreaArray != null && sendAreaArray.length >= 2) {
					sendArea.setCity(sendAreaArray[1]);
				}
				if (sendAreaArray != null && sendAreaArray.length >= 3) {
					sendArea.setDistrict(sendAreaArray[2]);
				}
				// 把Area放入Order里面
				order.setSendArea(sendArea);
			}

			// 2.2 封装收件人区域
			if (StringUtils.isNoneBlank(recAreaInfo)) {
				String[] recAreaArray = recAreaInfo.split("/");

				// 封装Area对象
				Area recArea = new Area();
				if (recAreaArray != null && recAreaArray.length >= 1) {
					recArea.setProvince(recAreaArray[0]);
				}
				if (recAreaArray != null && recAreaArray.length >= 2) {
					recArea.setCity(recAreaArray[1]);
				}
				if (recAreaArray != null && recAreaArray.length >= 3) {
					recArea.setDistrict(recAreaArray[2]);
				}
				// 把Area放入Order里面
				order.setRecArea(recArea);
			}

			//远程BOS后台，发送数据
			WebClient
				.create(Constants.BOS_URL+"/services/orderService")
				.type(MediaType.APPLICATION_JSON)
				.post(order);
			
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", e.getMessage());
		}finally{
			writeJson(result);
		}
		
	}
}
