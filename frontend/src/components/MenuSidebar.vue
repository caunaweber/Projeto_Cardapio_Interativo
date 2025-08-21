<template>
  <v-navigation-drawer
    v-model="internalValue"
    :location="location"
    :temporary="temporary"
    :width="width"
  >
    <v-card flat>
      <v-card-title class="font-weight-bold text-h6">
        <slot name="title">
          {{ title }}
        </slot>
      </v-card-title>
      <v-divider />

      <slot name="content">
        <v-list>
          <v-list-item v-for="item in items" :key="item.id">
            <template #prepend>
              <v-avatar size="40" v-if="item.imagem">
                <v-img :src="item.imagem" />
              </v-avatar>
            </template>

            <v-list-item-title>{{ item.nome }}</v-list-item-title>
            <v-list-item-subtitle v-if="item.qtd && item.preco">
              {{ item.qtd }}x - R$ {{ (item.qtd * item.preco).toFixed(2) }}
            </v-list-item-subtitle>

            <template #append>
              <v-btn
                color="error"
                icon
                size="small"
                variant="text"
                @click="$emit('remover', item)"
              >
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </template>
          </v-list-item>
        </v-list>
      </slot>

      <slot name="footer">
        <v-divider />
        <v-card-text
          v-if="showTotal"
          class="d-flex justify-space-between font-weight-bold"
        >
          <span>Total:</span>
          <span>R$ {{ totalPreco.toFixed(2) }}</span>
        </v-card-text>

        <v-card-actions v-if="showAction">
          <v-btn
            block
            :color="actionColor"
            rounded="xl"
            size="large"
            variant="flat"
            @click="$emit('finalizar')"
          >
            {{ actionText }}
          </v-btn>
        </v-card-actions>
      </slot>
    </v-card>
  </v-navigation-drawer>
</template>

<script setup>
import { computed } from "vue"

const props = defineProps({
  modelValue: Boolean,
  items: {
    type: Array,
    default: () => [],
  },
  totalPreco: {
    type: Number,
    default: 0,
  },
  title: {
    type: String,
    default: "Sidebar",
  },
  actionText: {
    type: String,
    default: "Confirmar",
  },
  actionColor: {
    type: String,
    default: "secondary",
  },
  showTotal: {
    type: Boolean,
    default: true,
  },
  showAction: {
    type: Boolean,
    default: true,
  },
  location: {
    type: String,
    default: "right",
  },
  temporary: {
    type: Boolean,
    default: true,
  },
  width: {
    type: [String, Number],
    default: 350,
  },
})

const emit = defineEmits(["update:modelValue", "remover", "finalizar"])

const internalValue = computed({
  get: () => props.modelValue,
  set: (val) => emit("update:modelValue", val),
})
</script>
