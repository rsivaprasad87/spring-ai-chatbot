package com.example.springairag.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class OpenAIClient {
    private static final String OPENAI_BASE = "https://api.openai.com/v1";
    private final OkHttpClient http = new OkHttpClient();
    private final String apiKey;
    private final ObjectMapper mapper = new ObjectMapper();

    public OpenAIClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public double[] createEmbedding(String model, String input) throws IOException {
        String url = OPENAI_BASE + "/embeddings";
        String json = mapper.createObjectNode()
            .put("model", model)
            .put("input", input)
            .toString();

        Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(json, MediaType.get("application/json")))
            .header("Authorization", "Bearer " + apiKey)
            .build();

        try (Response resp = http.newCall(request).execute()) {
            if (!resp.isSuccessful()) throw new IOException("Embedding request failed: " + resp);
            JsonNode root = mapper.readTree(resp.body().string());
            JsonNode emb = root.get("data").get(0).get("embedding");
            double[] arr = new double[emb.size()];
            for (int i = 0; i < emb.size(); i++) arr[i] = emb.get(i).doubleValue();
            return arr;
        }
    }

    public String chatCompletion(String model, String systemPrompt, String userPrompt) throws IOException {
        String url = OPENAI_BASE + "/chat/completions";
        ObjectMapper m = mapper;
        JsonNode messages = m.createArrayNode()
            .add(m.createObjectNode().put("role", "system").put("content", systemPrompt))
            .add(m.createObjectNode().put("role", "user").put("content", userPrompt));

        String bodyJson = m.createObjectNode()
            .put("model", model)
            .set("messages", messages)
            .toString();

        Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(bodyJson, MediaType.get("application/json")))
            .header("Authorization", "Bearer " + apiKey)
            .build();

        try (Response resp = http.newCall(request).execute()) {
            if (!resp.isSuccessful()) throw new IOException("Chat request failed: " + resp);
            JsonNode root = mapper.readTree(resp.body().string());
            return root.get("choices").get(0).get("message").get("content").asText();
        }
    }
}
