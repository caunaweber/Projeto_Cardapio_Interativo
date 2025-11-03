<template>
  <MenuAppBar title="🍳 Restaurante - Gerenciamento de Comandas" />

  <v-container fluid>
    <v-overlay
      class="align-center justify-center"
      :model-value="comandaStore.loadingLista"
      persistent
    >
      <v-progress-circular color="primary" indeterminate size="64" />
      <h3 class="mt-4 text-white">Carregando comandas...</h3>
    </v-overlay>

    <v-alert
      v-if="comandaStore.error && !comandaStore.loadingLista"
      border="start"
      class="mb-6 mx-4"
      prominent
      title="Erro ao Carregar Comandas"
      type="error"
    >
      {{ comandaStore.error.message || 'Não foi possível buscar os dados.' }}
      <template #append>
        <v-btn class="ml-4" color="info" variant="tonal" @click="tentaCarregarDeNovo">
          Tentar Novamente
        </v-btn>
      </template>
    </v-alert>

    <v-container v-if="!comandaStore.error">
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

    <v-alert
      v-if="!comandaStore.loadingLista && comandasFiltradas.length === 0 && !comandaStore.error"
      class="ma-4"
      type="info"
    >
      Nenhuma comanda encontrada com os filtros atuais.
    </v-alert>

    <v-row dense>
      <v-col
        v-for="comanda in comandasFiltradas"
        :key="comanda.numero"
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

    <v-dialog v-model="dialogEditar" max-width="600px">
      <v-card>
        <v-card-title class="text-h6 font-weight-bold">
          Editar Comanda #{{ comandaEditando?.numero }}
        </v-card-title>
        <v-divider />
        <v-card-text>
          <v-row
            v-for="(item, i) in comandaEditando?.itens"
            :key="i"
            class="mb-2"
            dense
          >
            <v-col cols="10">
              <v-text-field
                v-model="item.nome"
                dense
                hide-details
                label="Produto"
                variant="outlined"
              />
            </v-col>
            <v-col cols="2">
              <v-text-field
                v-model="item.qtd"
                dense
                hide-details
                label="Qtd"
                type="number"
                variant="outlined"
              />
            </v-col>
          </v-row>
        </v-card-text>
        <v-divider />
        <v-card-actions>
          <v-spacer />
          <v-btn
            color="error"
            :disabled="comandaStore.loadingAcao"
            text
            @click="dialogEditar = false"
          >
            Cancelar
          </v-btn>
          <v-btn
            color="primary"
            :disabled="comandaStore.loadingAcao"
            :loading="comandaStore.loadingAcao"
            @click="salvarEdicao"
          >
            Salvar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" rounded="xl" timeout="3000">
      {{ snackbar.text }}
    </v-snackbar>
  </v-container>
</template>

<script setup>
  import { computed, onMounted, reactive, ref } from 'vue'
  import KitchenCard from '@/components/KitchenCard.vue'
  import MenuAppBar from '@/components/MenuAppBar.vue'
  import { useComandaStore } from '@/stores/comandaStore'

  const comandaStore = useComandaStore()

  const filtro = ref('A Entregar')
  const search = ref('')

  const dialogEditar = ref(false)
  const comandaEditando = ref(null)

  const snackbar = reactive({ show: false, text: '', color: 'error' })

  onMounted(() => {
    tentaCarregarDeNovo()
  })

  async function tentaCarregarDeNovo () {
    try {
      await comandaStore.carregarComandas()
    } catch (error) {
      console.error('Erro pego pelo componente:', error)
    }
  }

  const comandasFiltradas = computed(() => {
    return comandaStore.comandas.filter(c => {
      const passaFiltroStatus = filtro.value === 'Todos' || c.status === filtro.value
      const termo = search.value.toLowerCase()
      const passaFiltroSearch
        = String(c.mesa).includes(termo)
          || String(c.numero).includes(termo)
          || (c.itens && c.itens.some(i => i.nome.toLowerCase().includes(termo)))

      return passaFiltroStatus && passaFiltroSearch
    })
  })

  function atualizaStatus (comanda) {
    if (comanda.status === 'A Entregar') {
      console.log('implementar o PUT para o ID:', comanda.id)
    } else if (comanda.status === 'Entregue') {
      // eslint-disable-next-line unicorn/prefer-structured-clone
      comandaEditando.value = JSON.parse(JSON.stringify(comanda))
      dialogEditar.value = true
    }
  }

  function salvarEdicao () {
    if (comandaEditando.value) {
      console.log('implementar o PUT para o ID:', comandaEditando.value.id)
    }
    dialogEditar.value = false
  }

  // function mostrarSnackbar (texto, cor = 'error') {
  //   snackbar.text = texto
  //   snackbar.color = cor
  //   snackbar.show = true
  // }
</script>
