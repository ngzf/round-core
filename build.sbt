resolvers += "Zitadelz Snapshots" at "https://gitlab.com/nzf/artifactory/raw/master/snapshots/"

libraryDependencies ++= Seq(
)

Keys.fork in Test := true

javaOptions in Test += "-Dzitadelz.runMode=test"

name := "TEMPLATE"

description := "A template for sbt projects."

version := "0.1-SNAPSHOT"

organization := "com.zitadelz"

organizationHomepage := Some(url("http://zitadelz.com"))

homepage := Some(url("http://youtrack/TEMPLATE"))
