package core

import java.util.UUID

import akka.actor.{ActorLogging, Actor}
import core.FastPathReceiver.FastMessage

object SlowPathReceiver {
  case class SlowMessage(uuid: UUID, message: String)
}

class SlowPathReceiver extends Actor with ActorLogging {

  import SlowPathReceiver._

  def receive: Receive = {
    case SlowMessage(uuid, body) => log.info(s"slow: $uuid $body")
  }
}

object FastPathReceiver {
  case class FastMessage(uuid: UUID, message: String)
}

class FastPathReceiver extends Actor with ActorLogging {

  def receive: Receive = {
    case FastMessage(uuid, body) => log.info(s"fast: $uuid, $body")
  }
}

