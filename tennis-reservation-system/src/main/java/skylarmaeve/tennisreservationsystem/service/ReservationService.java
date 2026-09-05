package skylarmaeve.tennisreservationsystem.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import skylarmaeve.tennisreservationsystem.dao.CourtDao;
import skylarmaeve.tennisreservationsystem.dao.CustomerDao;
import skylarmaeve.tennisreservationsystem.dao.ReservationDao;
import skylarmaeve.tennisreservationsystem.dto.ReservationDto;
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


    public ReservationDto create(ReservationDto dto) {
        if (dto.getStartTime().isAfter(dto.getEndTime()) || dto.getStartTime().isEqual(dto.getEndTime())) {
            throw new ReservationException("End time must be after start time");
        }

        Court court = courtDao.findById(dto.getCourtNumber())
                .orElseThrow(() -> new ReservationException("Selected Court does not exist."));

        if (!reservationDao.isFreeTimeSlot(court.getCourtNumber(), dto.getStartTime(), dto.getEndTime())) {
            throw new ReservationException("Selected time slot is already reserved");
        }

        Customer customer = customerDao.findByPhoneNumber(dto.getPhoneNumber()).orElseGet(() -> {
            Customer newCustomer = new Customer();
            newCustomer.setPhoneNumber(dto.getPhoneNumber());
            newCustomer.setName(dto.getCustomerName());
            newCustomer = customerDao.save(newCustomer);
            return newCustomer;
        });

        long duration = Duration.between(dto.getStartTime(), dto.getEndTime()).toMinutes();
        BigDecimal pricePerMinute = court.getSurfaceType().getPricePerMinute();
        BigDecimal price = pricePerMinute.multiply(BigDecimal.valueOf(duration));
        System.out.println("Price singles: " + price);
        if (dto.isDoubles()) {

            price = price.multiply(new BigDecimal("1.5"));
            System.out.println("Price doubles: " + price);

        }

        Reservation reservation = new Reservation();

        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setDoubles(dto.isDoubles());
        reservation.setPrice(price);

        reservationDao.save(reservation);
        return new ReservationDto(reservation);
    }

    public ReservationDto get(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ReservationException("Reservation not found."));
        return new ReservationDto(reservation);
    }

    public List<ReservationDto> getAll() {
        return reservationDao.findAll()
                .stream().map(ReservationDto::new)
                .toList();
    }

    public List<ReservationDto> getReservationsByCourtNumber(Integer courtNumber) {
        return reservationDao.findByCourtNumber(courtNumber)
                .stream().map(ReservationDto::new)
                .toList();
    }

    public List<ReservationDto> getReservationsByPhoneNumber(String phoneNumber, boolean onlyFuture) {
        return reservationDao.findByPhoneNumber(phoneNumber, onlyFuture)
                .stream().map(ReservationDto::new)
                .toList();
    }

    public ReservationDto update(Long id, ReservationDto dto) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ReservationException("Reservation not found."));

        boolean isFree = reservationDao.isFreeTimeSlot(dto.getCourtNumber(), dto.getStartTime(), dto.getEndTime(), id);
        if (!isFree) {
            throw new ReservationException("Reservation slot is full");
        }
        Court court = courtDao.findById(dto.getCourtNumber())
                .orElseThrow(() -> new ReservationException("Selected Court does not exist."));

        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());
        reservation.setDoubles(dto.isDoubles());
        reservation.setCourt(court);

        long duration = Duration.between(dto.getStartTime(), dto.getEndTime()).toMinutes();
        BigDecimal pricePerMinute = reservation.getCourt().getSurfaceType().getPricePerMinute();



        BigDecimal price = pricePerMinute.multiply(BigDecimal.valueOf(duration));
        if (dto.isDoubles()) {
            price = price.multiply(new BigDecimal("1.5"));
        }
        reservation.setPrice(price);

        reservationDao.save(reservation);
        return new ReservationDto(reservation);
    }

    public void delete(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ReservationException("Selected Reservation does not exist."));
        reservationDao.delete(reservation);
    }


}
