package ir.softernet.co2.monitoring.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "measurement_log")
public class MeasurementLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "co2")
    private Integer co2;

    @Column(name = "moment")
    private Date moment;

    @ManyToMany(mappedBy = "measurementLogs",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.MERGE})
    private Set<Status> statuses = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MeasurementLog log = (MeasurementLog) o;
        return Objects.equals(id, log.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id);
    }
}
