package com.sync.core.element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.sync.core.utils.ClassSize;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 默认行数据
 * @Author: Yan XinYu
 * @Date: 2021-03-06 14:21
 */
public class Record {

	private static final int RECORD_AVERAGE_COLUMN_NUMBER = 16;

	private final Map<String,Column> columns;

	private int byteSize;

	private volatile boolean isTerminate = false;

	/**
	 * 首先是Record本身需要的内存
 	 */
	private int memorySize = ClassSize.DefaultRecordHead;

	public Record() {
		this.columns = new HashMap<>(RECORD_AVERAGE_COLUMN_NUMBER);
	}

	public Map<String, Column> getColumns() {
		return columns;
	}

	public boolean isTerminate() {
		return isTerminate;
	}

	public void terminate(boolean terminate) {
		this.isTerminate = terminate;
	}

	public void addColumn(Column column) {
		columns.put(column.getColumnName(),column);
		incrByteSize(column);
	}

	public Column getColumn(String columnName) {
		return columns.get(columnName);
	}

	@Override
	public String toString() {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("size", this.getColumnNumber());
		json.put("data", this.columns.values());
		return JSON.toJSONString(json);
	}

	public int getColumnNumber() {
		return this.columns.size();
	}

	public int getByteSize() {
		return byteSize;
	}

	public int getMemorySize(){
		return memorySize;
	}

	public void mapping(Map<String, String> mapping) {
		if(mapping == null || mapping.isEmpty()){return;}
		for (Map.Entry<String, String> entry : mapping.entrySet()) {
			if(columns.containsKey(entry.getKey())){
				Column column = columns.get(entry.getKey());
				column.setColumnName(entry.getValue());
				columns.put(entry.getValue(),columns.remove(entry.getKey()));
			}
		}
	}

	/**
	 * 减少内存
	 * @param column 字段
	 */
	private void decrByteSize(final Column column) {
		if (null == column) {
			return;
		}

		byteSize -= column.getByteSize();

		//内存的占用是column对象的头 再加实际大小
		memorySize = memorySize -  ClassSize.ColumnHead - column.getByteSize();
	}

	/**
	 * 增加内存
	 * @param column 字段
	 */
	private void incrByteSize(final Column column) {
		if (null == column) {
			return;
		}

		byteSize += column.getByteSize();

		//内存的占用是column对象的头 再加实际大小
		memorySize = memorySize + ClassSize.ColumnHead + column.getByteSize();
	}

}
