package org.openjfx.service;

import org.openjfx.entity.ExportNote;

public interface ExportNoteService {
    ExportNote getExportNoteByInventoryID(int inventoryID);
    int create(ExportNote exportNote);
}
