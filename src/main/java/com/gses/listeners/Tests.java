package cpt.listeners;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class Tests {

	private StaticMulticaster<Listener> staticManager = new StaticMulticaster<Listener>(Listener.class, NullListener.class, ListenerWrapper.class);
	private Listener staticMulticaster = staticManager.empty();

	private Listener dynamicMulticaster = DynamicMulticaster.empty(Listener.class);
	private final List<Listener> list = new CopyOnWriteArrayList();

	public void add(final Listener listener) {
		staticMulticaster = staticManager.add(staticMulticaster, listener);
		dynamicMulticaster = DynamicMulticaster.add(Listener.class, dynamicMulticaster, listener);
		list.add(listener);
	}

	public void staticMulticast(final Integer i, final Double d) {
		staticMulticaster.updated(i, d);
	}

	public void dynamicMulticast(final Integer i, final Double d) {
		dynamicMulticaster.updated(i, d);
	}

	public void unicast(final Integer i, final Double d) {
		list.forEach(l -> { l.updated(i, d); });
	}
}
