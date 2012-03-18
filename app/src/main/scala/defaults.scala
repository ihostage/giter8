package giter8

trait Defaults { self: Giter8 =>
  def prepareDefaults(
    repo: String,
    properties: Option[FileInfo]
  ) = Ls.lookup(fetchDefaults(repo, properties))

  def fetchDefaults(repo: String, properties: Option[FileInfo]) =
    properties.map { fileinfo =>
      http(show(repo, fileinfo.hash) >> readProps _ )
    }.getOrElse { Map.empty }

  def readProps(stm: java.io.InputStream) = {
    import scala.collection.JavaConversions._
    val p = new java.util.Properties
    p.load(stm)
    stm.close()
    (Map.empty[String, String] /: p.propertyNames) { (m, k) =>
      m + (k.toString -> p.getProperty(k.toString))
    }
  }
}
