package com.client.address.infrastructure.repository;

import com.client.address.infrastructure.entity.ClientEntity;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(ClientEntity.class)
public interface ClientRepository {

    @SqlQuery("SELECT * FROM clients WHERE id = :id")
    Optional<ClientEntity> findById(@Bind("id") Long id);

    @SqlQuery("SELECT * FROM clients WHERE email = :email")
    Optional<ClientEntity> findByEmail(@Bind("email") String email);

    @GetGeneratedKeys
    @SqlUpdate("""
        INSERT INTO clients (name, email, phone, document, document_type, password, created_at)
        VALUES (:name, :email, :phone, :document, :documentType, :password, :createdAt)
    """)
    Long save(@BindBean ClientEntity client);

    @SqlUpdate("""
        UPDATE clients SET
        name = :name, email = :email, phone = :phone, document = :document,
        document_type = :documentType, password = :password
        WHERE id = :id
    """)
    int update(@BindBean ClientEntity client);

    @SqlUpdate("DELETE FROM clients WHERE id = :id")
    int delete(@Bind("id") Long id);

    @SqlQuery("""
        SELECT * FROM clients
        WHERE name ILIKE :name -- Simplified WHERE clause
        ORDER BY created_at DESC
        LIMIT :limit OFFSET :offset
    """)
    List<ClientEntity> findAllPaginated(
            @Bind("name") String name,
            @Bind("limit") int limit,
            @Bind("offset") int offset
    );

    @SqlQuery("""
        SELECT COUNT(*) FROM clients
        WHERE name ILIKE :name -- Simplified WHERE clause
    """)
    long countWithFilters(
            @Bind("name") String name
    );
}