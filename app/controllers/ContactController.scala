package controllers

import javax.inject.{Inject, Singleton}

import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

@Singleton
class ContactController @Inject() (val reactiveMongoApi: ReactiveMongoApi)
  extends MongoController with ReactiveMongoComponents {

  def create = TODO

  def update(id:String) = TODO

  def list = TODO

  def get(id:String) = TODO

  def remove(id:String) = TODO

}
