package com.impressiveinteractive.checkmark;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CheckMarkTest {
    @Test
    public void testAccessorsAndMutators() throws Exception {
        CheckMark.testAccessorsAndMutators(TestClass.class);
        assertThat(Reflection.createInstance(TestClass.class).getNumber(), is(10));
    }
}
