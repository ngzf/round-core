import sbt._
import Keys._

import com.round.sbt.SbtRound.autoImport._

import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

object ApplicationBuild extends Build {

  lazy val formattingPreferences = FormattingPreferences()
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(SpacesAroundMultiImports, true) // this was changed in 0.1.7 scalariform, setting this to preserve default.

  lazy val formatSettings = SbtScalariform.scalariformSettings ++ Seq(
    ScalariformKeys.preferences in Compile := formattingPreferences,
    ScalariformKeys.preferences in Test := formattingPreferences)

  val buildSettings = Defaults.coreDefaultSettings ++ roundSettings ++ formatSettings ++ Seq(
    name := "TEMPLATE",
    description := "A template for sbt projects.",
    libraryDependencies ++= Seq(
    )
  ) ++ super.settings

  lazy val root = Project(id = "root", base = file("."), settings = buildSettings)
}
