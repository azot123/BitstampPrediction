name := "BitstampPrediction"

version := "1.0"

scalaVersion := "2.11.7"


resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"


resolvers += "Sonatype Releases"  at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.2"

libraryDependencies += "org.mongodb" %% "casbah" % "2.8.2"

libraryDependencies += "net.sourceforge.jexcelapi" % "jxl" % "2.6.12"

libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.2.2"

libraryDependencies += "net.finmath" % "finmath-lib" % "1.2.19"
