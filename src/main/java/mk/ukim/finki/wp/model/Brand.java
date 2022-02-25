package mk.ukim.finki.wp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "brands")
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 2500)
    private String logo;

    public Brand(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }
}
