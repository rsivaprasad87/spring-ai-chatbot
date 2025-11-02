package com.example.springairag.api;

import com.example.springairag.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    public ChatController(ChatService chatService) { this.chatService = chatService; }

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody ChatRequest req) throws Exception {
        String answer = chatService.answerQuestion(req.getMessage());
        return ResponseEntity.ok(new ChatResponse(answer));
    }

    public static class ChatRequest {
        private String message;
        public String getMessage(){return message;}
        public void setMessage(String message){this.message = message;}
    }

    public static class ChatResponse {
        private final String answer;
        public ChatResponse(String answer){this.answer = answer;}
        public String getAnswer(){return answer;}
    }
}
