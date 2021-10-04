package phone.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "image")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "path")
    private String path;
    @Column(name = "type")
    private String type;
    @Column(name = "size")
    private Long size;
    @Column(name = "token")
    private String token;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
//    @Lob
//    byte[] content;

}
