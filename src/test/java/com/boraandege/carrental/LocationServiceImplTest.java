package com.boraandege.carrental;
import com.boraandege.carrental.dto.LocationDTO;
import com.boraandege.carrental.model.Location;
import com.boraandege.carrental.mapper.LocationMapper;
import com.boraandege.carrental.repository.LocationRepository;
import com.boraandege.carrental.service.impl.LocationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationServiceImpl locationService;

    @Test
    void testAddLocation() {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCode("LOC1");
        locationDTO.setName("Downtown Office");
        locationDTO.setAddress("123 Main St");

        Location location = new Location();
        location.setCode("LOC1");
        location.setName("Downtown Office");
        location.setAddress("123 Main St");

        Location savedLocation = new Location();
        savedLocation.setId(1L);
        savedLocation.setCode("LOC1");
        savedLocation.setName("Downtown Office");
        savedLocation.setAddress("123 Main St");

        LocationDTO savedLocationDTO = new LocationDTO();
        savedLocationDTO.setId(1L);
        savedLocationDTO.setCode("LOC1");
        savedLocationDTO.setName("Downtown Office");
        savedLocationDTO.setAddress("123 Main St");

        when(locationMapper.toEntity(locationDTO)).thenReturn(location);
        when(locationRepository.save(location)).thenReturn(savedLocation);
        when(locationMapper.toDTO(savedLocation)).thenReturn(savedLocationDTO);

        LocationDTO result = locationService.addLocation(locationDTO);

        assertNotNull(result);
        assertEquals("LOC1", result.getCode());
        verify(locationRepository, times(1)).save(location);
        verify(locationMapper, times(1)).toEntity(locationDTO);
        verify(locationMapper, times(1)).toDTO(savedLocation);
    }

    @Test
    void testGetLocationById() {
        Location location = new Location();
        location.setId(1L);
        location.setCode("LOC1");

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setCode("LOC1");

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(locationMapper.toDTO(location)).thenReturn(locationDTO);

        LocationDTO result = locationService.getLocationById(1L);

        assertNotNull(result);
        assertEquals("LOC1", result.getCode());
        verify(locationRepository, times(1)).findById(1L);
        verify(locationMapper, times(1)).toDTO(location);
    }

    @Test
    void testGetLocationByCode() {
        Location location = new Location();
        location.setId(1L);
        location.setCode("LOC1");

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setCode("LOC1");

        when(locationRepository.findByCode("LOC1")).thenReturn(Optional.of(location));
        when(locationMapper.toDTO(location)).thenReturn(locationDTO);

        LocationDTO result = locationService.getLocationByCode("LOC1");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(locationRepository, times(1)).findByCode("LOC1");
        verify(locationMapper, times(1)).toDTO(location);
    }

    @Test
    void testGetAllLocations() {
        Location location1 = new Location();
        location1.setId(1L);
        location1.setCode("LOC1");

        Location location2 = new Location();
        location2.setId(2L);
        location2.setCode("LOC2");

        LocationDTO locationDTO1 = new LocationDTO();
        locationDTO1.setId(1L);
        locationDTO1.setCode("LOC1");

        LocationDTO locationDTO2 = new LocationDTO();
        locationDTO2.setId(2L);
        locationDTO2.setCode("LOC2");

        when(locationRepository.findAll()).thenReturn(Arrays.asList(location1, location2));
        when(locationMapper.toDTO(location1)).thenReturn(locationDTO1);
        when(locationMapper.toDTO(location2)).thenReturn(locationDTO2);

        List<LocationDTO> result = locationService.getAllLocations();

        assertEquals(2, result.size());
        verify(locationRepository, times(1)).findAll();
        verify(locationMapper, times(1)).toDTO(location1);
        verify(locationMapper, times(1)).toDTO(location2);
    }

    @Test
    void testUpdateLocation() {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setCode("LOC1");
        locationDTO.setName("Updated Office");
        locationDTO.setAddress("456 Main St");

        Location existingLocation = new Location();
        existingLocation.setId(1L);
        existingLocation.setCode("LOC1");
        existingLocation.setName("Downtown Office");
        existingLocation.setAddress("123 Main St");

        Location updatedLocation = new Location();
        updatedLocation.setId(1L);
        updatedLocation.setCode("LOC1");
        updatedLocation.setName("Updated Office");
        updatedLocation.setAddress("456 Main St");

        LocationDTO updatedLocationDTO = new LocationDTO();
        updatedLocationDTO.setId(1L);
        updatedLocationDTO.setCode("LOC1");
        updatedLocationDTO.setName("Updated Office");
        updatedLocationDTO.setAddress("456 Main St");

        when(locationRepository.findById(1L)).thenReturn(Optional.of(existingLocation));
        when(locationRepository.save(existingLocation)).thenReturn(updatedLocation);
        when(locationMapper.toDTO(updatedLocation)).thenReturn(updatedLocationDTO);

        LocationDTO result = locationService.updateLocation(1L, locationDTO);

        assertNotNull(result);
        assertEquals("Updated Office", result.getName());
        verify(locationRepository, times(1)).findById(1L);
        verify(locationRepository, times(1)).save(existingLocation);
        verify(locationMapper, times(1)).toDTO(updatedLocation);
    }

    @Test
    void testDeleteLocation() {
        Location location = new Location();
        location.setId(1L);

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        locationService.deleteLocation(1L);

        verify(locationRepository, times(1)).findById(1L);
        verify(locationRepository, times(1)).delete(location);
    }
}
