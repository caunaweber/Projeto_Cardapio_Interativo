<template>
  <v-card class="pa-4 rounded-xl elevation-2 card-border" min-height="180px">
    <v-card-title class="font-weight-bold text-h6 d-flex align-center justify-space-between">
      <span>{{ title }}</span>
      <v-icon :color="iconColor">{{ icon }}</v-icon>
    </v-card-title>

    <v-subheader class="mb-2">Total: <strong>{{ total }}</strong> vendas</v-subheader>

    <v-divider class="my-2" />

    <v-list density="compact">
      <v-list-item v-for="(item, index) in items" :key="index">
        <template #prepend>
          <v-avatar class="mr-2" color="primary" size="28">
            <span class="text-caption font-weight-bold white--text">{{ index + 1 }}</span>
          </v-avatar>
        </template>

        <v-list-item-title>{{ item.nome }}</v-list-item-title>
        <v-list-item-subtitle>{{ item.vendas }} vendas</v-list-item-subtitle>
      </v-list-item>
    </v-list>
  </v-card>
</template>

<script setup>
  defineProps({
    title: String,
    icon: String,
    iconColor: { type: String, default: 'primary' },
    items: { type: Array, default: () => [] },
    total: { type: Number, default: 0 },
  })
</script>

<style scoped>
.card-border {
  border: 2px solid rgba(var(--v-theme-primary), 0.75);
}
</style>
