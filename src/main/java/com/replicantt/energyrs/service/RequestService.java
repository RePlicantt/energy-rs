package com.replicantt.energyrs.service;

import java.util.List;

import org.springframework.stereotype.Service;

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
    @Transactional
    public Request addRequest(Request request) {
        // Загружаем текущее состояние ID (один запрос вместо двух)
        RequestIdState idState = requestIdStateRepository.findById("current_state")
            .orElseThrow(() -> new RuntimeException("Request ID state not found"));

        this.currentLetter = idState.getCurrentLetter();
        this.currentNumber = idState.getCurrentNumber();

        String generatedId = generateRequestId();
        request.setId(generatedId); // Устанавливаем сгенерированный ID в запрос

        if (!request.getType().equals("electricity") && !request.getType().equals("gas")) {
            throw new RuntimeException("Invalid type. Must be 'electricity' or 'gas'.");
        }
        if (!request.getAction().equals("connection") && !request.getAction().equals("shutdown")) {
            throw new RuntimeException("Invalid type. Must be 'connection' or 'shutdown'.");
        }

        idState.setCurrentLetter(this.currentLetter);
        idState.setCurrentNumber(this.currentNumber);

        return requestRepository.save(request);
    }

    public void deleteRequest(String id) {
        requestRepository.deleteById(id);
    }

    @Transactional
    public String generateRequestId(){
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

        // Если достигнут лимит (999999), сбрасываем число и увеличиваем букву
        if (currentNumber > MAX_NUMBER) {
            currentNumber = 1;
            currentLetter = getNextLetter(currentLetter);
        }


        return requestId;
    }

    public void updateRequest(String id, String type, String action) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
        
        request.setType(type);
        request.setAction(action);

        requestRepository.save(request);
    }

    private char getNextLetter(char currentLetter) {
        if (currentLetter == 'Z') {
            return 'A'; // Если достигли 'Z', сбрасываем на 'A'
        } else {
            return (char) (currentLetter + 1); // Переход к следующей букве
        }
    }

}
