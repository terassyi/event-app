package domain.entity

import java.time.format.DateTimeFormatter

import scala.util.Random
import java.time.LocalDateTime

object DateFormatter {

  private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:mm")
  //メソッド

  //あとで切り分け
  def generateId(): Int = {
    //乱数を生成してその値をイベントIDとする
    val r = new Random
    Math.abs(r.nextInt())
  }
  //

  //スレッドセーフ
  def date2string(date: LocalDateTime): String = {
   //文字列を日付に変換
    formatter.format(date)
  }

  def string2date(date: String): LocalDateTime = {
    LocalDateTime.from(formatter.parse(date))
  }
}
