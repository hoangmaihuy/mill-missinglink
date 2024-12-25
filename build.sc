import $ivy.`de.tototec::de.tobiasroeser.mill.vcs.version::0.4.1`
import $ivy.`de.tototec::de.tobiasroeser.mill.integrationtest::0.7.1`
import $ivy.`io.chris-kipp::mill-ci-release::0.2.0`

import mill._
import mill.scalalib._
import mill.scalalib.publish._
import mill.scalalib.api.ZincWorkerUtil.scalaNativeBinaryVersion

import de.tobiasroeser.mill.integrationtest._
import io.kipp.mill.ci.release.CiReleaseModule
import io.kipp.mill.ci.release.SonatypeHost

def millVersionFile = T.source(PathRef(os.pwd / ".mill-version"))

def millVersion = T {
  os.read(millVersionFile().path).trim
}

object `mill-missinglink` extends ScalaModule with CiReleaseModule {

  override def scalaVersion = "2.13.14"

  override def sonatypeHost = Some(SonatypeHost.s01)

  override def versionScheme: T[Option[VersionScheme]] = T(Option(VersionScheme.EarlySemVer))

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
    ivy"com.spotify:missinglink-core:0.2.11"
  )

}

object itest extends MillIntegrationTestModule {

  override def millTestVersion = millVersion

  override def pluginsUnderTest = Seq(`mill-missinglink`)

  def testBase = millSourcePath / "src"

  private def successMissinglinkCheck = Seq(
    TestInvocation.Targets(Seq("missinglinkCheck"))
  )

  private def errorMissinglinkCheck = Seq(
    TestInvocation.Targets(Seq("missinglinkCheck"), expectedExitCode = 1)
  )

  override def testInvocations = Seq(
    PathRef(testBase / "do-not-fail-on-conflicts") -> successMissinglinkCheck,
    PathRef(testBase / "exclude-problematic-dependency") -> errorMissinglinkCheck,
    PathRef(testBase / "has-problematic-dependency") -> errorMissinglinkCheck,
    PathRef(testBase / "ignore-destination-package") -> successMissinglinkCheck,
    PathRef(testBase / "ignore-source-package") -> successMissinglinkCheck,
    PathRef(testBase / "ignores-unused-dependency") -> successMissinglinkCheck,
    PathRef(testBase / "scans-dependencies") -> errorMissinglinkCheck,
    PathRef(testBase / "target-destination-package") -> successMissinglinkCheck,
    PathRef(testBase / "target-source-package") -> errorMissinglinkCheck,
    PathRef(testBase / "uses-problematic-dependency") -> errorMissinglinkCheck
  )

}
