package com.impressiveinteractive.checkmark;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CheckMarkTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDumbMock() throws Exception {
        assertThat(CheckMark.dumbMock(Object.class), is(instanceOf(Object.class)));
        assertThat(CheckMark.dumbMock(String.class), is(instanceOf(String.class)));

        assertThat(CheckMark.dumbMock(Boolean.class), is(instanceOf(Boolean.class)));
        assertThat(CheckMark.dumbMock(Byte.class), is(instanceOf(Byte.class)));
        assertThat(CheckMark.dumbMock(Character.class), is(instanceOf(Character.class)));
        assertThat(CheckMark.dumbMock(Short.class), is(instanceOf(Short.class)));
        assertThat(CheckMark.dumbMock(Integer.class), is(instanceOf(Integer.class)));
        assertThat(CheckMark.dumbMock(Long.class), is(instanceOf(Long.class)));
        assertThat(CheckMark.dumbMock(Float.class), is(instanceOf(Float.class)));
        assertThat(CheckMark.dumbMock(Double.class), is(instanceOf(Double.class)));

        assertThat(CheckMark.dumbMock(boolean.class), is(false));
        assertThat(CheckMark.dumbMock(byte.class), is((byte) 0));
        assertThat(CheckMark.dumbMock(char.class), is('\u0000'));
        assertThat(CheckMark.dumbMock(short.class), is((short) 0));
        assertThat(CheckMark.dumbMock(int.class), is(0));
        assertThat(CheckMark.dumbMock(long.class), is(0l));
        assertThat(CheckMark.dumbMock(float.class), is(0f));
        assertThat(CheckMark.dumbMock(double.class), is(0.0));

        assertThat(CheckMark.dumbMock(Double[].class), is(emptyArray()));

        assertThat(CheckMark.dumbMock(TestInterface.class), is(instanceOf(TestInterface.class)));
        assertThat(CheckMark.dumbMock(TestAbstractClass.class), is(instanceOf(TestAbstractClass.class)));
        assertThat(CheckMark.dumbMock(TestClass.class), is(instanceOf(TestClass.class)));
        assertThat(CheckMark.dumbMock(TestEnum.class), is(instanceOf(TestEnum.class)));
    }

    @Test
    public void testCreateInstance() throws Exception {
        assertThat(CheckMark.createInstance(Object.class), is(instanceOf(Object.class)));
        assertThat(CheckMark.createInstance(String.class), is(equalTo("")));

        assertThat(CheckMark.createInstance(Boolean.class), is(false));
        assertThat(CheckMark.createInstance(Byte.class), is((byte) 0));
        assertThat(CheckMark.createInstance(Character.class), is('\u0000'));
        assertThat(CheckMark.createInstance(Short.class), is((short) 0));
        assertThat(CheckMark.createInstance(Integer.class), is(0));
        assertThat(CheckMark.createInstance(Long.class), is(0l));
        assertThat(CheckMark.createInstance(Float.class), is(0f));
        assertThat(CheckMark.createInstance(Double.class), is(0.0));

        assertThat(CheckMark.createInstance(TestClass.class), is(instanceOf(TestClass.class)));
    }

    @Test
    public void testCreateInstanceInterface() throws Exception {
        exception.expect(IllegalArgumentException.class);

        CheckMark.createInstance(TestInterface.class);
    }

    @Test
    public void testCreateInstanceAbstractClass() throws Exception {
        exception.expect(IllegalArgumentException.class);

        CheckMark.createInstance(TestAbstractClass.class);
    }

    @Test
    public void testCreateInstanceEnum() throws Exception {
        exception.expect(UnsupportedOperationException.class);

        CheckMark.createInstance(TestEnum.class);
    }

    @Test
    public void testAccessorsAndMutators() throws Exception {
        CheckMark.testAccessorsAndMutators(TestClass.class);
        assertThat(CheckMark.createInstance(TestClass.class).getNumber(), is(10));
    }
}
