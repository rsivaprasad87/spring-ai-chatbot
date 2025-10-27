package com.example.springairag.service;

import com.example.springairag.model.DocumentChunk;
import com.example.springairag.openai.OpenAIClient;
import com.example.springairag.store.VectorStore;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentIngestService {
    private final OpenAIClient openAIClient;
    private final VectorStore vectorStore;
    private final String embeddingModel;

    public DocumentIngestService(OpenAIClient openAIClient, VectorStore vectorStore, String embeddingModel) {
        this.openAIClient = openAIClient;
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
    }

    public void ingestPdf(InputStream pdfStream, String documentId) throws Exception {
        try (PDDocument doc = PDDocument.load(pdfStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(doc);
            ingestText(documentId, text);
        }
    }

    public void ingestText(String documentId, String text) throws Exception {
        List<String> chunks = chunkText(text, 800, 100);
        int idx = 0;
        for (String chunk : chunks) {
            double[] emb = openAIClient.createEmbedding(embeddingModel, chunk);
            DocumentChunk dc = new DocumentChunk(documentId, idx++, chunk, emb);
            vectorStore.add(dc);
        }
    }

    private List<String> chunkText(String text, int chunkSize, int overlap) {
        List<String> out = new ArrayList<>();
        int pos = 0;
        while (pos < text.length()) {
            int end = Math.min(text.length(), pos + chunkSize);
            String chunk = text.substring(pos, end).trim();
            if (!chunk.isEmpty()) out.add(chunk);
            pos = Math.max(end - overlap, end);
        }
        return out;
    }
}
