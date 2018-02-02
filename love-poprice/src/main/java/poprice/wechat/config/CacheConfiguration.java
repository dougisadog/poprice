package poprice.wechat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import poprice.wechat.Constants;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * http://www.cnblogs.com/feika/p/4431599.html
 * this is weired...
 * http://www.tuicool.com/articles/m2qAfqn
 */
@Configuration
@EnableCaching
@AutoConfigureAfter(value = { DatabaseConfiguration.class})
@Profile("!" + Constants.SPRING_PROFILE_FAST)
public class CacheConfiguration {

    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Environment env;

    private CacheManager cacheManager;

    @PreDestroy
    public void destroy() {
        log.info("Closing Cache Manager");
    }

    @Bean
    public CacheManager cacheManager() {
        log.debug("No cache");
        cacheManager = new NoOpCacheManager();
        return cacheManager;
    }
}
