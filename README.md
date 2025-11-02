# Spring AI RAG - Minimal Example

This is a minimal Spring Boot project that demonstrates a Retrieval-Augmented Generation (RAG) flow:

- Ingest documents (text or PDF)
- Create embeddings using OpenAI embeddings API
- Store embeddings in a simple in-memory vector store
- Answer user chat questions by retrieving relevant chunks and calling the OpenAI Chat API with strict system prompts so the model only answers from provided documents.

## Quick start

1. Install Java 17 and Maven.
2. Set environment variable `OPENAI_API_KEY` to your OpenAI API key.
3. Build and run:

```bash
mvn clean package
java -jar target/spring-ai-rag-0.0.1-SNAPSHOT.jar
```

4. Ingest a text document:

```bash
curl -X POST "http://localhost:8080/ingest/text?docId=mydoc" -H "Content-Type: text/plain" --data-binary @mydoc.txt
```

5. Ask a question:

```bash
curl -X POST "http://localhost:8080/chat" -H "Content-Type: application/json" -d '{"message":"What does mydoc say about refunds?"}'
```

## Notes

- Open AI key is not free . Register a key https://platform.openai.com/ and add credits to the account . 
- Accessing the API without credits in the account will end with rate limit exceeded error (http response code 429) 


