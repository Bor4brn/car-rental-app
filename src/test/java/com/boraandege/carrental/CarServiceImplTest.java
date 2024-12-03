package com.boraandege.carrental;

import com.boraandege.carrental.dto.CarDTO;
import com.boraandege.carrental.model.Car;
import com.boraandege.carrental.enums.CarStatus;
import com.boraandege.carrental.enums.CarType;
import com.boraandege.carrental.enums.TransmissionType;
import com.boraandege.carrental.exception.ResourceNotFoundException;
import com.boraandege.carrental.mapper.CarMapper;
import com.boraandege.carrental.repository.CarRepository;
import com.boraandege.carrental.service.impl.CarServiceImpl;
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
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void testAddCar() {
        CarDTO carDTO = new CarDTO();
        carDTO.setBarcodeNumber("CAR123");
        carDTO.setBrand("Toyota");
        carDTO.setModel("Camry");
        carDTO.setDailyPrice(BigDecimal.valueOf(50));
        carDTO.setTransmissionType(TransmissionType.AUTOMATIC);
        carDTO.setCarType(CarType.STANDARD);

        Car car = new Car();
        car.setBarcodeNumber("CAR123");
        car.setBrand("Toyota");
        car.setModel("Camry");
        car.setDailyPrice(BigDecimal.valueOf(50));
        car.setTransmissionType(TransmissionType.AUTOMATIC);
        car.setCarType(CarType.STANDARD);
        car.setStatus(CarStatus.AVAILABLE);

        CarDTO savedCarDTO = new CarDTO();
        savedCarDTO.setBarcodeNumber("CAR123");
        savedCarDTO.setBrand("Toyota");
        savedCarDTO.setModel("Camry");
        savedCarDTO.setDailyPrice(BigDecimal.valueOf(50));
        savedCarDTO.setTransmissionType(TransmissionType.AUTOMATIC);
        savedCarDTO.setCarType(CarType.STANDARD);

        when(carMapper.toEntity(carDTO)).thenReturn(car);
        when(carRepository.save(any(Car.class))).thenReturn(car);
        when(carMapper.toDTO(car)).thenReturn(savedCarDTO);
        CarDTO result = carService.addCar(carDTO);

        assertNotNull(result);
        assertEquals("CAR123", result.getBarcodeNumber());
        assertEquals("Toyota", result.getBrand());
        assertEquals("Camry", result.getModel());
        assertEquals(BigDecimal.valueOf(50), result.getDailyPrice());
        assertEquals(TransmissionType.AUTOMATIC, result.getTransmissionType());
        assertEquals(CarType.STANDARD, result.getCarType());

        verify(carMapper, times(1)).toEntity(carDTO);
        verify(carRepository, times(1)).save(any(Car.class));
        verify(carMapper, times(1)).toDTO(car);
    }

    @Test
    void testGetCarById() {
        Car car = new Car();
        car.setId(1L);
        car.setBarcodeNumber("CAR123");
        car.setBrand("Toyota");
        car.setModel("Camry");

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        CarDTO carDTO = new CarDTO();
        carDTO.setBarcodeNumber("CAR123");
        carDTO.setBrand("Toyota");
        carDTO.setModel("Camry");
        when(carMapper.toDTO(car)).thenReturn(carDTO);

        CarDTO result = carService.getCarById(1L);

        assertNotNull(result);
        assertEquals("CAR123", result.getBarcodeNumber());
        assertEquals("Toyota", result.getBrand());
        assertEquals("Camry", result.getModel());

        verify(carRepository, times(1)).findById(1L);
        verify(carMapper, times(1)).toDTO(car);
    }

    @Test
    void testGetCarById_NotFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carService.getCarById(1L));
    }

    @Test
    void testGetAllCars() {
        Car car1 = new Car();
        car1.setId(1L);
        car1.setBarcodeNumber("CAR123");

        Car car2 = new Car();
        car2.setId(2L);
        car2.setBarcodeNumber("CAR456");

        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));

        List<CarDTO> result = carService.getAllCars();

        assertEquals(2, result.size());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCar() {
        CarDTO carDTO = new CarDTO();
        carDTO.setBarcodeNumber("CAR123");
        carDTO.setBrand("Toyota");
        carDTO.setModel("Corolla");

        Car existingCar = new Car();
        existingCar.setId(1L);
        existingCar.setBarcodeNumber("CAR123");
        existingCar.setBrand("Toyota");
        existingCar.setModel("Camry");

        CarDTO updatedCarDTO = new CarDTO();
        updatedCarDTO.setBarcodeNumber("CAR123");
        updatedCarDTO.setBrand("Toyota");
        updatedCarDTO.setModel("Corolla");

        when(carRepository.findById(1L)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(any(Car.class))).thenReturn(existingCar);
        when(carMapper.toDTO(existingCar)).thenReturn(updatedCarDTO);

        CarDTO result = carService.updateCar(1L, carDTO);

        assertNotNull(result);
        assertEquals("Corolla", result.getModel());
        assertEquals("Toyota", result.getBrand());
        assertEquals("CAR123", result.getBarcodeNumber());

        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(any(Car.class));
        verify(carMapper, times(1)).toDTO(existingCar);
    }

    @Test
    void testDeleteCar() {
        Car car = new Car();
        car.setId(1L);

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        carService.deleteCar(1L);

        verify(carRepository, times(1)).delete(car);
    }

    @Test
    void testSearchAvailableCars() {
        Car car = new Car();
        car.setId(1L);
        car.setCarType(CarType.STANDARD);
        car.setTransmissionType(TransmissionType.AUTOMATIC);
        car.setStatus(CarStatus.AVAILABLE);
        car.setBrand("Toyota");
        car.setModel("Corolla");

        CarDTO carDTO = new CarDTO();
        carDTO.setId(1L);
        carDTO.setCarType(CarType.STANDARD);
        carDTO.setTransmissionType(TransmissionType.AUTOMATIC);
        carDTO.setStatus(CarStatus.AVAILABLE);
        carDTO.setBrand("Toyota");
        carDTO.setModel("Corolla");

        when(carRepository.findByStatusAndCarTypeAndTransmissionType(
                CarStatus.AVAILABLE, CarType.STANDARD, TransmissionType.AUTOMATIC))
                .thenReturn(List.of(car));
        when(carMapper.toDTO(car)).thenReturn(carDTO);

        List<CarDTO> result = carService.searchAvailableCars(CarType.STANDARD, TransmissionType.AUTOMATIC);

        assertEquals(1, result.size());
        assertEquals(carDTO, result.get(0));

        verify(carRepository, times(1))
                .findByStatusAndCarTypeAndTransmissionType(CarStatus.AVAILABLE, CarType.STANDARD, TransmissionType.AUTOMATIC);
    }

    @Test
    void testDeleteCarByBarcode_Success() {
        Car car = new Car();
        car.setId(1L);
        car.setBarcodeNumber("CAR123");
        car.setStatus(CarStatus.AVAILABLE);
        car.setReservations(List.of());

        when(carRepository.findByBarcodeNumber("CAR123")).thenReturn(Optional.of(car));

        boolean result = carService.deleteCarByBarcode("CAR123");

        assertTrue(result);
        verify(carRepository, times(1)).delete(car);
    }

    @Test
    void testDeleteCarByBarcode_Failure() {
        Car car = new Car();
        car.setId(1L);
        car.setBarcodeNumber("CAR123");
        car.setStatus(CarStatus.RESERVED);

        when(carRepository.findByBarcodeNumber("CAR123")).thenReturn(Optional.of(car));

        boolean result = carService.deleteCarByBarcode("CAR123");

        assertFalse(result);
        verify(carRepository, never()).delete(car);
    }
}
