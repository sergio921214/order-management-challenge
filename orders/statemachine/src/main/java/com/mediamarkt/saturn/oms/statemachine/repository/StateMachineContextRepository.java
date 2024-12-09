package com.mediamarkt.saturn.oms.statemachine.repository;

import com.mediamarkt.saturn.oms.statemachine.entity.StateMachineContextEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateMachineContextRepository extends JpaRepository<StateMachineContextEntity, String> {

}

