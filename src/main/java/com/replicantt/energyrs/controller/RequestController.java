package com.replicantt.energyrs.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.replicantt.energyrs.repository.Request;
import com.replicantt.energyrs.service.RequestService;

@RestController
public class RequestController {

    private final RequestService requestService;
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/requests")
    public List<Request> findAll() {
        return requestService.getAllRequests();
    }

    @PostMapping("/requests")
    public Request addRequest(@RequestBody Request request) {
        return requestService.addRequest(request);
    }

    @DeleteMapping("/requests/{id}")
    public void deleteRequest(@PathVariable String id) {
        requestService.deleteRequest(id);
    }

    @PutMapping("/requests/{id}")
    public void updateRequest(
        @RequestParam(required = true) String id,
        @RequestParam(required = true) String type,
        @RequestParam(required = true) String action
    ) {
        requestService.updateRequest(id, type, action);
    }
}
