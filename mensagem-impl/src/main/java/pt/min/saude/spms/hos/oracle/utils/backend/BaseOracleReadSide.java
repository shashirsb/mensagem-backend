 // Failed to get sources. Instead, stub sources have been generated by the disassembler.
 // Implementation of methods is unavailable.
 package pt.min.saude.spms.hos.oracle.utils.backend;
 public abstract class BaseOracleReadSide<T> implements pt.min.saude.spms.hos.common.classes.backend.Upgradable {
   
   protected final pt.min.saude.spms.hos.common.classes.backend.LogBuilder log;
   
   protected final com.lightbend.lagom.javadsl.persistence.jpa.JpaSession jpaSession;
   protected final java.lang.String oracleDatabase;
   
   protected final java.lang.String tableName;
   
   protected final com.fasterxml.jackson.databind.ObjectMapper objectMapper;
   
   protected final java.lang.Class<T> stateClass;
   
   protected com.datastax.driver.core.PreparedStatement upsertStatement;
   
   public  BaseOracleReadSide(pt.min.saude.spms.hos.common.classes.backend.LogBuilder log, com.typesafe.config.Config configuration, com.lightbend.lagom.javadsl.persistence.jpa.JpaSession jpaSession, io.vavr.control.Option<java.lang.String> configKeyOracleKeyspace, akka.actor.ActorSystem actorSystem, java.lang.Class<T> stateClass) {}
   
  //  public  BaseOracleReadSide(pt.min.saude.spms.hos.common.classes.backend.LogBuilder log, com.typesafe.config.Config configuration, com.lightbend.lagom.javadsl.persistence.jpa.JpaSession jpaSession, io.vavr.control.Option<java.lang.String> configKeyOracleKeyspace, io.vavr.control.Option<java.lang.String> tableName, akka.actor.ActorSystem actorSystem, java.lang.Class<T> stateClass) {
  //  }
   
   public  io.vavr.concurrent.Future<com.datastax.driver.core.BoundStatement> upsert(java.lang.String id, T object, pt.min.saude.spms.hos.common.classes.backend.LogPrincipal principal) {
     return null;
   }
   
   public  io.vavr.concurrent.Future<com.datastax.driver.core.BoundStatement> upsert(java.lang.String id, T object, io.vavr.control.Option<pt.min.saude.spms.hos.common.classes.backend.Stamp> stamp) {
     return null;
   }
   
   public  io.vavr.concurrent.Future<io.vavr.control.Option<T>> select(java.lang.String id, pt.min.saude.spms.hos.common.classes.backend.LogPrincipal logPrincipal) {
     return null;
   }
   
   public  io.vavr.concurrent.Future<io.vavr.control.Option<T>> select(java.lang.String id, io.vavr.control.Option<pt.min.saude.spms.hos.common.classes.backend.Stamp> stamp) {
     return null;
   }
   
   public  io.vavr.concurrent.Future<akka.Done> setup() {
     return null;
   }
   
   public  java.util.concurrent.CompletableFuture<akka.Done> prepare() {
     return null;
   }
   
   protected  java.util.concurrent.CompletableFuture<akka.Done> createTable() {
     return null;
   }
   
   protected <E> java.util.concurrent.CompletableFuture<akka.Done> registerCodecs(com.datastax.driver.core.Session session, java.lang.Class<E> tClass) {
     return null;
   }
   
   protected  java.util.concurrent.CompletableFuture<akka.Done> prepareStatements() {
     return null;
   }
   
   private static java.lang.Object $deserializeLambda$(java.lang.invoke.SerializedLambda arg0) {
     return null;
   }
   
   private akka.Done lambda$prepareStatements$27(io.vavr.Lazy context, com.datastax.driver.core.PreparedStatement r, java.lang.Throwable t) {
     return null;
   }
   
   private com.datastax.driver.core.PreparedStatement lambda$prepareStatements$26(com.datastax.driver.core.PreparedStatement statement) {
     return null;
   }
   
   private java.lang.String lambda$prepareStatements$25() {
     return null;
   }
   
   private akka.Done lambda$registerCodecs$24(io.vavr.Lazy context, com.datastax.driver.core.CodecRegistry r, java.lang.Throwable t) {
     return null;
   }
   
   private com.datastax.driver.core.CodecRegistry lambda$registerCodecs$23(com.datastax.driver.core.Session session, java.lang.Class tClass) {
     return null;
   }
   
   private java.lang.String lambda$registerCodecs$22() {
     return null;
   }
   
   private akka.Done lambda$createTable$21(io.vavr.Lazy context, akka.Done r, java.lang.Throwable t) {
     return null;
   }
   
   private java.lang.String lambda$createTable$20() {
     return null;
   }
   
   private akka.Done lambda$prepare$19(io.vavr.Lazy context, java.util.concurrent.CompletableFuture r, java.lang.Throwable t) {
     return null;
   }
   
   private java.util.concurrent.CompletableFuture lambda$prepare$18(java.util.concurrent.CompletableFuture doneRegister) {
     return null;
   }
   
   private java.util.concurrent.CompletableFuture lambda$prepare$17(com.datastax.driver.core.Session session) {
     return null;
   }
   
   private java.lang.String lambda$prepare$16() {
     return null;
   }
   
   private akka.Done lambda$setup$15(io.vavr.Lazy context, akka.Done r, java.lang.Throwable t) {
     return null;
   }
   
   private java.lang.String lambda$setup$14() {
     return null;
   }
   
   private void lambda$select$13(io.vavr.Lazy context, io.vavr.control.Option stamp, java.lang.Throwable failure) {
   }
   
   private io.vavr.control.Option lambda$select$12(java.util.Optional optionalRow) {
     return null;
   }
   
   private java.lang.Object lambda$null$11(com.datastax.driver.core.Row row) {
     return null;
   }
   
   private java.lang.String lambda$select$10(java.lang.String id, io.vavr.control.Option stamp) {
     return null;
   }
   
   private static java.lang.String lambda$null$9(pt.min.saude.spms.hos.common.classes.backend.Stamp s) {
     return null;
   }
   
   private void lambda$select$8(io.vavr.Lazy context, pt.min.saude.spms.hos.common.classes.backend.LogPrincipal logPrincipal, java.lang.Throwable failure) {
   }
   
   private io.vavr.control.Option lambda$select$7(java.util.Optional optionalRow) {
     return null;
   }
   
   private java.lang.Object lambda$null$6(com.datastax.driver.core.Row row) {
     return null;
   }
   
   private java.lang.String lambda$select$5(java.lang.String id, pt.min.saude.spms.hos.common.classes.backend.LogPrincipal logPrincipal) {
     return null;
   }
   
   private void lambda$upsert$4(io.vavr.Lazy context, io.vavr.control.Option stamp, java.lang.Throwable failure) {
   }
   
   private com.datastax.driver.core.BoundStatement lambda$upsert$38814393$1(java.lang.String id, java.lang.Object object) throws java.lang.Throwable {
     return null;
   }
   
   private java.lang.String lambda$upsert$3(java.lang.String id, java.lang.Object object, io.vavr.control.Option stamp) {
     return null;
   }
   
   private static java.lang.String lambda$null$2(pt.min.saude.spms.hos.common.classes.backend.Stamp s) {
     return null;
   }
   
   private void lambda$upsert$1(io.vavr.Lazy context, pt.min.saude.spms.hos.common.classes.backend.LogPrincipal principal, java.lang.Throwable failure) {
   }
   
   private com.datastax.driver.core.BoundStatement lambda$upsert$47c33edb$1(java.lang.String id, java.lang.Object object) throws java.lang.Throwable {
     return null;
   }
   
   private java.lang.String lambda$upsert$0(java.lang.String id, java.lang.Object object, pt.min.saude.spms.hos.common.classes.backend.LogPrincipal principal) {
     return null;
   }
 }