package com.innobiz.orange.web.ap.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** EL에서 static 함수 호출용 */
@SuppressWarnings("serial")
public class ELCall extends HashMap<String, Object> {
	
	//private static final long serialVersionUID = 3073041887807416934L;

	private static Pattern pattern = Pattern.compile("(.+)\\.([^\\.]+)");
	
	@SuppressWarnings({ "unchecked"})
	@Override
	public Object get(Object key) {
		String fullyQualifiedMethodName = (String) key;

		// format of key is package.Class.method
//		Pattern pattern = Pattern.compile("(.+)\\.([^\\.]+)");
		Matcher m = pattern.matcher(fullyQualifiedMethodName);
		if (m.matches()) {
			String fqClassName = m.group(1);
			if(!fqClassName.startsWith("com") && !fqClassName.startsWith("java") && !fqClassName.startsWith("org")){
				fqClassName = "com.innobiz.orange.web."+fqClassName.substring(0, 2).toLowerCase()+".utils."+fqClassName;
			}
			String methodName = m.group(2);
			Class<Object> clazz;
			try {
				clazz = (Class<Object>) Class.forName(fqClassName);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("Invalid method name: "
						+ key, e);
			}
			Method[] methods = clazz.getMethods();
			for (final Method method : methods) {
				if ((method.getModifiers() & Modifier.STATIC) == 0) {
					continue;
				}

				if (method.getName().equals(methodName)) {
					// return the first method found
					int numParameters = method.getParameterTypes().length;

					if (numParameters == 0) {
						return invokeMethod(method);
					}

					return new ELMethod(numParameters) {
						@Override
						public Object result(Object[] args) {
							return invokeMethod(method, args);
						}
					};
				}
			}
		}

		throw new IllegalArgumentException("Invalid method name: " + key
				+ ". Must be a fully qualified class and method name");
	}

	private Object invokeMethod(final Method method, Object... args) {
		try {
			return method.invoke(null, args);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Exception while executing method", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Exception while executing method", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Exception while executing method", e);
		}
	}
}