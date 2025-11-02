package org.openjfx.service;

import org.openjfx.entity.BookingDetail;

public interface BookingDetailService {
    BookingDetail getBookingDetailByTableID(int tableID);
}
