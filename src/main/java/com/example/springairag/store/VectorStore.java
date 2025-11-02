package com.example.springairag.store;

import com.example.springairag.model.DocumentChunk;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class VectorStore {
    private final Map<String, DocumentChunk> chunks = new ConcurrentHashMap<>();

    public void add(DocumentChunk chunk) {
        chunks.put(chunk.getId(), chunk);
    }

    public List<DocumentChunk> search(double[] queryEmbedding, int k) {
        return chunks.values().stream()
            .map(c -> new AbstractMap.SimpleEntry<>(c, cosineSimilarity(queryEmbedding, c.getEmbedding())))
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .limit(k)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private double cosineSimilarity(double[] a, double[] b) {
        if (a == null || b == null || a.length != b.length) return 0.0;
        double dot = 0.0, na = 0.0, nb = 0.0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }
        return dot / (Math.sqrt(na) * Math.sqrt(nb) + 1e-10);
    }

    public long size() { return chunks.size(); }
}
