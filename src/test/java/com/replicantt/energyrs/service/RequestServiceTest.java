package com.replicantt.energyrs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.replicantt.energyrs.repository.Request;
import com.replicantt.energyrs.repository.Request.EnumStatus;
import com.replicantt.energyrs.DTO.RequestDTO;
import com.replicantt.energyrs.repository.RequestIdState;
import com.replicantt.energyrs.repository.RequestIdStateRepository;
import com.replicantt.energyrs.repository.RequestRepository;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;
    @Mock
    private RequestIdStateRepository requestIdStateRepository;

    @InjectMocks
    private RequestService requestService;

    @Test
    void testGetAllRequests() {
        Request request = new Request();
        Request request1 = new Request();

        when(requestRepository.findAll()).thenReturn(List.of(request, request1));
        
        List<Request> requests = requestService.getAllRequests();
        
        verify(requestRepository).findAll();
        
        assertNotNull(requests);
        assertEquals(2, requests.size());
    }

    @Test
    void testGetRequestById() {
        Request mockRequest = new Request();
        mockRequest.setId("RQA-000001");
        mockRequest.setCustomerId((long) 6);
        mockRequest.setType("gas");
        mockRequest.setAction("shutdown");
        mockRequest.setStatus(Request.EnumStatus.SUBMITTED);
        LocalDateTime createdAt = LocalDateTime.of(2023, 10, 1, 12, 0, 0);
        mockRequest.setCreatedAt(createdAt);

        when(requestRepository.findById("RQA-000001")).thenReturn(Optional.of(mockRequest));

        Request result = requestService.getRequestById("RQA-000001");

        verify(requestRepository).findById("RQA-000001");

        assertNotNull(result);
        assertEquals("RQA-000001", result.getId());
        assertEquals(6L, result.getCustomerId());
        assertEquals("gas", result.getType());
        assertEquals("shutdown", result.getAction());
        assertEquals(Request.EnumStatus.SUBMITTED, result.getStatus());
        assertEquals(createdAt, result.getCreatedAt());
    }

    @Test 
    void testGetRequestById_withInvalidID () {
        String invalidId = "INVALID_ID";

        when(requestRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            requestService.getRequestById(invalidId);
        });
    }

    @Test
    // Проверка генерации по умолчанию
    void testGenerateRequestId_Default() {
        RequestIdState mockRequestIdState = new RequestIdState();
        mockRequestIdState.setCurrentLetter('A');
        mockRequestIdState.setCurrentNumber(1);

        when(requestIdStateRepository.findById("current_state")).thenReturn(Optional.of(mockRequestIdState));
        String result = requestService.generateRequestId();

        assertEquals("RQA-000001", result);
        assertEquals(2, mockRequestIdState.getCurrentNumber());
        assertEquals('A', mockRequestIdState.getCurrentLetter());

    }

    @Test
    // Проверка смены буквы в имени запроса после достижения максимального числа
    void testGenerateRequestId_IncrementLetter() {
        RequestIdState mockRequestIdState = new RequestIdState();
        mockRequestIdState.setCurrentLetter('A');
        mockRequestIdState.setCurrentNumber(1000000);

        when(requestIdStateRepository.findById("current_state")).thenReturn(Optional.of(mockRequestIdState));
        String result1 = requestService.generateRequestId();

        assertEquals("RQB-000001", result1);
        assertEquals(2, mockRequestIdState.getCurrentNumber());
        assertEquals('B', mockRequestIdState.getCurrentLetter());
    }

    @Test
    // Проверка генерации со случайными значениями
    void testGenerateRequestId_CutomValues() {
        RequestIdState mockRequestIdState = new RequestIdState();
        mockRequestIdState.setCurrentLetter('H');
        mockRequestIdState.setCurrentNumber(625476);

        when(requestIdStateRepository.findById("current_state")).thenReturn(Optional.of(mockRequestIdState));
        String result2 = requestService.generateRequestId();

        assertEquals("RQH-625476", result2);
        assertEquals(625477, mockRequestIdState.getCurrentNumber());
        assertEquals('H', mockRequestIdState.getCurrentLetter());
    }

    @Test
    void testAddRequest() {
        RequestIdState mockRequestIdState = new RequestIdState();
        mockRequestIdState.setCurrentLetter('A');
        mockRequestIdState.setCurrentNumber(1);

        RequestDTO mockRequestDTO = new RequestDTO();
        mockRequestDTO.setCustomerId((long) 6);
        mockRequestDTO.setType("gas");
        mockRequestDTO.setAction("shutdown");
        mockRequestDTO.setStatus(null);
        LocalDateTime testCreatedAt = LocalDateTime.of(2023, 10, 1, 12, 0, 0);
        mockRequestDTO.setCreatedAt(testCreatedAt);

        when(requestIdStateRepository.findById("current_state")).thenReturn(Optional.of(mockRequestIdState));
        when(requestRepository.save(any(Request.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Request result = requestService.addRequest(mockRequestDTO);

        assertEquals("RQA-000001", result.getId());
        assertEquals(6L, result.getCustomerId());
        assertEquals("gas", result.getType());
        assertEquals("shutdown", result.getAction());
        assertEquals(Request.EnumStatus.SUBMITTED, result.getStatus());
        assertEquals(testCreatedAt, result.getCreatedAt());

    }

    @Test
    void testDeleteRequest() {
        String id = "RQA-000001";

        when(requestRepository.existsById(id)).thenReturn(true);
        requestService.deleteRequest(id);

        verify(requestRepository).deleteById(id);
    }

    @Test
    void testDeleteRequest_withId() {
        String InvalidId = "INVALID_ID";

        when(requestRepository.existsById(InvalidId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            requestService.deleteRequest(InvalidId);
        });
    }

    @Test
    void testUpdateRequest() {
        Request mockRequest = new Request();
        mockRequest.setId("RQA-000001");
        mockRequest.setCustomerId((long) 6);
        mockRequest.setType("gas");
        mockRequest.setAction("shutdown");
        mockRequest.setStatus(null);
        LocalDateTime testCreatedAt = LocalDateTime.of(2023, 10, 1, 12, 0, 0);
        mockRequest.setCreatedAt(testCreatedAt);

        when(requestRepository.findById("RQA-000001")).thenReturn(Optional.of(mockRequest));
        when(requestRepository.save(any(Request.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Request result = requestService.updateRequest(
            "RQA-000001", "electricity", "connection", EnumStatus.CANCELLED
            );
        
        assertEquals("RQA-000001", result.getId());
        assertEquals(6L, result.getCustomerId());
        assertEquals("electricity", result.getType());
        assertEquals("connection", result.getAction());
        assertEquals(Request.EnumStatus.CANCELLED, result.getStatus());
        assertEquals(testCreatedAt, result.getCreatedAt());
    }

    @Test
    void testUpdateRequest_withInvalidId() {
        String invalidId = "INVALID_ID";

        when(requestRepository.findById(invalidId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            requestService.updateRequest("RQA-000001", "electricity", "connection", EnumStatus.CANCELLED);
        });
    }

}
