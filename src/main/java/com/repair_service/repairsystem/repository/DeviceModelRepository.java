package com.repair_service.repairsystem.repository;

import com.repair_service.repairsystem.entity.DeviceModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceModelRepository extends JpaRepository<DeviceModel, Long> {
    Optional<DeviceModel> findByModelName(String modelName);
}
