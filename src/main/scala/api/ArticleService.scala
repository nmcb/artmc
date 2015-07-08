package api

import akka.actor.ActorRef
import akka.util.Timeout
import core.ArticleManager.{Register, Registered, NotRegistered}
import core.model.Article
import spray.http._
import spray.routing.Directives

import scala.concurrent.ExecutionContext

class ArticleService(articles: ActorRef)(implicit executionContext: ExecutionContext)
extends Directives with DefaultJsonFormats {

  import akka.pattern.ask

  import scala.concurrent.duration._

  implicit val timeout = Timeout(2.seconds)

  implicit val articleFormat       = jsonFormat4(Article)
  implicit val registerFormat      = jsonFormat1(Register)
  implicit val registeredFormat    = jsonFormat3(Registered)
  implicit val notRegisteredFormat = jsonFormat2(NotRegistered)
  val route =
    path("articles") {
      post {
        handleWith { article: Register => (articles ? article).mapTo[Either[NotRegistered, Registered]] }
      }
    }
  case class ImageUploaded(size: Int)

  implicit object EitherErrorSelector extends ErrorSelector[NotRegistered.type] {
    def apply(v: NotRegistered.type): StatusCode = StatusCodes.BadRequest
  }
}
