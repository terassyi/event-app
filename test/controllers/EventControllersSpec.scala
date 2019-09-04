package controllers

import domain.entity.{DateFormatter, Event, Vote, VotingValue}
import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json._
import EventReadWrites._

class EventControllersSpec extends FlatSpec with Matchers {
//  "fromJson" should "return Option[Event] class instance." in {
//    val json: JsValue = Json.parse(
//      """
//        |{
//        |    "id": 1,
//        |    "eventName": "example_event",
//        |    "candidateDates":[
//        |    {
//        |        "date": "2019/08/30",
//        |        "votes": [
//        |                {
//        |                    "participant": "name1",
//        |                    "status": 1
//        |                },
//        |                {
//        |                    "participant": "name2",
//        |                    "status": 2
//        |                }
//        |            ]
//        |        },
//        |    {
//        |        "date": "2019/09/01",
//        |        "vote": [
//        |            {
//        |                "participant": "name1",
//        |                "status":1
//        |            },
//        |            {
//        |                "participant": "name2",
//        |                "status": 0
//        |            }
//        |            ]
//        |      }
//        |     ],
//        |    "deadline": "2019-08-29",
//        |    "comment": "example comment"
//        |  }
//      """.stripMargin)
//
//    println(json)
//    assert(EventReadWrites.fromJson(json).get.id == 1)
//  }

  "votingValueReads" should "return jsvalue" in {
    val json: JsValue = Json.parse(
      """{
            "participant":"name1",
            "status": 2
          }
      """.stripMargin)
    println(json)
//    val voteResult: JsResult[Vote] = json.validate[Vote]
//    println(voteResult)
//    val vote: Vote = voteResult match {
//      case v: JsSuccess[Vote] => v.get
//      case e: JsError => Vote("none", VotingValue.Batu)
//    }
//    println(vote)
//    assert(vote == Vote("name1", VotingValue.Maru))
  }

  "toJson" should "return json format of vote class" in {
    val vote: Vote = Vote("name1", VotingValue.Maru)
    val json: JsValue = Json.toJson(vote)
    print(json)
    assert(json == Json.parse(
      """
        |{
        | "participant": "name1",
        | "status":2
        |}
      """.stripMargin))
  }

  "toJson" should "return json format of event class" in {
    val event: Event = Event(1, "test_event",
      Map((DateFormatter.string2date("2019-08-30 19:00") -> Seq(Vote("name1", VotingValue.Sankaku), Vote("name2", VotingValue.Maru))),
        (DateFormatter.string2date("2019-09-01 19:00") -> Seq(Vote("name1", VotingValue.Sankaku), Vote("name2", VotingValue.Batu)))),
        DateFormatter.string2date("2019-08-29 19:00"),
        "test_comment")

    val a: JsValue = Json.parse(
      """
        |{
        |    "id": 1,
        |    "eventName": "test_event",
        |    "candidateDates":[
        |    {
        |        "date": "2019-08-30 19:00",
        |        "votes": [
        |                {
        |                    "participant": "name1",
        |                    "status": 1
        |                },
        |                {
        |                    "participant": "name2",
        |                    "status": 2
        |                }
        |            ]
        |        },
        |    {
        |        "date": "2019-09-01 19:00",
        |        "vote": [
        |            {
        |                "participant": "name1",
        |                "status":1
        |            },
        |            {
        |                "participant": "name2",
        |                "status": 0
        |            }
        |            ]
        |      }
        |     ],
        |    "deadline": "2019-08-29 19:00",
        |    "comment": "test_comment"
        |  }
      """.stripMargin)

    val json = Json.toJson(event)
    assert(json == a)
  }

}