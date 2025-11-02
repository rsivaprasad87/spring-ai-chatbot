package com.example.springairag.service;

import com.example.springairag.model.DocumentChunk;
import com.example.springairag.openai.OpenAIClient;
import com.example.springairag.store.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final OpenAIClient openAI;
    private final VectorStore store;
    private final String embeddingModel;
    private final String chatModel;

    public ChatService(OpenAIClient openAI, VectorStore store, String embeddingModel, String chatModel) {
        this.openAI = openAI;
        this.store = store;
        this.embeddingModel = embeddingModel;
        this.chatModel = chatModel;
    }

    public String answerQuestion(String question) throws Exception {
        double[] qEmb = openAI.createEmbedding(embeddingModel, question);
        List<DocumentChunk> top = store.search(qEmb, 5);

        if (top.isEmpty()) {
            return "I don't know based on the provided documents.";
        }

        String ctx = top.stream()
                .map(c -> String.format("[doc:%s chunk:%d]\n%s", c.getDocumentId(), c.getChunkIndex(), c.getText()))
                .collect(Collectors.joining("\n\n"));

        String systemPrompt = "You are an assistant who must answer questions using only the provided CONTEXT. " +
                "If the answer cannot be found in the context, respond EXACTLY: \"I don't know based on the provided documents.\" " +
                "Do not hallucinate or use outside knowledge. When you answer, add a short 'SOURCES:' line that lists the chunk tags used.";

        String userPrompt = "CONTEXT:\n" + ctx + "\n\nQUESTION:\n" + question +
                "\n\nAnswer concisely and only from the CONTEXT. If missing, respond with the exact phrase: \"I don't know based on the provided documents.\"";

        String result = openAI.chatCompletion(chatModel, systemPrompt, userPrompt);
        return result;
    }
}
