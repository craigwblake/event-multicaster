package cpt.listeners;

@FunctionalInterface
public interface Listener {

	void updated(Integer key, Double value);
}
