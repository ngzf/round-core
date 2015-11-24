import sbt._
import Keys._

import com.round.sbt.SbtRound.autoImport._

object ApplicationBuild extends Build {

  val buildSettings = Defaults.coreDefaultSettings ++ roundSettings

  lazy val root = Project(id = "root", base = file("."), settings = buildSettings)
}
