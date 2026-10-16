package com.shopkart.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

record ChatRequest(String message) {}
record ChatReply(String reply) {}

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping
    public ChatReply chat(@RequestBody ChatRequest request) {
        String answer = chatClient.prompt()
                .user(request.message())
                .call()
                .content();
        return new ChatReply(answer);
    }
}