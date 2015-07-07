package core

import java.util.UUID

import akka.actor.{Actor, Props}

object Dispatcher {
  case class SendMessage(to: UUID, message: String)
}

class Dispatcher extends Actor {

  import Dispatcher._
  import FastPathReceiver._
  import SlowPathReceiver._

  val fast = context.actorOf(Props[FastPathReceiver])
  val slow = context.actorOf(Props[SlowPathReceiver])

  def receive: Receive = {
    case SendMessage(to, message) if message.contains("fast") =>
      fast ! FastMessage(to, message)
    case SendMessage(to, message) if message.contains("slow") =>
      slow ! SlowMessage(to, message)
  }
}
