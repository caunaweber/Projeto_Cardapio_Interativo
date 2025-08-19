<template>
    <v-dialog v-model="internalValue" max-width="500">
      <v-card rounded="xl">
        <v-card-title class="text-h6 font-weight-bold">
          ✅ Confirmar Pedido
        </v-card-title>
  
        <v-divider />
  
        <v-card-text>
          <v-list>
            <v-list-item v-for="produto in pedidos" :key="produto.id">
              <v-list-item-title>
                {{ produto.nome }}
              </v-list-item-title>
              <v-list-item-subtitle>
                {{ produto.qtd }}x - R$ {{ (produto.qtd * produto.preco).toFixed(2) }}
              </v-list-item-subtitle>
            </v-list-item>
          </v-list>
  
          <v-divider class="my-2" />
  
          <div class="text-right font-weight-bold text-subtitle-1">
            Total: R$ {{ totalPreco.toFixed(2) }}
          </div>
        </v-card-text>
  
        <v-card-actions class="d-flex justify-end">
          <v-btn variant="text" @click="$emit('update:modelValue', false)">Cancelar</v-btn>
          <v-btn color="success" rounded="xl" @click="$emit('confirmar')">
            Confirmar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </template>
  
  <script setup>
  import { computed } from 'vue'
  
  const props = defineProps({
    modelValue: Boolean,
    pedidos: Array,
    totalPreco: Number
  })
  
  defineEmits(['update:modelValue', 'confirmar'])
  
  // criar proxy para usar v-model interno
  const internalValue = computed({
    get: () => props.modelValue,
    set: (val) => emit('update:modelValue', val)
  })
  </script>
  