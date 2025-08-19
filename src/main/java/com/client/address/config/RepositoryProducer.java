package com.client.address.config;

import com.client.address.infrastructure.repository.AddressRepository;
import com.client.address.infrastructure.repository.ClientRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@ApplicationScoped
public class RepositoryProducer {

    private final Jdbi jdbi;

    @Inject
    public RepositoryProducer(DataSource dataSource) {
        this.jdbi = Jdbi.create(dataSource)
                .installPlugin(new SqlObjectPlugin());
    }

    @Produces
    public ClientRepository clientRepository() {
        return jdbi.onDemand(ClientRepository.class);
    }

    @Produces
    public AddressRepository addressRepository() {
        return jdbi.onDemand(AddressRepository.class);
    }
}