import $ivy.`de.tototec::de.tobiasroeser.mill.vcs.version::0.4.0`
import $ivy.`de.tototec::de.tobiasroeser.mill.integrationtest::0.7.1`

import mill._
import mill.scalalib._
import mill.scalalib.publish._
import mill.scalalib.api.ZincWorkerUtil.scalaNativeBinaryVersion

import de.tobiasroeser.mill.integrationtest._
import de.tobiasroeser.mill.vcs.version.VcsVersion

def millVersionFile = T.source(PathRef(os.pwd / ".mill-version"))

def millVersion = T {
  os.read(millVersionFile().path).trim
}

object `mill-missinglink` extends ScalaModule with PublishModule {

  override def scalaVersion = "2.13.11"

  override def publishVersion: T[String] = T {
    VcsVersion
      .vcsState()
      .format(
        dirtyHashDigits = 0,
        untaggedSuffix = "-SNAPSHOT"
      )
  }

  override def pomSettings = PomSettings(
    description = "Missinglink for Mill",
    organization = "io.github.hoangmaihuy",
    url = "https://github.com/hoangmaihuy/mill-missinglink",
    licenses = Seq(License.`Apache-2.0`),
    versionControl = VersionControl.github(owner = "hoangmaihuy", repo = "mill-missinglink"),
    developers = Seq(Developer("hoangmaihuy", "Hoang Mai", "https://github.com/hoangmaihuy"))
  )

  override def artifactName = "mill-missinglink"

  override def artifactSuffix =
    "_mill" + scalaNativeBinaryVersion(millVersion()) +
      super.artifactSuffix()

  override def scalacOptions = Seq("-Ywarn-unused", "-deprecation")

  override def compileIvyDeps = super.compileIvyDeps() ++ Agg(
    ivy"com.lihaoyi::mill-scalalib:${millVersion()}"
  )

  override def ivyDeps = Agg(
    ivy"com.spotify:missinglink-core:0.2.9"
  )

}

object itest extends MillIntegrationTestModule {

  override def millTestVersion = millVersion

  override def pluginsUnderTest = Seq(`mill-missinglink`)

  def testBase = millSourcePath / "src"

  private def missingCheckTestInvocation = Seq(
    TestInvocation.Targets(Seq("missinglinkCheck"))
  )

  override def testInvocations = Seq(
    PathRef(testBase / "do-not-fail-on-conflicts") ->  missingCheckTestInvocation,
    PathRef(testBase / "exclude-problematic-dependency") ->  missingCheckTestInvocation
  )

}
