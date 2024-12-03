package com.boraandege.carrental;
import com.boraandege.carrental.dto.EquipmentDTO;
import com.boraandege.carrental.model.Equipment;
import com.boraandege.carrental.exception.ResourceNotFoundException;
import com.boraandege.carrental.mapper.EquipmentMapper;
import com.boraandege.carrental.repository.EquipmentRepository;
import com.boraandege.carrental.service.impl.EquipmentServiceImpl;
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
class EquipmentServiceImplTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private EquipmentMapper equipmentMapper;


    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    @Test
    void testAddEquipment() {
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setName("GPS");
        equipmentDTO.setPrice(BigDecimal.valueOf(10));

        Equipment equipment = new Equipment();
        equipment.setName("GPS");
        equipment.setPrice(BigDecimal.valueOf(10));

        when(equipmentMapper.toEntity(equipmentDTO)).thenReturn(equipment);
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        EquipmentDTO result = equipmentService.addEquipment(equipmentDTO);

        assertNotNull(result);
        assertEquals("GPS", result.getName());
        assertEquals(BigDecimal.valueOf(10), result.getPrice());
        verify(equipmentMapper, times(1)).toEntity(equipmentDTO);
        verify(equipmentRepository, times(1)).save(any(Equipment.class));
        verify(equipmentMapper, times(1)).toDTO(equipment);
    }

    @Test
    void testGetEquipmentById() {
        Equipment equipment = new Equipment();
        equipment.setId(1L);
        equipment.setName("GPS");
        equipment.setPrice(BigDecimal.valueOf(10));

        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setId(1L);
        equipmentDTO.setName("GPS");
        equipmentDTO.setPrice(BigDecimal.valueOf(10));

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(equipmentMapper.toDTO(equipment)).thenReturn(equipmentDTO);

        EquipmentDTO result = equipmentService.getEquipmentById(1L);

        assertNotNull(result, "The result should not be null");
        assertEquals("GPS", result.getName());
        assertEquals(BigDecimal.valueOf(10), result.getPrice());
        verify(equipmentRepository, times(1)).findById(1L);
        verify(equipmentMapper, times(1)).toDTO(equipment);
    }


    @Test
    void testGetEquipmentById_NotFound() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.getEquipmentById(1L));
    }

    @Test
    void testGetAllEquipments() {
        Equipment equipment1 = new Equipment();
        equipment1.setId(1L);
        equipment1.setName("GPS");

        Equipment equipment2 = new Equipment();
        equipment2.setId(2L);
        equipment2.setName("Child Seat");

        when(equipmentRepository.findAll()).thenReturn(Arrays.asList(equipment1, equipment2));

        List<EquipmentDTO> result = equipmentService.getAllEquipments();

        assertEquals(2, result.size());
        verify(equipmentRepository, times(1)).findAll();
    }

    @Test
    void testUpdateEquipment() {
        EquipmentDTO equipmentDTO = new EquipmentDTO();
        equipmentDTO.setName("GPS Updated");
        equipmentDTO.setPrice(BigDecimal.valueOf(12));

        Equipment existingEquipment = new Equipment();
        existingEquipment.setId(1L);
        existingEquipment.setName("GPS");
        existingEquipment.setPrice(BigDecimal.valueOf(10));

        Equipment updatedEquipment = new Equipment();
        updatedEquipment.setId(1L);
        updatedEquipment.setName("GPS Updated");
        updatedEquipment.setPrice(BigDecimal.valueOf(12));

        EquipmentDTO updatedEquipmentDTO = new EquipmentDTO();
        updatedEquipmentDTO.setId(1L);
        updatedEquipmentDTO.setName("GPS Updated");
        updatedEquipmentDTO.setPrice(BigDecimal.valueOf(12));


        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(existingEquipment));
        when(equipmentRepository.save(existingEquipment)).thenReturn(updatedEquipment);
        when(equipmentMapper.toDTO(updatedEquipment)).thenReturn(updatedEquipmentDTO);


        EquipmentDTO result = equipmentService.updateEquipment(1L, equipmentDTO);


        assertNotNull(result, "The result should not be null");
        assertEquals("GPS Updated", result.getName());
        assertEquals(BigDecimal.valueOf(12), result.getPrice());
        verify(equipmentRepository, times(1)).findById(1L);
        verify(equipmentRepository, times(1)).save(existingEquipment);
        verify(equipmentMapper, times(1)).toDTO(updatedEquipment);
    }


    @Test
    void testDeleteEquipment() {
        Equipment equipment = new Equipment();
        equipment.setId(1L);

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        equipmentService.deleteEquipment(1L);

        verify(equipmentRepository, times(1)).delete(equipment);
    }
}
