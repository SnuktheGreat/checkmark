package com.impressiveinteractive.checkmark;

import java.lang.reflect.Method;

public interface MethodInvocationListener {
	void invoked(Method method) throws CheckMarkException;
}
