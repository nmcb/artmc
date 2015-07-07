package api

import akka.actor.ActorRef
import scala.concurrent.ExecutionContext
import spray.routing.Directives
import core.Dispatcher$

class DispatcherService(dispatcher: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  import core.Dispatcher._

  implicit val sendMessageFormat = jsonFormat2(SendMessage)

   val route =
     path("messages") {
       post {
         handleWith { sm: SendMessage => dispatcher ! sm; "{}" }
       }
     }
}