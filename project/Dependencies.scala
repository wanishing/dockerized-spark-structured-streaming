import sbt._

object Dependencies {

  val spark: List[ModuleID] = List(
    "org.apache.spark" %% "spark-sql" % "3.3.0",
    "org.apache.spark" %% "spark-sql-kafka-0-10" % "3.3.0",
    "org.apache.hadoop" % "hadoop-aws" % "3.3.2",
    "io.delta" %% "delta-core" % "1.2.1"
  )

  val test: List[ModuleID] =
    (List(
      "org.scalatest" %% "scalatest-flatspec" % "3.2.12",
      "org.scalatest" %% "scalatest" % "3.2.12"
    ) ++ spark)
      .map(_ % IntegrationTest)

}
