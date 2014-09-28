package com.impressiveinteractive.checkmark;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ReflectionTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetField() throws Exception {
        Field expected = Super.class.getDeclaredField("stringy");
        assertThat(Reflection.getField(Super.class, "stringy"), is(expected));
        assertThat(Reflection.getField(Derived.class, "stringy"), is(expected));
    }

    @Test
    public void testGetFieldNoSuchField() throws Exception {
        exception.expect(NoSuchFieldException.class);

        Reflection.getField(Other.class, "stringy");
    }

    @SuppressWarnings("UnusedDeclaration")
    public class Super {
        private String stringy;
    }

    public class Derived extends Super {
        // Nothing new
    }

    @SuppressWarnings("UnusedDeclaration")
    public class Other {
        private String notStringy;
    }
}
