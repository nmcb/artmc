package api

import spray.testkit.Specs2RouteTest
import spray.routing.Directives
import org.specs2.mutable.Specification
import spray.http.HttpResponse

class ArticlesServiceSpec extends Specification with Directives with Specs2RouteTest {

  "routing should support" >> {
    "direct route" in {
      Get() ~> complete(HttpResponse()) ~> (_.response) === HttpResponse()
    }
  }
}
