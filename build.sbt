resolvers += "Zitadelz Snapshots" at "https://gitlab.com/nzf/artifactory/raw/master/snapshots/"

libraryDependencies ++= Seq(
  "commons-codec" % "commons-codec" % "1.9",
  "net.sandrogrzicic" %% "scalabuff-runtime" % "1.3.8",
  "com.typesafe.play" %%  "play" % Version.playVersion,
  "org.specs2" %% "specs2" % "2.3.12" % "test"
)

sourceGenerators in Compile <+= sourceManaged in Compile zip baseDirectory map { (files: (java.io.File, java.io.File)) =>
  val (srcManaged, base) = files
  val outDir = new File(srcManaged, "/protuf/")
  outDir.mkdirs()
  net.sandrogrzicic.scalabuff.compiler.ScalaBuff.main(Array("--scala_out=" + outDir, "--proto_path=" + base + "/src/main/protuf/"))
  (outDir ** "*.scala").get
}

watchSources <++= baseDirectory map { path => ((path / "src" / "main" / "protuf") ** "*.proto").get }

sourceGenerators in Test <+= sourceManaged in Test zip baseDirectory map { (files: (java.io.File, java.io.File)) =>
  val (srcManaged, base) = files
  val outDir = new File(srcManaged, "/protuf/")
  outDir.mkdirs()
  net.sandrogrzicic.scalabuff.compiler.ScalaBuff.main(Array("--scala_out=" + outDir, "--proto_path=" + base + "/src/test/protuf/"))
  (outDir ** "*.scala").get
}

watchSources <++= baseDirectory map { path => ((path / "src" / "test" / "protuf") ** "*.proto").get }

Keys.fork in Test := true

javaOptions in Test += "-Dzitadelz.runMode=test"

name := "Core"

description := "Core types of Zitadelz."

version := Version.zitadelzVersion

organization := "com.zitadelz"

organizationHomepage := Some(url("http://zitadelz.com"))

homepage := Some(url("http://youtrack/TEMPLATE"))
