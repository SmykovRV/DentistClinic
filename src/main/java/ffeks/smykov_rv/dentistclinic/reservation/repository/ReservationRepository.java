package ffeks.smykov_rv.dentistclinic.reservation.repository;

import ffeks.smykov_rv.dentistclinic.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findReservationByReservationDate(LocalDate reservationDate);

    Optional<Reservation> findById(long id);

    @Query("""
        SELECT r 
        FROM Reservation r 
        JOIN r.location l
        join l.doctors d
        WHERE r.reservationDate = :date 
        AND l.id = :locationId
        AND d.id = :doctorId
        AND r.isCanceled = false
        AND (r.startTime BETWEEN :startTime AND :endTime or r.endTime BETWEEN :startTime AND :endTime)
    """)
    Optional<Reservation> findReservationByReservationDateTimeDoctorAndLocation(
            @Param("date") LocalDate date,
            @Param("locationId") Long locationId,
            @Param("doctorId") Long doctorId,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Modifying
    @Query("""
    update Reservation 
    set isCanceled = true 
    where id = :reservationId
    """)
    void cancelReservationByAdmin(@Param("reservationId") long reservationId);


    Optional<Reservation> getReservationsById(long id);

    List<Reservation> getByLocationId(Long locationId);

    List<Reservation> getReservationsByLocationIdAndDoctorId(Long locationId, long doctorId);

    List<Reservation> getReservationsByDoctorId(Long doctorId);

//    Optional<Reservation> findById(long id);

    @Modifying
    @Query("""
    update Reservation 
    set isAccepted = true 
    where id = :reservationId
    """)
    void acceptReservation(Long reservationId);

    @Query("""
    SELECT r 
    FROM Reservation r
    JOIN r.userAccount ua
    where ua.phoneNumber = :username
    """)
    List<Reservation> getReservationsByUserAccount(@Param("username") String username);
//    @Query("SELECT r FROM Reservation r WHERE r.reservationDate = :date AND r.startTime BETWEEN :startTime AND :endTime ")
//    boolean isExistReservationByDateStartAndEndTime(LocalDate date, LocalTime startTime, LocalTime endTime);

    List<Reservation> findByReservationDateBetweenAndIsCanceledFalse( LocalDate today,  LocalDate until);
}
