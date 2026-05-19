<template>
  <div class="user-app-container">
    <Navbar />
    <main class="user-main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import Navbar from '@/components/Navbar.vue'
</script>

<style scoped>
.user-app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.user-main-content {
  flex: 1;
}
</style>
