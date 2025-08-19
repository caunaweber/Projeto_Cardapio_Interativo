<template>
    <v-app-bar app class="px-4" color="primary" :elevation="2" flat rounded="lg">
        <v-app-bar-title class="font-weight-bold text-h5">🍴 Restaurante</v-app-bar-title>
        <v-spacer />
        <v-btn icon variant="text" @click="$emit('toggleTheme')">
            <v-icon size="26">{{ isDark ? 'mdi-weather-sunny' : 'mdi-weather-night' }}</v-icon>
        </v-btn>
        <v-btn class="ml-3 font-weight-bold" color="secondary" rounded="xl" variant="flat"
            @click="$emit('toggleDrawer')">
            Meus pedidos
        </v-btn>
    </v-app-bar>
</template>

<script setup>
defineProps({
    isDark: Boolean
})
</script>