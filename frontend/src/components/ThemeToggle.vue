<template>
  <v-btn class="border-md rounded-xl" density="comfortable" icon @click="toggleTheme">
    <v-icon size="26">
      {{ isDark ? 'mdi-weather-night' : 'mdi-weather-sunny' }}
    </v-icon>
  </v-btn>
</template>

<script setup>
  import { computed } from 'vue'
  import { useTheme } from 'vuetify'

  const theme = useTheme()

  const isDark = computed(() => theme.global.current.value.dark)

  const toggleTheme = () => {
    theme.global.name.value = isDark.value ? 'light' : 'dark'
  }
</script>
