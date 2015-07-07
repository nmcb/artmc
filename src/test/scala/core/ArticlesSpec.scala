package core

import akka.testkit.{ImplicitSender, TestKit}
import akka.actor.ActorSystem
import org.specs2.mutable.SpecificationLike
import java.util.UUID

class ArticlesSpec extends TestKit(ActorSystem()) with SpecificationLike with CoreActors with Core with ImplicitSender {
  import ArticleManager._

  private def mkArticle(sku: String): Article = Article(UUID.randomUUID(), "nike air", "shoes", sku)

  sequential

  "article registration should" >> {

    "reject invalid article sku" in {
      val article = mkArticle("xxx")
      articles ! Register(article)
      expectMsg(Left(NotRegistered(article.uuid,"xxx")))
      success
    }

    "accept valid article" in {
      val article = mkArticle("12345678-90")
      articles ! Register(article)
      expectMsg(Right(Registered(article.uuid, 90, 12345678)))
      success
    }
  }

}
