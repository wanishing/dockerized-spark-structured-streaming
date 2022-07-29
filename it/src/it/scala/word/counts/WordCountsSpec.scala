package word.counts

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.rand

class WordCountsSpec extends SparkSpec {

  it should "count words from lines" in {
    import spark.implicits._
    val lines: DataFrame =
      Seq("dog", "cat dog")
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
          .as[(String, Long)]
          .collect()
          .toMap

      wordToCount shouldEqual Map("dog" -> 2, "cat" -> 1)
    }

  }
}
