package com.boraandege.carrental;

import com.boraandege.carrental.dto.ReservationDTO;
import com.boraandege.carrental.model.*;
import com.boraandege.carrental.enums.CarStatus;
import com.boraandege.carrental.exception.BusinessException;
import com.boraandege.carrental.mapper.ReservationMapper;
import com.boraandege.carrental.repository.*;
import com.boraandege.carrental.service.impl.ReservationServiceImpl;
import com.boraandege.carrental.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetReservationByNumber_Success() {
        String reservationNumber = "12345678";
        Reservation reservation = new Reservation();
        reservation.setReservationNumber(reservationNumber);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservationNumber(reservationNumber);

        when(reservationRepository.findByReservationNumber(reservationNumber))
                .thenReturn(Optional.of(reservation));

        when(reservationMapper.toDTO(reservation)).thenReturn(reservationDTO);

        ReservationDTO result = reservationService.getReservationByNumber(reservationNumber);

        assertNotNull(result);
        assertEquals(reservationNumber, result.getReservationNumber());

        verify(reservationRepository).findByReservationNumber(reservationNumber);
        verify(reservationMapper).toDTO(reservation);
    }

    @Test
    public void testGetReservationByNumber_ReservationNotFound() {
        String reservationNumber = "12345678";

        when(reservationRepository.findByReservationNumber(reservationNumber))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                reservationService.getReservationByNumber(reservationNumber)
        );
    }

    @Test
    public void testMakeReservation_Success() {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setCarBarcodeNumber("ABC123");
        reservationDTO.setMemberId(1L);
        reservationDTO.setPickUpLocationCode("LOC1");
        reservationDTO.setDropOffLocationCode("LOC2");
        reservationDTO.setDayCount(3);

        Car car = new Car();
        car.setBarcodeNumber("ABC123");
        car.setStatus(CarStatus.AVAILABLE);
        car.setDailyPrice(new BigDecimal("100.00"));

        Member member = new Member();
        member.setId(1L);

        Location pickUpLocation = new Location();
        pickUpLocation.setCode("LOC1");

        Location dropOffLocation = new Location();
        dropOffLocation.setCode("LOC2");

        Reservation reservation = new Reservation();
        reservation.setReservationNumber("12345678");
        reservation.setCar(car);
        reservation.setMember(member);
        reservation.setPickUpLocation(pickUpLocation);
        reservation.setDropOffLocation(dropOffLocation);

        when(carRepository.findByBarcodeNumber(reservationDTO.getCarBarcodeNumber()))
                .thenReturn(Optional.of(car));
        when(memberRepository.findById(reservationDTO.getMemberId()))
                .thenReturn(Optional.of(member));
        when(locationRepository.findByCode(reservationDTO.getPickUpLocationCode()))
                .thenReturn(Optional.of(pickUpLocation));
        when(locationRepository.findByCode(reservationDTO.getDropOffLocationCode()))
                .thenReturn(Optional.of(dropOffLocation));
        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(reservation);

        ReservationDTO savedReservationDTO = new ReservationDTO();
        savedReservationDTO.setReservationNumber("12345678");
        when(reservationMapper.toDTO(reservation)).thenReturn(savedReservationDTO);

        ReservationDTO result = reservationService.makeReservation(reservationDTO);

        assertNotNull(result);
        assertEquals("12345678", result.getReservationNumber());

        verify(reservationRepository).save(any(Reservation.class));
        verify(reservationMapper).toDTO(reservation);
    }

    @Test
    public void testMakeReservation_CarNotAvailable() {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setCarBarcodeNumber("ABC123");

        Car car = new Car();
        car.setBarcodeNumber("ABC123");
        car.setStatus(CarStatus.LOANED);

        when(carRepository.findByBarcodeNumber(reservationDTO.getCarBarcodeNumber()))
                .thenReturn(Optional.of(car));

        assertThrows(BusinessException.class, () -> reservationService.makeReservation(reservationDTO));
    }

    @Test
    public void testMakeReservation_MemberNotFound() {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setCarBarcodeNumber("ABC123");
        reservationDTO.setMemberId(1L);

        Car car = new Car();
        car.setBarcodeNumber("ABC123");
        car.setStatus(CarStatus.AVAILABLE);

        when(carRepository.findByBarcodeNumber(reservationDTO.getCarBarcodeNumber()))
                .thenReturn(Optional.of(car));
        when(memberRepository.findById(reservationDTO.getMemberId()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.makeReservation(reservationDTO));
    }

    @Test
    public void testMakeReservation_LocationNotFound() {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setCarBarcodeNumber("ABC123");
        reservationDTO.setMemberId(1L);
        reservationDTO.setPickUpLocationCode("LOC1");
        reservationDTO.setDropOffLocationCode("LOC2");

        Car car = new Car();
        car.setBarcodeNumber("ABC123");
        car.setStatus(CarStatus.AVAILABLE);

        Member member = new Member();
        member.setId(1L);

        when(carRepository.findByBarcodeNumber(reservationDTO.getCarBarcodeNumber()))
                .thenReturn(Optional.of(car));
        when(memberRepository.findById(reservationDTO.getMemberId()))
                .thenReturn(Optional.of(member));
        when(locationRepository.findByCode(reservationDTO.getPickUpLocationCode()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.makeReservation(reservationDTO));
    }
}
