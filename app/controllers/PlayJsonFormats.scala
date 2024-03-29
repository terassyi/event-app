package controllers

import java.time.LocalDateTime

import domain.entity.{Candidate, CandidateDates, Event, Vote, VotingValue}
import play.api.libs.functional.syntax._
import play.api.libs.json._

object PlayJsonFormats {
  //jsonへの変換
  /*
  eventの抽象構文木の例
  {
    "id": 1,
    "eventName": "example_event",
    "candidateDates" [
      {
        date: "2019",
        votes: [
              {
                "participant": "name1",
                "status": 1
              },
               ...
            ]
        }
      },
      {
        date: "2019",
        vote: {
        }
      },

     ]
    "deadline": "2019-08-29",
    "comment": "example comment"
  }
   */
  //  case class CandidateDates(date: LocalDateTime, votes: Seq[Vote])

  /*
     it's a wrapper class which helps to convert Map -> Array
   */


  implicit val votingValueReads: Reads[VotingValue] = (value: JsValue) =>
    JsSuccess(VotingValue.from(value.validate[Int].getOrElse(0)))

  implicit val voteReads: Reads[Vote] = (
    (__ \ "participant").read[String] and
      (__ \"status").read[VotingValue]
    )(Vote)


  implicit val candidateReads: Reads[Candidate] = (
    (__ \ "date").read[LocalDateTime] and
      (__ \ "votes").read[Seq[Vote]]
    )(Candidate.apply _)

  //  implicit val candidateSeqReads: Reads[Seq[Candidate]] = Reads.seq(candidateReads)

  implicit val candidateDatesReads: Reads[CandidateDates] = __.read[Seq[Candidate]].map(CandidateDates)



  //  implicit val candidateDatesReads: Reads[CandidateDates] = (v: JsValue) =>
  //    v.

  implicit val eventReads: Reads[Event] = (
    (__ \ "id").read[Int] and
      (__ \ "eventName").read[String] and
      (__ \ "candidateDates").read[CandidateDates] and
      (__ \ "deadline").read[LocalDateTime] and
      (__ \ "comment").read[String]
    )(Event.apply _)


  def eventToJson(event: Event): JsValue = Json.toJson(event)

  def eventListToJson(eventList: Map[Int, String]) = Json.toJson(eventList)

  implicit val votingValueWrites: Writes[VotingValue] = (v: VotingValue) =>
    Json.toJson(VotingValue.toInt(v))

  implicit val voteWrites: Writes[Vote] = (
    (__ \ "participant").write[String] and
      (__ \ "status").write[VotingValue]
    )(unlift(Vote.unapply))

  implicit val candidateWrites: Writes[Candidate] = (
    (__ \ "date").write[LocalDateTime] and
      (__ \ "votes").write[Seq[Vote]]
    )(unlift(Candidate.unapply))

  implicit val candidateDatesWrites: Writes[CandidateDates] = (c: CandidateDates) =>
    Json.toJson(c.candidates)

  implicit val eventWrites: Writes[Event] = (
    (__ \ "id").write[Int] and
      (__ \ "eventName").write[String] and
      (__ \ "candidateDates").write[CandidateDates] and
      (__ \ "deadline").write[LocalDateTime] and
      (__ \ "comment").write[String]
    )(unlift(Event.unapply))

  implicit val eventAndPlannerWrites: Writes[(Event, String)] = (
    (__ \ "event").write[Event] and
      (__ \ "planner").write[String]
    ).tupled

  implicit val eventAndPlannerReads: Reads[(Event, String)] = (
    (__ \ "event").read[Event] and
      (__ \ "planner").read[String]
  ).tupled


  def eventFromJson(value: JsValue): Option[Event] = {
    val eventResult: JsResult[Event] = value.validate[Event]
    eventResult match {
      case e: JsSuccess[Event] => e.asOpt
      case e: JsError => None
    }
  }

  def eventAndPlannerFromJson(value: JsValue): Option[(Event, String)] = {
    val eventAndPlannerResult: JsResult[(Event, String)] = value.validate[(Event, String)]
    eventAndPlannerResult match {
      case ep: JsSuccess[(Event, String)] => ep.asOpt
      case e: JsError => None
    }
  }


  //イベント一覧取得
  //json構造
  /*
  {
    [
      {
        "id": 1,
        "eventName": "test1"
      },
      {
        "id": 2,
        "eventName": "test2"
      }
    ]
  }
   */


  //イベントの変更のJsonのやりとり

  //投票内容のJsonのやりとり

  //投票のJsonの構造
  /*
  {
    "id": 1,
    "participant": "name1",
    "votes":[
              {
                "date": "2019-09-04T19:00:00",
                "status": 2
              },
              {
                "date": "2019-09-05T20:00:00",
                "status": 0
              }
            ]
  }
   */
  case class ParticipateStatus(date: LocalDateTime, status: VotingValue)

  case class Voting(id: Int, participant: String, votes: Seq[ParticipateStatus])

  implicit val participateStatusReads: Reads[ParticipateStatus] = (
    (__ \ "date").read[LocalDateTime] and
      (__ \ "status").read[VotingValue]
    )(ParticipateStatus.apply _)

  implicit val votingReads: Reads[Voting] = (
    (__ \ "id").read[Int] and
      (__ \ "participant").read[String] and
      (__ \ "votes").read[Seq[ParticipateStatus]]
    )(Voting.apply _)

  def votingFromJson(value: JsValue): Option[Voting] = {
    val votingResult: JsResult[Voting] = value.validate[Voting]
    votingResult match {
      case v: JsSuccess[Voting] => v.asOpt
      case e: JsError => None
    }
  }

  //投票結果のJsonのやりとり
  //Jsonの構造
  /*
  {[
    {
      "date": "2019-09-04T19:00:00",
      "result": 10
     },
     {
      "date": "2019-09-05T19:00:00",
      "result": 5
     }
    ]
  }
   */

  case class VotingResult(date: LocalDateTime, result: Int)
  implicit val votingResultWrites: Writes[VotingResult] = (
    (__ \ "date").write[LocalDateTime] and
      (__ \ "result").write[Int]
    )(unlift(VotingResult.unapply))

  implicit val eventNameAndCommentReads: Reads[(Int, String)] = (
    (__ \ "id").read[Int] and
      (__ \ "change").read[String]
  )tupled

  implicit val eventCandidateDatesReads: Reads[(Int, CandidateDates)] = (
    (__ \ "id").read[Int] and
      (__ \ "candidateDates").read[CandidateDates]
  )tupled

  implicit val eventDeadLineReads: Reads[(Int, LocalDateTime)] = (
    (__ \ "id").read[Int] and
      (__ \ "deadline").read[LocalDateTime]
  )tupled




  def eventNameFromJson(value: JsValue): Option[(Int, String)] = value.validate[(Int, String)] match {
    case v: JsSuccess[(Int, String)] => v.asOpt
    case e: JsError => None
  }

  def eventCandidateDatesFromJson(value: JsValue): Option[(Int, CandidateDates)] = {
    value.validate[(Int, CandidateDates)] match {
      case v: JsSuccess[(Int, CandidateDates)] => v.asOpt
      case e: JsError => None
    }
  }

  def eventDeadLineFromJson(value: JsValue): Option[(Int, LocalDateTime)] = {
    value.validate[(Int, LocalDateTime)] match {
      case v: JsSuccess[(Int, LocalDateTime)] => v.asOpt
      case e: JsError => None
    }
  }

  def eventCommentFromJson(value: JsValue): Option[(Int, String)] = {
    value.validate[(Int, String)] match {
      case v: JsSuccess[(Int, String)] => v.asOpt
      case e: JsError => None
    }
  }

}
