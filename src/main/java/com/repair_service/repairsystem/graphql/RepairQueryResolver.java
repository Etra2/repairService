package com.repair_service.repairsystem.graphql;

import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.repository.RepairRequestRepository;
import org.springframework.stereotype.Component;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;

import java.util.List;

@Component
public class RepairQueryResolver {

    private final RepairRequestRepository repairRequestRepository;

    public RepairQueryResolver(RepairRequestRepository repairRequestRepository) {
        this.repairRequestRepository = repairRequestRepository;
    }

    @QueryMapping
    public List<RepairRequest> getAllRepairRequests() {
        return repairRequestRepository.findAll();
    }

    @QueryMapping
    public RepairRequest getRepairRequestById(@Argument Long id) {
        return repairRequestRepository.findById(id).orElse(null);
    }
}
