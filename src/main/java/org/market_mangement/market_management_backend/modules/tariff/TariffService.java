package org.market_mangement.market_management_backend.modules.tariff;

import lombok.RequiredArgsConstructor;
import org.market_mangement.market_management_backend.modules.meter.UtilityType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TariffService {


    private final TariffRepository tariffRepository;

    public List<Tariff> getAllTariffs() {
        return tariffRepository.findAll();
    }

    public Optional<Tariff> getTariffById(Long id) {
        return tariffRepository.findById(id);
    }

    public List<Tariff> getTariffsByUtilityType(UtilityType utilityType) {
        return tariffRepository.findByUtilityType(utilityType);
    }

    public List<Tariff> getTariffsByUtilityTypeOrderedByDate(UtilityType utilityType) {
        return tariffRepository.findByUtilityTypeOrderByEffectiveFromDesc(utilityType);
    }

    public Optional<Tariff> getTariffByUtilityTypeAndDate(UtilityType utilityType, LocalDate effectiveFrom) {
        return tariffRepository.findByUtilityTypeAndEffectiveFrom(utilityType, effectiveFrom);
    }

    public Tariff createTariff(Tariff tariff) {
        return tariffRepository.save(tariff);
    }

    public Tariff updateTariff(Long id, Tariff tariff) {
        tariff.setId(id);
        return tariffRepository.save(tariff);
    }

    public void deleteTariff(Long id) {
        tariffRepository.deleteById(id);
    }
}