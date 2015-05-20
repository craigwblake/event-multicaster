package cpt.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import static java.lang.reflect.Proxy.*;

public final class StaticMulticaster<T> {

	protected final Class<T> t;
	protected final T NULL;
	protected final Class<? extends Wrapper<T>> wrapper;

	public StaticMulticaster(final Class<T> t, final Class<? extends T> NULL, final Class<? extends Wrapper<T>> wrapper) {
		this.t = t;
		this.NULL = construct(NULL);
		this.wrapper = wrapper;
	}

	protected boolean isNull(final T current) {
		return current == NULL;
	}

	protected boolean isWrapper(final T current) {
		return current != null && current.getClass() == wrapper;
	}

	public T empty() {
		return NULL;
	}

	public T add(final T current, final T additional) {
		if (current == null || isNull(current)) return additional;

		return construct(wrapper, current, additional);
	}

	public T remove(final T current, final T removal) {
		if (current == null || current == NULL || current == removal) return NULL;
		else if (wrapper.isAssignableFrom(current.getClass())) {
			final Wrapper<T> wrapper = (Wrapper<T>) getInvocationHandler(current);
			final T one = wrapper.one == removal ? null : remove(wrapper.one, removal);
			final T two = wrapper.two == removal ? null : remove(wrapper.two, removal);

			if (one == null) return two;
			else if (two == null) return one;
			else return add(one, two);

		} else return current;
	}

	protected T construct(final Class<?> clazz, final T... args) {
		final Class<?>[] types = new Class<?>[args.length];
		for (int x = 0; x < args.length; ++x) types[x] = t;
		try {
			final Constructor<?> ctor = clazz.getConstructor(types);
			return (T) ctor.newInstance(args);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
