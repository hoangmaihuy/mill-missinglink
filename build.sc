import $ivy.`de.tototec::de.tobiasroeser.mill.vcs.version::0.4.0`

import mill._
import mill.scalalib._
import mill.scalalib.publish._
import mill.scalalib.api.ZincWorkerUtil.scalaNativeBinaryVersion

import de.tobiasroeser.mill.vcs.version.VcsVersion

def millVersionFile = T.source(PathRef(os.pwd / ".mill-version"))

def millVersion = T {
  os.read(millVersionFile().path).trim
}

object `mill-missinglink` extends RootModule with ScalaModule with PublishModule {

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
    versionControl =
      VersionControl.github(owner = "hoangmaihuy", repo = "mill-missinglink"),
    developers =
      Seq(Developer("hoangmaihuy", "Hoang Mai", "https://github.com/hoangmaihuy"))
  )

  override def artifactName = "mill-missinglink"

  override def artifactSuffix =
    "_mill" + scalaNativeBinaryVersion(millVersion()) +
      super.artifactSuffix()

  override def scalacOptions = Seq("-Ywarn-unused", "-deprecation")

  override def ivyDeps = Agg(
    ivy"com.spotify:missinglink-core:0.2.9"
  )

}