package com.replicantt.energyrs.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.replicantt.energyrs.DTO.RequestDTO;
import com.replicantt.energyrs.repository.Request;
import com.replicantt.energyrs.service.RequestService;

import jakarta.validation.Valid;

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

    @GetMapping("/requests/{id}")
    public ResponseEntity<Request> findById(@PathVariable String id) {
        Request request = requestService.getRequestById(id);
        if (request != null) {
            return ResponseEntity.ok(request); // Возвращаем 200 OK с найденным запросом
        } else {
            return ResponseEntity.notFound().build(); // Возвращаем 404 Not Found, если запрос не найден
        }
    }


    @PostMapping("/requests")
    public ResponseEntity<Void> addRequest(@Valid @RequestBody RequestDTO requestDTO) {     
        requestService.addRequest(requestDTO);
        return ResponseEntity.status(201).build(); // Возвращаем 201 Created, если запрос успешно добавлен
    }

    @DeleteMapping("/requests/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable String id) {
        try {
            requestService.deleteRequest(id);
            return ResponseEntity.noContent().build(); // Возвращаем 204 No Content, если запрос успешно удален
        } catch (RuntimeException  e) {
            return ResponseEntity.notFound().build(); // Возвращаем 404 Not Found, если запрос не найден
        }
    }

    @PutMapping("/requests/{id}")
    public ResponseEntity<Void> updateRequest(
        @PathVariable(required = true) String id,
        @RequestParam(required = true) String type,
        @RequestParam(required = true) String action
    ) {
        Request request = requestService.getRequestById(id);
        if (request != null) {
            requestService.updateRequest(id, type, action);
            return ResponseEntity.ok().build(); // Возвращаем 200 OK, если запрос успешно обновлен
        } else {
            return ResponseEntity.notFound().build(); // Возвращаем 404 Not Found, если запрос не найден
        }
    }
}
