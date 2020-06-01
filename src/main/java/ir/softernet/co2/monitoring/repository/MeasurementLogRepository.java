package ir.softernet.co2.monitoring.repository;

import ir.softernet.co2.monitoring.model.MeasurementLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Date;

/**
 * Jpa based repository for domain: {@link ir.softernet.co2.monitoring.model.MeasurementLog}
 *
 * @author saman
 */
public interface MeasurementLogRepository extends JpaRepository<MeasurementLog, Integer> {

    Collection<MeasurementLog> findFirst2ByUuidAndMomentBeforeOrderByMomentDesc(String uuid, Date upMoment);

    @Query("SELECT MAX(log.co2) FROM MeasurementLog log WHERE log.uuid = ?1 and log.moment > ?2")
    int maxCO2FromSpecificDateTillNow(String uuid, Date fromDate);

    @Query("SELECT AVG(log.co2) FROM MeasurementLog log WHERE log.uuid = ?1 and log.moment > ?2")
    int avgCO2FromSpecificDateTillNow(String uuid, Date fromDate);

}
