package com.impressiveinteractive.checkmark;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class MethodFilter {
	private final boolean inclusive;
	private final Set<Method> methods;

	private MethodFilter(boolean inclusive, Set<Method> methods) {
		this.inclusive = inclusive;
		this.methods = methods;
	}

	public boolean isInclusive() {
		return inclusive;
	}

	public Set<Method> getMethods() {
		return methods;
	}

	public static interface Builder {
		MethodFilter build();
	}

	public static class InclusiveBuilder implements Builder {
		private final MockTracker tracker;
		private final Set<Method> methods = new HashSet<>();

		public InclusiveBuilder(MockTracker tracker, Method method) {
			this.tracker = tracker;
			this.methods.add(method);
		}

		public InclusiveBuilder andInclude(Object anything) {
			methods.add(tracker.getCalledLast());
			return this;
		}

		@Override
		public MethodFilter build() {
			return new MethodFilter(true, methods);
		}
	}

	public static class ExclusiveBuilder implements Builder {
		private final MockTracker tracker;
		private final Set<Method> methods = new HashSet<>();

		public ExclusiveBuilder(MockTracker tracker, Method method) {
			this.tracker = tracker;
			this.methods.add(method);
		}

		public ExclusiveBuilder andExclude(Object anything) {
			methods.add(tracker.getCalledLast());
			return this;
		}

		@Override
		public MethodFilter build() {
			return new MethodFilter(false, methods);
		}
	}
}
