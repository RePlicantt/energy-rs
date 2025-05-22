package com.replicantt.energyrs.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.replicantt.energyrs.DTO.ConnectionDTO;
import com.replicantt.energyrs.repository.Connection;
import com.replicantt.energyrs.repository.ConnectionRepository;
import com.replicantt.energyrs.repository.Connection.EnumRequestType;
import com.replicantt.energyrs.repository.Connection.EnumStatus;

@ExtendWith(MockitoExtension.class)
public class ConnectionServiceTest {

    @Mock
    private ConnectionRepository connectionRepository;

    @InjectMocks
    private ConnectionService connectionService;

    @Test
    void testGetConnection() {
        Connection mockConnection = new Connection(); 
        mockConnection.setMeterId("000000001");
        mockConnection.setDependedRequestId("RQA-000001");
        mockConnection.setType(EnumRequestType.ELECTRICITY);
        mockConnection.setStatus(EnumStatus.ACTIVE);
        mockConnection.setConnectionDate(LocalDate.of(2023, 10, 1));

        when(connectionRepository.findByMeterId("000000001")).thenReturn(mockConnection);

        Connection result = connectionService.getConnection("000000001");

        verify(connectionRepository).findByMeterId("000000001");

        assertNotNull(result);
        assertEquals("000000001", result.getMeterId());
        assertEquals("RQA-000001", result.getDependedRequestId());
        assertEquals(EnumRequestType.ELECTRICITY, result.getType());
        assertEquals(EnumStatus.ACTIVE, result.getStatus());
        assertEquals(LocalDate.of(2023, 10, 1), result.getConnectionDate());
    }

    @Test
    void testGetConnection_withInvalidMeterId() {
        String invalidMeterId = "INVALID_METER_ID";

        when(connectionRepository.findByMeterId(invalidMeterId)).thenReturn(null);
        
        assertThrows(RuntimeException.class, () -> {
            connectionService.getConnection(invalidMeterId);
        });
    }

    @Test
    void testAddConnection() {
        ConnectionDTO mockConnectionDTO = new ConnectionDTO();

        mockConnectionDTO.setMeterId("000000001");
        mockConnectionDTO.setDependedRequestId("RQA-000001");
        mockConnectionDTO.setType("ELECTRICITY");
        mockConnectionDTO.setStatus("ACTIVE");
        mockConnectionDTO.setConnectionDate(LocalDate.of(2023, 10, 1));

        when(connectionRepository.save(any(Connection.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Connection result = connectionService.addConnection(mockConnectionDTO);

        assertNotNull(result);
        assertEquals("000000001", result.getMeterId());
        assertEquals("RQA-000001", result.getDependedRequestId());
        assertEquals(EnumRequestType.ELECTRICITY, result.getType());
        assertEquals(EnumStatus.ACTIVE, result.getStatus());
        assertEquals(LocalDate.of(2023, 10, 1), result.getConnectionDate());
    }

    @Test
    void testAddConnection_withExistingId() {
        String existingMeterId = "EXISTING_METER_ID";

        ConnectionDTO mockConnectionDTO = new ConnectionDTO();
        mockConnectionDTO.setMeterId(existingMeterId);
        mockConnectionDTO.setDependedRequestId("RQA-000001");
        mockConnectionDTO.setType("ELECTRICITY");
        mockConnectionDTO.setStatus("ACTIVE");
        mockConnectionDTO.setConnectionDate(LocalDate.of(2023, 10, 1));

        when(connectionRepository.existsByMeterId(existingMeterId)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            connectionService.addConnection(mockConnectionDTO);
        });

    }

    @Test 
    void testDeleteConnection() {
        String meterId = "000000001";

        when(connectionRepository.existsByMeterId(meterId)).thenReturn(true);
        connectionService.deleteConnection(meterId);
        verify(connectionRepository).deleteByMeterId(meterId);
        
    }

    @Test 
    void testDeleteConnection_withInvalidMeterId() {
        String invalidMeterId = "INVALID_METER_ID";

        when(connectionRepository.existsByMeterId(invalidMeterId)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> {
            connectionService.deleteConnection(invalidMeterId);
        });
    }

    @Test
    void testUpdateConnection() {
        Connection mockConnection = new Connection();
        mockConnection.setMeterId("000000001");
        mockConnection.setDependedRequestId("RQA-000001");
        mockConnection.setType(EnumRequestType.GAS);
        mockConnection.setStatus(EnumStatus.INACTIVE);

        String meterId = "000000001";

        String dependedRequestIdToUpdate = "RQA-000002";
        EnumRequestType typeToUpdate = EnumRequestType.ELECTRICITY;
        EnumStatus statusToUpdate = EnumStatus.ACTIVE;

        when(connectionRepository.findByMeterId(meterId)).thenReturn(mockConnection);
        when(connectionRepository.save(any(Connection.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Connection result = connectionService.updateConnection(meterId, dependedRequestIdToUpdate, typeToUpdate, statusToUpdate);

        assertEquals(meterId, result.getMeterId());
        assertEquals(dependedRequestIdToUpdate, result.getDependedRequestId());
        assertEquals(typeToUpdate, result.getType());
        assertEquals(statusToUpdate, result.getStatus());

    }

    @Test
    void testUpdateConnection_withInvalidMeterId() {
        String invalidMeterId = "INVALID_METER_ID";
        String dependedRequestIdToUpdate = "RQA-000002";
        EnumRequestType typeToUpdate = EnumRequestType.ELECTRICITY;
        EnumStatus statusToUpdate = EnumStatus.ACTIVE;

        when(connectionRepository.findByMeterId(invalidMeterId)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            connectionService.updateConnection(invalidMeterId, dependedRequestIdToUpdate, typeToUpdate, statusToUpdate);
        });

    }

}
