package com.impressiveinteractive.checkmark;

import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedDeclaration")
abstract class TestAbstractClass implements TestInterface {
	private boolean booleanPrimitiveValue;
	private byte bytePrimitiveValue;
	private char charPrimitiveValue;
	private short shortPrimitiveValue;
	private int intPrimitiveValue;
	private long longPrimitiveValue;
	private float floatPrimitiveValue;
	private double doublePrimitiveValue;

	private List<String> list;
	private Map<Integer, String> map;

	private Object objectValue;
	private String stringValue;

	private Boolean booleanValue;
	private Byte byteValue;
	private Character characterValue;
	private Short shortValue;
	private Integer integerValue;
	private Long longValue;
	private Float floatValue;
	private Double doubleValue;

	private Double[] doubleArrayValue;

	private TestInterface testInterfaceValue;
	private TestAbstractClass testAbstractClassValue;
	private TestClass testClassValue;
	private TestEnum testEnumValue;

	private final int finalPrimitiveInt = 15;
	private final Integer finalInt = 15;

	@Override
	public boolean isBooleanPrimitiveValue() {
		return booleanPrimitiveValue;
	}

	@Override
	public void setBooleanPrimitiveValue(boolean booleanPrimitiveValue) {
		this.booleanPrimitiveValue = booleanPrimitiveValue;
	}

	@Override
	public byte getBytePrimitiveValue() {
		return bytePrimitiveValue;
	}

	@Override
	public void setBytePrimitiveValue(byte bytePrimitiveValue) {
		this.bytePrimitiveValue = bytePrimitiveValue;
	}

	@Override
	public char getCharPrimitiveValue() {
		return charPrimitiveValue;
	}

	@Override
	public void setCharPrimitiveValue(char charPrimitiveValue) {
		this.charPrimitiveValue = charPrimitiveValue;
	}

	@Override
	public short getShortPrimitiveValue() {
		return shortPrimitiveValue;
	}

	@Override
	public void setShortPrimitiveValue(short shortPrimitiveValue) {
		this.shortPrimitiveValue = shortPrimitiveValue;
	}

	@Override
	public int getIntPrimitiveValue() {
		return intPrimitiveValue;
	}

	@Override
	public void setIntPrimitiveValue(int intPrimitiveValue) {
		this.intPrimitiveValue = intPrimitiveValue;
	}

	@Override
	public long getLongPrimitiveValue() {
		return longPrimitiveValue;
	}

	@Override
	public void setLongPrimitiveValue(long longPrimitiveValue) {
		this.longPrimitiveValue = longPrimitiveValue;
	}

	@Override
	public float getFloatPrimitiveValue() {
		return floatPrimitiveValue;
	}

	@Override
	public void setFloatPrimitiveValue(float floatPrimitiveValue) {
		this.floatPrimitiveValue = floatPrimitiveValue;
	}

	@Override
	public double getDoublePrimitiveValue() {
		return doublePrimitiveValue;
	}

	@Override
	public void setDoublePrimitiveValue(double doublePrimitiveValue) {
		this.doublePrimitiveValue = doublePrimitiveValue;
	}

	@Override
	public List<String> getList() {
		return list;
	}

	@Override
	public void setList(List<String> list) {
		this.list = list;
	}

	@Override
	public Map<Integer, String> getMap() {
		return map;
	}

	@Override
	public void setMap(Map<Integer, String> map) {
		this.map = map;
	}

	@Override
	public Object getObjectValue() {
		return objectValue;
	}

	@Override
	public void setObjectValue(Object objectValue) {
		this.objectValue = objectValue;
	}

	@Override
	public String getStringValue() {
		return stringValue;
	}

	@Override
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	@Override
	public Boolean getBooleanValue() {
		return booleanValue;
	}

	@Override
	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	@Override
	public Byte getByteValue() {
		return byteValue;
	}

	@Override
	public void setByteValue(Byte byteValue) {
		this.byteValue = byteValue;
	}

	@Override
	public Character getCharacterValue() {
		return characterValue;
	}

	@Override
	public void setCharacterValue(Character characterValue) {
		this.characterValue = characterValue;
	}

	@Override
	public Short getShortValue() {
		return shortValue;
	}

	@Override
	public void setShortValue(Short shortValue) {
		this.shortValue = shortValue;
	}

	@Override
	public Integer getIntegerValue() {
		return integerValue;
	}

	@Override
	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	@Override
	public Long getLongValue() {
		return longValue;
	}

	@Override
	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	@Override
	public Float getFloatValue() {
		return floatValue;
	}

	@Override
	public void setFloatValue(Float floatValue) {
		this.floatValue = floatValue;
	}

	@Override
	public Double getDoubleValue() {
		return doubleValue;
	}

	@Override
	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	@Override
	public Double[] getDoubleArrayValue() {
		return doubleArrayValue;
	}

	@Override
	public void setDoubleArrayValue(Double[] doubleArrayValue) {
		this.doubleArrayValue = doubleArrayValue;
	}

	@Override
	public TestInterface getTestInterfaceValue() {
		return testInterfaceValue;
	}

	@Override
	public void setTestInterfaceValue(TestInterface testInterfaceValue) {
		this.testInterfaceValue = testInterfaceValue;
	}

	@Override
	public TestAbstractClass getTestAbstractClassValue() {
		return testAbstractClassValue;
	}

	@Override
	public void setTestAbstractClassValue(TestAbstractClass testAbstractClassValue) {
		this.testAbstractClassValue = testAbstractClassValue;
	}

	@Override
	public TestClass getTestClassValue() {
		return testClassValue;
	}

	@Override
	public void setTestClassValue(TestClass testClassValue) {
		this.testClassValue = testClassValue;
	}

	@Override
	public TestEnum getTestEnumValue() {
		return testEnumValue;
	}

	@Override
	public void setTestEnumValue(TestEnum testEnumValue) {
		this.testEnumValue = testEnumValue;
	}

	public int getFinalPrimitiveInt() {
		return finalPrimitiveInt;
	}

	public Integer getFinalInt() {
		return finalInt;
	}
}
