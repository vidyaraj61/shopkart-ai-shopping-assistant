import { useState, useRef, useEffect } from 'react';

const API = 'http://localhost:8080/api';

export default function ChatWidget() {
  const [messages, setMessages] = useState([
    { role: 'assistant', content: 'Hi! I am the ShopKart assistant. What are you shopping for today?' },
  ]);
  const [input, setInput] = useState('');
  const [sending, setSending] = useState(false);
  const endRef = useRef(null);

  // Auto-scroll to the newest message
  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  async function sendMessage(e) {
    e.preventDefault();
    const text = input.trim();
    if (!text || sending) return;

    // Optimistically show the user's message
    setMessages((prev) => [...prev, { role: 'user', content: text }]);
    setInput('');
    setSending(true);

    try {
      // For now this is a placeholder. Lesson 4 points it at the real agent.
      const reply = await mockReply(text);
      setMessages((prev) => [...prev, { role: 'assistant', content: reply }]);
    } catch (err) {
      setMessages((prev) => [
        ...prev,
        { role: 'assistant', content: 'Sorry, something went wrong. Please try again.' },
      ]);
    } finally {
      setSending(false);
    }
  }

  return (
    <div className="chat">
      <div className="messages">
        {messages.map((m, i) => (
          <div key={i} className={`bubble ${m.role}`}>
            {m.content}
          </div>
        ))}
        {sending && <div className="bubble assistant typing">Thinking...</div>}
        <div ref={endRef} />
      </div>

      <form className="composer" onSubmit={sendMessage}>
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Ask about products, stock, or place an order..."
          disabled={sending}
        />
        <button type="submit" disabled={sending || !input.trim()}>
          Send
        </button>
      </form>
    </div>
  );
}

// Temporary stub - replaced by a real fetch to /api/chat in lesson 4.
async function sendMessage(e) {
  e.preventDefault();
  const text = input.trim();
  if (!text || sending) return;

  const nextMessages = [...messages, { role: 'user', content: text }];
  setMessages(nextMessages);
  setInput('');
  setSending(true);

  try {
    const res = await fetch(`${API}/chat`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ messages: nextMessages }),
    });
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const data = await res.json();
    setMessages((prev) => [...prev, { role: 'assistant', content: data.reply }]);
  } catch (err) {
    setMessages((prev) => [
      ...prev,
      { role: 'assistant', content: 'Sorry, I could not reach the assistant. Try again.' },
    ]);
  } finally {
    setSending(false);
  }
}
function sendMessageStreaming(text) {
  // Show the user's message and an empty assistant bubble to fill in
  setMessages((prev) => [
    ...prev,
    { role: 'user', content: text },
    { role: 'assistant', content: '' },
  ]);

  const url = `${API}/chat/stream?message=${encodeURIComponent(text)}`;
  const source = new EventSource(url);

  source.onmessage = (event) => {
    // Append this chunk to the last (assistant) message
    setMessages((prev) => {
      const next = [...prev];
      const last = next[next.length - 1];
      next[next.length - 1] = { ...last, content: last.content + event.data };
      return next;
    });
  };

  source.onerror = () => {
    // SSE closes with an error event when the stream ends; tidy up.
    source.close();
    setSending(false);
  };
}
async function handleSubmit(e) {
  e.preventDefault();
  const text = input.trim();
  if (!text || sending) return;
  setInput('');
  setSending(true);
  sendMessageStreaming(text);
}
async function streamViaFetch(messages) {
  const res = await fetch(`${API}/chat/stream`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ messages }),
  });
  const reader = res.body.getReader();
  const decoder = new TextDecoder();

  while (true) {
    const { value, done } = await reader.read();
    if (done) break;
    const chunk = decoder.decode(value, { stream: true });
    // append chunk to the assistant message, same as onmessage above
    appendToAssistant(chunk);
  }
}