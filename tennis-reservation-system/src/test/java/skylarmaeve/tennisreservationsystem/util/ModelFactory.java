package skylarmaeve.tennisreservationsystem.util;


import skylarmaeve.tennisreservationsystem.model.Court;
import skylarmaeve.tennisreservationsystem.model.Customer;
import skylarmaeve.tennisreservationsystem.model.Reservation;
import skylarmaeve.tennisreservationsystem.model.SurfaceType;
import skylarmaeve.tennisreservationsystem.model.user.AppUser;
import skylarmaeve.tennisreservationsystem.model.user.Role;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ModelFactory {



    public static AppUser makeAppUser(String username, Role role) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole(role);
        return user;
    }

    public static Court makeCourt(SurfaceType surfaceType, Integer courtNumber) {
        Court court = new Court();
        court.setSurfaceType(surfaceType);
        court.setCourtNumber(courtNumber);
        return court;
    }

    public static Customer makeCustomer(String phoneNumber) {
        Customer customer = new Customer();
        customer.setName("customer");
        customer.setPhoneNumber(phoneNumber);
        return customer;
    }

    public static Reservation makeReservation(Court court, Customer customer) {
        Reservation reservation = new Reservation();
        reservation.setCourt(court);
        reservation.setCustomer(customer);
        reservation.setDoubles(false);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setEndTime(LocalDateTime.now().plusHours(1));
        reservation.setPrice(BigDecimal.valueOf(300));
        return reservation;
    }

    public static SurfaceType makeSurfaceType(String name) {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setName(name);
        surfaceType.setPricePerMinute(BigDecimal.valueOf(1));
        return surfaceType;
    }
}
