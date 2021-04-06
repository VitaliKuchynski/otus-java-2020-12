package ru.otus.crm.service;

import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.crm.model.Client;
import ru.otus.core.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBService<Client> {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private Cache<Long, Client> cache;
    private final CacheHelper cacheHelper;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cacheHelper = new CacheHelper();
        this.cache = cacheHelper.initEhcache();

    }

    @Override
    public Client saveEntity(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();

            if (clientCloned.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                cache.put(clientCloned.getId(), clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            cache.put(clientCloned.getId(), clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getEntity(long id) {

        if(cache.containsKey(id)){
            return Optional.ofNullable(cache.get(id));
        }

        return transactionManager.doInTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }
}
