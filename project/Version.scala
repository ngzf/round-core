object Version {
  val akkaVersion = "2.4.0"
  val playVersion = "2.4.3"
  val scalaVersion = "2.11.7"
  
  private def major: Int = 1
  private def minor: Int = 0
  private def fix: Int = 0
  private def snapshot: Boolean = true
  def ours(major: Int = major, minor: Int = minor, fix: Int = fix, snapshot: Boolean = snapshot): String =
    s"$major.$minor.$fix-${ if (snapshot) "SNAPSHOT" else "" }"
}
