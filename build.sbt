libraryDependencies ++= Seq(
)

Keys.fork in Test := true

javaOptions in Test += "-Dzitadelz.runMode=test"

name := "TEMPLATE"

description := "A template for sbt projects."

version := Version.ours()

organization := "com.zitadelz"

organizationHomepage := Some(url("http://zitadelz.com"))

homepage := Some(url("http://youtrack/TEMPLATE"))
