<template>
  <MenuAppBar title="🍳 Restaurante - Gerenciamento de Comandas">
    <template #actions>
      <v-select
        v-model="filtro"
        density="compact"
        hide-details
        :items="['A Entregar', 'Entregue', 'Todos']"
        style="max-width: 150px"
        variant="outlined"
      />
    </template>
  </MenuAppBar>

  <v-container class="py-6" fluid>
    <h2 class="mb-6 text-h5 font-weight-bold text-primary border-b-md pb-4">
      Comandas <span v-if="filtro !== 'Todos'">- {{ filtro }}</span>
    </h2>

    <v-row dense>
      <v-col
        v-for="comanda in comandasFiltradas"
        :key="comanda.mesa"
        cols="12"
        lg="3"
        md="4"
        sm="6"
      >
        <KitchenCard
          :action-color="comanda.acao.cor"
          :action-text="comanda.acao.texto"
          :itens="comanda.itens"
          :mesa="comanda.mesa"
          :num="comanda.numero"
          :status="comanda.status"
          :tempo="comanda.tempo"
          @action="atualizaStatus(comanda)"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
  import { computed, ref } from 'vue'
  import KitchenCard from '@/components/KitchenCard.vue'
  import MenuAppBar from '@/components/MenuAppBar.vue'

  const filtro = ref('A Entregar')

  const comandas = ref([
    {
      mesa: 1,
      status: 'A Entregar',
      numero: 101,
      itens: [
        { qtd: 1, nome: 'prato 1' },
        { qtd: 2, nome: 'entrada 2' },
        { qtd: 1, nome: 'limonada' },
      ],
      tempo: '20:30',
      acao: { texto: 'Finalizar', cor: 'success' },
    },
    {
      mesa: 2,
      status: 'Entregue',
      numero: 102,
      itens: [
        { qtd: 1, nome: 'prato 1' },
        { qtd: 1, nome: 'prato 2' },
        { qtd: 2, nome: 'vinho' },
      ],
      tempo: '01:15',
      acao: { texto: 'Editar', cor: 'primary' },
    },
    {
      mesa: 3,
      status: 'A Entregar',
      numero: 103,
      itens: [
        { qtd: 1, nome: 'prato 1' },
        { qtd: 2, nome: 'entrada 2' },
        { qtd: 1, nome: 'limonada' },
      ],
      tempo: '20:30',
      acao: { texto: 'Finalizar', cor: 'success' },
    },
    {
      mesa: 4,
      status: 'A Entregar',
      numero: 104,
      itens: [
        { qtd: 1, nome: 'prato 1' },
        { qtd: 2, nome: 'entrada 2' },
        { qtd: 1, nome: 'limonada' },
      ],
      tempo: '20:30',
      acao: { texto: 'Finalizar', cor: 'success' },
    },
    {
      mesa: 5,
      status: 'A Entregar',
      numero: 105,
      itens: [
        { qtd: 1, nome: 'prato 1' },
        { qtd: 2, nome: 'entrada 2' },
        { qtd: 1, nome: 'limonada' },
      ],
      tempo: '20:30',
      acao: { texto: 'Finalizar', cor: 'success' },
    },
    {
      mesa: 6,
      status: 'Entregue',
      numero: 106,
      itens: [
        { qtd: 1, nome: 'prato 1' },
        { qtd: 1, nome: 'prato 2' },
        { qtd: 2, nome: 'vinho' },
      ],
      tempo: '01:15',
      acao: { texto: 'Editar', cor: 'primary' },
    },
    {
      mesa: 7,
      status: 'Entregue',
      numero: 107,
      itens: [
        { qtd: 1, nome: 'prato 1' },
        { qtd: 1, nome: 'prato 2' },
        { qtd: 2, nome: 'vinho' },
      ],
      tempo: '01:15',
      acao: { texto: 'Editar', cor: 'primary' },
    },
    {
      mesa: 8,
      status: 'Entregue',
      numero: 108,
      itens: [
        { qtd: 1, nome: 'prato 1' },
        { qtd: 1, nome: 'prato 2' },
        { qtd: 2, nome: 'vinho' },
      ],
      tempo: '01:15',
      acao: { texto: 'Editar', cor: 'primary' },
    },
  ])

  const comandasFiltradas = computed(() => {
    if (filtro.value === 'Todos') return comandas.value
    return comandas.value.filter(c => c.status === filtro.value)
  })

  function atualizaStatus (comanda) {
    if (comanda.status === 'A Entregar') {
      comanda.status = 'Entregue'
      comanda.acao = { texto: 'Editar', cor: 'primary' }
    } else if (comanda.status === 'Entregue') {
      comanda.status = 'A Entregar'
      comanda.acao = { texto: 'Finalizar', cor: 'success' }
    }
  }
</script>
