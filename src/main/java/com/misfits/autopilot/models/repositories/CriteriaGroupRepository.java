package com.misfits.autopilot.models.repositories;

import com.misfits.autopilot.models.entity.CriteriaGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CriteriaGroupRepository extends JpaRepository<CriteriaGroup, Long> {

}
