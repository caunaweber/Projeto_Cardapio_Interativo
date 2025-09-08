<template>
  <MenuAppBar title="🍳 Restaurante - Gerenciamento de Comandas" />

  <v-container fluid>

    <v-container>
      <v-row>
        <v-col cols="12" md="9">
          <v-text-field
            v-model="search"
            hide-details
            label="Pesquisar comandas por número..."
            prepend-inner-icon="mdi-magnify"
            rounded="xl"
            variant="outlined"
          />
        </v-col>
        <v-col cols="12" md="3">
          <v-select
            v-model="filtro"
            hide-details
            :items="[ 'A Entregar', 'Todos', 'Entregue']"
            label="Filtrar por status"
            prepend-inner-icon="mdi-filter"
            rounded="xl"
            variant="outlined"
          />
        </v-col>
      </v-row>
    </v-container>
    <h2 class="mb-6 text-h5 font-weight-bold text-primary border-b-md pb-4">
      Comandas <span v-if="filtro !== 'Todos'">- {{ filtro }}</span>
    </h2>

    <v-row dense>
      <v-col
        v-for="comanda in comandasFiltradas"
        :key="comanda.mesa"
        class="d-flex"
        cols="12"
        lg="3"
        md="4"
        sm="6"
      >
        <KitchenCard
          :action-color="comanda.acao.cor"
          :action-text="comanda.acao.texto"
          class="flex-grow-1"
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
  const search = ref('')

  const comandas = ref([
    {
      mesa: 1,
      status: 'A Entregar',
      numero: 101,
      itens: [
        { qtd: 1, nome: 'Filé Mignon' },
        { qtd: 2, nome: 'Batata Frita' },
        { qtd: 1, nome: 'Coca-Cola' },
        { qtd: 1, nome: 'Filé Mignon' },
        { qtd: 2, nome: 'Batata Frita' },
        { qtd: 1, nome: 'Coca-Cola' },
        { qtd: 1, nome: 'Coca-Cola' },
      ],
      tempo: '00:25',
      acao: { texto: 'Finalizar', cor: 'success' },
    },
    {
      mesa: 2,
      status: 'Entregue',
      numero: 102,
      itens: [
        { qtd: 1, nome: 'Prato Executivo' },
        { qtd: 1, nome: 'Suco Natural' },
      ],
      tempo: '00:40',
      acao: { texto: 'Editar', cor: 'primary' },
    },
    {
      mesa: 3,
      status: 'A Entregar',
      numero: 103,
      itens: [
        { qtd: 2, nome: 'Pizza Calabresa' },
        { qtd: 1, nome: 'Vinho Tinto' },
      ],
      tempo: '00:15',
      acao: { texto: 'Finalizar', cor: 'success' },
    },
    {
      mesa: 4,
      status: 'A Entregar',
      numero: 104,
      itens: [
        { qtd: 1, nome: 'Salada Caesar' },
        { qtd: 1, nome: 'Água com Gás' },
      ],
      tempo: '00:10',
      acao: { texto: 'Finalizar', cor: 'success' },
    },
    {
      mesa: 5,
      status: 'Entregue',
      numero: 105,
      itens: [
        { qtd: 3, nome: 'Hambúrguer Artesanal' },
        { qtd: 3, nome: 'Refrigerante' },
      ],
      tempo: '00:55',
      acao: { texto: 'Editar', cor: 'primary' },
    },
    {
      mesa: 6,
      status: 'A Entregar',
      numero: 106,
      itens: [
        { qtd: 1, nome: 'Sopa do Dia' },
        { qtd: 2, nome: 'Pão de Alho' },
      ],
      tempo: '00:20',
      acao: { texto: 'Finalizar', cor: 'success' },
    },
    {
      mesa: 7,
      status: 'Entregue',
      numero: 107,
      itens: [
        { qtd: 1, nome: 'Strogonoff de Frango' },
        { qtd: 1, nome: 'Arroz Branco' },
        { qtd: 1, nome: 'Suco de Uva' },
      ],
      tempo: '01:05',
      acao: { texto: 'Editar', cor: 'primary' },
    },
    {
      mesa: 8,
      status: 'A Entregar',
      numero: 108,
      itens: [
        { qtd: 2, nome: 'Espaguete à Bolonhesa' },
        { qtd: 1, nome: 'Suco de Laranja' },
      ],
      tempo: '00:30',
      acao: { texto: 'Finalizar', cor: 'success' },
    },
    {
      mesa: 9,
      status: 'Entregue',
      numero: 109,
      itens: [
        { qtd: 1, nome: 'Sushi Combo' },
        { qtd: 1, nome: 'Chá Verde' },
      ],
      tempo: '01:15',
      acao: { texto: 'Editar', cor: 'primary' },
    },
    {
      mesa: 10,
      status: 'A Entregar',
      numero: 110,
      itens: [
        { qtd: 1, nome: 'Feijoada' },
        { qtd: 2, nome: 'Caipirinha' },
      ],
      tempo: '00:50',
      acao: { texto: 'Finalizar', cor: 'success' },
    },
  ])

  const comandasFiltradas = computed(() => {
    return comandas.value.filter(c => {
      const passaFiltroStatus = filtro.value === 'Todos' || c.status === filtro.value
      const termo = search.value.toLowerCase()
      const passaFiltroSearch
        = String(c.mesa).includes(termo)
          || String(c.numero).includes(termo)
          || c.itens.some(i => i.nome.toLowerCase().includes(termo))

      return passaFiltroStatus && passaFiltroSearch
    })
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
