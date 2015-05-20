package cpt.listeners;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.reflect.Proxy.*;

public final class DynamicMulticaster {

	protected static boolean isNull(final Object current) {
		return isProxyClass(current.getClass()) && getInvocationHandler(current) == NULL;
	}

	protected static boolean isWrapper(final Object current) {
		return isProxyClass(current.getClass()) && getInvocationHandler(current).getClass() == Wrapper.class;
	}

	public static <T> T empty(final Class<T> clazz) {
		return (T) newProxyInstance(DynamicMulticaster.class.getClassLoader(), new Class<?>[] { clazz }, NULL);
	}

	public static <T> T add(final Class<T> clazz, final T current, final T additional) {
		if (current == null || isNull(current)) return additional;
		else return (T) newProxyInstance(DynamicMulticaster.class.getClassLoader(), new Class<?>[] { clazz }, new Wrapper(current, additional));
	}

	public static <T> T remove(final Class<T> clazz, final T current, final T removal) {
		if (current == null || isNull(current) || current == removal) return empty(clazz);
		else if (isWrapper(current)) {
			final Wrapper<T> wrapper = (Wrapper<T>) getInvocationHandler(current);
			final T one = wrapper.one == removal ? null : remove(clazz, wrapper.one, removal);
			final T two = wrapper.two == removal ? null : remove(clazz, wrapper.two, removal);

			if (one == null) return two;
			else if (two == null) return one;
			else return add(clazz, one, two);

		} else return current;
	}

	protected static final InvocationHandler NULL = new InvocationHandler() {
		public Object invoke(final Object proxy, final Method method, final Object[] args) { return null; }
	};

	protected static class Wrapper<T> implements InvocationHandler {

		protected final T one;
		protected final T two;

		public Wrapper(final T one, final T two) {
			this.one = one;
			this.two = two;
		}

		public Object invoke(final Object proxy, final Method method, final Object[] args) {
			try {
				method.invoke(one, args);
				method.invoke(two, args);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
			return null;
		}
	}
}
