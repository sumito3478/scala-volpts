/*Copyright 2014 sumito3478 <sumito3478@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
import sbt._
import Keys._

object Build extends Build {
  // suppress debug messages from bintray-sbt (actually async-http-client)
  import org.slf4j.LoggerFactory
  import ch.qos.logback.classic.Level
  import ch.qos.logback.classic.Logger
  LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger].setLevel(Level.INFO)

  import scalariform.formatter.preferences._
  import com.typesafe.sbt.SbtScalariform
  lazy val scalariformSettings = SbtScalariform.scalariformSettings ++ Seq(
    SbtScalariform.ScalariformKeys.preferences := FormattingPreferences()
      .setPreference(DoubleIndentClassDeclaration, true))

  import fmpp.FmppPlugin._

  lazy val bintraySettings = bintray.Plugin.bintraySettings ++ Seq(
    bintray.Keys.bintrayOrganization in bintray.Keys.bintray := Some("volpts"))

  lazy val releaseSettings = {
    import sbtrelease.ReleasePlugin.ReleaseKeys._
    sbtrelease.ReleasePlugin.releaseSettings ++ Seq(
      crossBuild := true,
      tagComment <<= (version in ThisBuild) map (v => s"Release $v"),
      commitMessage <<= (version in ThisBuild) map (v => s"Bump version number to $v"))
  }

  lazy val ratsSettings = {
    import SBTRatsPlugin._
    sbtRatsSettings ++ Seq(
      ratsMainModule <<= scalaSource {
        _ / "svolpts" / "parser" / "Volpts.syntax"
      },
      ratsUseScalaLists := true,
      ratsUseScalaOptions := true,
      ratsUseScalaPositions := true,
      ratsDefineASTClasses := true,
      ratsDefinePrettyPrinter := true,
      ratsUseKiama := true,
      ratsUseDefaultLayout := false,
      ratsUseDefaultComments := false,
      ratsUseDefaultWords := false,
      libraryDependencies += "com.googlecode.kiama" %% "kiama" % "1.5.2")
  }

  import sbtunidoc.Plugin.unidocSettings

  import Dependencies._

  lazy val svolpts = project.in(file("."))
    .settings(
      javaOptions := Seq("-Xms1024m"),
      organization := "info.sumito3478",
      scalaVersion := "2.10.3",
      crossScalaVersions := Seq("2.10.3"),
      crossVersion <<= scalaVersion { sv => if (sv contains "-" ) CrossVersion.full else CrossVersion.binary },
      fork := true,
      resolvers += Resolver.sonatypeRepo("releases"),
      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-reflect" % _),
      addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.0-M1" cross CrossVersion.full),
      licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
      libraryDependencies ++= libraries,
      scalaSource := file("src/main/scala"),
      scalacOptions ++= Seq(
        "-encoding", "utf-8",
        "-target:jvm-1.7",
        "-deprecation",
        "-feature",
        "-unchecked",
        "-Xexperimental",
        "-Xcheckinit",
        "-Xdivergence211",
        "-Xlint",
        "-Yinfer-argument-types"))
    .configs(Fmpp)
    .settings(scalariformSettings ++ fmppSettings ++ bintraySettings ++ releaseSettings ++ unidocSettings ++ ratsSettings : _*)
}
