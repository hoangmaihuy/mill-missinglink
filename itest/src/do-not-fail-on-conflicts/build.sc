import $file.plugins

import mill._, scalalib._
import io.github.hoangmaihuy.missinglink._

object `do-not-fail-on-conflicts` extends RootModule with ScalaModule with MissinglinkCheckModule {

  override def scalaVersion = "2.13.14"

  override def runIvyDeps = super.runIvyDeps() ++ Seq(ivy"com.google.guava:guava:18.0")

  override def ivyDeps = super.ivyDeps() ++ Seq(ivy"com.google.guava:guava:14.0")

  override def missinglinkFailOnConflicts = false

}
