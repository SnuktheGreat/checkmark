package com.impressiveinteractive.checkmark;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("UnusedDeclaration")
public class ReflectionTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDumbMock() throws Exception {
        assertThat(Reflection.dumbMock(Object.class), is(instanceOf(Object.class)));
        assertThat(Reflection.dumbMock(String.class), is(instanceOf(String.class)));

        assertThat(Reflection.dumbMock(Boolean.class), is(instanceOf(Boolean.class)));
        assertThat(Reflection.dumbMock(Byte.class), is(instanceOf(Byte.class)));
        assertThat(Reflection.dumbMock(Character.class), is(instanceOf(Character.class)));
        assertThat(Reflection.dumbMock(Short.class), is(instanceOf(Short.class)));
        assertThat(Reflection.dumbMock(Integer.class), is(instanceOf(Integer.class)));
        assertThat(Reflection.dumbMock(Long.class), is(instanceOf(Long.class)));
        assertThat(Reflection.dumbMock(Float.class), is(instanceOf(Float.class)));
        assertThat(Reflection.dumbMock(Double.class), is(instanceOf(Double.class)));

        assertThat(Reflection.dumbMock(boolean.class), is(true));
        assertThat(Reflection.dumbMock(byte.class), is((byte) 1995));
        assertThat(Reflection.dumbMock(char.class), is('\u1995'));
        assertThat(Reflection.dumbMock(short.class), is((short) 1995));
        assertThat(Reflection.dumbMock(int.class), is(1995));
        assertThat(Reflection.dumbMock(long.class), is(1995l));
        assertThat(Reflection.dumbMock(float.class), is(1995f));
        assertThat(Reflection.dumbMock(double.class), is(1995.0));

        assertThat(Reflection.dumbMock(Double[].class), is(emptyArray()));

        assertThat(Reflection.dumbMock(TestInterface.class), is(instanceOf(TestInterface.class)));
        assertThat(Reflection.dumbMock(TestAbstractClass.class), is(instanceOf(TestAbstractClass.class)));
        assertThat(Reflection.dumbMock(TestImplementingClass.class), is(instanceOf(TestImplementingClass.class)));
        assertThat(Reflection.dumbMock(TestEnum.class), is(instanceOf(TestEnum.class)));
    }

    @Test
    public void testCreateInstance() throws Exception {
        assertThat(Reflection.createInstance(Object.class), is(instanceOf(Object.class)));
        assertThat(Reflection.createInstance(String.class), is(equalTo("")));

        assertThat(Reflection.createInstance(Boolean.class), is(true));
        assertThat(Reflection.createInstance(Byte.class), is((byte) 1995));
        assertThat(Reflection.createInstance(Character.class), is('\u1995'));
        assertThat(Reflection.createInstance(Short.class), is((short) 1995));
        assertThat(Reflection.createInstance(Integer.class), is(1995));
        assertThat(Reflection.createInstance(Long.class), is(1995l));
        assertThat(Reflection.createInstance(Float.class), is(1995f));
        assertThat(Reflection.createInstance(Double.class), is(1995.0));

        assertThat(Reflection.createInstance(TestImplementingClass.class), is(instanceOf(TestImplementingClass.class)));
    }

    @Test
    public void testCreateInstanceInterface() throws Exception {
        exception.expect(IllegalArgumentException.class);

        Reflection.createInstance(TestInterface.class);
    }

    @Test
    public void testCreateInstanceAbstractClass() throws Exception {
        exception.expect(IllegalArgumentException.class);

        Reflection.createInstance(TestAbstractClass.class);
    }

    @Test
    public void testCreateInstanceEnum() throws Exception {
        exception.expect(UnsupportedOperationException.class);

        Reflection.createInstance(TestEnum.class);
    }

    @Test
    public void testGetField() throws Exception {
        Field expected = TestAbstractClass.class.getDeclaredField("stringValue");
        assertThat(Reflection.getField(TestAbstractClass.class, "stringValue"), is(expected));
        assertThat(Reflection.getField(TestImplementingClass.class, "stringValue"), is(expected));
    }

    @Test
    public void testGetFieldNoSuchField() throws Exception {
        exception.expect(NoSuchFieldException.class);

        Reflection.getField(Other.class, "stringy");
    }

    public static interface TestInterface {
        String getStringValue();

        void setStringValue(String stringValue);
    }

    public static abstract class TestAbstractClass implements TestInterface {
        private String stringValue;

        @Override
        public String getStringValue() {
            return stringValue;
        }

        @Override
        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }
    }

    public class TestImplementingClass extends TestAbstractClass {
        // Nothing new
    }

    public static enum TestEnum {
        TRUE, FALSE, MAYBE
    }

    public class Other {
        private int intValue;
    }
}
