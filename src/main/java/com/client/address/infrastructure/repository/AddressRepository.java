package com.client.address.infrastructure.repository;

import com.client.address.infrastructure.entity.AddressEntity;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

@RegisterBeanMapper(AddressEntity.class)
public interface AddressRepository {

    @SqlQuery("SELECT * FROM addresses WHERE id = :id")
    Optional<AddressEntity> findById(@Bind("id") Long id);

    @SqlQuery("SELECT * FROM addresses WHERE client_id = :clientId")
    List<AddressEntity> findByClientId(@Bind("clientId") Long clientId);

    @GetGeneratedKeys
    @SqlUpdate("""
        INSERT INTO addresses (name, street, number, complement, district, city, state, zip_code, main_address, client_id, created_at)
        VALUES (:name, :street, :number, :complement, :district, :city, :state, :zipCode, :mainAddress, :clientId, :createdAt)
    """)
    Long save(@BindBean AddressEntity address);

    @SqlUpdate("""
        UPDATE addresses SET
        name = :name, street = :street, number = :number, complement = :complement,
        district = :district, city = :city, state = :state, zip_code = :zipCode,
        main_address = :mainAddress
        WHERE id = :id
    """)
    void update(@BindBean AddressEntity address);

    @SqlUpdate("DELETE FROM addresses WHERE id = :id")
    void delete(@Bind("id") Long id);

    @SqlUpdate("DELETE FROM addresses WHERE client_id = :clientId")
    void deleteByClientId(@Bind("clientId") Long clientId);
}