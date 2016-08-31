import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.JsObject
import play.api.libs.json.Json._

import play.api.test._
import reactivemongo.bson.BSONObjectID


class ContactSpec extends PlaySpecification{
  sequential

  "Assignment" should {
    val app: Application = GuiceApplicationBuilder().build()
    var id: String = ""
    var contactJs = obj(
      "firstName" -> "John",
      "lastName" -> "Doe",
      "email" -> "john.doe@example.org"
    )

    "return 404 if contact id not found" in{
      route(app, FakeRequest(GET,
        s"/contacts/${BSONObjectID.generate().stringify}")) must beSome.which (status(_) == NOT_FOUND)
    }

    "return 201 with contact id after create" in {
      val Some(res) = route(app, FakeRequest(POST, "/contacts").withJsonBody(contactJs))

      status(res) must_== CREATED
      id = contentAsString(res)
      id must_!= ""
    }

    "return 400 with invalid contact json" in {
      route(app, FakeRequest(POST, s"/contacts").withJsonBody(contactJs - "email")) must
        beSome.which (status(_) == BAD_REQUEST)
    }

    "return 200 with correct contact when finding with ID" in {
      val Some(res) = route(app, FakeRequest(GET, s"/contacts/$id"))
      status(res) must_== OK
      contentAsJson(res) must_== contactJs ++ obj("_id" -> id)
    }

    "return 200 list that contains created contact" in {
      val Some(res) = route(app, FakeRequest(GET, "/contacts"))

      status(res) must_== OK
      contentAsJson(res).as[List[JsObject]] must contain(contactJs ++ obj("id" -> id))
    }

    "return 200 after updating contact and fetching it again getting correct entity" in {
      contactJs = contactJs ++ obj("email" -> "doe.john@example.org")
      route(app, FakeRequest(PUT, s"/contacts/$id").withJsonBody(contactJs)) must beSome.which (status(_) == OK)

      val Some(res) = route(app, FakeRequest(GET, s"/contacts/$id"))
      contentAsJson(res) must_== contactJs ++ obj("id" -> id)
    }

    "return 200 when deleting contact and after fetching it should return 404" in {
      route(app, FakeRequest(DELETE, s"/contacts/$id")) must beSome.which (status(_) == OK)

      route(app,FakeRequest(GET, s"/contacts/$id")) must beSome.which (status(_) == NOT_FOUND)
    }

  }
}
