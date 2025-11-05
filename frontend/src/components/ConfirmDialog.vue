<template>
  <v-dialog v-model="internalValue" max-width="400" persistent>
    <v-card class="rounded-xl">
      <v-card-title class="text-h6 font-weight-bold">
        {{ title }}
      </v-card-title>

      <v-card-text>
        {{ message }}
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="cancel">Cancelar</v-btn>
        <v-btn color="error" rounded="xl" @click="confirm">Confirmar</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

  <script setup>
  import { ref, watch } from 'vue'

  const props = defineProps({
    modelValue: { type: Boolean, default: false },
    title: { type: String, default: 'Confirmação' },
    message: { type: String, default: 'Você tem certeza dessa ação?' },
  })
  const emit = defineEmits(['update:modelValue', 'confirm', 'cancel'])

  const internalValue = ref(props.modelValue)

  watch(() => props.modelValue, val => internalValue.value = val)
  watch(internalValue, val => emit('update:modelValue', val))

  function confirm () {
    emit('confirm')
    internalValue.value = false
  }
  function cancel () {
    emit('cancel')
    internalValue.value = false
  }
  </script>
