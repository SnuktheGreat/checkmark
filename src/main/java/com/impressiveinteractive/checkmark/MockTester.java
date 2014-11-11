package com.impressiveinteractive.checkmark;

import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Set;

public class MockTester<T> {
	private final MockTracker tracker = new MockTracker();
	private final Class<T> mockClass;
	private final T trackedMock;

	public MockTester(Class<T> mockClass) {
		this.mockClass = mockClass;
		trackedMock = Mockito.mock(mockClass, new TrackingAnswer(tracker));
	}

	public MockTester<T> testAccessorsIncluding(MethodSelector<T> selector) throws CheckMarkException {
		AccessorInvocationListener listener = listen(selector, new AccessorInvocationListener(mockClass));

		Set<Method> accessors = listener.getCalledAccessors();
		System.out.println("Including:");
		for (Method accessor : accessors) {
			System.out.println(accessor);
		}

		return this;
	}

	public MockTester<T> testAccessorsExcluding(MethodSelector<T> selector) throws CheckMarkException {
		AccessorInvocationListener listener = listen(selector, new AccessorInvocationListener(mockClass));

		Set<Method> accessors = listener.getCalledAccessors();
		System.out.println("Excluding:");
		for (Method accessor : accessors) {
			System.out.println(accessor);
		}

		return this;
	}

	public MockTester<T> testModifiersIncluding(MethodSelector<T> selector) throws CheckMarkException {
		ModifierInvocationListener listener = listen(selector, new ModifierInvocationListener(mockClass));

		Set<Method> accessors = listener.getCalledModifiers();
		System.out.println("Including:");
		for (Method accessor : accessors) {
			System.out.println(accessor);
		}

		return this;
	}

	public MockTester<T> testModifiersExcluding(MethodSelector<T> selector) throws CheckMarkException {
		ModifierInvocationListener listener = listen(selector, new ModifierInvocationListener(mockClass));

		Set<Method> accessors = listener.getCalledModifiers();
		System.out.println("Excluding:");
		for (Method accessor : accessors) {
			System.out.println(accessor);
		}

		return this;
	}

	public MockTester testEqualsAndHashCode(MethodSelector<T> selector) {
		return this;
	}

	private <L extends MethodInvocationListener> L listen(MethodSelector<T> selector, L listener) {
		tracker.setListener(listener);
		selector.withMethods(trackedMock);
		tracker.unsetListener();
		return listener;
	}
}
