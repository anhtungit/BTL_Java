package org.openjfx.service;

import org.openjfx.entity.ImportNote;

public interface ImportNoteService {
    ImportNote getImportNoteByInventoryID(int inventoryID);
}
