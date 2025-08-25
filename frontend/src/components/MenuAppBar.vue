<template>
  <v-app-bar
    app
    class="px-4"
    :color="color"
    :elevation="elevation"
    flat
    rounded="lg"
  >
    <v-app-bar-title class="font-weight-bold text-h5">
      <slot name="title">
        {{ title }}
      </slot>
    </v-app-bar-title>

    <v-spacer />

    <ThemeToggle />

    <slot name="actions">
      <v-btn
        class="ml-3 font-weight-bold"
        :color="actionColor"
        rounded="xl"
        variant="flat"
        @click="$emit('toggleDrawer')"
      >
        {{ actionText }}
      </v-btn>
    </slot>
  </v-app-bar>
</template>

<script setup>
  import ThemeToggle from '@/components/ThemeToggle.vue'
  defineEmits(['toggleDrawer'])
  defineProps({
    isDark: Boolean,
    title: {
      type: String,
      default: '🍴 Restaurante',
    },
    color: {
      type: String,
      default: 'primary',
    },
    elevation: {
      type: [String, Number],
      default: 2,
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
</script>
