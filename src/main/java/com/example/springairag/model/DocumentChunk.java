package com.example.springairag.model;

import java.util.UUID;

public class DocumentChunk {
    private final String id = UUID.randomUUID().toString();
    private final String documentId;
    private final int chunkIndex;
    private final String text;
    private final double[] embedding;

    public DocumentChunk(String documentId, int chunkIndex, String text, double[] embedding) {
        this.documentId = documentId;
        this.chunkIndex = chunkIndex;
        this.text = text;
        this.embedding = embedding;
    }

    public String getId() { return id; }
    public String getDocumentId() { return documentId; }
    public int getChunkIndex() { return chunkIndex; }
    public String getText() { return text; }
    public double[] getEmbedding() { return embedding; }
}
