package org.market_mangement.market_management_backend.modules.reading;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReadingService {

    private final ReadingRepository readingRepository;

    public List<Reading> findAll() {
        return readingRepository.findAll();
    }

    public Reading findById(Long id) {
        return readingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reading not found with id=" + id));
    }

    public List<Reading> findByMeterId(Long meterId) {
        return readingRepository.findByMeterId(meterId);
    }

    public List<Reading> findByPeriod(String period) {
        return readingRepository.findByPeriod(period);
    }

    public Reading findByMeterIdAndPeriod(Long meterId, String period) {
        return readingRepository.findByMeterIdAndPeriod(meterId, period)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Reading not found for meter=" + meterId + " and period=" + period));
    }

    @Transactional
    public Reading create(Reading reading) {
        return readingRepository.save(reading);
    }

    @Transactional
    public Reading update(Long id, Reading updatedReading) {
        Reading existing = findById(id);
        existing.setMeterId(updatedReading.getMeterId());
        existing.setPeriod(updatedReading.getPeriod());
        existing.setPrevReading(updatedReading.getPrevReading());
        existing.setCurrReading(updatedReading.getCurrReading());
        existing.setMultiplier(updatedReading.getMultiplier());
        existing.setConsumption(updatedReading.getConsumption());
        existing.setReadAt(updatedReading.getReadAt());
        return readingRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Reading existing = findById(id);
        readingRepository.delete(existing);
    }
}