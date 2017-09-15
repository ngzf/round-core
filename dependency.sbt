libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.10",
  Library.elastic4s("core"),
  Library.play(),
  Library.scalaz(),
  Library.scalapb(),
  Library.scalapb() % "protobuf",
  Library.specs2()
)
