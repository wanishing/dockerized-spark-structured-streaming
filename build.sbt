import com.typesafe.sbt.packager.docker.Cmd
import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val src: Project = (project in file("."))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    name := "dockerized-spark-structured-streaming",
    libraryDependencies ++= Dependencies.spark,
    dockerCommands := List(
      Cmd("FROM", "spark:3.3.1-hadoop3-scala2.13"),
      Cmd("ADD", "opt/docker/lib", "/opt/spark/jars/"),
      Cmd("USER", "${spark_uid}"),
      Cmd("EXPOSE", "4040"),
      Cmd("WORKDIR", "/opt/spark/work-dir")
    ),
    dockerGroupLayers := PartialFunction.empty
  )

lazy val it: Project = (project in file("it"))
  .configs(IntegrationTest)
  .settings(
    name := "it",
    Defaults.itSettings,
    libraryDependencies ++= Dependencies.test
  )
