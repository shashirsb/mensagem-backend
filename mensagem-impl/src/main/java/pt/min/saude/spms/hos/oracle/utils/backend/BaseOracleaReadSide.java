 // Failed to get sources. Instead, stub sources have been generated by the disassembler.
 // Implementation of methods is unavailable.
 package pt.min.saude.spms.hos.oracle.utils.backend;
 public abstract class BaseOracleaReadSide<T> implements pt.min.saude.spms.hos.common.classes.backend.Upgradable {
   
   protected final pt.min.saude.spms.hos.common.classes.backend.LogBuilder log;
   

   protected final com.lightbend.lagom.javadsl.persistence.jpa.JpaSession jpaSession;
   
   protected final java.lang.String oracleDatabase;
   
   protected final java.lang.String tableName;
   
   protected final com.fasterxml.jackson.databind.ObjectMapper objectMapper;
   
   protected final java.lang.Class<T> stateClass;
   
   protected com.datastax.driver.core.PreparedStatement upsertStatement;
   
   protected final BaseOracleReadSide(pt.min.saude.spms.hos.common.classes.backend.LogBuilder log, com.typesafe.config.Config configuration, com.lightbend.lagom.javadsl.persistence.jpa.JpaSession jpaSession, io.vavr.control.Option<java.lang.String> configKeyOracleDatabase, akka.actor.ActorSystem actorSystem, java.lang.Class<T> stateClass) {
   }
   
   protected final BaseOracleReadSide(pt.min.saude.spms.hos.common.classes.backend.LogBuilder log, com.typesafe.config.Config configuration, com.lightbend.lagom.javadsl.persistence.jpa.JpaSession jpaSession, io.vavr.control.Option<java.lang.String> configKeyOracleDatabase, io.vavr.control.Option<java.lang.String> tableName, akka.actor.ActorSystem actorSystem, java.lang.Class<T> stateClass) {
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
   
 }