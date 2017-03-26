resolvers := Seq(
  "Round Artifactory" at s"file://${System.getenv("HOME")}/Dropbox/artifactory/libs-snapshot-local",
  Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns),
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases",
  "Typesafe Maven Repository" at "http://repo.typesafe.com/typesafe/maven-releases")
