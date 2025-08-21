<template>
    <v-footer app class="pa-2" :color="color" :dark="dark" :elevation="elevation">
        <div class="text-body-1">
            <slot name="left">
                🛒 Itens: <strong>{{ totalItens }}</strong> |
                💰 Total: <strong>R$ {{ totalPreco.toFixed(2) }}</strong>
            </slot>
        </div>

        <v-spacer />

        <slot name="actions">
            <v-btn class="font-weight-bold" :color="buttonColor" rounded="xl" size="large" variant="flat"
                @click="$emit('action')">
                {{ buttonText }}
            </v-btn>
        </slot>
    </v-footer>
</template>

<script setup>
defineProps({
    totalItens: {
        type: Number,
        default: 0
    },
    totalPreco: {
        type: Number,
        default: 0
    },
    buttonText: {
        type: String,
        default: "Finalizar pedido"
    },
    buttonColor: {
        type: String,
        default: "secondary"
    },
    color: {
        type: String,
        default: "primary"
    },
    dark: {
        type: Boolean,
        default: true
    },
    elevation: {
        type: [Number, String],
        default: 8
    }
})

defineEmits(['action'])
</script>