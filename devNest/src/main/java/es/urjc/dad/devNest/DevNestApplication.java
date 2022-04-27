package es.urjc.dad.devNest;

import java.util.Collections;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

@SpringBootApplication
@EnableCaching
@EnableHazelcastHttpSession
public class DevNestApplication {

    private static final Log logger = LogFactory.getLog(DevNestApplication.class);

    public static void main(String[] args) {
        String newLine = System.lineSeparator();
        System.out.println(newLine +
                           "*******************************" + newLine +
                           "*********** DEVNEST ***********" + newLine +
                           "*******************************" + newLine +
                           "Developed by:" + newLine +
                           "- Pablo de la Hoz Menendez" + newLine +
                           "- Carlos Chamizo Cano" + newLine +
                           "- Juan Jose Perez Abad" + newLine +
                           "*******************************" + newLine);

        SpringApplication.run(DevNestApplication.class, args);
    }

    /**
    * Method in charge of initializing the database cache
    */
    @Bean
    public CacheManager cacheManager()
    {
        logger.info("Initializing database cache");
        return new ConcurrentMapCacheManager("devnest");
    }

    /**
    * Method in charge of initializing Hazelcast
    */
    @Bean
    public Config config()
    {
        logger.info("Initializing Hazelcast");
        
        Config config = new Config();
        JoinConfig joinConfig = config.getNetworkConfig().getJoin();

        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getTcpIpConfig().setEnabled(true).setMembers(Collections.singletonList("127.0.0.1"));

        return config;
    }
}
