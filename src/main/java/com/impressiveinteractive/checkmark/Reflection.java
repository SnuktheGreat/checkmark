package com.impressiveinteractive.checkmark;

import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * Utility class to help with common reflection tasks.
 */
public final class Reflection {
    /**
     * The default seed used for the delegate call to {@link #dumbMock(Class, long)} when calling
     * {@link #dumbMock(Class)}.
     */
    public static final long DEFAULT_SEED = 1l;

    private static final String NO_VALID_CONSTRUCTOR_FAIL_MESSAGE =
            "No valid constructor for %s among the %d candidates.";
    private static final String CREATE_ABSTRACT_FAIL_MESSAGE =
            "Can not create an instance for an interface or abstract class %s.";
    private static final String UNKNOWN_PRIMITIVE_MESSAGE = "Not a known primitive: %s";

    private Reflection() {
        throw new AssertionError("Private constructor.");
    }

    /**
     * Create a "dumb" mock. A dumb mock is <strong><em>any</em></strong> kind of non-null value. Different types will
     * return different objects which can be a Mockito compatible mock, but this is not guaranteed.
     *
     * @param cls The class to get a dumb mock for.
     * @param <T> The expected type.
     * @return The dumb mock of the given type.
     * @throws ReflectiveOperationException Thrown when creating an instance was not possible.
     * @see #dumbMock(Class, long)
     */
    public static <T> T dumbMock(Class<T> cls) throws ReflectiveOperationException {
        return dumbMock(cls, DEFAULT_SEED);
    }

    /**
     * Create a "dumb" mock. A dumb mock is <strong><em>any</em></strong> kind of non-null value. Different types will
     * return different objects which can be a Mockito compatible mock, but this is not guaranteed. More specifically:
     * <ul>
     * <li>Primitive types will return a boxed primitive value generated from the given seed;</li>
     * <li>Array types will return an empty array of the given type;</li>
     * <li>Enumeration types will return one of its values influenced by the given seed;</li>
     * <li>Final types will return an instance created by {@link #createInstance(Class)};</li>
     * <li>All other types will be handled by {@link Mockito#mock(Class)}.</li>
     * </ul>
     *
     * @param cls  The class to get a dumb mock for.
     * @param seed A seed used for the generation of {@link String} and primitive values.
     * @param <T>  The expected type.
     * @return The dumb mock of the given type.
     * @throws ReflectiveOperationException Thrown when creating an instance was not possible.
     */
    @SuppressWarnings("unchecked")
    public static <T> T dumbMock(Class<T> cls, long seed) throws ReflectiveOperationException {
        if (cls.isPrimitive()) {
            return (T) createPrimitive(cls, seed);
        } else if (cls.isArray()) {
            return (T) Array.newInstance(cls.getComponentType(), 0);
        } else if (cls.isEnum()) {
            Field[] fields = cls.getFields();
            Field selection = fields[((int) (seed % fields.length))];
            return (T) selection.get(selection.getName());
        } else if (cls == String.class) {
            return (T) Long.toString(seed);
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

    /**
     * Get the {@link Field} for the given name from the given {@link Class}. This method is like
     * {@link Class#getDeclaredField(String)}, except that it will also try all {@link Class#getSuperclass() super
     * classes} if the given class itself does not contain the field. If the field can not be found on any of the super
     * classes, the {@link NoSuchFieldException} is still thrown.
     *
     * @param cls  The {@link Class} to get the {@link Field} for.
     * @param name The name of the {@link Field} to recover.
     * @return The {@link Field} if part of the class hierarchy.
     * @throws NoSuchFieldException Thrown when no such field can be found in the class hierarchy.
     */
    public static Field getField(Class<?> cls, String name) throws NoSuchFieldException {
        try {
            return cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = cls.getSuperclass();
            if (superclass != null) {
                return getField(superclass, name);
            } else {
                throw new NoSuchFieldException(String.format("Could not find field %s.%s.",
                        cls.getCanonicalName(), name));
            }
        }
    }

    /**
     * Get all fields for the given {@link Class}. This includes all fields from all super classes with any visibility
     * modifier, but excludes {@link Field#isSynthetic() sythetic fields}.
     *
     * @param cls The {@link Class} to get the fields for.
     * @return All fields for the given {@link Class}.
     */
    public static Set<Field> getFields(Class<?> cls) {
        Set<Field> allFields = new HashSet<>();

        Class<?> current = cls;
        while (current != Object.class) {
            Field[] localFields = current.getDeclaredFields();
            for (Field field : localFields) {
                if (!field.isSynthetic()) {
                    allFields.add(field);
                }
            }
            current = current.getSuperclass();
        }

        return allFields;
    }

    private static Object createPrimitive(Class<?> cls, long seed) {
        if (cls.equals(boolean.class)) {
            return Boolean.TRUE;
        } else if (cls.equals(byte.class)) {
            return (byte) seed;
        } else if (cls.equals(char.class)) {
            return (char) (seed % 0x10000);
        } else if (cls.equals(short.class)) {
            return (short) seed;
        } else if (cls.equals(int.class)) {
            return (int) seed;
        } else if (cls.equals(long.class)) {
            return seed;
        } else if (cls.equals(float.class)) {
            return (float) seed;
        } else if (cls.equals(double.class)) {
            return (double) seed;
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
