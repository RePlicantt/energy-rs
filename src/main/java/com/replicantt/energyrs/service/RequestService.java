package com.replicantt.energyrs.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.replicantt.energyrs.DTO.RequestDTO;
import com.replicantt.energyrs.repository.Request;
import com.replicantt.energyrs.repository.RequestIdState;
import com.replicantt.energyrs.repository.RequestIdStateRepository;
import com.replicantt.energyrs.repository.RequestRepository;

import jakarta.transaction.Transactional;

@Service
public class RequestService {
    
    private final RequestRepository requestRepository;
    private final RequestIdStateRepository requestIdStateRepository;

    private static final String PREFIX = "RQ";
    private static final String SUFFIX_FORMAT = "%06d"; // Для форматирования чисел как 000001, 000002 и т.д.
    private static final int MAX_NUMBER = 999999; // Максимальное число для ID

    private char currentLetter = 'A'; // Начальная буква
    private int currentNumber = 1; // Начальное число


    public RequestService(RequestRepository requestRepository, RequestIdStateRepository requestIdStateRepository) {
        this.requestRepository = requestRepository;
        this.requestIdStateRepository = requestIdStateRepository;
    }

    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    public Request getRequestById(String id) {
        return requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
    }

    @Transactional
    public Request addRequest(RequestDTO requestDTO) {
        
        String generatedId = generateRequestId();
        requestDTO.setId(generatedId); // Устанавливаем сгенерированный ID в запрос

        // На всякий случай, если вдруг что-то поламается и будет генерировать дубликаты
        if (requestRepository.existsById(generatedId)) {
            throw new RuntimeException("Request already exists with ID: " + generatedId);
        }

        if (!requestDTO.getType().equals("electricity") && !requestDTO.getType().equals("gas")) {
            throw new RuntimeException("Invalid type. Must be 'electricity' or 'gas'.");
        }
        if (!requestDTO.getAction().equals("connection") && !requestDTO.getAction().equals("shutdown")) {
            throw new RuntimeException("Invalid type. Must be 'connection' or 'shutdown'.");
        }
        if (requestDTO.getStatus() == null) {
            requestDTO.setStatus("SUBMITTED"); // Устанавливаем статус по умолчанию
        } else {
            try {
                Request.EnumStatus.valueOf(requestDTO.getStatus());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status. Must be one of: SUBMITTED, IN_PROGRESS, COMPLETED, REJECTED, CANCELLED.");
            }
        }

        Request requestToSave = Request.builder()
            .id(generatedId)
            .customerId(requestDTO.getCustomerId())
            .type(requestDTO.getType())
            .action(requestDTO.getAction())
            .status(Request.EnumStatus.valueOf(requestDTO.getStatus()))
            .createdAt(requestDTO.getCreatedAt())
            .build();

        return requestRepository.save(requestToSave);
    }

    public void deleteRequest(String id) {
        if (!requestRepository.existsById(id)) {
            throw new RuntimeException("Request not found with ID: " + id);
        } else {
            requestRepository.deleteById(id);
        }
    }

    @Transactional
    public String generateRequestId(){
        // Загружаем текущее состояние ID
        RequestIdState idState = requestIdStateRepository.findById("current_state")
            .orElseThrow(() -> new RuntimeException("Request ID state not found"));

        this.currentLetter = idState.getCurrentLetter();
        this.currentNumber = idState.getCurrentNumber();
        
        // Если достигнут лимит (999999), сбрасываем число и увеличиваем букву
        if (currentNumber > MAX_NUMBER) {
            currentNumber = 1;
            currentLetter = getNextLetter(currentLetter);
        }
        
        // Формируем строку с текущей буквой и номером
        String formattedNumber = String.format(SUFFIX_FORMAT, currentNumber);
        // Генерируем ID в формате RQ{буква}-{номер}
        String requestId = PREFIX + currentLetter + "-" + formattedNumber;
        
        // Проверяем, существует ли уже такой ID в базе данных
        while (requestRepository.existsById(requestId)) {
            // Если ID уже существует, увеличиваем номер и повторяем
            currentNumber++;
            formattedNumber = String.format(SUFFIX_FORMAT, currentNumber);
            requestId = PREFIX + currentLetter + "-" + formattedNumber;
        }

        // Обновляем номер
        currentNumber++;


        idState.setCurrentLetter(this.currentLetter);
        idState.setCurrentNumber(this.currentNumber);

        return requestId;
    }

    @Transactional
    public Request updateRequest(String id, String type, String action, Request.EnumStatus status) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setType(type);
        request.setAction(action);
        request.setStatus(status);

        return requestRepository.save(request);
    }

    private char getNextLetter(char currentLetter) {
        if (currentLetter == 'Z') {
            return 'A'; // Если достигли 'Z', сбрасываем на 'A'
        } else {
            return (char) (currentLetter + 1); // Переход к следующей букве
        }
    }

}
