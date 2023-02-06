package pharmacyinformationsystem.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pharmacyinformationsystem.model.TimeOffRequest;
import pharmacyinformationsystem.service.TimeOffRequestService;

@RestController
@CrossOrigin
@RequestMapping("/api/time-off-requests")
public class TimeOffRequestController {

    private final TimeOffRequestService timeOffRequestService;

    @Autowired
    public TimeOffRequestController(TimeOffRequestService timeOffRequestService) {
        this.timeOffRequestService = timeOffRequestService;
    }

    @PreAuthorize("hasAnyRole('PHARMACIST', 'DERMATOLOGIST')")
    @PostMapping(value = "/{medicalWorkerId}/{role}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeOffRequest> createTimeOffRequest(@RequestBody TimeOffRequest request, @PathVariable("medicalWorkerId") Integer id, @PathVariable("role") String role) {
        return new ResponseEntity<>(timeOffRequestService.createTimeOffRequest(request, id, role), HttpStatus.OK);
    }
}
