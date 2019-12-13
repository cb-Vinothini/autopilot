package com.misfits.autopilot.models.repositories;

import com.misfits.autopilot.models.entity.ActionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionGroupRepository extends JpaRepository<ActionGroup, Long> {
}
