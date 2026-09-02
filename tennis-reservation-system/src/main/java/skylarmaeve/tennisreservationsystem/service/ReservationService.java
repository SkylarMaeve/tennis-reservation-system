package skylarmaeve.tennisreservationsystem.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import skylarmaeve.tennisreservationsystem.dao.CourtDao;
import skylarmaeve.tennisreservationsystem.dao.CustomerDao;
import skylarmaeve.tennisreservationsystem.dao.ReservationDao;
import skylarmaeve.tennisreservationsystem.dto.CourtDto;
import skylarmaeve.tennisreservationsystem.dto.ReservationRequest;
import skylarmaeve.tennisreservationsystem.dto.ReservationResponseDto;
import skylarmaeve.tennisreservationsystem.exception.CourtException;
import skylarmaeve.tennisreservationsystem.exception.ReservationException;
import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.Customer;
import skylarmaeve.tennisreservationsystem.model.Reservation;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Transactional
@Service
public class ReservationService {
    private final CourtDao courtDao;
    private final ReservationDao reservationDao;
    private final CustomerDao customerDao;

    public ReservationService(CourtDao courtDao, ReservationDao reservationDao, CustomerDao customerDao) {
        this.courtDao = courtDao;
        this.reservationDao = reservationDao;
        this.customerDao = customerDao;
    }


    public ReservationResponseDto createReservation(ReservationRequest dto) {
        if (dto.getStartTime().isAfter(dto.getEndTime()) || dto.getStartTime().isEqual(dto.getEndTime())) {
            throw new ReservationException("End time must be after start time");
        }

        if (dto.getStartTime().isAfter(dto.getEndTime()) || dto.getStartTime().isEqual(dto.getEndTime())) {
            throw new ReservationException("End time must be after start time");
        }

        Court court = courtDao.findById(dto.getCourtNumber())
                .orElseThrow(() -> new ReservationException("Selected Court does not exist."));

        if (!reservationDao.isFreeTimeSlot(court.getCourtNumber(), dto.getStartTime(), dto.getEndTime())) {
            throw new ReservationException("End time must be after start time");
        }

        Customer customer = customerDao.findByPhoneNumber(dto.getCustomerPhoneNumber()).orElseGet(() -> {
            Customer newCustomer = new Customer();
            newCustomer.setPhoneNumber(dto.getCustomerPhoneNumber());
            newCustomer.setName(dto.getCustomerName());
            return newCustomer;
        });

        Long duration = Duration.between(dto.getStartTime(), dto.getEndTime()).toMinutes();
        BigDecimal pricePerMinute = court.getSurfaceType().getPricePerMinute();
        BigDecimal price = pricePerMinute.multiply(BigDecimal.valueOf(duration));

        if (dto.isDoubles()) {
            price = pricePerMinute.multiply(new BigDecimal("1.5"));
        }

        Reservation reservation = new Reservation();

        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setDoubles(dto.isDoubles());
        reservation.setPrice(price);

        reservationDao.save(reservation);
        return new ReservationResponseDto(reservation);
    }

    public ReservationResponseDto get(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ReservationException("Reservation not found."));
        return new ReservationResponseDto(reservation);
    }

    public List<ReservationResponseDto> getAll() {
        return reservationDao.findAll()
                .stream().map(ReservationResponseDto::new)
                .toList();
    }

    public List<ReservationResponseDto> getReservationsByCourtNumber(Integer courtNumber) {
        return reservationDao.findByCourtNumber(courtNumber)
                .stream().map(ReservationResponseDto::new)
                .toList();
    }

    public List<ReservationResponseDto> getReservationsByPhoneNumber(String phoneNumber, boolean onlyFuture) {
        return reservationDao.findByPhoneNumber(phoneNumber, onlyFuture)
                .stream().map(ReservationResponseDto::new)
                .toList();
    }

    public ReservationResponseDto updateReservation(Long id, ReservationRequest dto) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ReservationException("Reservation not found."));

        boolean isFree = reservationDao.isFreeTimeSlot(dto.getCourtNumber(), dto.getStartTime(), dto.getEndTime(), id);
        if (!isFree) {
            throw new ReservationException("Reservation slot is full");
        }

        Long duration = Duration.between(dto.getStartTime(), dto.getEndTime()).toMinutes();
        BigDecimal pricePerMinute = reservation.getCourt().getSurfaceType().getPricePerMinute();

        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setPrice(pricePerMinute.multiply(BigDecimal.valueOf(duration)));

        if (reservation.isDoubles() != dto.isDoubles()) {
            reservation.setDoubles(dto.isDoubles());
            BigDecimal price = reservation.getPrice();

            // From Single to Doubles
            if (reservation.isDoubles()) {
                reservation.setPrice(price.multiply(new BigDecimal("1.5")));
            } else // From Doubles to Singles
            {
                reservation.setPrice(price.divide(new BigDecimal("1.5")));
            }
        }

        reservationDao.save(reservation);
        return new ReservationResponseDto(reservation);
    }

    public void delete(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ReservationException("Selected Reservation does not exist."));
        reservationDao.delete(reservation);
    }


}
