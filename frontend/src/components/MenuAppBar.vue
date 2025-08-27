<template>
  <v-app-bar
    app
    class="px-6 py-2 d-flex align-center"
    elevation="0"
    flat
    style="background: transparent; position: fixed; top: 0; left: 0; right: 0; z-index: 10;"
  >
    <div class="d-flex align-center pa-2 px-4 glass-card">
      <v-avatar class="mr-2" size="40">
        <v-img alt="Logo Restaurante" src="/logo.png" />
      </v-avatar>
      <span class="font-weight-bold text-h6">
        {{ title }}
      </span>
    </div>

    <v-spacer />

    <div class="d-flex align-center pa-2 px-4 glass-card">
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
    actionText: {
      type: String,
      default: 'Meus pedidos',
    },
    actionColor: {
      type: String,
      default: 'secondary',
    },
  })

  defineEmits(['toggle-drawer'])
</script>

<style scoped>
.glass-card {
  background-color: rgb(var(--v-theme-primary));
  border-radius: 16px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

</style>
