import sbt.Keys._
import sbt._
import sbtrelease.Version

name := "gospel-as-you-go-backend"

resolvers ++= Seq(
    Resolver.sonatypeRepo("public"),
    "Millhouse Bintray"  at "http://dl.bintray.com/themillhousegroup/maven",
)
scalaVersion := "2.12.6"
releaseNextVersion := { ver => Version(ver).map(_.bumpMinor.string).getOrElse("Error") }
assemblyJarName in assembly := "gospel-as-you-go-backend.jar"

libraryDependencies ++= Seq(
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.7",
    "com.amazonaws" % "aws-java-sdk-sns" % "1.11.456",
    "com.amazonaws" % "aws-java-sdk-s3" % "1.11.456",
    "com.amazonaws" % "aws-lambda-java-events" % "2.2.1",
    "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
    "com.themillhousegroup" %% "scoup" % "0.4.6",
    "org.scalaz" %% "scalaz-core" % "7.2.27",
)

scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-feature",
    "-Xfatal-warnings")
