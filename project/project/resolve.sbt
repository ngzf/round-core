resolvers := Seq(
  Resolver.file("Round Artifactory", file(s"${System.getenv("HOME")}/raccoon/Dropbox/artifactory/libs-snapshot-local"))(Resolver.ivyStylePatterns),
  Resolver.defaultLocal,
  Resolver.mavenLocal,
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases",
  "Typesafe Maven Repository" at "http://repo.typesafe.com/typesafe/maven-releases")
