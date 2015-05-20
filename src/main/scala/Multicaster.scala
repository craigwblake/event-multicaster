package cpt.listeners

import java.lang.reflect.Constructor
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Proxy._
import java.util.Arrays.copyOf
import java.util.Arrays.stream
import scala.reflect.runtime.universe._

trait ScalaWrapper[T] { self: T =>
	val one: T
	val two: T
}

//case class ScalaMulticaster [T, Y <: Wrapper[T]] (val empty: T)(implicit ttag: ClassTag[T], ytag: ClassTag[Y]) {
case class ScalaMulticaster [T, Y <: ScalaWrapper[T]] (val empty: T)(implicit tag: ClassTag[Y]) {

	protected def isNull (current: T): Boolean = current == null || current == empty

	protected def isWrapper (current: T): Boolean = current != null && current.getClass() == wrapper

	protected def construct (args: T*): T = {
		//val types: Array[Class[T]] = args.map(ttag.runtimeClass)
		tag.runtimeClass.newInstance(args)
	}

	def add (current: T, additional: T): T =
		if (isNull(current)) additional
		else construct(tag, current, additional)

	def remove (current: T, removal: T): T =
		if (isNull(current) || current == removal) empty
		else if (tag.runtimeClass.isAssignableFrom(current)) {
			val wrapper: Wrapper[T] = current.asInstanceOf[Wrapper[T]]
			val one = if (wrapper.one == removal) null else remove(wrapper.one, removal)
			val two = if (wrapper.two == removal) null else remove(wrapper.two, removal)

			(one, two) match {
				case (null, two) => two
				case (one, null) => one
				case (one, two) => add(one, two)
			}

		} else current
}
