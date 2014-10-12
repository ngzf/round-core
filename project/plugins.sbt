// Comment to get more information during initialization
logLevel := Level.Warn

resolvers := Seq(
  "Zitadelz Artifactory" at "http://artifactory:8081/artifactory/repo",
  Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns),
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases",
  "Typesafe Maven Repository" at "http://repo.typesafe.com/typesafe/maven-releases")

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")

libraryDependencies += "net.sandrogrzicic" %% "scalabuff-compiler" % "1.3.8-z" % "compile"
