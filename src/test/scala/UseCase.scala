package test.scala

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class UseCase extends Simulation{

  val httpProtocol = http.baseUrl("http://computer-database.gatling.io").disableWarmUp
  val csvFeeder = csv("computerDetails.csv").circular
  val records: Seq[Map[String, Any]] = csv("computerDetails.csv").readRecords

  val feeder = Array(
    Map("name" -> "A15", "company" -> 4),
    Map("name" -> "A16", "company" -> 5),
    Map("name" -> "A17", "company" -> 6)
  ).random

  val scn = scenario("FirstScenario")
  .foreach(records, "record") {
    exec(flattenMapIntoAttributes("${record}"))
      .exec(
        http("CreateNewComp")
          .post("/computers")
          .formParamMap(Map("name" ->"${name}", "company" ->"${company}"))
          .check(substring("${name}"))
      )
  }



  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)

}
