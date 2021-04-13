package com.sync.core.element;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class DoubleColumn extends Column {

	public DoubleColumn(final String columnName,final String data) {
		this(columnName,data, null == data ? 0 : data.length());
		this.validate(data);
	}

	public DoubleColumn(final String columnName,Long data) {
		this(columnName,data == null ? (String) null : String.valueOf(data));
	}

	public DoubleColumn(final String columnName,Integer data) {
		this(columnName,data == null ? (String) null : String.valueOf(data));
	}

	/**
	 * Double无法表示准确的小数数据，我们不推荐使用该方法保存Double数据，建议使用String作为构造入参
	 * 
	 * */
	public DoubleColumn(final String columnName,final Double data) {
		this(columnName,data == null ? (String) null
				: new BigDecimal(String.valueOf(data)).toPlainString());
	}

	/**
	 * Float无法表示准确的小数数据，我们不推荐使用该方法保存Float数据，建议使用String作为构造入参
	 * 
	 * */
	public DoubleColumn(final String columnName,final Float data) {
		this(columnName,data == null ? (String) null
				: new BigDecimal(String.valueOf(data)).toPlainString());
	}

	public DoubleColumn(final String columnName,final BigDecimal data) {
		this(columnName,null == data ? (String) null : data.toPlainString());
	}

	public DoubleColumn(final String columnName,final BigInteger data) {
		this(columnName,null == data ? (String) null : data.toString());
	}

	public DoubleColumn(final String columnName) {
		this(columnName,(String) null);
	}

	private DoubleColumn(final String columnName,final String data, int byteSize) {
		super(columnName,data, Type.DOUBLE, byteSize);
	}

	@Override
	public BigDecimal asBigDecimal() {
		if (null == this.getRawData()) {
			return null;
		}

		try {
			return new BigDecimal((String) this.getRawData());
		} catch (NumberFormatException e) {
			throw new RuntimeException(String.format("String[%s] 无法转换为Double类型 .", this.getRawData()));
		}
	}

	@Override
	public Double asDouble() {
		if (null == this.getRawData()) {
			return null;
		}

		String string = (String) this.getRawData();

		boolean isDoubleSpecific = string.equals("NaN")
				|| string.equals("-Infinity") || string.equals("+Infinity");
		if (isDoubleSpecific) {
			return Double.valueOf(string);
		}

		BigDecimal result = this.asBigDecimal();
		OverFlowUtil.validateDoubleNotOverFlow(result);

		return result.doubleValue();
	}

	@Override
	public Long asLong() {
		if (null == this.getRawData()) {
			return null;
		}

		BigDecimal result = this.asBigDecimal();
		OverFlowUtil.validateLongNotOverFlow(result.toBigInteger());

		return result.longValue();
	}

	@Override
	public BigInteger asBigInteger() {
		if (null == this.getRawData()) {
			return null;
		}

		return this.asBigDecimal().toBigInteger();
	}

	@Override
	public String asString() {
		if (null == this.getRawData()) {
			return null;
		}
		return (String) this.getRawData();
	}

	@Override
	public Boolean asBoolean() {
		throw new RuntimeException( "Double类型无法转为Bool .");
	}

	@Override
	public Date asDate() {
		throw new RuntimeException( "Double类型无法转为Date类型 .");
	}

	@Override
	public byte[] asBytes() {
		throw new RuntimeException( "Double类型无法转为Bytes类型 .");
	}

	private void validate(final String data) {
		if (null == data) {
			return;
		}

		if (data.equalsIgnoreCase("NaN") || data.equalsIgnoreCase("-Infinity")
				|| data.equalsIgnoreCase("Infinity")) {
			return;
		}

		try {
			new BigDecimal(data);
		} catch (Exception e) {
			throw new RuntimeException(String.format("String[%s]无法转为Double类型 .", data));
		}
	}

}