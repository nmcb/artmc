package core

import java.util.UUID
import api.DefaultJsonFormats

case class Article(uuid: UUID, name: String, category: String, sku: String)