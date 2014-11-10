package com.impressiveinteractive.checkmark;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class TrackingAnswer implements Answer {
	private final MockTracker tracker;

	public TrackingAnswer(MockTracker tracker) {
		this.tracker = tracker;
	}

	@Override
	public Object answer(InvocationOnMock invocation) throws Throwable {
		tracker.lastCall(invocation.getMethod());
		return null;
	}
}
