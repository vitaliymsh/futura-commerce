import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5175,
    open: false,
    strictPort: false
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  }
})
