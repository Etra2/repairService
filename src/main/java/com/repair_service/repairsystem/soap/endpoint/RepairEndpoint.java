package com.repair_service.repairsystem.soap.endpoint;

import com.repair_service.repairsystem.soap.gen.RepairRequest;
import com.repair_service.repairsystem.soap.gen.RepairResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class RepairEndpoint {

    private static final String NAMESPACE_URI = "http://repair_service.com/soap";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RepairRequest")
    @ResponsePayload
    public RepairResponse handleRepair(@RequestPayload RepairRequest request) {
        RepairResponse response = new RepairResponse();
        response.setStatus("Repair received for device ID: " + request.getDeviceId());
        return response;
    }
}
