package com.misfits.autopilot.models.repositories;

import com.misfits.autopilot.models.entity.ActionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionGroupRepository extends JpaRepository<ActionGroup, Long> {
}
