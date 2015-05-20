package cpt.listeners;

public class ListenerWrapper extends Wrapper<Listener> implements Listener {

	public ListenerWrapper(final Listener one, final Listener two) {
		super(one, two);
	}

	public void updated(final Integer key, final Double value) {
		one.updated(key, value);
		two.updated(key, value);
	}
}
