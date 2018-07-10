package com.itheima.web.action;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.take_delivery.PageBean;
import com.itheima.bos.domain.take_delivery.Promotion;
import com.itheima.bos.utils.Constants;
import com.itheima.bos.web.action.BaseAction;

@Controller
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/promotion")
public class PromotionAction extends BaseAction<Promotion>{

	@Override
	protected Specification<Promotion> buildSpecification() {
		return null;
	}
	
	//接收当前页码和页面大小
	private Integer pageIndex;
	private Integer pageSize;
	public Integer getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 分页查询
	 * @throws Exception 
	 */
	@Action("/queryByPage")
	public void queryByPage() throws Exception{
		
		//远程调用BOS后台
		PageBean pageBean = (PageBean) WebClient
			.create(Constants.BOS_URL+"/services/promotionService/"+pageIndex+"/"+pageSize+"")
			.accept(MediaType.APPLICATION_JSON)
			.get(PageBean.class);
		
		List<Promotion> content = pageBean.getContent();
		Long total = pageBean.getTotal();
		
		//修改图片路径
		for(Promotion p:content){
			p.setTitleImg(Constants.BOS_HOST+p.getTitleImg());
		}
		
		result.put("content", content);
		result.put("total", total);
		writeJson(result);
	}
	

}
