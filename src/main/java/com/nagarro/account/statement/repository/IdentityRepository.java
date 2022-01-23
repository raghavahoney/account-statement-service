package com.nagarro.account.statement.repository;

import com.nagarro.account.statement.model.IdentityAttributes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityRepository extends CrudRepository<IdentityAttributes, String> {

}
