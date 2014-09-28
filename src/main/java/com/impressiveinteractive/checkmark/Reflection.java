package com.impressiveinteractive.checkmark;

import java.lang.reflect.Field;

/**
 * Utility class to help with common reflection tasks.
 */
public final class Reflection {
    private Reflection(){
        throw new AssertionError("Private constructor.");
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
                throw new NoSuchFieldException(String.format("Could not find field %s.", name));
            }
        }
    }
}
