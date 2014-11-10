package com.impressiveinteractive.checkmark;

public class MockTester {
	private final MockTracker tracker;
	private final Object trackedMock;
	private MethodFilter accessorsFilter;

	public MockTester(MockTracker tracker, Object trackedMock) {
		this.tracker = tracker;
		this.trackedMock = trackedMock;
	}

	public MockTester testAccessors(MethodFilter.Builder filterBuilder) {
		return testAccessors(filterBuilder.build());
	}

	public MockTester testAccessors(MethodFilter filter) {
		this.accessorsFilter = filter;
		return this;
	}

	public MockTester testModifiers(MethodFilter filter) {
		return this;
	}

	public MockTester testEqualsAndHashCode() {
		return this;
	}

	public void please() {
		System.out.println(accessorsFilter.getMethods());
	}
}
