organization := "com.gses"

name := "listeners"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.2"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

javacOptions in doc := Seq("-source", "1.8")

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.2"

//override def managedStyle = ManagedStyle.Maven

//lazy val publishTo = Resolver.file("local", Path.userHome / ".m2" / "repository" asFile)
