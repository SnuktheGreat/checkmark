package com.impressiveinteractive.checkmark;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class AccessorInvocationListener implements MethodInvocationListener {
	private final Set<Method> candidateAccessors = new HashSet<>();
	private final Set<Method> calledAccessors = new HashSet<>();

	public AccessorInvocationListener(Class<?> cls) throws CheckMarkException {
		try {
			PropertyDescriptor[] descriptors =
					Introspector.getBeanInfo(cls, Object.class).getPropertyDescriptors();
			for (PropertyDescriptor descriptor : descriptors) {
				Method readMethod = descriptor.getReadMethod();
				if (readMethod != null) {
					candidateAccessors.add(readMethod);
				}
			}
		} catch (IntrospectionException e) {
			throw new CheckMarkException("Could not extract accessor methods.", e);
		}
	}

	@Override
	public void invoked(Method method) throws CheckMarkException {
		if (!candidateAccessors.contains(method)) {
			throw new CheckMarkException("The called method (%s) is not an accessor.", method);
		}
		calledAccessors.add(method);
	}

	public Set<Method> getCalledAccessors() {
		return calledAccessors;
	}
}
