import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object StreamingWordCounts extends App {

  val kafkaBroker: String = System.getenv("KAFKA_BROKER")
  val wordsTopic: String = System.getenv("LINES_TOPIC_NAME")
  val bucket: String = System.getenv("S3_BUECKT")
  val table: String = System.getenv("S3_TABLE_KEY")
  val s3endpoint: String = System.getenv("AWS_S3_ENDPOINT")
  val awsAccessKey: String = System.getenv("AWS_ACCESS_KEY_ID")
  val awsSecretKey: String = System.getenv("AWS_SECRET_ACCESS_KEY")

  val spark: SparkSession = SparkSession.builder
    .master("local[*]")
    .appName("StreamingWordCounts")
    .config("spark.hadoop.fs.s3a.endpoint", s"http://$s3endpoint")
    .config(
      "spark.hadoop.fs.s3a.impl",
      "org.apache.hadoop.fs.s3a.S3AFileSystem"
    )
    .config("spark.hadoop.fs.s3a.access.key", awsAccessKey)
    .config("spark.hadoop.fs.s3a.secret.key", awsSecretKey)
    .config("spark.hadoop.fs.s3a.path.style.access", "true")
    .config("spark.hadoop.fs.s3a.change.detection.version.required", "false")
    .config("spark.sql.shuffle.partitions", "6")
    .config(
      "spark.jars.ivy",
      "/tmp/.ivy2"
    )
    .getOrCreate()

  import spark.implicits._

  // Create DataFrame representing the stream of input lines from kafka topic
  val lines: Dataset[String] = spark.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", kafkaBroker)
    .option("subscribe", wordsTopic)
    .load()
    .selectExpr("CAST(value AS STRING)")
    .as[String]

  // Split the lines into words
  val words: Dataset[String] = lines
    .flatMap(_.split(" "))
    .withColumnRenamed("value", "word")
    .as[String]

  // Generate word count
  val wordCounts: DataFrame = words
    .groupBy("word")
    .count()

  // Writing the counts to delta table
  val query: StreamingQuery = wordCounts.writeStream
    .outputMode(OutputMode.Complete())
    .format("delta")
    .option("checkpointLocation", s"s3a://$bucket/_checkpoints/")
    .start(s"s3a://$bucket/$table/")

  query.awaitTermination()

}
