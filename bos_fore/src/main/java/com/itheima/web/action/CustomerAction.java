package com.itheima.web.action;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.itheima.bos.utils.Constants;
import com.itheima.bos.utils.MailUtils;
import com.itheima.bos.utils.SmsUtils;
import com.itheima.bos.web.action.BaseAction;
import com.itheima.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;

@Controller
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/customer")
public class CustomerAction extends BaseAction<Customer>{

	@Override
	protected Specification<Customer> buildSpecification() {
		return null;
	}

	/**
	 * 发送短信验证码
	 * @throws Exception 
	 */
	@Action("sendSms")
	public void sendSms() throws Exception{
		//取出模块的telephone
		String telephone = this.getModel().getTelephone();
		
		//系统随机生成4位数字字符串
		String code = RandomStringUtils.randomNumeric(4);
		
		//把验证码存入session，以便用户验证判断
		ActionContext.getContext().getSession().put("code", code);
		
		
		System.out.println("验证码："+code);
		//调用工具类发送短信
		boolean flag = SmsUtils.sendSms(telephone, 
				"物流系统", 
				"SMS_100735040", 
				"{\"code\":\""+code+"\"}");
		
		result.put("success", flag);
		writeJson(result);
	}
	
	//接收验证码
	private String validCode;
	public String getValidCode() {
		return validCode;
	}
	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
	
	//注入RedisTemplate
	@Resource
	private RedisTemplate<String,Object> redisTemplate;
	
	/**
	 * 注册客户
	 * @throws Exception 
	 */
	@Action("saveCustomer")
	public void saveCustomer() throws Exception{
		//获取页面的数据
		Customer cust = this.getModel();
		//1.判断手机验证是否正确
		//1.1 获取用户的验证码（属性驱动接收）
		//1.2 获取系统发送的验证码（从session取出）
		String code = (String)ActionContext.getContext().getSession().get("code");
		if(code ==null || !code.equals(validCode)){
			result.put("success", false);
			result.put("msg", "手机验证码输入有误");
			writeJson(result);
			return;
		}
		
		//2.判断客户手机是否被注册
		Customer customer = WebClient
			.create(Constants.CRM_URL+"/services/customerService/findByTelephone/"+cust.getTelephone())
			.accept(MediaType.APPLICATION_JSON)
			.get(Customer.class);
		
		if(customer!=null){
			//客户手机已经注册
			result.put("success", false);
			result.put("msg", "该客户手机已经注册，请更换!");
			writeJson(result);
			return;
		}
		
		try {
			//3.保存客户数据
			WebClient
				.create(Constants.CRM_URL+"/services/customerService")
				.type(MediaType.APPLICATION_JSON)
				.post(cust);
			
			//清空session的验证码
			ActionContext.getContext().getSession().remove("code");
			
			//系统生成随机激活码
			String activeCode = UUID.randomUUID().toString();
			
			//把激活码存储
			/**
			 * 1）session对象：存储的时间问题，持久化问题
			 * 2）数据库：在客户表添加激活码字段    解决持久化问题，效率并不高
			 * 3）redis缓存: 持久化功能，设置存储时间，查询效率非常高
			 */
			redisTemplate.opsForValue().set(cust.getTelephone(), activeCode,48,TimeUnit.HOURS);
			
			
			//使用JavaMail发送一封注册激活邮件
			String title = "BOS物流系统客户激活邮件";
			String content = "尊敬的用户：<br/>恭喜你在本系统注册成为会员，请在48小时内点击以下链接进行激活，激活后可以登录。<br/>"
					+ "激活链接： <a href='http://localhost:9082/bos_fore/customer/activeCustomer.action?telephone="+cust.getTelephone()+"&activeCode="+activeCode+"'>激活</a>";
			MailUtils.sendMail(cust.getEmail(), title, content);
			
			
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
		}finally{
			writeJson(result);
		}
	}
	
	//接收激活码
	private String activeCode;
	public String getActiveCode() {
		return activeCode;
	}
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}

	/**
	 * 激活方法
	 * @throws Exception 
	 */
	@Action("activeCustomer")
	public void activeCustomer() throws Exception{
		Customer cust = this.getModel();
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=utf-8");
		//1.取出redis的激活码
		String dbActiveCode = (String)redisTemplate.opsForValue().get(cust.getTelephone());
		
		//2.判断激活码是否有效
		if(dbActiveCode==null){
			response.getWriter().write("无效激活");
			return;
		}
		
		//3.判断激活码是否匹配
		if(!dbActiveCode.equals(activeCode)){
			response.getWriter().write("激活码错误");
			return;
		}
		
		//4.判断客户是否已经注册
		Customer customer = WebClient
				.create(Constants.CRM_URL+"/services/customerService/findByTelephone/"+cust.getTelephone())
				.accept(MediaType.APPLICATION_JSON)
				.get(Customer.class);
		
		if(customer==null){
			response.getWriter().write("无效的客户");
			return;
		}
		
		//5.判断客户是否已经激活
		if(customer.getType()!=null && customer.getType().equals("1")){
			response.getWriter().write("客户已经激活，无需再次激活");
			return;
		}
		
		try {
			//6.激活客户
			WebClient
				.create(Constants.CRM_URL+"/services/customerService/"+cust.getTelephone())
				.put(null);
			
			//把激活码删除
			redisTemplate.delete(cust.getTelephone());
			
			response.getWriter().write("激活成功，请登录。<a href='http://localhost:9082/bos_fore/login.html'>登录</a>");
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("激活失败。"+e.getMessage());
		}
	}
	
	/**
	 * 客户登录
	 * @throws Exception 
	 */
	@Action("login")
	public void login() throws Exception{
		Customer cust = this.getModel();
		//调用CRM方法
		//1.查询当前客户账户是否存在
		Customer customer = WebClient
				.create(Constants.CRM_URL+"/services/customerService/findByTelephone/"+cust.getTelephone())
				.accept(MediaType.APPLICATION_JSON)
				.get(Customer.class);
		
		if(customer==null){
			result.put("success", false);
			result.put("msg", "用户没有注册!");
			writeJson(result);
			return;
		}
		
		//2.判断密码
		if(!customer.getPassword().equals(cust.getPassword())){
			result.put("success", false);
			result.put("msg", "密码有误!");
			writeJson(result);
			return;
		}
		
		//3.判断客户是否已经激活
		if(customer.getType()==null || !customer.getType().equals("1")){
			result.put("success", false);
			result.put("msg", "账户未激活!");
			writeJson(result);
			return;
		}
		
		//登录成功
		//保存用户数据到session里面
		ActionContext.getContext().getSession().put("customer", customer);
		result.put("success", true);
		writeJson(result);
	}
}
