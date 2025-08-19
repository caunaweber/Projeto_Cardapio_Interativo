<template>
    <v-card class="h-100 d-flex flex-column pa-2" elevation="6" hover rounded="xl">
        <v-img :alt="produto.nome" class="rounded-xl" cover height="180" :src="produto.imagem" />

        <v-card-title class="text-center font-weight-bold mt-2">
            {{ produto.nome }}
        </v-card-title>

        <v-card-subtitle class="text-center text-medium-emphasis">
            R$ {{ produto.preco.toFixed(2) }}
        </v-card-subtitle>

        <v-spacer />

        <v-card-actions class="justify-center pb-3">
            <v-btn color="error" icon size="small" variant="outlined" @click="$emit('remover', produto)">
                <v-icon>mdi-minus</v-icon>
            </v-btn>

            <span class="mx-3 font-weight-bold text-subtitle-1">{{ produto.qtd }}</span>

            <v-btn color="success" icon size="small" variant="flat" @click="$emit('adicionar', produto)">
                <v-icon>mdi-plus</v-icon>
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script setup>
defineProps({
    produto: Object
})
</script>