package core

import akka.actor.{ActorSystem, Props}

trait Core {
  implicit def system: ActorSystem
}

trait BootedCore extends Core {
  implicit lazy val system = ActorSystem("article-core")
  sys.addShutdownHook(system.shutdown())
}

trait CoreActors {
  this: Core =>
  val articles = system.actorOf(Props[ArticleManager])
}