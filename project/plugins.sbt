// Comment to get more information during initialization
logLevel := Level.Warn

com.round.sbt.Resolve.resolveSettings

addSbtPlugin("com.round" %% "round-build" % Version.ours())

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.5.1")
