import sbt._
import Keys._

object Publish {
  val artifactory = "http://artifactory:8081/artifactory/"

  /**
   * content of ~/.sbt.credentials:
   *
   * realm=Artifactory Realm
   * host=artifactory
   * user=YOUR_USERNAME
   * password=YOUR_PASSWORD
   */
  val defaultSettings = Seq(
    credentials += Credentials(Path.userHome / ".sbt" / ".credentials"),
    publishMavenStyle := true,
    publishArtifact in Compile := true,
    publishArtifact in Test := false,
    publishTo <<= (version) { (v: String) =>
      if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at artifactory + "libs-snapshot-local")
      else Some("releases" at artifactory + "libs-release-local")
    },
    // Alter publish config not to publish source code (publishLocalConfiguration is unchanged, source will be published when issuing publish-local command)
    publishConfiguration ~= { config =>
      val newArts = config.artifacts.filterKeys(_.`type` != Artifact.SourceType)
      new PublishConfiguration(config.ivyFile, config.resolverName, newArts, config.checksums, config.logging)
    })
}
