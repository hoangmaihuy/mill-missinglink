import $file.plugins

import mill._, scalalib._
import io.github.hoangmaihuy.missinglink._

object `ignore-source-package` extends RootModule with ScalaModule with MissinglinkCheckModule {

  override def scalaVersion = "2.13.16"

  override def runIvyDeps = super.runIvyDeps() ++ Seq(ivy"com.google.guava:guava:18.0")

  override def ivyDeps = super.ivyDeps() ++ Seq(ivy"com.google.guava:guava:14.0")

  override def missinglinkIgnoreSourcePackages = Seq(
    IgnoredPackage("test"),
    IgnoredPackage("whatever")
  )

}
