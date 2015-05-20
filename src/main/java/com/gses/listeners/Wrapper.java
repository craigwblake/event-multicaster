package cpt.listeners;

public abstract class Wrapper<T> {

	public final T one;
	public final T two;

	public Wrapper(final T one, final T two) {
		this.one = one;
		this.two = two;
	}
}
