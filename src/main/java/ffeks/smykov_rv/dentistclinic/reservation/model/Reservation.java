package ffeks.smykov_rv.dentistclinic.reservation.model;

import ffeks.smykov_rv.dentistclinic.security.model.UserAccount;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "reservations", name = "reservation")
@Setter
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "reservation_description",  nullable = false)
    private String reservationDescription;

    @Column(name = "reservation_date",  nullable = false)
    private LocalDate reservationDate;

    @Column(name = "start_time",  nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time",  nullable = false)
    private LocalTime endTime;

    @ManyToOne()
    @JoinColumn(name = "location_id",  nullable = false)
    private Location location;

    @ManyToOne()
    @JoinColumn(name = "doctor_id",  nullable = false)
    private Doctor doctor;

    @ManyToOne()
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @Column(name = "is_accepted",  nullable = false)
    private boolean isAccepted;

    @Column(name = "is_canceled",  nullable = false)
    private boolean isCanceled = false;
}
