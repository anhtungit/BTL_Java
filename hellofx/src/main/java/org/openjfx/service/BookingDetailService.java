package org.openjfx.service;

import org.openjfx.entity.BookingDetail;
import org.openjfx.entity.Table;

public interface BookingDetailService {
    BookingDetail getBookingDetailNewlestByTableID(int tableID);
    void changeTableInBookingDetail(Table sourceTable, Table destTable);
}
