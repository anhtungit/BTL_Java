package org.openjfx.service;

import org.openjfx.entity.Invoice;
import org.openjfx.entity.InvoiceDetail;

import java.util.List;

public interface InvoiceDetailService {
    List<InvoiceDetail> getInvoiceDetailByInvoiceID(int invoiceID);
    void deleteAll(Invoice invoice);
}
