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

    <v-container v-if="!comandaStore.error" class="pb-0">
      <v-row>
        <v-col cols="12" md="9">
          <v-text-field
            v-model="search"
            hide-details
            label="Pesquisar comandas..."
            prepend-inner-icon="mdi-magnify"
            rounded="xl"
            variant="outlined"
          />
        </v-col>
        <v-col cols="12" md="3">
          <v-select
            v-model="filtro"
            hide-details
            :items="['A Entregar', 'Todos', 'Entregue']"
            label="Filtrar por status"
            prepend-inner-icon="mdi-filter"
            rounded="xl"
            variant="outlined"
          />
        </v-col>
      </v-row>
    </v-container>

    <h2
      class="mb-6 mt-2 text-h5 font-weight-bold text-primary border-b-md pb-4 d-flex justify-space-between align-center"
    >
      <div>
        Comandas
        <span v-if="filtro !== 'Todos'">- {{ filtro }}</span>
      </div>

      <v-btn
        v-if="filtro === 'Entregue' && comandasFiltradas.length > 0"
        color="error"
        :disabled="comandaStore.loadingAcao"
        :loading="comandaStore.loadingAcao"
        prepend-icon="mdi-delete"
        variant="flat"
        @click="abrirConfirmacaoExcluirTodas"
      >
        Excluir todas as comandas entregues
      </v-btn>
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
        lg="4"
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
          @delete="abrirConfirmacao(comanda)"
          @edit="abrirEdicao(comanda)"
        />
      </v-col>
    </v-row>

    <v-dialog v-model="dialogEditar" max-width="600px">
      <v-card rounded="xl">
        <v-card-title class="text-h6 font-weight-bold d-flex justify-space-between align-center">
          ✏️ Editar Comanda #{{ comandaEditando?.numero }}
          <v-btn
            color="success"
            rounded="xl"
            size="small"
            variant="tonal"
            @click="adicionarItem"
          >
            ➕ Adicionar Item
          </v-btn>
        </v-card-title>

        <v-divider />

        <v-card-text>
          <v-row
            v-for="(item, i) in comandaEditando?.itens"
            :key="i"
            class="mb-2"
            dense
          >
            <v-col cols="8">
              <v-select
                v-model="item.pratoId"
                dense
                hide-details
                item-title="nome"
                item-value="id"
                :items="pratos"
                label="Prato"
                variant="outlined"
              />
            </v-col>

            <v-col cols="3">
              <v-text-field
                v-model="item.qtd"
                dense
                hide-details
                label="Qtd"
                min="1"
                type="number"
                variant="outlined"
              />
            </v-col>

            <v-col class="d-flex align-center justify-center" cols="1">
              <v-btn
                color="error"
                icon
                size="small"
                variant="text"
                @click="removerItem(i)"
              >
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </v-col>
          </v-row>
        </v-card-text>

        <v-divider />

        <v-card-actions>
          <v-spacer />
          <v-btn color="error" text @click="dialogEditar = false">Cancelar</v-btn>
          <v-btn color="primary" @click="salvarEdicao">Salvar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <ConfirmDialog
      v-model="dialogConfirm"
      message="Tem certeza que deseja excluir esta comanda?"
      title="Excluir Comanda"
      @confirm="deletarComanda"
    />

    <ConfirmDialog
      v-model="dialogConfirmExcluirTodas"
      message="Tem certeza que deseja excluir todas as comandas entregues? Esta ação não poderá ser desfeita."
      title="Excluir Todas as Comandas Entregues"
      @confirm="deletarTodasEntregues"
    />

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" rounded="xl" timeout="3000">
      {{ snackbar.text }}
    </v-snackbar>
  </v-container>
</template>

<script setup>
  import ConfirmDialog from '@/components/ConfirmDialog.vue'
  import { useComandaStore } from '@/stores/comandaStore'
  import { usePratosStore } from '@/stores/pratosStore'

  const comandaStore = useComandaStore()
  const pratosStore = usePratosStore()
  const filtro = ref('A Entregar')
  const search = ref('')
  const dialogEditar = ref(false)
  const dialogConfirm = ref(false)
  const dialogConfirmExcluirTodas = ref(false)
  const comandaEditando = ref(null)
  const comandaSelecionada = ref(null)
  const snackbar = reactive({ show: false, text: '', color: 'error' })
  const pratos = computed(() => pratosStore.pratos)

  onMounted(async () => {
    try {
      await Promise.all([
        comandaStore.carregarComandas(),
        pratosStore.carregarPratos(),
      ])
    } catch (error) {
      console.error('Erro ao carregar dados iniciais:', error)
    }
  })

  async function tentaCarregarDeNovo () {
    try {
      await comandaStore.carregarComandas()
    } catch (error) {
      console.error('Erro pego pelo componente:', error)
    }
  }

  function abrirEdicao (comanda) {
    comandaEditando.value = {
      ...comanda,
      itens: comanda.itens.map(i => ({
        pratoId: i.id,
        qtd: i.qtd,
      })),
    }
    dialogEditar.value = true
  }

  function abrirConfirmacao (comanda) {
    comandaSelecionada.value = comanda
    dialogConfirm.value = true
  }

  function abrirConfirmacaoExcluirTodas () {
    dialogConfirmExcluirTodas.value = true
  }

  const comandasFiltradas = computed(() => {
    return comandaStore.comandas.filter(c => {
      const passaFiltroStatus = filtro.value === 'Todos' || c.status.toLowerCase() === filtro.value.toLowerCase()
      const termo = search.value.toLowerCase()
      const passaFiltroSearch
        = String(c.mesa).includes(termo)
          || String(c.numero).includes(termo)
          || (c.itens && c.itens.some(i => i.nome.toLowerCase().includes(termo)))
      return passaFiltroStatus && passaFiltroSearch
    })
  })

  async function atualizaStatus (comanda) {
    try {
      if (comanda.status === 'A Entregar') {
        await comandaStore.atualizarStatusComanda(comanda.id, 'ENTREGUE')
        snackbar.text = '✅ Comanda finalizada com sucesso!'
        snackbar.color = 'success'
      }
    } catch {
      snackbar.text = 'Erro ao atualizar status da comanda.'
      snackbar.color = 'error'
    } finally {
      snackbar.show = true
    }
  }

  async function salvarEdicao () {
    try {
      const dto = {
        itens: comandaEditando.value.itens.map(i => ({
          pratoId: i.pratoId,
          qtd: i.qtd,
        })),
      }
      await comandaStore.atualizarComanda(comandaEditando.value.id, dto)
      await comandaStore.carregarComandas()
      snackbar.text = '✏️ Comanda atualizada com sucesso!'
      snackbar.color = 'success'
    } catch (error) {
      snackbar.text = error.response?.data?.message || 'Erro ao salvar edição.'
      snackbar.color = 'error'
    } finally {
      dialogEditar.value = false
      snackbar.show = true
    }
  }

  async function deletarComanda () {
    try {
      await comandaStore.deletarComanda(comandaSelecionada.value.id)
      snackbar.text = '🗑️ Comanda excluída com sucesso!'
      snackbar.color = 'success'
    } catch {
      snackbar.text = 'Erro ao excluir comanda.'
      snackbar.color = 'error'
    } finally {
      snackbar.show = true
    }
  }

  async function deletarTodasEntregues () {
    try {
      await comandaStore.deletarComandasEntregues()
      snackbar.text = '🗑️ Todas as comandas entregues foram excluídas com sucesso!'
      snackbar.color = 'success'
    } catch {
      snackbar.text = 'Erro ao excluir comandas entregues.'
      snackbar.color = 'error'
    } finally {
      snackbar.show = true
    }
  }

  function adicionarItem () {
    const primeiroPrato = pratos.value[0]
    comandaEditando.value.itens.push({
      pratoId: primeiroPrato ? primeiroPrato.id : null,
      qtd: 1,
    })
  }

  function removerItem (index) {
    if (comandaEditando.value.itens.length > 1) {
      comandaEditando.value.itens.splice(index, 1)
    } else {
      snackbar.text = 'A comanda deve ter pelo menos um item.'
      snackbar.color = 'error'
      snackbar.show = true
    }
  }
</script>
