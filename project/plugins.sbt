// The Lagom plugin
addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.5.4")
// Platform Tooling plugin
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.4.0")
// Sbt-buildinfo generates Scala source from your build definitions.
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")


addSbtPlugin("net.aichler" % "sbt-jupiter-interface" % "0.8.2")
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.1.0")

//uncomment when needed...
//addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")
//addSbtPlugin("io.get-coursier" % "sbt-coursier" % "2.0.0-RC5-2")