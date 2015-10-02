object Version {
  val akka = "2.4.0"
  val play = "2.4.3"
  val scala = "2.11.7"

  val scalaz = "7.1.4"
  val scalabuff = "1.4.0"
  val specs2 = "3.6.4"
  val logback = "1.1.3"

  def major: Int = 1
  def minor: Int = 0
  def fix: Int = 0
  def snapshot: Boolean = true
  def ours(major: Int = major, minor: Int = minor, fix: Int = fix, snapshot: Boolean = snapshot): String =
    s"$major.$minor.$fix${ if (snapshot) "-SNAPSHOT" else "" }"
}
