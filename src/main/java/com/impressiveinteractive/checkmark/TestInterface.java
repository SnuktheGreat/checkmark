package com.impressiveinteractive.checkmark;

import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedDeclaration")
interface TestInterface {
	int getNumber();

	boolean isBooleanPrimitiveValue();

	void setBooleanPrimitiveValue(boolean booleanPrimitiveValue);

	byte getBytePrimitiveValue();

	void setBytePrimitiveValue(byte bytePrimitiveValue);

	char getCharPrimitiveValue();

	void setCharPrimitiveValue(char charPrimitiveValue);

	short getShortPrimitiveValue();

	void setShortPrimitiveValue(short shortPrimitiveValue);

	int getIntPrimitiveValue();

	void setIntPrimitiveValue(int intPrimitiveValue);

	long getLongPrimitiveValue();

	void setLongPrimitiveValue(long longPrimitiveValue);

	float getFloatPrimitiveValue();

	void setFloatPrimitiveValue(float floatPrimitiveValue);

	double getDoublePrimitiveValue();

	void setDoublePrimitiveValue(double doublePrimitiveValue);

	List<String> getList();

	void setList(List<String> list);

	Map<Integer, String> getMap();

	void setMap(Map<Integer, String> map);

	Object getObjectValue();

	void setObjectValue(Object objectValue);

	String getStringValue();

	void setStringValue(String stringValue);

	Boolean getBooleanValue();

	void setBooleanValue(Boolean booleanValue);

	Byte getByteValue();

	void setByteValue(Byte byteValue);

	Character getCharacterValue();

	void setCharacterValue(Character characterValue);

	Short getShortValue();

	void setShortValue(Short shortValue);

	Integer getIntegerValue();

	void setIntegerValue(Integer integerValue);

	Long getLongValue();

	void setLongValue(Long longValue);

	Float getFloatValue();

	void setFloatValue(Float floatValue);

	Double getDoubleValue();

	void setDoubleValue(Double doubleValue);

	Double[] getDoubleArrayValue();

	void setDoubleArrayValue(Double[] doubleArrayValue);

	TestInterface getTestInterfaceValue();

	void setTestInterfaceValue(TestInterface testInterfaceValue);

	TestAbstractClass getTestAbstractClassValue();

	void setTestAbstractClassValue(TestAbstractClass testAbstractClassValue);

	TestClass getTestClassValue();

	void setTestClassValue(TestClass testClassValue);

	TestEnum getTestEnumValue();

	void setTestEnumValue(TestEnum testEnumValue);
}
