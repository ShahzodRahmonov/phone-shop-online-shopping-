package phone.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "supplier")
public class SupplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private ProfileEntity profile;

    private String carModel;
    private String driverLicenceNumber;
    @Column(name = "visible", columnDefinition = "boolean default true")
    public Boolean visible;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

}
