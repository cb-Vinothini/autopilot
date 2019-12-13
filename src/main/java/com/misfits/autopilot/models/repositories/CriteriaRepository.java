package com.misfits.autopilot.models.repositories;

import com.misfits.autopilot.models.entity.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriteriaRepository  extends JpaRepository<Criteria, Long> {
}
