package com.example.springairag.config;

import com.example.springairag.openai.OpenAIClient;
import com.example.springairag.service.ChatService;
import com.example.springairag.service.DocumentIngestService;
import com.example.springairag.store.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${app.openai.embeddingsModel}")
    private String embeddingsModel;

    @Value("${app.openai.chatModel}")
    private String chatModel;

    @Bean
    public OpenAIClient openAIClient() {
        String key = System.getenv("OPENAI_API_KEY");
        if (key == null) throw new IllegalStateException("OPENAI_API_KEY not set in environment");
        return new OpenAIClient(key);
    }

    @Bean
    public VectorStore vectorStore() {
        return new VectorStore();
    }

    @Bean
    public DocumentIngestService documentIngestService(OpenAIClient client, VectorStore store) {
        return new DocumentIngestService(client, store, embeddingsModel);
    }

    @Bean
    public ChatService chatService(OpenAIClient client, VectorStore store) {
        return new ChatService(client, store, embeddingsModel, chatModel);
    }
}
