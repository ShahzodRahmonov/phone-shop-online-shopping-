package phone.shop.entity;

import lombok.Getter;
import lombok.Setter;
import phone.shop.types.ProfileRole;
import phone.shop.types.ProfileStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "email", unique = true, nullable = false)  // = login
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "contact")
    private String contact;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProfileStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ProfileRole role;
    @Column(name = "created_date")
    private LocalDateTime createdDate;

}
