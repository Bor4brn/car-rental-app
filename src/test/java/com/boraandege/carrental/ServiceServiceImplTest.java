package com.boraandege.carrental;

import com.boraandege.carrental.dto.ServiceDTO;
import com.boraandege.carrental.model.AdditionalService;
import com.boraandege.carrental.exception.ResourceNotFoundException;
import com.boraandege.carrental.mapper.ServiceMapper;
import com.boraandege.carrental.repository.ServiceRepository;
import com.boraandege.carrental.service.impl.ServiceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceMapper serviceMapper;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    @Test
    void testAddService() {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("Roadside Assistance");
        serviceDTO.setPrice(BigDecimal.valueOf(20));

        AdditionalService service = new AdditionalService();
        service.setName("Roadside Assistance");
        service.setPrice(BigDecimal.valueOf(20));

        AdditionalService savedService = new AdditionalService();
        savedService.setId(1L);
        savedService.setName("Roadside Assistance");
        savedService.setPrice(BigDecimal.valueOf(20));

        ServiceDTO savedServiceDTO = new ServiceDTO();
        savedServiceDTO.setId(1L);
        savedServiceDTO.setName("Roadside Assistance");
        savedServiceDTO.setPrice(BigDecimal.valueOf(20));

        when(serviceMapper.toEntity(serviceDTO)).thenReturn(service);
        when(serviceRepository.save(service)).thenReturn(savedService);
        when(serviceMapper.toDTO(savedService)).thenReturn(savedServiceDTO);

        ServiceDTO result = serviceService.addService(serviceDTO);

        assertNotNull(result);
        assertEquals("Roadside Assistance", result.getName());
        verify(serviceRepository, times(1)).save(service);
        verify(serviceMapper, times(1)).toEntity(serviceDTO);
        verify(serviceMapper, times(1)).toDTO(savedService);
    }

    @Test
    void testGetServiceById() {
        AdditionalService service = new AdditionalService();
        service.setId(1L);
        service.setName("Roadside Assistance");

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(1L);
        serviceDTO.setName("Roadside Assistance");

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(serviceMapper.toDTO(service)).thenReturn(serviceDTO);

        ServiceDTO result = serviceService.getServiceById(1L);

        assertNotNull(result);
        assertEquals("Roadside Assistance", result.getName());
        verify(serviceRepository, times(1)).findById(1L);
        verify(serviceMapper, times(1)).toDTO(service);
    }

    @Test
    void testGetServiceById_NotFound() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> serviceService.getServiceById(1L));
        verify(serviceRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllServices() {
        AdditionalService service1 = new AdditionalService();
        service1.setId(1L);
        service1.setName("Roadside Assistance");

        AdditionalService service2 = new AdditionalService();
        service2.setId(2L);
        service2.setName("Insurance");

        ServiceDTO serviceDTO1 = new ServiceDTO();
        serviceDTO1.setId(1L);
        serviceDTO1.setName("Roadside Assistance");

        ServiceDTO serviceDTO2 = new ServiceDTO();
        serviceDTO2.setId(2L);
        serviceDTO2.setName("Insurance");

        when(serviceRepository.findAll()).thenReturn(Arrays.asList(service1, service2));
        when(serviceMapper.toDTO(service1)).thenReturn(serviceDTO1);
        when(serviceMapper.toDTO(service2)).thenReturn(serviceDTO2);

        List<ServiceDTO> result = serviceService.getAllServices();

        assertEquals(2, result.size());
        verify(serviceRepository, times(1)).findAll();
        verify(serviceMapper, times(1)).toDTO(service1);
        verify(serviceMapper, times(1)).toDTO(service2);
    }

    @Test
    void testUpdateService() {
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setName("Roadside Assistance Updated");
        serviceDTO.setPrice(BigDecimal.valueOf(25));

        AdditionalService existingService = new AdditionalService();
        existingService.setId(1L);
        existingService.setName("Roadside Assistance");
        existingService.setPrice(BigDecimal.valueOf(20));

        AdditionalService updatedService = new AdditionalService();
        updatedService.setId(1L);
        updatedService.setName("Roadside Assistance Updated");
        updatedService.setPrice(BigDecimal.valueOf(25));

        ServiceDTO updatedServiceDTO = new ServiceDTO();
        updatedServiceDTO.setId(1L);
        updatedServiceDTO.setName("Roadside Assistance Updated");
        updatedServiceDTO.setPrice(BigDecimal.valueOf(25));

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(existingService));
        when(serviceRepository.save(existingService)).thenReturn(updatedService);
        when(serviceMapper.toDTO(updatedService)).thenReturn(updatedServiceDTO);

        ServiceDTO result = serviceService.updateService(1L, serviceDTO);

        assertNotNull(result);
        assertEquals("Roadside Assistance Updated", result.getName());
        verify(serviceRepository, times(1)).findById(1L);
        verify(serviceRepository, times(1)).save(existingService);
        verify(serviceMapper, times(1)).toDTO(updatedService);
    }

    @Test
    void testDeleteService() {
        AdditionalService service = new AdditionalService();
        service.setId(1L);

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));

        serviceService.deleteService(1L);

        verify(serviceRepository, times(1)).findById(1L);
        verify(serviceRepository, times(1)).delete(service);
    }
}
