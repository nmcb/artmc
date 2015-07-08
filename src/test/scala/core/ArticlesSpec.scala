package core

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import core.model.Article
import org.specs2.mutable.SpecificationLike

class ArticlesSpec extends TestKit(ActorSystem()) with SpecificationLike with CoreActors with Core with ImplicitSender {

  import ArticleManager._

  private def mkArticle(sku: String): Article = Article(UUID.randomUUID(), "nike air", "shoes", sku)

  sequential

  "article registration should" >> {

    "reject articles with an invalid sku" in {
      val article = mkArticle("xxx")
      articles ! Register(article)
      expectMsg(Left(NotRegistered(article.uuid, "xxx")))
      success
    }

    "accept articles with a valid sku" in {
      val article = mkArticle("12345678-90")
      articles ! Register(article)
      expectMsg(Right(Registered(article.uuid, 90, 12345678)))
      success
    }
  }

}
