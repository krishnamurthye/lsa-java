package com.pulsar;

import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.data.mongodb.host=localhost",
        "spring.data.mongodb.port=27017"
})
public class EmbeddedMongoTest {

    private static MongodExecutable mongodExecutable;

    @BeforeAll
    static void setUp() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        MongodConfigBuilder mongodConfigBuilder = new MongodConfigBuilder();
        mongodConfigBuilder.net(new de.flapdoodle.embed.mongo.config.Net(27017, Network.localhostIsIPv6()));
        mongodConfigBuilder.version(Version.Main.PRODUCTION);
        mongodExecutable = starter.prepare(mongodConfigBuilder.build());
        mongodExecutable.start();

    }

    @AfterAll
    static void tearDown() {
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }
    }
}
