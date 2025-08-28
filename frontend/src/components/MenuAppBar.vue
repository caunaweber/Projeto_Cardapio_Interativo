<template>
  <v-app-bar
    app
    class="glass-appbar d-flex align-center px-4"
    flat
    height="56"
  >
    <div class="d-flex align-center">
      <v-avatar class="mr-2" size="40">
        <v-img alt="Logo Restaurante" src="/logo.png" />
      </v-avatar>
      <span class="font-weight-bold text-h6">
        {{ title }}
      </span>
    </div>

    <v-spacer />

    <div class="d-flex align-center">
      <ThemeToggle class="mr-3" />
      <slot name="actions" :toggle-drawer="$emit.bind(null, 'toggle-drawer')" />
    </div>
  </v-app-bar>
</template>

<script setup>
  import ThemeToggle from '@/components/ThemeToggle.vue'

  defineProps({
    title: {
      type: String,
      default: '🍴 Restaurante',
    },
  })

  defineEmits(['toggle-drawer'])
</script>

<style scoped>
.glass-appbar {
  background: rgba(var(--v-theme-surface), 0.7);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(var(--v-theme-primary));
}
.v-avatar {
  border: 2px solid rgba(var(--v-theme-primary), 0.8);
}
</style>
