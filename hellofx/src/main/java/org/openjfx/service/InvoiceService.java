package org.openjfx.service;

import org.openjfx.entity.Invoice;

public interface InvoiceService {
    Invoice getInvoiceByInvoiceID(int invoiceID);
    int create();
    void save(Invoice invoice);
    void delete(Invoice invoice);
}
