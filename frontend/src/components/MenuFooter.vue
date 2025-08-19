<template>
    <v-footer app class="pa-2" color="primary" dark elevation="8">
        <div class="text-body-1">
            🛒 Itens: <strong>{{ totalItens }}</strong> |
            💰 Total: <strong>R$ {{ totalPreco.toFixed(2) }}</strong>
        </div>
        <v-spacer />
        <v-btn class="font-weight-bold" color="secondary" rounded="xl" size="large" variant="flat"
            @click="$emit('finalizar')">
            Finalizar pedido
        </v-btn>
    </v-footer>
</template>

<script setup>
defineProps({
    totalItens: Number,
    totalPreco: Number
})

defineEmits(['finalizar'])
</script>