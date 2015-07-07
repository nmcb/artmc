package api

import akka.actor.{Actor, ActorLogging}
import spray.http.StatusCodes._
import spray.http._
import spray.routing._
import spray.util.LoggingContext

import scala.util.control.NonFatal

case class ErrorResponseException(responseStatus: StatusCode, response: Option[HttpEntity]) extends Exception

trait FailureHandling {this: HttpService =>

  def rejectionHandler: RejectionHandler = RejectionHandler.Default

  def exceptionHandler(implicit log: LoggingContext) = ExceptionHandler {

    case e: IllegalArgumentException => ctx =>
      loggedFailureResponse(ctx, e,
        message = "didn't make sense: " + e.getMessage,
        error = NotAcceptable)

    case e: NoSuchElementException => ctx =>
      loggedFailureResponse(ctx, e,
        message = "missing information.",
        error = NotFound)

    case t: Throwable => ctx =>
      loggedFailureResponse(ctx, t)
  }

  private def loggedFailureResponse(ctx: RequestContext, thrown: Throwable,
                                    message: String = "having problems.",
                                    error: StatusCode = InternalServerError)
                                   (implicit log: LoggingContext): Unit = {
    log.error(thrown, ctx.request.toString)
    ctx.complete((error, message))
  }
}

class RoutedHttpService(route: Route) extends Actor with HttpService with ActorLogging {

  implicit val handler = ExceptionHandler {
    case NonFatal(ErrorResponseException(statusCode, entity)) => ctx =>
      ctx.complete((statusCode, entity))

    case NonFatal(e) => ctx => {
      log.error(e, InternalServerError.defaultMessage)
      ctx.complete(InternalServerError)
    }
  }
  implicit def actorRefFactory = context
  def receive: Receive = runRoute(route)(
    handler,
    RejectionHandler.Default,
    context,
    RoutingSettings.default,
    LoggingContext.fromActorRefFactory
  )
}
