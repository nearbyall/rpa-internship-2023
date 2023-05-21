package by.fin.web.controller;

import by.fin.service.WeekendService;
import by.fin.service.dto.WeekendDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest controller for managing weekends.
 *
 * <p>This controller provides the entry points for the API operations related to weekends. The API operation
 * includes fetching all weekend records from the database.</p>
 *
 * <p>It interacts with the {@link WeekendService} to perform the business logic operations.</p>
 *
 * <h2>API Endpoints:</h2>
 * <ul>
 *     <li>GET /api/weekends: Fetches all weekend records from the database.</li>
 * </ul>
 *
 * @see by.fin.service.WeekendService
 */
@RequestMapping("api/weekends")
@RestController
@RequiredArgsConstructor
public class WeekendController {

    private final WeekendService weekendService;

    /**
     * GET endpoint for retrieving all weekends data.
     *
     * @return a response entity with the HTTP status and a list of all weekends.
     */
    @GetMapping
    public ResponseEntity<List<WeekendDTO>>findAll(){
        return new ResponseEntity<>(weekendService.findAll(), HttpStatus.OK);
    }

}