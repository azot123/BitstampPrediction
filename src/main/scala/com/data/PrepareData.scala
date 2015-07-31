package com.data

import java.io.File
import java.util

import com.mongodb.casbah.Imports._
import com.github.tototoshi.csv.CSVReader
import net.finmath.timeseries.models.parametric.{ARMAGARCH, GARCH}
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.log4j.Logger

case class Price(timestamp: Int, price: Double, volume: Double)

/**
 * Created by cenk on 7/30/15.
 */
object PrepareData extends App {
  val logger = Logger.getLogger(PrepareData.getClass())

  val prices = new util.ArrayDeque[Price]()

  def readFromCsv(): Unit = {
    try {
      val reader = CSVReader.open(new File("sample.csv"))
      val it = reader.iterator
      while (it.hasNext) {
        val priceSeq = it.next()
        print("reading the price for " + priceSeq(0))
        prices.add(new Price(
          priceSeq(0).toInt,
          priceSeq(1).toString().toDouble,
          priceSeq(2).toString().toDouble
        ))
        if (prices.size() == 30) {
          val pList = List[Double]()
          val itr = prices.iterator()
          for (itr.hasNext){
            pList :+ itr.next().price
          }
          val pricesArr: Array[Double]  = (pList map (_.toDouble)).toArray
          //createModelsAndInsertToDb(price = prices.getLast(),(new ARMAGARCH(pricesArr)).,GARCH(pricesArr) )
        }
      }
    } catch {
      case exp: Exception => {
        logger.error(exp)
      }
    }
  }


  private def createModelsAndInsertToDb(price: Price, arma: Double, arch: Double): Option[Boolean] = {
    try {

      val db = MongoConnections.mongoClient
      val builder = MongoDBObject.newBuilder
      val dataRow = MongoDBObject(
        "timestamp" -> price.timestamp,
        "price" -> price.price,
        "volume" -> price.volume,
        "arma" -> arma,
        "arch" -> arch
      )
      db.insert(dataRow, WriteConcern.Safe)
      Some(true)
    } catch {
      case exp: Exception => {
        logger.error("Cannot insert the row to db", exp)
        return Some(false)
      }
    }
  }
}

object MongoConnections {
  final val collectionName = "bitstamp"
  final val dbName = "btcPrices"
  lazy val mongoClient = MongoClient("localhost", 27017)(dbName)(collectionName)
}
