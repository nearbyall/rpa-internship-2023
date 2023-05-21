package by.fin.service;

import by.fin.repository.entity.Weekend;
import by.fin.service.dto.WeekendDTO;

import java.util.List;

public interface WeekendService {

    List<WeekendDTO> findAll();

}