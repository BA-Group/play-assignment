package modules

import com.google.inject.name.{Named, Names}
import com.google.inject.{AbstractModule, Inject}
import de.flapdoodle.embed.mongo.{Command, MongodStarter}
import de.flapdoodle.embed.mongo.config.{Net, MongodConfigBuilder, RuntimeConfigBuilder}
import de.flapdoodle.embed.process.runtime.Network
import play.api.Logger
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future


class EmbedMongo @Inject()(lifecycle: ApplicationLifecycle, @Named("mongoPort") port: Int) extends Mongo {
  val mongod = MongodStarter.getInstance(new RuntimeConfigBuilder()
    .defaultsWithLogger(Command.MongoD, Logger("mongod").logger)
    .build()).prepare(new MongodConfigBuilder()
    .version(de.flapdoodle.embed.mongo.distribution.Version.Main.PRODUCTION)
    .net(new Net(port, Network.localhostIsIPv6))
    .build)
  lifecycle.addStopHook{ () =>
    Future.successful(mongod.stop())
  }
}

trait Mongo

class EmbedMongoModule extends AbstractModule {
  def configure() = {
    bindConstant().annotatedWith(Names.named("mongoPort")).to(31337)
    bind(classOf[Mongo]).to(classOf[EmbedMongo]).asEagerSingleton()
  }
}
