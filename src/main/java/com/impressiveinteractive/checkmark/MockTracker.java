package com.impressiveinteractive.checkmark;

import java.lang.reflect.Method;

public class MockTracker {
	private MethodInvocationListener listener;

	public void setListener(MethodInvocationListener listener) {
		this.listener = listener;
	}

	public void unsetListener() {
		this.listener = null;
	}

	void lastCall(Method method) throws CheckMarkException {
		if (listener == null) {
			throw new IllegalStateException("No MethodInvocationListener registered on the MockTracker.");
		}

		listener.invoked(method);
	}
}
