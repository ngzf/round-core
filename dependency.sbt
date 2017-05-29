libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.10",
  "joda-time" % "joda-time" % "2.9.9",
  Library.elastic4s("core"),
  Library.play(),
  Library.scalaz(),
  Library.scalapb(),
  Library.scalapb() % "protobuf",
  Library.specs2()
)
