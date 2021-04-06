package ru.otus.crm.service;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.model.Client;

public class CacheHelper {

    private static final Logger logger = LoggerFactory.getLogger(CacheHelper.class);

    private CacheManager cacheManager;

    private Cache<Long, Client> cache;

    public Cache<Long, Client> initEhcache() {

        this.cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        var cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(event ->
                                logger.info("{}, value: {}", event.getKey(), event.getNewValue()),
                        EventType.CREATED, EventType.UPDATED)
                .ordered().synchronous();

        this.cache = cacheManager.createCache("Demo-Cache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, Client.class,
                        ResourcePoolsBuilder.heap(10))
                        .withService(cacheEventListenerConfiguration)
                        .build());

        logger.info("Cache setup is done");

        return cache;
    }

    private void closeEhcache() {
        this.cacheManager.close();
    }
}
