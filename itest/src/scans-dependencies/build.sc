import $file.plugins

import mill._, scalalib._
import io.github.hoangmaihuy.missinglink._

object `scans-dependencies` extends RootModule with ScalaModule with MissinglinkCheckModule {

  object `has-problematic-dependency` extends ScalaModule {

    override def scalaVersion = "2.13.16"

    override def ivyDeps = super.ivyDeps() ++ Seq(ivy"com.google.guava:guava:14.0")

  }

  override def scalaVersion = "2.13.16"

  override def ivyDeps = super.ivyDeps() ++ Seq(ivy"com.google.guava:guava:18.0")

  override def moduleDeps = Seq(`has-problematic-dependency`)

  override def missinglinkScanDependencies = true

}
