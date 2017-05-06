resolvers := Seq(
  "Round Artifactory" at s"file://${System.getenv("HOME")}/Dropbox/artifactory/libs-snapshot-local",
  Resolver.defaultLocal,
  Resolver.mavenLocal,
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases",
  "Typesafe Maven Repository" at "http://repo.typesafe.com/typesafe/maven-releases")
