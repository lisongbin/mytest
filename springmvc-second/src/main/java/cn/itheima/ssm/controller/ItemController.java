package cn.itheima.ssm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.itheima.ssm.entity.Item;
import cn.itheima.ssm.entity.QueryVo;
import cn.itheima.ssm.service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
/*	@RequestMapping("/queryItem.do")
	public ModelAndView queryItem() {
		
		ModelAndView mav = new ModelAndView();
		
		List<Item> list = itemService.queryAllItem();
		
		mav.addObject("itemList",list);
		
		mav.setViewName("/item/itemList");
		
		return mav;
	}*/
	
	@RequestMapping("/queryItemById.do")
	public String queryItemById(Model model,Integer id) {
		
		Item i = itemService.queryItemById(id);
		
		model.addAttribute("item", i);
		
		return "/item/itemEdit";
	}
	
	@RequestMapping("/updateItem.do")
	public String updateItem(Item item) {
		
		try {
			itemService.updateItem(item);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "/common/failure";
		}
		
		return "/common/success";
	}
	
	@RequestMapping("/queryItem.do")
	public ModelAndView queryItem(QueryVo vo,Integer[] ids) {
		
		ModelAndView mav = new ModelAndView();
		
		List<Item> itemList = itemService.queryAllItem();
		
		mav.addObject("itemList", itemList);
		
		mav.setViewName("/item/itemList");
		
		return mav;
	}
}
