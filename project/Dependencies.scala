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

object Dependencies {
  val logback = "ch.qos.logback" % "logback-classic" % "1.0.13"

  val scalatest = "org.scalatest" %% "scalatest" % "2.0+" // we need 2.0.1-SNAP<n> for Scala 2.11.0

  val slf4j = "org.slf4j" % "slf4j-api" % "1.7.5"

  val lz4 = "net.jpountz.lz4" % "lz4" % "1.2.0"

  val async_http_client = "com.ning" % "async-http-client" % "1.7.23"

  object netty {
    object constants {
      val version = "4.0.14.Final"
      val name = "netty"
      val group = "io.netty"
    }
    import constants._
    val Seq(buffer, http) = Seq("buffer", "codec-http").map(a => group % s"$name-$a" % version)
  }
  object jackson {
    object constants {
      val version = "2.3.1"
      val name = "jackson"
      object group {
        val prefix = s"com.fasterxml.$name"
        val core = s"$prefix.core"
        val module = s"$prefix.module"
      }
      val module = s"$name-module"
    }
    import constants._
    val Seq(core, databind) = Seq("core", "databind").map(a => group.core % s"$name-$a" % version)
    val Seq(afterburner) = Seq("afterburner").map(a => group.module % s"$module-$a" % version)
  }
  object commons {
    val io = "commons-io" % "commons-io" % "2.4"
  }
  object dispatch {
    object constants {
      val version = "0.11.0"
      val name = "dispatch"
      val group = "net.databinder.dispatch"
    }
    import constants._
    val Seq(core) = Seq("core").map(a => group %% s"$name-$a" % version)
  }
  object akka {
    object constants {
      val version = "2.2.3"
      val name = "akka"
      val group = "com.typesafe.akka"
    }
    import constants._
    val Seq(actor) = Seq("actor").map(a => group %% s"$name-$a" % version)
  }
  val libraries = Seq(slf4j, commons.io, akka.actor, scalatest, logback, netty.buffer, lz4, jackson.core, jackson.databind,
    jackson.afterburner, async_http_client, dispatch.core)
}
