package org.openjfx.service;

import org.openjfx.entity.Invoice;

public interface InvoiceService {
    Invoice getInvoiceByInvoiceID(int invoiceID);
}
