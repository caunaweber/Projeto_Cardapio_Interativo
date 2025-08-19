<template>
  <v-navigation-drawer v-model="internalValue" location="right" temporary width="350">
    <v-card flat>
      <v-card-title class="font-weight-bold text-h6">
        🛒 Meus Pedidos
      </v-card-title>
      <v-divider />

      <v-list>
        <v-list-item v-for="produto in pedidos" :key="produto.id">
          <template #prepend>
            <v-avatar size="40">
              <v-img :src="produto.imagem" />
            </v-avatar>
          </template>

          <v-list-item-title>
            {{ produto.nome }}
          </v-list-item-title>
          <v-list-item-subtitle>
            {{ produto.qtd }}x - R$ {{ (produto.qtd * produto.preco).toFixed(2) }}
          </v-list-item-subtitle>

          <template #append>
            <v-btn color="error" icon size="small" variant="text" @click="$emit('remover', produto)">
              <v-icon>mdi-delete</v-icon>
            </v-btn>
          </template>
        </v-list-item>
      </v-list>

      <v-divider />

      <v-card-text class="d-flex justify-space-between font-weight-bold">
        <span>Total:</span>
        <span>R$ {{ totalPreco.toFixed(2) }}</span>
      </v-card-text>

      <v-card-actions>
        <v-btn block color="secondary" rounded="xl" size="large" variant="flat" @click="$emit('finalizar')">
          Finalizar pedido
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-navigation-drawer>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  modelValue: Boolean,
  pedidos: Array,
  totalPreco: Number,
})

const emit = defineEmits(['update:modelValue', 'remover', 'finalizar'])

const internalValue = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})
</script>
