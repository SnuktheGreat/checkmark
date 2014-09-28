package com.impressiveinteractive.checkmark;

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
 * Utility class that can helps test common use cases like accessors abd mutators.
 */
public final class CheckMark {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckMark.class);

    private static final String ACCESSOR_FAIL_MESSAGE = "Accessor to field %s.%s does not work.";
    private static final String MUTATOR_FAIL_MESSAGE = "Mutator to field %s.%s does not work.";
    private static final String NO_GETTER_MESSAGE =
            "Ignoring getter for property \"{}\" since its field can not be found. Is this a getter?";

    private CheckMark() {
        throw new AssertionError("Private constructor called");
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
                    throw new AssertionError(String.format(ACCESSOR_FAIL_MESSAGE, cls.getCanonicalName(), name), e);
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
                    throw new AssertionError(String.format(MUTATOR_FAIL_MESSAGE, cls.getCanonicalName(), name), e);
                }
            }
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
