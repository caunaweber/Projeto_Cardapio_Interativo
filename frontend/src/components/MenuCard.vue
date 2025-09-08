<template>
  <v-card class="h-100 d-flex flex-column pa-2 menu-card-border" :elevation="3" rounded="xl">
    <div class="position-relative">
      <v-img
        v-if="item?.imagem"
        :alt="item?.nome || 'Item'"
        class="rounded-xl"
        cover
        height="180"
        :src="item.imagem"
      />

      <v-badge
        v-if="qtd > 0"
        class="badge-quantidade mr-2"
        color="success"
        :content="qtd"
        overlap
      />
    </div>

    <v-card-title class="text-center font-weight-bold mt-2">
      {{ item?.nome }}
    </v-card-title>

    <v-card-subtitle v-if="item?.preco !== undefined" class="text-center text-medium-emphasis">
      R$ {{ item.preco.toFixed(2) }}
    </v-card-subtitle>

    <v-spacer />

    <v-card-actions class="justify-center pb-3">
      <v-btn
        color="error"
        icon
        size="small"
        variant="outlined"
        @click="$emit('remover', item.id)"
      >
        <v-icon>mdi-minus</v-icon>
      </v-btn>

      <span class="mx-3 font-weight-bold text-subtitle-1">
        {{ qtd }}
      </span>

      <v-btn
        color="success"
        icon
        size="small"
        variant="flat"
        @click="$emit('adicionar', item.id)"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup>
  defineProps({
    item: { type: Object, required: true },
    qtd: { type: Number, default: 0 },
  })

  defineEmits(['adicionar', 'remover'])
</script>

<style>
.menu-card-border {
  border: 2px solid rgba(var(--v-theme-primary), 0.75);
}

.position-relative {
  position: relative;
}

.badge-quantidade {
  position: absolute;
  top: 8px;
  right: 8px;
  font-weight: bold;
}
</style>
