import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, rand}

class WordCountsSpec extends SparkSpec {

  it should "count words from lines" in {
    import spark.implicits._
    val lines: DataFrame =
      Seq("There are these two young fish swimming along",
          "and they happen to meet an older fish swimming the other way")
        .toDF("value")
        .withColumn("key", rand())
        .selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")

    lines.write
      .format("kafka")
      .option("kafka.bootstrap.servers", kafkaBroker)
      .option("topic", linesTopic)
      .save()

    eventually {
      val wordToCount: Map[String, Long] =
        spark.read
          .format("delta")
          .load(wordCountsTable)
          .filter(col("word").isInCollection(Seq("fish", "way")))
          .as[(String, Long)]
          .collect()
          .toMap

      wordToCount shouldEqual Map("fish" -> 2, "way" -> 1)
    }

  }
}
