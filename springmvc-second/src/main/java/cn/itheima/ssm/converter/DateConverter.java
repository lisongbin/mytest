
/**   
* @Title: DateConverter.java 
* @Package cn.itheima.ssm.converter 
* @Description: TODO(��һ�仰�������ļ���ʲô) 
* @author ���� С����ʦ 
* @date 2018-6-27 ����12:57:29 
* @version V1.0   
*/
package cn.itheima.ssm.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/** 
 * @ClassName: DateConverter 
 * @Description: �Զ�������ת����
 * @author ���� С����ʦ  
 * @date 2018-6-27 ����12:57:29 
 * 
 * Converter<S, T> 
 * 
 * S,Source��Դ��ת��֮ǰ�����ݣ��������ַ������͵���Ʒ����
 * T,Target��Ŀ�꣬ת��֮������ݣ�������Date���͵���Ʒ����
 *  
 */
public class DateConverter implements Converter<String, Date> {

	/**
	 * ʵ��ת���߼��ķ���
	 * 2016-02-03 13:22:53
	 */
	public Date convert(String source) {
		// 1.���ڸ�ʽ������
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			// ת���ɹ���ֱ�ӷ���
			return format.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// ת��ʧ�ܣ�����null
		return null;
	}

}
