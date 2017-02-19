import org.apache.spark.SparkContext

object  ScalaApp {

  def main(args: Array[String]) {
    val sc = new SparkContext("local[2]", "First Spark App")
    val data = sc.textFile("data/UserPurchaseHistory.csv")
      .map(line => line.split(","))
      .map(purchaseRecord => (purchaseRecord(0), purchaseRecord(1), purchaseRecord(2)))
    val numParchases = data.count()
    val uniqueUser = data.map { case (user, product, price) => user }.distinct().count()
    val totalRevenue = data.map { case (user, product, price) => price.toDouble }.sum()
    val productsByPopularity = data
      .map { case (user, product, price) => (product, 1) }
      .reduceByKey(_ + _)
      .collect()
      .sortBy(-_._2)
    val mostPopular = productsByPopularity(0)
    println("total purchases :" + numParchases)
    println("uique user:" + uniqueUser)
    println("total revenue:" + totalRevenue)
    println("Most popular product:%s with :%d purchases".format(mostPopular._1, mostPopular._2))
  }
}
