
/**   
* @Title: DateConverter.java 
* @Package cn.itheima.ssm.converter 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 传智 小杨老师 
* @date 2018-6-27 下午12:57:29 
* @version V1.0   
*/
package cn.itheima.ssm.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/** 
 * @ClassName: DateConverter 
 * @Description: 自定义日期转换器
 * @author 传智 小杨老师  
 * @date 2018-6-27 下午12:57:29 
 * 
 * Converter<S, T> 
 * 
 * S,Source，源，转换之前的数据，这里是字符串类型的商品日期
 * T,Target，目标，转换之后的数据，这里是Date类型的商品日期
 *  
 */
public class DateConverter implements Converter<String, Date> {

	/**
	 * 实现转换逻辑的方法
	 * 2016-02-03 13:22:53
	 */
	public Date convert(String source) {
		// 1.日期格式化对象
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			// 转换成功，直接返回
			return format.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// 转换失败，返回null
		return null;
	}

}
