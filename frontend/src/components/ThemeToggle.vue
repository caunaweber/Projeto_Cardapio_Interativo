<script setup>
  import { computed } from 'vue'
  import { useTheme } from 'vuetify'

  const theme = useTheme()
  const isDark = computed({
    get () {
      return theme.global.current.value.dark
    },
    set (value) {
      const themeName = value ? 'dark' : 'light'
      theme.change(themeName)
      localStorage.setItem('theme', themeName)
    },
  })
</script>

<template>
  <v-btn class="border-md rounded-xl" density="comfortable" icon @click="isDark = !isDark">
    <v-icon size="26">
      {{ isDark ? 'mdi-weather-night' : 'mdi-weather-sunny' }}
    </v-icon>
  </v-btn>
</template>
