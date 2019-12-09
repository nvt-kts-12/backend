package nvt.kts.ticketapp.domain.dto.report;


import lombok.Data;
import lombok.NoArgsConstructor;
import nvt.kts.ticketapp.domain.dto.location.PlainLocationDTO;
import nvt.kts.ticketapp.domain.model.event.EventCategory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;

@Data
@NoArgsConstructor
public class LocationReportDTO {

    @NotNull
    private PlainLocationDTO locationDTO;
    @NotNull
    @PositiveOrZero
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
