package com.gses.executive;

import org.junit.*;
import java.lang.reflect.*;

import static org.junit.Assert.*;

public class MulticasterTest {

	@FunctionalInterface
	public static interface Listen {
		void ping();
	}

	@Test
	public void testEmpty() {
		final Listen empty = Multicaster.empty(Listen.class);
		assertNotNull(empty);
		assertEquals(Multicaster.NULL, Proxy.getInvocationHandler(empty));
		empty.ping();
	}

	@Test
	public void testSingle() {
		final Listen listen = () -> {};
		final Listen empty = Multicaster.empty(Listen.class);
		final Listen single = Multicaster.add(Listen.class, empty, listen);
		assertNotNull(single);
		assertEquals(listen, single);
		single.ping();
	}

	@Test
	public void testDual() {
		final Listen one = () -> {};
		final Listen two = () -> {};
		Listen listen = Multicaster.empty(Listen.class);
		listen = Multicaster.add(Listen.class, listen, one);
		listen = Multicaster.add(Listen.class, listen, two);

		assertNotNull(listen);
		assertEquals(Multicaster.Wrapper.class, Proxy.getInvocationHandler(listen).getClass());
		listen.ping();
	}

	@Test
	public void testRemove() {
		final Listen one = () -> {};
		final Listen two = () -> {};
		Listen listen = Multicaster.empty(Listen.class);
		listen = Multicaster.add(Listen.class, listen, one);
		listen = Multicaster.add(Listen.class, listen, two);

		assertEquals(one, Multicaster.remove(Listen.class, listen, two));
		assertEquals(two, Multicaster.remove(Listen.class, listen, one));
	}

	@Test
	public void testRemoveDeep() {
		final Listen one = () -> {};
		final Listen two = () -> {};
		final Listen three = () -> {};
		Listen listen = Multicaster.empty(Listen.class);
		listen = Multicaster.add(Listen.class, listen, one);
		listen = Multicaster.add(Listen.class, listen, two);
		listen = Multicaster.add(Listen.class, listen, three);

		assertEquals(three, Multicaster.remove(Listen.class, Multicaster.remove(Listen.class, listen, one), two));
	}
}
