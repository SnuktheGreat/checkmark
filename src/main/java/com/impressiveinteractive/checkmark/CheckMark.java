package com.impressiveinteractive.checkmark;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Utility class that can helps test common use cases like accessors, mutators, constructors and more.
 */
public final class CheckMark {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckMark.class);

    private static final String ACCESSOR_FAIL_MESSAGE = "Accessor to field %s.%s does not work.";
    private static final String MUTATOR_FAIL_MESSAGE = "Mutator to field %s.%s does not work.";
    private static final String NO_VALID_CONSTRUCTOR_FAIL_MESSAGE =
            "No valid constructor for %s among the %d candidates.";
    private static final String CREATE_ABSTRACT_FAIL_MESSAGE =
            "Can not create an instance for an interface or abstract class %s.";
    private static final String UNKNOWN_PRIMITIVE_MESSAGE = "Not a known primitive: %s";
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
     * @throws ReflectiveOperationException Thrown when a {@link #dumbMock(Class) dumb mock} could not be created when
     *                                      filling in the blanks.
     */
    public static void testAccessorsAndMutators(Class<?> cls)
            throws IntrospectionException, ReflectiveOperationException {
        testAccessorsAndMutators(createInstance(cls));
    }

    /**
     * Test all the accessors and mutators for the given instance. Accessor and mutator information will be scanned for
     * using the {@link Introspector}. These methods will then be called while reflection is used to make sure they
     * <em>get</em> or <em>set</em> as expected.
     *
     * @param instance The instance to test.
     * @throws IntrospectionException       Thrown when bean information could not be recovered from the given class.
     * @throws ReflectiveOperationException Thrown when a {@link #dumbMock(Class) dumb mock} could not be created when
     *                                      filling in the blanks.
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
     * @throws ReflectiveOperationException Thrown when a {@link #dumbMock(Class) dumb mock} could not be created when
     *                                      filling in the blanks.
     */
    public static void testAccessors(Class<?> cls) throws IntrospectionException, ReflectiveOperationException {
        testAccessors(createInstance(cls));
    }

    /**
     * Test all the accessors for the given instance. Accessor information will be scanned for using the
     * {@link Introspector}. These methods will then be called while reflection is used to make sure they <em>get</em>
     * as expected.
     *
     * @param instance The instance to test.
     * @throws IntrospectionException       Thrown when bean information could not be recovered from the given class.
     * @throws ReflectiveOperationException Thrown when a {@link #dumbMock(Class) dumb mock} could not be created when
     *                                      filling in the blanks.
     */
    public static void testAccessors(Object instance) throws IntrospectionException, ReflectiveOperationException {
        Class<?> cls = instance.getClass();
        PropertyDescriptor[] descriptors =
                Introspector.getBeanInfo(cls, Object.class).getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            Method readMethod = descriptor.getReadMethod();
            if (readMethod != null) {
                Object mock = dumbMock(descriptor.getPropertyType());
                String name = descriptor.getName();

                try {
                    Field field = Reflection.getField(cls, name);
                    field.setAccessible(true);
                    if (Modifier.isFinal(field.getModifiers())) {
                        mock = field.get(instance);
                    } else {
                        field.set(instance, mock);
                    }
                    if (!mock.equals(readMethod.invoke(instance))) {
                        throw new AssertionError(String.format(ACCESSOR_FAIL_MESSAGE, cls, name));
                    }
                } catch (NoSuchFieldException e) {
                    LOGGER.warn(NO_GETTER_MESSAGE, name);
                } catch (Exception e) {
                    throw new AssertionError(String.format(ACCESSOR_FAIL_MESSAGE, cls, name), e);
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
     * @throws ReflectiveOperationException Thrown when a {@link #dumbMock(Class) dumb mock} could not be created when
     *                                      filling in the blanks.
     */
    public static void testMutators(Class<?> cls) throws IntrospectionException, ReflectiveOperationException {
        testMutators(createInstance(cls));
    }

    /**
     * Test all the mutators for the given instance. Mutator information will be scanned for using the
     * {@link Introspector}. These methods will then be called while reflection is used to make sure they <em>set</em>
     * as expected.
     *
     * @param instance The instance to test.
     * @throws IntrospectionException       Thrown when bean information could not be recovered from the given class.
     * @throws ReflectiveOperationException Thrown when a {@link #dumbMock(Class) dumb mock} could not be created when
     *                                      filling in the blanks.
     */
    public static void testMutators(Object instance) throws IntrospectionException, ReflectiveOperationException {
        Class<?> cls = instance.getClass();
        PropertyDescriptor[] descriptors =
                Introspector.getBeanInfo(cls, Object.class).getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod != null) {
                Object mock = dumbMock(descriptor.getPropertyType());
                String name = descriptor.getName();

                try {
                    writeMethod.invoke(instance, mock);

                    Field field = Reflection.getField(cls, name);
                    field.setAccessible(true);
                    if (!mock.equals(field.get(instance))) {
                        throw new AssertionError(String.format(MUTATOR_FAIL_MESSAGE, cls, name));
                    }
                } catch (Exception e) {
                    throw new AssertionError(String.format(MUTATOR_FAIL_MESSAGE, cls, name), e);
                }
            }
        }
    }

    /**
     * Create a "dumb" mock. A dumb mock is <strong><em>any</em></strong> kind of non-null value. Different types will
     * return different objects which can be a Mockito compatible mock, but this is not guaranteed. More specifically:
     * <ul>
     * <li>Primitive types will return a boxed primitive 0 value;</li>
     * <li>Array types will return an empty array of the given type;</li>
     * <li>Enumeration types will return the first value of that type;</li>
     * <li>Final types will return an instance created by {@link #createInstance(Class)};</li>
     * <li>All other types will be handled by {@link Mockito#mock(Class)}.</li>
     * </ul>
     *
     * @param cls The class to get a dumb mock for.
     * @param <T> The expected type.
     * @return The dumb mock of the given type.
     * @throws ReflectiveOperationException Thrown when creating an instance was not possible.
     */
    @SuppressWarnings("unchecked")
    public static <T> T dumbMock(Class<T> cls) throws ReflectiveOperationException {
        if (cls.isPrimitive()) {
            return (T) createPrimitive(cls);
        } else if (cls.isArray()) {
            return (T) Array.newInstance(cls.getComponentType(), 0);
        } else if (cls.isEnum()) {
            Field first = cls.getFields()[0];
            return (T) first.get(first.getName());
        } else if (Modifier.isFinal(cls.getModifiers())) {
            return createInstance(cls);
        }
        return Mockito.mock(cls);
    }

    /**
     * Create an actual, testable instance of the given class. This will never be a mock. Note that this method does not
     * create instances of interfaces or abstract classes.
     * </p>
     * Enumeration types are currently also not supported. Enumeration testing has not been thought through at this
     * point.
     *
     * @param cls The class to create an instance for.
     * @return An instance of the given class.
     * @throws ReflectiveOperationException
     */
    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Class<T> cls) throws ReflectiveOperationException {
        if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) {
            throw new IllegalArgumentException(String.format(CREATE_ABSTRACT_FAIL_MESSAGE, cls));
        } else if (cls.isEnum()) {
            throw new UnsupportedOperationException("Enumeration types are currently not supported.");
        }

        List<Constructor<?>> constructors = new LinkedList<>(asList(cls.getDeclaredConstructors()));
        Collections.sort(constructors, new ConstructorLengthComparator());
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameters = constructor.getParameterTypes();
            Object[] arguments = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                arguments[i] = dumbMock(parameters[i]);
            }
            try {
                return (T) constructor.newInstance(arguments);
            } catch (ReflectiveOperationException e) {
                // Ignore, try another constructor
            }
        }
        throw new IllegalArgumentException(String.format(NO_VALID_CONSTRUCTOR_FAIL_MESSAGE, cls, constructors.size()));
    }

    private static Object createPrimitive(Class<?> cls) {
        if (cls.equals(boolean.class)) {
            return Boolean.FALSE;
        } else if (cls.equals(byte.class)) {
            return (byte) 0;
        } else if (cls.equals(char.class)) {
            return '\u0000';
        } else if (cls.equals(short.class)) {
            return (short) 0;
        } else if (cls.equals(int.class)) {
            return 0;
        } else if (cls.equals(long.class)) {
            return 0l;
        } else if (cls.equals(float.class)) {
            return 0f;
        } else if (cls.equals(double.class)) {
            return 0.0;
        }
        throw new IllegalArgumentException(String.format(UNKNOWN_PRIMITIVE_MESSAGE, cls));
    }

    private static class ConstructorLengthComparator implements Comparator<Constructor<?>> {
        @Override
        public int compare(Constructor<?> o1, Constructor<?> o2) {
            return o1.getParameterTypes().length - o2.getParameterTypes().length;
        }
    }
}
