package com.shopkart.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.*;

@Configuration
public class ChatConfig {

    // This app has TWO ToolCallbackProvider beans - our own MCP-server provider
    // (shopKartToolProvider, a MethodToolCallbackProvider) and the MCP client's
    // auto-configured one. Injecting the generic ToolCallbackProvider type would
    // be ambiguous (NoUniqueBeanDefinitionException), so we inject the client's
    // concrete type, SyncMcpToolCallbackProvider, which resolves it cleanly.
    @Bean
    public ChatClient chatClient(ChatModel chatModel,
                                 com.shopkart.agent.ShopKartTools localTools,
                                 SyncMcpToolCallbackProvider mcpTools) {  // remote tools from MCP client
        return ChatClient.builder(chatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultTools(localTools)          // your @Tool methods
                .defaultToolCallbacks(mcpTools)    // tools from the MCP shipping server
                .build();
    }

    private static final String SYSTEM_PROMPT = "You are the ShopKart shopping assistant...";
}