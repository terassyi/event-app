
package entity

import java.time.LocalDateTime

//参加者のクラス
case class Participant(name: String) {

  //メソッド定義
  def createVoting(event: Event, date:  LocalDateTime, votingStatus: Int): Event = {
    //投票する
    val newVoting: Seq[Vote] = event.candidateDates(date) :+ Vote(name, Int2VotingValue(votingStatus))
    event.copy(
      candidateDates = event.candidateDates + (date -> newVoting)
    )
  }

  def updateVoting(event: Event, date: LocalDateTime, votingStatus:  Int): Event = {
    //投票内容を変更する
//    val newVoting: Seq[Vote] = event.candidateDates(date).collect{
//      case v.name == name => Vote(name, Int2VotingValue(votingStatus))
//    }

    val newVoting: Seq[Vote] = event.candidateDates(date).filter(vote => vote.name != name)

    event.copy (
      candidateDates = (event.candidateDates - date) + (date -> (newVoting :+ Vote(name, Int2VotingValue(votingStatus))))
      )
  }

  def deleteVoting(event: Event, date: LocalDateTime): Event = {
    //投票を削除する
    val newVoting: Seq[Vote] = event.candidateDates(date).filter(vote => vote.name != name)
    event.copy(
      candidateDates = (event.candidateDates - date) + (date -> newVoting)
    )
  }

  def Int2VotingValue(votingStatus: Int): VotingValue = {
    votingStatus match {
    case 2 => VotingValue.Maru
    case 0 => VotingValue.Batu
    case 1 => VotingValue.Sankaku

    }
  }
}