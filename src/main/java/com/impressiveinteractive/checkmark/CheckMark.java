package com.impressiveinteractive.checkmark;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Test common pieces of code like accessors, mutators, equals and hashCode.
 */
public final class CheckMark {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckMark.class);
	private static final int SEED_ONE = 1337;
    private static final int SEED_TWO = 1338;

    private static final String ACCESSOR_FAIL_MESSAGE = "Accessor to field %s.%s does not work.";
    private static final String ACCESSOR_EXCEPTION_MESSAGE = "Accessor to field %s.%s threw an exception.";
    private static final String MUTATOR_FAIL_MESSAGE = "Mutator to field %s.%s does not work.";
    private static final String MUTATOR_EXCEPTION_MESSAGE = "Mutator to field %s.%s threw an exception.";
    private static final String NO_GETTER_MESSAGE =
            "Ignoring getter for property \"{}\" since its field can not be found. Is this a getter?";
    private static final String EQUAL_ON_NULL_MESSAGE = "Equals for %s returns true with a null input.";
    private static final String EQUAL_ON_NEW_OBJECT_MESSAGE = "Equals for %s returns true with a new Object() input.";
    private static final String SAME_INSTANCE_NOT_EQUAL_MESSAGE =
            "Equals returns false when comparing same instance of %s.\n" +
                    "Instance:\n%s";
    private static final String EXACT_COPY_NOT_EQUAL_MESSAGE =
            "Equals returns false on two exact copies of %s.\n" +
                    "Instance A:\n%s\n" +
                    "Instance B:\n%s";
    private static final String FIELD_NOT_USED_IN_EQUALS_MESSAGE = "Field %s is not used in equals.";
    private static final String EXACT_COPY_HASH_CODE_NOT_EQUAL_MESSAGE = "HashCode returns a different result on two exact copies of %s.\n" +
            "HashCode instance A:\n%s\n" +
            "HashCode instance B:\n%s";

    private CheckMark() {
        throw new AssertionError("Private constructor called");
    }

	public static <T> MockTester<T> test(Class<T> cls){
		return new MockTester<>(cls);
	}


	/**
     * Test all the accessors and mutators for the given class. An instance of the given class will be created and
     * accessor and mutator information will be scanned for using the {@link Introspector}. These methods will then be
     * called while reflection is used to make sure they <em>get</em> or <em>set</em> as expected.
     *
     * @param cls The {@link Class} to test.
     * @throws IntrospectionException       Thrown when bean information could not be recovered from the given class.
     * @throws ReflectiveOperationException Thrown when one or more fields could not be tested using reflection.
     */
    public static void testAccessorsAndMutators(Class<?> cls)
            throws IntrospectionException, ReflectiveOperationException {
        testAccessorsAndMutators(Reflection.createInstance(cls));
    }

    /**
     * Test all the accessors and mutators for the given instance. Accessor and mutator information will be scanned for
     * using the {@link Introspector}. These methods will then be called while reflection is used to make sure they
     * <em>get</em> or <em>set</em> as expected.
     *
     * @param instance The instance to test.
     * @throws IntrospectionException       Thrown when bean information could not be recovered from the given class.
     * @throws ReflectiveOperationException Thrown when one or more fields could not be tested using reflection.
     */
    public static void testAccessorsAndMutators(Object instance)
            throws IntrospectionException, ReflectiveOperationException {
        testAccessors(instance);
        testMutators(instance);
    }

    /**
     * Test all the accessors for the given class. An instance of the given class will be created and accessor
     * information will be scanned for using the {@link Introspector}. These methods will then be called while
     * reflection is used to make sure they <em>get</em> as expected.
     *
     * @param cls The {@link Class} to test.
     * @throws IntrospectionException       Thrown when bean information could not be recovered from the given class.
     * @throws ReflectiveOperationException Thrown when one or more fields could not be tested using reflection.
     */
    public static void testAccessors(Class<?> cls) throws IntrospectionException, ReflectiveOperationException {
        testAccessors(Reflection.createInstance(cls));
    }

    /**
     * Test all the accessors for the given instance. Accessor information will be scanned for using the
     * {@link Introspector}. These methods will then be called while reflection is used to make sure they <em>get</em>
     * as expected.
     *
     * @param instance The instance to test.
     * @throws IntrospectionException       Thrown when bean information could not be recovered from the given class.
     * @throws ReflectiveOperationException Thrown when one or more fields could not be tested using reflection.
     */
    public static void testAccessors(Object instance) throws IntrospectionException, ReflectiveOperationException {
        Class<?> cls = instance.getClass();
        PropertyDescriptor[] descriptors =
                Introspector.getBeanInfo(cls, Object.class).getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            Method readMethod = descriptor.getReadMethod();
            if (readMethod != null) {
                Object mock = Reflection.dumbMock(descriptor.getPropertyType());
                String name = descriptor.getName();

                try {
                    Field field = Reflection.getField(cls, name);
                    field.setAccessible(true);
                    if (Modifier.isFinal(field.getModifiers())) {
                        mock = field.get(instance);
                    } else {
                        field.set(instance, mock);
                    }
                    if (!checkReadMethod(instance, readMethod, mock)) {
                        throw new AssertionError(String.format(ACCESSOR_FAIL_MESSAGE, cls.getCanonicalName(), name));
                    }
                } catch (NoSuchFieldException e) {
                    LOGGER.warn(NO_GETTER_MESSAGE, name);
                } catch (Exception e) {
                    throw new AssertionError(String.format(ACCESSOR_EXCEPTION_MESSAGE, cls.getCanonicalName(), name), e);
                }
            }
        }
    }

    /**
     * Test all the mutators for the given class. An instance of the given class will be created and mutator information
     * will be scanned for using the {@link Introspector}. These methods will then be called while reflection is used to
     * make sure they <em>set</em> as expected.
     *
     * @param cls The {@link Class} to test.
     * @throws IntrospectionException       Thrown when bean information could not be recovered from the given class.
     * @throws ReflectiveOperationException Thrown when one or more fields could not be tested using reflection.
     */
    public static void testMutators(Class<?> cls) throws IntrospectionException, ReflectiveOperationException {
        testMutators(Reflection.createInstance(cls));
    }

    /**
     * Test all the mutators for the given instance. Mutator information will be scanned for using the
     * {@link Introspector}. These methods will then be called while reflection is used to make sure they <em>set</em>
     * as expected.
     *
     * @param instance The instance to test.
     * @throws IntrospectionException       Thrown when bean information could not be recovered from the given class.
     * @throws ReflectiveOperationException Thrown when one or more fields could not be tested using reflection.
     */
    public static void testMutators(Object instance) throws IntrospectionException, ReflectiveOperationException {
        Class<?> cls = instance.getClass();
        PropertyDescriptor[] descriptors =
                Introspector.getBeanInfo(cls, Object.class).getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod != null) {
                Object mock = Reflection.dumbMock(descriptor.getPropertyType());
                String name = descriptor.getName();

                try {
                    writeMethod.invoke(instance, mock);

                    Field field = Reflection.getField(cls, name);
                    field.setAccessible(true);
                    if (!checkField(field, instance, mock)) {
                        throw new AssertionError(String.format(MUTATOR_FAIL_MESSAGE, cls.getCanonicalName(), name));
                    }
                } catch (Exception e) {
                    throw new AssertionError(String.format(MUTATOR_EXCEPTION_MESSAGE, cls.getCanonicalName(), name), e);
                }
            }
        }
    }

    /**
     * Test the {@link Object#equals(Object)} and {@link Object#hashCode()} methods on the given class. It is expected
     * that equals only returns true when <strong><em>all fields</em></strong> fields of the given item have
     * <strong><em>exactly the same value</em></strong>. For hashCode it is expected that an exact copy returns the
     * exact same hashCode, but different items <em>may</em> do the same.
     * <p/>
     * The full list of checks are as followed:
     * <ul>
     * <li>Check whether equals returns true if the same instance is given;</li>
     * <li>Check whether equals is false when null is given;</li>
     * <li>Check whether equals is false if a new Object() is given;</li>
     * <li>Check whether equals is true if an exact copy is given;</li>
     * <li>Check whether hashCode returns the same value for an exact copy;</li>
     * <li>Check whether equals returns false when <string><em>any</em></string> field value on the copy is
     * different.</li>
     * </ul>
     *
     * @param cls The {@link Class} to test the {@link Object#equals(Object)} and {@link Object#hashCode()} methods for.
     * @throws ReflectiveOperationException
     */
    @SuppressWarnings("ObjectEqualsNull")
    public static void testEqualsAndHashCode(Class<?> cls) throws ReflectiveOperationException {
        Object instanceA = Reflection.createInstance(cls);
        Object instanceB = Reflection.createInstance(cls);
        setFieldsToSameValue(cls, instanceA, instanceB);

        if (instanceA.equals(null)) {
            throw new AssertionError(String.format(EQUAL_ON_NULL_MESSAGE, cls.getCanonicalName()));
        } else if (instanceA.equals(new Object())) {
            throw new AssertionError(String.format(EQUAL_ON_NEW_OBJECT_MESSAGE, cls.getCanonicalName()));
        } else if (!instanceA.equals(instanceA)) {
            throw new AssertionError(String.format(SAME_INSTANCE_NOT_EQUAL_MESSAGE, cls.getCanonicalName(), instanceA));
        } else if (!instanceA.equals(instanceB) || !instanceB.equals(instanceA)) {
            throw new AssertionError(String.format(EXACT_COPY_NOT_EQUAL_MESSAGE,
                    cls.getCanonicalName(), instanceA, instanceB));
        } else if (instanceA.hashCode() != instanceB.hashCode()) {
            throw new AssertionError(String.format(EXACT_COPY_HASH_CODE_NOT_EQUAL_MESSAGE,
                    cls.getCanonicalName(), instanceA.hashCode(), instanceB.hashCode()));
        }

        for (Field field : Reflection.getFields(cls)) {
            testFieldUseInEquals(field, instanceA, instanceB);
        }
    }

    private static void testFieldUseInEquals(Field field, Object instanceA, Object instanceB) throws ReflectiveOperationException {
        field.setAccessible(true);
        Object oldValue = field.get(instanceB);
        field.set(instanceB, Reflection.dumbMock(field.getType(), SEED_TWO));
        if (instanceA.equals(instanceB) || instanceB.equals(instanceA)) {
            throw new AssertionError(String.format(FIELD_NOT_USED_IN_EQUALS_MESSAGE, field.getName()));
        }
        field.set(instanceB, oldValue);
    }

    private static void setFieldsToSameValue(Class<?> cls, Object instanceA, Object instanceB) throws ReflectiveOperationException {
        for (Field field : Reflection.getFields(cls)) {
            field.setAccessible(true);
            Object dumbMock = Reflection.dumbMock(field.getType(), SEED_ONE);
            field.set(instanceA, dumbMock);
            field.set(instanceB, dumbMock);
        }
    }

    private static boolean checkField(Field field, Object instance, Object expectedValue) throws IllegalAccessException {
        if (field.getType().isPrimitive()) {
            return expectedValue.equals(field.get(instance));
        }
        return expectedValue == field.get(instance);
    }

    private static boolean checkReadMethod(Object instance, Method readMethod, Object expectedValue) throws IllegalAccessException, InvocationTargetException {
        if (readMethod.getReturnType().isPrimitive()) {
            return expectedValue.equals(readMethod.invoke(instance));
        }
        return expectedValue == readMethod.invoke(instance);
    }
}
