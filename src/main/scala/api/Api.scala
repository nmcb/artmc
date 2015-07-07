package api

import core.{CoreActors, Core}
import akka.actor.Props
import spray.routing.RouteConcatenation

trait Api extends RouteConcatenation {
  this: CoreActors with Core =>

  private implicit val _ = system.dispatcher

  val routes =
    new ArticleService(articles).route ~
    new DispatcherService(dispatcher).route

  val rootService = system.actorOf(Props(new RoutedHttpService(routes)))

}
