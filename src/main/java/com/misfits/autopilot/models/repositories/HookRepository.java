package com.misfits.autopilot.models.repositories;

import com.misfits.autopilot.models.entity.Hook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HookRepository extends JpaRepository<Hook, Long> {

}
