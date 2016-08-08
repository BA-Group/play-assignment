package controllers

import javax.inject.{Inject, Singleton}

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Action
import play.modules.reactivemongo.json._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future

@Singleton
class ContactController @Inject() (val reactiveMongoApi: ReactiveMongoApi)
  extends MongoController with ReactiveMongoComponents {

  private def collection:Future[JSONCollection] = database.map(_.collection[JSONCollection]("contacts"))

  def create = TODO

  def update(id:String) = TODO

  def list = TODO

  def get(id:String) = Action.async { request => // since operations on mongo are asynchronous, we must use the `async`-version of the Action-handler
    // collection returns a Future, so we'll map it
    collection.flatMap { contactsCollection =>
      // and the find-function also returns a Future, hence we used flatMap
      contactsCollection.find(Json.obj("_id" -> id)).one[JsObject].map {
        // here we map the result of the find-operation, which returned an Option contained in Future.
        // we can use pattern matching to process the Option
        case Some(contact) => Ok(contact) // contact found, return HTTP OK status with the contact as payload
        case None => NotFound(s"No contact was found with id $id") // no contacts were found, return HTTP NOT_FOUND, with an error message
      }
    }
  }

  def remove(id:String) = TODO

}
