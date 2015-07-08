import api.Api
import core.{BootedCore, CoreActors}
import http.HttpServer

object Rest extends App with BootedCore with CoreActors with Api with HttpServer