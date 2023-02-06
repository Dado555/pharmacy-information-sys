package pharmacyinformationsystem.service;


import pharmacyinformationsystem.model.TimeOffRequest;

public interface TimeOffRequestService extends GenericService<TimeOffRequest> {
    TimeOffRequest createTimeOffRequest(TimeOffRequest request, Integer medicalWorkerId, String role);

    void approveTimeOff(Integer requestId, Boolean status);

    void approveTimeOffRequestEmail(TimeOffRequest timeOffRequest, Boolean status);
}