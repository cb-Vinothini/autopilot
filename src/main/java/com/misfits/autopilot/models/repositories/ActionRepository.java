package com.misfits.autopilot.models.repositories;

import com.misfits.autopilot.models.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
