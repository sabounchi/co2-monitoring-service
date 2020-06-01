package ir.softernet.co2.monitoring.repository;

import ir.softernet.co2.monitoring.model.CO2Level;
import ir.softernet.co2.monitoring.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Jpa based repository for domain: {@link ir.softernet.co2.monitoring.model.Status}
 *
 * @author saman
 */
public interface StatusRepository extends JpaRepository<Status, Integer> {

    Optional<Status> findFirstByUuidOrderByIdDesc(String uuid);

    Stream<Status> findAllByUuidAndLevelOrderByIdDesc(String uuid, CO2Level level);

}
