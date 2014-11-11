package com.impressiveinteractive.checkmark;

import java.util.Arrays;

public class CheckMarkException extends Exception {
	public CheckMarkException(String message) {
		super(message);
	}

	public CheckMarkException(String message, Object... objects) {
		super(parseToString(message, objects), parseToThrowable(objects));
	}

	private static Throwable parseToThrowable(Object[] objects) {
		Throwable throwable = null;
		if (objects[objects.length - 1] instanceof Throwable) {
			throwable = (Throwable) objects[objects.length - 1];
		}
		return throwable;
	}

	private static String parseToString(String message, Object[] objects) {
		Object[] formatArgs;
		if (objects.length > 0 && objects[objects.length - 1] instanceof Throwable) {
			formatArgs = Arrays.copyOfRange(objects, 0, objects.length - 1);
		} else {
			formatArgs = objects;
		}

		if (formatArgs.length == 0) {
			return message;
		} else {
			return String.format(message, formatArgs);
		}
	}
}
