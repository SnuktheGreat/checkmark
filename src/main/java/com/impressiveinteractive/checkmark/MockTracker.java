package com.impressiveinteractive.checkmark;

import java.lang.reflect.Method;

public class MockTracker {
	private Method calledLast;

	public Method getCalledLast() {
		return calledLast;
	}

	public Method extractCalledLast() {
		Method result = calledLast;
		calledLast = null;
		return result;
	}

	void lastCall(Method method) throws CheckMarkException {
		if (calledLast != null) {
			throw new CheckMarkException("Nothing done with method call (%s).", calledLast);
		}
		calledLast = method;
	}
}
