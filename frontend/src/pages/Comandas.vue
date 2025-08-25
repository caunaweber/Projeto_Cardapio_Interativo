<template>
  <v-app>
    <MenuAppBar
      title="🍳 Restaurante - Cozinha"
    >
      <template #actions>
        <v-select
          v-model="filtro"
          density="compact"
          hide-details
          :items="['Todos', 'A Entregar', 'Entregue']"
          style="max-width: 150px"
          variant="outlined"
        />
      </template>
    </MenuAppBar>

    <v-container class="mt-4">
      <h3 class="mb-4">Comandas Pendentes</h3>

      <KitchenCard
        v-for="comanda in comandasFiltradas"
        :key="comanda.mesa"
        :action-color="comanda.acao.cor"
        :action-text="comanda.acao.texto"
        :bg-color="comanda.bgColor"
        :itens="comanda.itens"
        :mesa="comanda.mesa"
        :status="comanda.status"
        :tempo="comanda.tempo"
        @action="handleAction(comanda)"
      />
    </v-container>
  </v-app>
</template>

<script setup>
  import { computed, ref } from 'vue'
  import KitchenCard from '@/components/KitchenCard.vue'
  import MenuAppBar from '@/components/MenuAppBar.vue'

  const filtro = ref('Todos')

  // Definindo cores compatíveis com o tema
  const comandas = ref([
    {
      mesa: 5,
      status: 'A Entregar',
      itens: [
        { qtd: 1, nome: 'prato 1' },
        { qtd: 2, nome: 'entrada 2' },
        { qtd: 1, nome: 'limonada' },
      ],
      tempo: '20:30',
      acao: { texto: 'Finalizado', cor: 'success' }, // botão verde do tema
      bgColor: 'surface', // fundo padrão do tema
    },
    {
      mesa: 6,
      status: 'Entregue',
      itens: [
        { qtd: 1, nome: 'prato 1' },
        { qtd: 1, nome: 'prato 2' },
        { qtd: 2, nome: 'vinho' },
      ],
      tempo: '01:15',
      acao: { texto: 'Editar', cor: 'primary' }, // botão azul do tema
      bgColor: 'surface', // fundo padrão do tema
    },
  ])

  const comandasFiltradas = computed(() => {
    if (filtro.value === 'Todos') return comandas.value
    return comandas.value.filter(c => c.status === filtro.value)
  })

  function handleAction (comanda) {
    console.log('Ação clicada na comanda:', comanda)
  }
</script>
