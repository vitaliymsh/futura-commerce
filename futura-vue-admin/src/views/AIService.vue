<template>
  <div class="ai-copilot-view">
    <div class="view-header fintech-card">
      <div class="header-left">
        <div class="header-title-row">
          <h2>Futura Copilot</h2>
          <span class="ai-badge fintech-pill blue">Futura Copilot</span>
        </div>
        <span class="subtext">Live order telemetry, rag search & diagnostic console</span>
      </div>
      <div class="header-actions">
        <button class="action-btn" @click="clearSession">
          <el-icon><Delete /></el-icon> New Session
        </button>
      </div>
    </div>

    <!-- Chat Container -->
    <div class="chat-terminal fintech-card">
      <div class="messages-viewport" ref="viewportRef">
        <div v-for="(msg, idx) in messages" :key="idx" :class="['chat-bubble-row', msg.sender]">
          <div class="avatar-badge">{{ msg.sender === 'ai' ? 'FC' : 'OP' }}</div>
          <div class="bubble-content">
            <div class="bubble-header">
              <span class="sender-name">{{ msg.sender === 'ai' ? 'Futura Copilot' : 'Operator' }}</span>
              <span class="timestamp metric-num">{{ msg.time }}</span>
            </div>
            <div class="bubble-body">
              {{ msg.text }}
            </div>
          </div>
        </div>

        <div v-if="thinking" class="chat-bubble-row ai">
          <div class="avatar-badge">FC</div>
          <div class="bubble-content">
            <div class="bubble-header">
              <span class="sender-name">Futura Copilot</span>
            </div>
            <div class="bubble-body thinking">
              <span class="dot"></span>
              <span class="dot"></span>
              <span class="dot"></span>
            </div>
          </div>
        </div>
      </div>

      <!-- Prompt Input Bar -->
      <div class="prompt-input-bar">
        <input
          v-model="inputPrompt"
          placeholder="Ask Futura Copilot: 'Lookup order #ORDER10029', 'Summarize delivery policies', 'Check stock of SKU 402'..."
          class="fintech-input prompt"
          @keyup.enter="handleSend"
          :disabled="thinking"
        />
        <button class="send-btn" @click="handleSend" :disabled="thinking || !inputPrompt.trim()">
          <el-icon><Promotion /></el-icon> Send
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { aiAPI } from '@/api'
import { Delete, Promotion } from '@element-plus/icons-vue'

const viewportRef = ref(null)
const inputPrompt = ref('')
const thinking = ref(false)
const memoryId = ref('admin_copilot_session_' + Date.now())

const messages = ref([
  {
    sender: 'ai',
    text: 'Greetings, Operator. Connected to Futura order lifecycle and product catalog. How can I assist?',
    time: '09:00'
  }
])

const handleSend = async () => {
  const q = inputPrompt.value.trim()
  if (!q || thinking.value) return

  const nowStr = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  messages.value.push({ sender: 'user', text: q, time: nowStr })
  inputPrompt.value = ''
  thinking.value = true
  scrollToBottom()

  try {
    const res = await aiAPI.chat(q, memoryId.value)
    const replyText = typeof res === 'string' ? res : (res?.data || res?.message || 'Acknowledged.')
    messages.value.push({ sender: 'ai', text: replyText, time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) })
  } catch (err) {
    messages.value.push({
      sender: 'ai',
      text: 'Telemetry query failed or copilot service unreachable.',
      time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    })
  } finally {
    thinking.value = false
    scrollToBottom()
  }
}

const clearSession = () => {
  memoryId.value = 'admin_copilot_session_' + Date.now()
  messages.value = [
    {
      sender: 'ai',
      text: 'Session reset. What would you like to inspect?',
      time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    }
  ]
}

const scrollToBottom = () => {
  nextTick(() => {
    if (viewportRef.value) {
      viewportRef.value.scrollTop = viewportRef.value.scrollHeight
    }
  })
}

onMounted(() => {
  scrollToBottom()
})
</script>

<style scoped>
.ai-copilot-view {
  display: flex;
  flex-direction: column;
  gap: 20px;
  height: calc(100vh - 128px);
}

.view-header {
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-left h2 {
  font-size: 1.25rem;
  font-weight: 700;
}

.subtext {
  font-size: 0.8rem;
  color: var(--text-muted);
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: var(--radius-pill);
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  color: #fff;
  font-size: 0.85rem;
  cursor: pointer;
}

.chat-terminal {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.messages-viewport {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.chat-bubble-row {
  display: flex;
  gap: 14px;
  max-width: 80%;
}

.chat-bubble-row.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.avatar-badge {
  width: 34px;
  height: 34px;
  border-radius: var(--radius-pill);
  background: var(--bg-card);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 0.75rem;
  color: var(--accent-cyan);
  border: 1px solid var(--border-subtle);
  flex-shrink: 0;
}

.chat-bubble-row.user .avatar-badge {
  color: #fff;
  background: var(--accent-blue);
}

.bubble-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.bubble-header {
  display: flex;
  align-items: center;
  gap: 8px;
}

.sender-name {
  font-size: 0.75rem;
  font-weight: 600;
  color: var(--text-secondary);
}

.timestamp {
  font-size: 0.7rem;
  color: var(--text-muted);
}

.bubble-body {
  background: var(--bg-secondary);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-lg);
  padding: 12px 16px;
  font-size: 0.9rem;
  line-height: 1.5;
  white-space: pre-wrap;
}

.chat-bubble-row.user .bubble-body {
  background: rgba(0, 117, 255, 0.15);
  border-color: rgba(0, 117, 255, 0.3);
}

.prompt-input-bar {
  padding: 16px 24px;
  border-top: 1px solid var(--border-subtle);
  display: flex;
  gap: 12px;
  background: var(--bg-primary);
}

.fintech-input.prompt {
  flex: 1;
  background: var(--bg-input);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-pill);
  padding: 12px 20px;
  color: #fff;
  font-size: 0.9rem;
  outline: none;
}

.fintech-input.prompt:focus {
  border-color: var(--accent-blue);
}

.send-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  background: linear-gradient(135deg, var(--accent-blue) 0%, #0056cc 100%);
  border: none;
  border-radius: var(--radius-pill);
  padding: 0 20px;
  color: #fff;
  font-weight: 600;
  cursor: pointer;
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.thinking .dot {
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--accent-cyan);
  margin-right: 4px;
  animation: pulse 1.2s infinite ease-in-out;
}

.thinking .dot:nth-child(2) { animation-delay: 0.2s; }
.thinking .dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes pulse {
  0%, 80%, 100% { transform: scale(0); opacity: 0.3; }
  40% { transform: scale(1); opacity: 1; }
}
</style>
