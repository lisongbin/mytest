package cn.itheima.ssm.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itheima.ssm.entity.Item;
import cn.itheima.ssm.mapper.ItemMapper;
import cn.itheima.ssm.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	
	public List<Item> queryAllItem() {
		
		List<Item> list = itemMapper.selectByExample(null);
		
		return list;
	}

	public Item queryItemById(int id) {

		Item item = itemMapper.selectByPrimaryKey(id);
		
		return item;
	}

	public void updateItem(Item item) {
		itemMapper.updateByPrimaryKeySelective(item);
		
	}

}
