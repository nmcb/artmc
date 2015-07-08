package http

import akka.io.IO
import api.Api
import core.{Core, CoreActors}
import spray.can.Http

trait HttpServer {
  this: Api with CoreActors with Core =>
  IO(Http)(system) ! Http.Bind(rootService, "0.0.0.0", port = 8080)
}
