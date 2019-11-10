package nvt.kts.ticketapp.domain.dto.report;


import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.location.PlainLocationDTO;
import nvt.kts.ticketapp.domain.model.event.EventCategory;

import java.util.Map;

@Data
@NoArgsConstructor
public class LocationReportDTO {

    private PlainLocationDTO locationDTO;
    private double totalIncome;
    private Map<EventCategory, Double> incomeByCategory;
    private Map<EventCategory, Integer> numOfEventDaysByCategory;

    public LocationReportDTO(PlainLocationDTO locationDTO, double totalIncome, Map<EventCategory,
                                Double> incomeByCategory, Map<EventCategory, Integer> numOfEventDaysByCategory) {
        this.locationDTO = locationDTO;
        this.totalIncome = totalIncome;
        this.incomeByCategory = incomeByCategory;
        this.numOfEventDaysByCategory = numOfEventDaysByCategory;
    }
}
