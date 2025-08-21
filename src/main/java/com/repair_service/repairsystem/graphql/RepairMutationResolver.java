package com.repair_service.repairsystem.graphql;

import com.repair_service.repairsystem.entity.RepairRequest;
import com.repair_service.repairsystem.entity.User;
import com.repair_service.repairsystem.repository.RepairRequestRepository;
import com.repair_service.repairsystem.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.Argument;

@Component
public class RepairMutationResolver {

    private final RepairRequestRepository repairRequestRepository;
    private final UserRepository userRepository;

    public RepairMutationResolver(RepairRequestRepository repairRequestRepository,
                                  UserRepository userRepository) {
        this.repairRequestRepository = repairRequestRepository;
        this.userRepository = userRepository;
    }

    @MutationMapping
    public RepairRequest createRepairRequest(@Argument String trackingId,
                                             @Argument String description,
                                             @Argument Long customerId) {
        User customer = userRepository.findById(customerId).orElseThrow();
        RepairRequest request = new RepairRequest();
        request.setTrackingId(trackingId);
        request.setDescription(description);
        request.setCustomer(customer);
        request.setStatus("OPEN");
        return repairRequestRepository.save(request);
    }

    @MutationMapping
    public RepairRequest updateRepairStatus(@Argument Long id, @Argument String status) {
        RepairRequest request = repairRequestRepository.findById(id).orElseThrow();
        request.setStatus(status);
        return repairRequestRepository.save(request);
    }

    @MutationMapping
    public Boolean deleteRepairRequest(@Argument Long id) {
        if (repairRequestRepository.existsById(id)) {
            repairRequestRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

