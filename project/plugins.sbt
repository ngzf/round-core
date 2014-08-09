// Comment to get more information during initialization
logLevel := Level.Warn

resolvers := Seq(Resolver.file("Local Ivy Repository", file(Path.userHome.absolutePath + "/.ivy2/local"))(Resolver.ivyStylePatterns),
  "Zitadelz Artifactory" at "http://artifactory/artifactory/repo",
  Resolver.url("Zitadelz Artifactory Ivy", url("http://artifactory:8081/artifactory/repo"))(Resolver.ivyStylePatterns))

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")

libraryDependencies += "net.sandrogrzicic" %% "scalabuff-compiler" % "1.3.8-z" % "compile"
