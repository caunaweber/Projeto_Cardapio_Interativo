<template>
  <v-card class="h-100 d-flex flex-column pa-2 menu-card-border" :elevation="elevation" rounded="xl">
    <v-img
      v-if="item?.imagem"
      :alt="item?.nome || 'Item'"
      class="rounded-xl"
      cover
      height="180"
      :src="item.imagem"
    />

    <v-card-title class="text-center font-weight-bold mt-2">
      {{ item?.nome }}
    </v-card-title>

    <v-card-subtitle v-if="item?.preco !== undefined" class="text-center text-medium-emphasis">
      R$ {{ item.preco.toFixed(2) }}
    </v-card-subtitle>

    <v-spacer />

    <v-card-actions class="justify-center pb-3">
      <slot name="actions">
        <v-btn
          color="error"
          icon
          size="small"
          variant="outlined"
          @click="$emit('remover', item)"
        >
          <v-icon>mdi-minus</v-icon>
        </v-btn>

        <span class="mx-3 font-weight-bold text-subtitle-1">
          {{ item?.qtd ?? 0 }}
        </span>

        <v-btn
          color="success"
          icon
          size="small"
          variant="flat"
          @click="$emit('adicionar', item)"
        >
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </slot>
    </v-card-actions>
  </v-card>
</template>

<script setup>
  defineProps({
    item: {
      type: Object,
      required: true,
    },
    elevation: {
      type: Number,
      default: 3,
    },
  })

  defineEmits(['adicionar', 'remover'])
</script>

<style>
.menu-card-border {
  border: 2px solid rgba(var(--v-theme-primary), 0.75);
}
</style>
