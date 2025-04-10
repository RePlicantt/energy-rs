package com.replicantt.energyrs.service;

import java.util.List;

import com.replicantt.energyrs.repository.Request;
import com.replicantt.energyrs.repository.RequestRepository;

public class RequestService {

    private final RequestRepository requestRepository;

    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request addRequest(Request request) {
        return requestRepository.save(request);
    }

    public void deleteRequest(Long id) {
        requestRepository.deleteById(id);
    }
    public void updateRequest(String id, String type, String action) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setType(type);
        request.setAction(action);

        requestRepository.save(request);
    }

}
