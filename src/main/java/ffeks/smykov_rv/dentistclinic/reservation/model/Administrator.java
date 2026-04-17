package ffeks.smykov_rv.dentistclinic.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ffeks.smykov_rv.dentistclinic.security.model.UserAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "reservations", name = "administrator")
public class Administrator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @ManyToOne()
    @JoinColumn(name = "location_id")
    @JsonIgnore
    private Location location;

}
