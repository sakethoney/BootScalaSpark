package com.example.demo.work

import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.mllib.recommendation.Rating 
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame


object MovieRatings extends InitSpark {

  def CalculateAverageRating() = {
    import spark.implicits._

    val version = spark.version
    println("SPARK VERSION = " + version)

    //val sumHundred = spark.range(1, 101).reduce(_ + _)
    //println(f"Sum 1 to 100 = $sumHundred")

    //val sourceFile = "hdfs://sandbox.hortonworks.com:8020/user/maria_dev/ratings.csv"
    val sourceFile = "ratings.csv"
    //val sourceFile = "/Users/sakethoney/Downloads/ml-latest-small/ratings.csv"

    println("Reading from csv file: " + sourceFile)
    //val ratings: Dataset[Rating] = reader.csv(sourceFile).as[Rating]

    val rawRatings: DataFrame[RawRatingData] = reader.format("com.databricks.spark.csv").load(sourceFile).cache().as[RawRatingData]
    rawRatings.show(10)

    val averageRating = rawRatings.groupBy("movieId").avg("rating")
    averageRating.show(10)
    val counts = rawRatings.groupBy("movieId").count()
    counts.show(10)
    val averagesAndCount = counts.join(averageRating, "movieId")
    averagesAndCount.show(10)


    val topTen = averagesAndCount.orderBy("avg(rating)").show(10)

    val ratings: RDD[Rating] = rawRatings.map((rrd: RawRatingData) => Rating(rrd.userId, rrd.movieId, rrd.rating)).rdd

    // Build the recommendation model using ALS
    val rank = 10
    val numIterations = 10
    val model = ALS.train(ratings, rank, numIterations, 0.01)

    //    persons.show(2)
    //    val averageAge = persons.agg(avg("age"))
    //                     .first.get(0).asInstanceOf[Double]
    //    println(f"Average Age: $averageAge%.2f")

    close
  }

}
