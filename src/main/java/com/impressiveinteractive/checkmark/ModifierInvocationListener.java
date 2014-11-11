package com.impressiveinteractive.checkmark;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class ModifierInvocationListener implements MethodInvocationListener {
	private final Set<Method> candidateModifiers = new HashSet<>();
	private final Set<Method> calledModifiers = new HashSet<>();

	public ModifierInvocationListener(Class<?> cls) throws CheckMarkException {
		try {
			PropertyDescriptor[] descriptors =
					Introspector.getBeanInfo(cls, Object.class).getPropertyDescriptors();
			for (PropertyDescriptor descriptor : descriptors) {
				Method writeMethod = descriptor.getWriteMethod();
				if (writeMethod != null) {
					candidateModifiers.add(writeMethod);
				}
			}
		} catch (IntrospectionException e) {
			throw new CheckMarkException("Could not extract accessor methods.", e);
		}
	}

	@Override
	public void invoked(Method method) throws CheckMarkException {
		if (!candidateModifiers.contains(method)) {
			throw new CheckMarkException("The called method (%s) is not a modifier.", method);
		}
		calledModifiers.add(method);
	}

	public Set<Method> getCalledModifiers() {
		return calledModifiers;
	}
}
