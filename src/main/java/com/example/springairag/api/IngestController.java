package com.example.springairag.api;

import com.example.springairag.service.DocumentIngestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ingest")
public class IngestController {
    private final DocumentIngestService ingestService;
    public IngestController(DocumentIngestService ingestService) { this.ingestService = ingestService; }

    @PostMapping("/pdf")
    public ResponseEntity<?> ingestPdf(@RequestParam("file") MultipartFile file) throws Exception {
        String docId = file.getOriginalFilename();
        ingestService.ingestPdf(file.getInputStream(), docId);
        return ResponseEntity.ok("Ingested: " + docId);
    }

    @PostMapping("/text")
    public ResponseEntity<?> ingestText(@RequestParam("docId") String docId, @RequestBody String text) throws Exception {
        ingestService.ingestText(docId, text);
        return ResponseEntity.ok("Ingested text: " + docId);
    }
}
