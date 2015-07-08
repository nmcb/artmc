import api.Api
import core.{BootedCore, CoreActors}
import http.HttpServer

object MerchantCenter extends App with BootedCore with CoreActors with Api with HttpServer