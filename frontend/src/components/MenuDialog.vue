<template>
  <v-dialog v-model="internalValue" :max-width="maxWidth">
    <v-card :rounded="rounded">
      <v-card-title v-if="$slots.title" class="text-h6 font-weight-bold">
        <slot name="title" />
      </v-card-title>

      <v-divider v-if="$slots.title" />

      <v-card-text>
        <slot />
      </v-card-text>

      <v-card-actions v-if="$slots.actions" class="d-flex justify-end">
        <slot name="actions" />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { computed } from "vue"

const props = defineProps({
  modelValue: Boolean,
  maxWidth: {
    type: [String, Number],
    default: 500
  },
  rounded: {
    type: String,
    default: "xl"
  }
})

const emit = defineEmits(["update:modelValue"])

const internalValue = computed({
  get: () => props.modelValue,
  set: (val) => emit("update:modelValue", val)
})
</script>
