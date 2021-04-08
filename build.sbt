import com.lightbend.lagom.core.LagomVersion
import sbt.Keys.javacOptions
import scala.concurrent.duration._

//-------------------------------------------------Versions-------------------------------------------------------------
val mensagemAPIVersion = "1.6"
val mensagemVersion = "2.1.0-ORCL_RC1"
val junit5Version = "5.3.1"
//---------------------------------------------------Dev----------------------------------------------------------------

lagomKafkaEnabled in ThisBuild := true
lagomKafkaCleanOnStart in ThisBuild := true
lagomServiceGatewayPort in ThisBuild := 9020
lagomServiceLocatorPort in ThisBuild := 9021
lagomCassandraPort in ThisBuild := 4010
lagomCassandraJvmOptions in ThisBuild := Seq("-Dcassandra.jmx.local.port=4100")
lagomCassandraCleanOnStart in ThisBuild := true
lagomCassandraMaxBootWaitingTime in ThisBuild := 180.seconds
lagomUnmanagedServices in ThisBuild := Map(
  "dominio-central-backend" -> "http://central.s3.dev.chbv.min-saude.pt",
  "acesso-backend" -> "http://backend.s3.dev.chbv.min-saude.pt"
)
//--------------------------------------------------Common--------------------------------------------------------------
version in ThisBuild := "1.1.0"
organization in ThisBuild := "pt.min.saude.spms.hos.base"
scalaVersion in ThisBuild := "2.12.6"
javacOptions in ThisBuild := Seq("-parameters")
updateOptions in ThisBuild := updateOptions.value.withGigahorse(false)
resolvers in ThisBuild += "Timestamp Nexus Repo" at nexusURL + "groups/maven-public"
val nexusURL = s"https://nexus.devops-spms.xyz/nexus/content/"
publishTo in ThisBuild := {
  if (isSnapshot.value)
    Some("SPMS Snapshots Nexus Repo" at nexusURL + "repositories/snapshots/")
  else
    Some("SPMS Releases Nexus Repo" at nexusURL + "repositories/releases/")
}
//----------------------------------------------------Modules-----------------------------------------------------------
val hosNS = "pt.min.saude.spms.hos"
val hosCommonNS = hosNS + ".common"
val commonClasses = hosCommonNS % "common-classes_2.12" % "2.0.3" //% withTest
val sharedKernelVersions = "2.1.0-RC13"
val serviceUtils = hosCommonNS % "service-utils_2.12" % sharedKernelVersions
val elasticUtils = hosCommonNS % "elastic-utils_2.12" % sharedKernelVersions
val cassandraUtils = hosCommonNS % "cassandra-utils_2.12" % sharedKernelVersions
val loggingUtils = hosCommonNS % "logging-utils_2.12" % "2.1.0-RC15"
val logbackKafkaAppender = "com.github.danielwegener" % "logback-kafka-appender" % "0.2.0-RC1"
//val vavrJackson = "io.vavr" % "vavr-jackson" % "0.10.0"
val junit5Jupiter = Seq("org.junit.jupiter" % "junit-jupiter-api" % junit5Version % Test,
  "org.junit.jupiter" % "junit-jupiter-params" % junit5Version % Test,
  "org.junit.jupiter" % "junit-jupiter-engine" % junit5Version % Test)
val junitInterface = Seq("org.junit.platform" % "junit-platform-runner" % "1.3.2" % Test,
  "net.aichler" % "jupiter-interface" % "0.7.0" % Test)
val lagomAkkaServiceDiscovery = "com.lightbend.lagom" %% "lagom-javadsl-akka-discovery-service-locator" % LagomVersion.current
val akkaDiscoverK8sApi = "com.lightbend.akka.discovery" %% "akka-discovery-kubernetes-api" % "1.0.0"
val hibernate = "org.hibernate" % "hibernate-core" % "5.4.27.Final"

val jpaApi                 = "org.hibernate.javax.persistence" % "hibernate-jpa-2.1-api"   % "1.0.0.Final"
val validationApi          = "javax.validation"                % "validation-api"          % "1.1.0.Final"

val akkaStreamTestkit    = "com.typesafe.akka" %% "akka-stream-testkit"    % LagomVersion.akka
val akkaDiscoveryKubernetesApi = "com.lightbend.akka.discovery" %% "akka-discovery-kubernetes-api"                % "1.0.9"
val lagomJavadslAkkaDiscovery  = "com.lightbend.lagom"          %% "lagom-javadsl-akka-discovery-service-locator" % LagomVersion.current


lazy val jacocoSettings =
  jacocoReportSettings := JacocoReportSettings(
    formats = Seq(
      JacocoReportFormats.ScalaHTML,
      JacocoReportFormats.XML
    )
  )

lazy val `mensagem-backend` = (project in file("."))
  .aggregate(
    `mensagem-api`, 
    `mensagem-impl`
  )
  .settings(
    jacocoAggregateReportSettings := JacocoReportSettings(
      formats = Seq(
        JacocoReportFormats.ScalaHTML,
        JacocoReportFormats.XML
      )
    )
      withFileEncoding ("UTF-8")
  )

lazy val `mensagem-api` = (project in file("mensagem-api"))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    publishTo := {
      if (isSnapshot.value)
        Some("SPMS Snapshots Nexus Repo" at nexusURL + "repositories/snapshots/")
      else
        Some("SPMS Releases Nexus Repo" at nexusURL + "repositories/releases/")
    },
    version := mensagemAPIVersion,
    buildInfoOptions += BuildInfoOption.BuildTime,
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lagomJavadslJackson,
      commonClasses,
      lagomAkkaServiceDiscovery,
      logback,
    ),
    libraryDependencies ++= junit5Jupiter,
    libraryDependencies ++= junitInterface,
    libraryDependencies ++= Seq(javaJdbc),
    jacocoSettings,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "pt.min.saude.spms.hos.base.mensagem.backend.api"
  )

lazy val `mensagem-impl` = (project in file("mensagem-impl"))
  .enablePlugins(LagomJava, BuildInfoPlugin)
  .settings(resolvers += "OAM 11g" at "https://maven.oracle.com",
    credentials += Credentials("OAM 11g", "login.oracle.com/mysso/signon.jsp", "shashi.rsb@hotmail.com", "Hitmewell123"),
    libraryDependencies += "com.oracle.ojdbc" % "ojdbc8" % "19.3.0.0"
    )
  .settings(
    version := mensagemVersion,
    lagomKafkaEnabled in ThisBuild := true,
    buildInfoOptions += BuildInfoOption.BuildTime,
    scalacOptions in(Compile, doc) := Seq("-groups", "-implicits"),
    libraryDependencies ++= Seq(
      lagomJavadslJackson,
      lagomJavadslPersistenceCassandra,
      lagomJavadslPersistenceJdbc,
      lagomJavadslPersistenceJpa,
      javaJdbc,
      lagomJavadslKafkaBroker,
      lagomLogback,
      filters,
      hibernate,
      commonClasses,
      serviceUtils,
      elasticUtils,
      cassandraUtils,
      loggingUtils,
      logback,
      logbackKafkaAppender,
      akkaDiscoverK8sApi,
      lagomAkkaServiceDiscovery
//      vavrJackson,
    ),
    libraryDependencies ++= junit5Jupiter,
    libraryDependencies ++= junitInterface,
    libraryDependencies ++= Seq(javaJdbc),
    jacocoSettings,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "pt.min.saude.spms.hos.base.mensagem.backend.impl"
  )
  .dependsOn(`mensagem-api`)
  .settings(lagomForkedTestSettings: _*)
