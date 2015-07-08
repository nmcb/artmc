package core

import java.util.UUID

import akka.actor.Actor
import akka.event.LoggingReceive
import core.model.Article

object ArticleManager {
  val skus = """(\d\d\d\d\d\d\d\d)-(\d\d)""".r
  case class Register(article: Article)
  case class Registered(uuid: UUID, id: Int, group: Int)
  case class NotRegistered(uuid: UUID, invalid: String)
}

class ArticleManager extends Actor {

  import ArticleManager._

  def receive: Receive = LoggingReceive {
    case Register(Article(uuid, _, _, skus(id, group))) => sender ! Right(Registered(uuid, group.toInt, id.toInt))
    case Register(Article(uuid, _, _, invalid))         => sender ! Left(NotRegistered(uuid, invalid))
  }
}
