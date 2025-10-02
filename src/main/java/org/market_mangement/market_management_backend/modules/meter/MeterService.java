package org.market_mangement.market_management_backend.modules.meter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeterService {

    private final MeterRepository meterRepository;

    public List<Meter> getAllMeters() {
        return meterRepository.findAll();
    }

    public Optional<Meter> getMeterById(Long id) {
        return meterRepository.findById(id);
    }

    public List<Meter> getMetersByShopId(Long shopId) {
        return meterRepository.findByShopId(shopId);
    }

    public List<Meter> getMetersByUtilityType(UtilityType utilityType) {
        return meterRepository.findByUtilityType(utilityType);
    }

    public List<Meter> getActiveMeters() {
        return meterRepository.findByActive(true);
    }

    public Meter createMeter(Meter meter) {
        return meterRepository.save(meter);
    }

    public Meter updateMeter(Long id, Meter meter) {
        meter.setId(id);
        return meterRepository.save(meter);
    }

    public void deleteMeter(Long id) {
        meterRepository.deleteById(id);
    }
}