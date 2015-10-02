object Version {
  val akkaVersion = "2.4.0"
  val playVersion = "2.4.3"
  val scalaVersion = "2.11.7"

  val scalazVersion = "7.1.4"
  val scalabuffVersion = "1.4.0"
  val specs2Version = "3.6.4"
  val logbackVersion = "1.1.3"

  private def major: Int = 1
  private def minor: Int = 0
  private def fix: Int = 0
  private def snapshot: Boolean = true
  def ours(major: Int = major, minor: Int = minor, fix: Int = fix, snapshot: Boolean = snapshot): String =
    s"$major.$minor.$fix${ if (snapshot) "-SNAPSHOT" else "" }"
}
