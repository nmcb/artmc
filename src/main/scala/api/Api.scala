package api

import akka.actor.Props
import core.{Core, CoreActors}
import spray.routing.RouteConcatenation

trait Api extends RouteConcatenation {
  this: CoreActors with Core =>

  private implicit val _ = system.dispatcher

  val routes = new ArticleService(articles).route

  val rootService = system.actorOf(Props(new RoutedHttpService(routes)))

}
