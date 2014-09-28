package com.impressiveinteractive.checkmark;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("UnusedDeclaration")
public class CheckMarkTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /*
     * Accessors
     */
    @Test
    public void testAccessors() throws Exception {
        CheckMark.testAccessors(AccessorClass.class);
    }

    public static class AccessorClass {
        private String accessible;

        public String getAccessible() {
            return accessible;
        }
    }

    @Test
    public void testAccessorsException() throws Exception {
        exception.expect(AssertionError.class);
        CheckMark.testAccessors(AccessorExceptionClass.class);
    }

    public static class AccessorExceptionClass {
        private String accessible;

        public String getAccessible() {
            throw new RuntimeException("boo");
        }
    }

    @Test
    public void testAccessorsIdentityFail() throws Exception {
        exception.expect(AssertionError.class);
        CheckMark.testAccessors(AccessorBrokenClass.class);
    }

    public static class AccessorBrokenClass {
        private String accessible;

        public String getAccessible() {
            return "";
        }
    }

    @Test
    public void testPrimitiveAccessors() throws Exception {
        CheckMark.testAccessors(PrimitiveAccessorClass.class);
    }

    public static class PrimitiveAccessorClass {
        private int accessible;

        public int getAccessible() {
            return accessible;
        }
    }

    @Test
    public void testPrimitiveAccessorsValueFail() throws Exception {
        exception.expect(AssertionError.class);
        CheckMark.testAccessors(PrimitiveAccessorBrokenClass.class);
    }

    public static class PrimitiveAccessorBrokenClass {
        private int accessible;

        public int getAccessible() {
            return 0;
        }
    }

    @Test
    public void testFinalAccessor() throws Exception {
        CheckMark.testAccessors(FinalAccessorClass.class);
    }

    public static class FinalAccessorClass {
        private final int accessible = 10;

        public int getAccessible() {
            return accessible;
        }
    }

    @Test
    @Ignore("It is impossible to see fault in the accessor when the returned primitive types have the same value, but" +
            " are set differently. See the test code for clarification.")
    public void testFinalAccessorFail() throws Exception {
        exception.expect(AssertionError.class);
        CheckMark.testAccessors(FinalAccessorClassFail.class);
    }

    public static class FinalAccessorClassFail {
        private final int accessible = 10;

        public int getAccessible() {
            return 10;
        }
    }

    @Test
    public void testSubAccessor() throws Exception {
        CheckMark.testAccessors(SubAccessorClass.class);
    }

    public static class SubAccessorClass extends AccessorClass {
        // No contents of its own.
    }

    /*
     * Mutators
     */
    @Test
    public void testMutators() throws Exception {
        CheckMark.testMutators(MutatorClass.class);
    }

    public static class MutatorClass {
        private String accessible;

        public void setAccessible(String accessible) {
            this.accessible = accessible;
        }
    }

    @Test
    public void testMutatorsException() throws Exception {
        exception.expect(AssertionError.class);
        CheckMark.testMutators(MutatorExceptionClass.class);
    }

    public static class MutatorExceptionClass {
        private String accessible;

        public void setAccessible(String accessible) {
            throw new RuntimeException("boo");
        }
    }

    @Test
    public void testMutatorsIdentityFail() throws Exception {
        exception.expect(AssertionError.class);
        CheckMark.testMutators(MutatorBrokenClass.class);
    }

    public static class MutatorBrokenClass {
        private String accessible;

        public void setAccessible(String accessible) {
            this.accessible = "";
        }
    }

    @Test
    public void testPrimitiveMutators() throws Exception {
        CheckMark.testMutators(PrimitiveMutatorClass.class);
    }

    public static class PrimitiveMutatorClass {
        private int accessible;

        public void setAccessible(int accessible) {
            this.accessible = accessible;
        }
    }

    @Test
    public void testPrimitiveMutatorsValueFail() throws Exception {
        exception.expect(AssertionError.class);
        CheckMark.testMutators(PrimitiveMutatorBrokenClass.class);
    }

    public static class PrimitiveMutatorBrokenClass {
        private int accessible;

        public void setAccessible(int accessible) {
            this.accessible = 0;
        }
    }

    @Test
    public void testSubMutator() throws Exception {
        CheckMark.testMutators(SubMutatorClass.class);
    }

    public static class SubMutatorClass extends MutatorClass {
        // No contents of its own.
    }

    /*
     * Accessors and mutators
     */
    @Test
    public void testAccessorsAndMutators() throws Exception {
        CheckMark.testAccessorsAndMutators(AccessorAndMutatorClass.class);
    }

    public static class AccessorAndMutatorClass {
        private String accessible;

        public String getAccessible() {
            return accessible;
        }

        public void setAccessible(String accessible) {
            this.accessible = accessible;
        }
    }
}
