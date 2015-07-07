package api

import spray.routing.Directives
import scala.concurrent.ExecutionContext
import akka.actor.ActorRef
import core.{Article, ArticleManager}
import akka.util.Timeout
import ArticleManager._
import spray.http._
import core.Article
import core.ArticleManager.Register
import scala.Some

class ArticleService(articles: ActorRef)(implicit executionContext: ExecutionContext)
  extends Directives with DefaultJsonFormats {

  case class ImageUploaded(size: Int)

  import akka.pattern.ask
  import scala.concurrent.duration._
  implicit val timeout = Timeout(2.seconds)

  implicit val articleFormat = jsonFormat4(Article)
  implicit val registerFormat = jsonFormat1(Register)
  implicit val registeredFormat = jsonFormat3(Registered)
  implicit val notRegisteredFormat = jsonFormat2(NotRegistered)

  implicit object EitherErrorSelector extends ErrorSelector[NotRegistered.type] {
    def apply(v: NotRegistered.type): StatusCode = StatusCodes.BadRequest
  }

  val route =
    path("articles") {
      post {
        handleWith { article: Register => (articles ? article).mapTo[Either[NotRegistered, Registered]] }
      }
    }
}
