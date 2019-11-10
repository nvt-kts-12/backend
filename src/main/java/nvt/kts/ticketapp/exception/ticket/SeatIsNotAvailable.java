package nvt.kts.ticketapp.exception.ticket;

import nvt.kts.ticketapp.domain.dto.event.SeatDTO;

public class SeatIsNotAvailable extends Exception {
    public SeatIsNotAvailable(SeatDTO seatDTO) {
        super(String.format("Seat with row %s and column %s for sector %s isn't available", seatDTO.getRow(), seatDTO.getCol(), seatDTO.getSectorId()));
    }
}
