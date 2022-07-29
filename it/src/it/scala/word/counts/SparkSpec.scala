package word.counts

import org.apache.spark.sql.SparkSession
import org.scalatest.concurrent.Eventually
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Minutes, Seconds, Span}

trait SparkSpec extends AnyFlatSpec with Matchers with Eventually {

  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(
      timeout = scaled(Span(3, Minutes)),
      interval = scaled(Span(15, Seconds))
    )

  val s3endpoint = "localhost:4566"
  val awsAccessKey = "foo"
  val awsSecretKey = "foo"
  val linesTopic = "lines"
  val kafkaBroker = "localhost:29092"
  val wordCountsTable = "s3a://my-bucket/word-counts"

  lazy val spark: SparkSession =
    SparkSession
      .builder()
      .master("local[*]")
      .appName("spark-it")
      .config("spark.hadoop.fs.s3a.endpoint", s"http://$s3endpoint")
      .config(
        "spark.hadoop.fs.s3a.impl",
        "org.apache.hadoop.fs.s3a.S3AFileSystem"
      )
      .config("spark.hadoop.fs.s3a.access.key", awsAccessKey)
      .config("spark.hadoop.fs.s3a.secret.key", awsSecretKey)
      .config("spark.hadoop.fs.s3a.path.style.access", "true")
      .config("spark.hadoop.fs.s3a.change.detection.version.required", "false")
      .getOrCreate()
}
