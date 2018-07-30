package net.dgg.framework.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 通过JSON序列化实现深度转换Bean<->Bean的Mapper
 * 
 * @author dingtao
 */
public class BeanMapper {

	/**
	 * 
	 */
	public static <T> T map(Object source, Class<T> destinationClass) {
		String json = JsonUtils.obj2Json(source);
		return JsonUtils.json2Obj(json, destinationClass);
	}

	/**
	 *  
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> mapList(Collection sourceList,
			Class<T> destinationClass) {

		if (CollectionUtils.isEmpty(sourceList)) {
			return new ArrayList<T>();
		}

		String json = JsonUtils.obj2Json(sourceList);
		List<T> result = JsonUtils.json2List(json, destinationClass);

		return result;
	}

	/***
	 * 
	 * @param sourceList
	 *            源数据
	 * @param listClass
	 *            接口
	 * @param destinationClass
	 *            实现类
	 * @return ***************************************************************
	 */
	public static <T, TIMP extends T> List<T> mapList(Collection<?> sourceList,
			Class<T> listClass, Class<TIMP> destinationClass) {

		if (CollectionUtils.isEmpty(sourceList)) {
			return new ArrayList<T>();
		}

		List<T> result = new ArrayList<T>();
		List<TIMP> list = mapList(sourceList, destinationClass);
		for (TIMP baseDTO : list) {
			result.add(baseDTO);
		}
		return result;
	}

}