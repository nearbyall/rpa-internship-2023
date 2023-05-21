package by.fin.service.impl;

import by.fin.repository.WeekendRepository;
import by.fin.service.WeekendService;
import by.fin.service.dto.WeekendDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WeekendsServiceImpl implements WeekendService {

    private final WeekendRepository weekendRepository;

    @Override
    public List<WeekendDTO> findAll() {
        return weekendRepository.findAll().stream()
                .map(weekend -> {
                    WeekendDTO dto = new WeekendDTO();
                    dto.setCalendarDate(weekend.getCalendarDate());
                    dto.setDayOff(weekend.isDayOff());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}