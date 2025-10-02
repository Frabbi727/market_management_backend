package org.market_mangement.market_management_backend.modules.periodinput;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PeriodInputService {

    private final PeriodInputRepository periodInputRepository;

    public List<PeriodInput> getAllPeriodInputs() {
        return periodInputRepository.findAll();
    }

    public List<PeriodInput> getAllPeriodInputsOrderedByPeriod() {
        return periodInputRepository.findAllByOrderByPeriodDesc();
    }

    public Optional<PeriodInput> getPeriodInputByPeriod(String period) {
        return periodInputRepository.findById(period);
    }

    public PeriodInput createPeriodInput(PeriodInput periodInput) {
        return periodInputRepository.save(periodInput);
    }

    public PeriodInput updatePeriodInput(String period, PeriodInput periodInput) {
        periodInput.setPeriod(LocalDate.parse(period));
        return periodInputRepository.save(periodInput);
    }

    public void deletePeriodInput(String period) {
        periodInputRepository.deleteById(period);
    }
}