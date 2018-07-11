package cn.itheima.ssm.service;

import java.util.List;

import cn.itheima.ssm.entity.Item;

public interface ItemService {
	List<Item> queryAllItem();

	Item queryItemById(int parseInt);
	
	void updateItem(Item item);
}
