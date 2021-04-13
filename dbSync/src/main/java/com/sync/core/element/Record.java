package com.sync.core.element;

import java.util.Map;

/**
 * @Description: 行数据，存储所有行字段名称，类型等
 * @Author: Yan XinYu
 * @Date: 2021-03-03 20:44
 */
public interface Record {

	boolean isTerminate();

	void terminate(boolean terminate);

	void addColumn(Column column);

	/**
	 * 获取字段
	 * @param columnName
	 * @return
	 */
	Column getColumn(String columnName);

	@Override
	String toString();

	/**
	 * 获取字段数量
	 * @return
	 */
	int getColumnNumber();

	/**
	 * 获取数据大小
	 * @return int 单条记录内存大小
	 */
	int getByteSize();

	/**
	 * 内存大小包含class本身占用内存
	 * @return
	 */
	int getMemorySize();

	/**
	 * 映射表字段
	 * @param mapping
	 */
	void mapping(Map<String,String> mapping);

}
