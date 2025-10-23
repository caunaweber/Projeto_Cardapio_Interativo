<template>
  <v-container fluid>
    <MenuAppBar title="📱 Gerenciamento - Aparelhos">
      <template #left>
        <v-btn icon @click="drawer = !drawer">
          <v-icon>mdi-menu</v-icon>
        </v-btn>
      </template>
    </MenuAppBar>

    <NavSidebar v-model="drawer" :pagina-atual="'aparelhos'" />

    <v-container class="py-6">
      <v-row class="mb-4">
        <v-col cols="12" md="9">
          <v-text-field
            v-model="search"
            hide-details
            label="Pesquisar aparelhos..."
            prepend-inner-icon="mdi-magnify"
            rounded="xl"
            variant="outlined"
          />
        </v-col>
        <v-col cols="12" md="3">
          <v-select
            v-model="filtroStatus"
            hide-details
            item-title="label"
            item-value="value"
            :items="statusComTodos"
            label="Filtrar por status"
            prepend-inner-icon="mdi-filter"
            rounded="xl"
            variant="outlined"
          />
        </v-col>
      </v-row>

      <v-data-table
        class="elevation-2 rounded-xl"
        density="comfortable"
        :headers="headers"
        :items="aparelhosFiltrados"
        :items-per-page="10"
      >
        <template #item.mesaNum="{ item }">
          Mesa {{ item.mesaNum ?? '—' }}
        </template>

        <template #item.dataRegistry="{ item }">
          <span>{{ formatarData(item.dataRegistry) }}</span>
        </template>

        <template #item.validated="{ item }">
          <div class="d-flex align-center justify-start">
            <v-chip
              :color="item.validated ? 'success' : 'error'"
              :prepend-icon="item.validated ? 'mdi-check-circle' : 'mdi-alert-circle'"
              size="small"
              variant="flat"
            >
              {{ item.validated ? 'Validado' : 'Pendente' }}
            </v-chip>

            <v-tooltip location="top" text="Invalidar Acesso">
              <template #activator="{ props }">
                <v-btn
                  v-if="item.validated"
                  v-bind="props"
                  class="ml-1"
                  color="warning"
                  icon
                  size="x-small"
                  variant="text"
                  @click.stop="abrirConfirmInvalidar(item)"
                >
                  <v-icon>mdi-lock-reset</v-icon>
                </v-btn>
              </template>
            </v-tooltip>

          </div> </template>

        <template #item.deviceId="{ item }">
          <v-chip
            v-if="item.deviceId"
            color="primary"
            size="small"
            text-color="white"
            variant="outlined"
          >
            {{ item.deviceId }}
          </v-chip>
          <span v-else>Indefinido</span>
        </template>

        <template #item.acoes="{ item }">
          <v-tooltip text="Editar">
            <template #activator="{ props }">
              <v-btn
                v-bind="props"
                color="primary"
                icon
                size="small"
                variant="text"
                @click="abrirDialog(item)"
              >
                <v-icon>mdi-pencil</v-icon>
              </v-btn>
            </template>
          </v-tooltip>

          <v-tooltip text="Remover">
            <template #activator="{ props }">
              <v-btn
                v-bind="props"
                color="error"
                icon
                size="small"
                variant="text"
                @click="abrirConfirm(item)"
              >
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </template>
          </v-tooltip>
        </template>

        <template #no-data>
          <v-alert border="start" class="ma-4" type="info">
            Nenhum aparelho encontrado.
          </v-alert>
        </template>
      </v-data-table>
    </v-container>

    <MenuDialog v-model="dialog">
      <template #title>✏️ Editar Aparelho</template>

      <template #default>
        <v-form ref="formRef">
          <v-text-field
            v-model.number="form.mesaNum"
            clearable
            label="Número da Mesa"
            :rules="[rules.minUmOuNulo]"
            type="number"
          />
          <v-text-field
            v-model="form.deviceId"
            disabled
            label="ID do Dispositivo"
          />
        </v-form>
      </template>

      <template #actions>
        <v-btn variant="text" @click="dialog = false">Cancelar</v-btn>
        <v-btn color="success" rounded="xl" @click="salvarEdicao">Salvar</v-btn>
      </template>
    </MenuDialog>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" rounded="xl" timeout="2500">
      {{ snackbar.text }}
    </v-snackbar>

    <ConfirmDialog
      v-model="confirmDialogRemover"
      message="Tem certeza que deseja excluir este aparelho? Essa ação não pode ser desfeita."
      title="Remover aparelho"
      @confirm="confirmarRemover"
    />

    <ConfirmDialog
      v-model="confirmDialogInvalidar"
      confirm-button-color="warning"
      confirm-button-text="Invalidar"
      message="Tem certeza que deseja invalidar o acesso deste aparelho? Ele precisará ser validado novamente com a chave secreta para voltar a funcionar."
      title="Invalidar Aparelho"
      @confirm="confirmarInvalidar"
    />
  </v-container>
</template>

<script setup>
  import { computed, onMounted, reactive, ref } from 'vue'
  import { useDeviceAdminStore } from '@/stores/deviceAdminStore'

  const drawer = ref(false)
  const search = ref('')
  const dialog = ref(false)
  const aparelhoSelecionado = ref(null)
  const confirmDialogRemover = ref(false)
  const confirmDialogInvalidar = ref(false)
  const snackbar = reactive({ show: false, text: '', color: 'success' })
  const formRef = ref(null)

  const deviceAdminStore = useDeviceAdminStore()

  const headers = [
    { title: 'Número da Mesa', key: 'mesaNum' },
    { title: 'Device ID', key: 'deviceId', align: 'center' },
    { title: 'Data de Criação', key: 'dataRegistry', align: 'center' },
    { title: 'Status', key: 'validated', align: 'center' },
    { title: 'Ações', key: 'acoes', align: 'center', sortable: false },
  ]

  const rules = {
    required: value => {
      if (value === 0) return true
      return !!value || 'Este campo é obrigatório.'
    },
    minUmOuNulo: value =>
      (!value && value !== 0) || value >= 1 || 'O número da mesa deve ser 1 ou maior.',
  }

  const filtroStatus = ref('TODOS')

  const statusComTodos = computed(() => [
    { label: 'Todos', value: 'TODOS' },
    { label: 'Validado', value: true },
    { label: 'Pendente', value: false },
  ])

  const aparelhosFiltrados = computed(() => {
    let items = deviceAdminStore.aparelhos

    if (filtroStatus.value !== 'TODOS') {
      items = items.filter(a => a.validated === filtroStatus.value)
    }

    if (search.value) {
      const termo = search.value.toLowerCase()
      items = items.filter(a =>
        a.mesaNum?.toString().includes(termo)
        || a.deviceId?.toLowerCase().includes(termo),
      )
    }

    return items
  })

  const form = reactive({
    id: null,
    mesaNum: null,
    deviceId: '',
  })

  onMounted(async () => {
    try {
      await deviceAdminStore.carregarAparelhos()
    } catch (error) {
      const mensagem = error.response?.data?.message || 'Erro ao carregar aparelhos'
      mostrarSnackbar(mensagem, 'error')
      console.error(error)
    }
  })

  function abrirDialog (aparelho) {
    aparelhoSelecionado.value = aparelho
    Object.assign(form, { ...aparelho })
    dialog.value = true
  }

  async function salvarEdicao () {
    const { valid } = await formRef.value.validate()
    if (!valid) {
      mostrarSnackbar('Por favor, preencha os campos obrigatórios.', 'error')
      return
    }

    try {
      await deviceAdminStore.atualizarAparelho({ ...form })
      mostrarSnackbar('Aparelho atualizado com sucesso!', 'success')
      dialog.value = false
    } catch (error) {
      let mensagem = 'Erro ao salvar aparelho'
      if (error.response?.data) {
        const data = error.response.data
        if (Array.isArray(data) && data.length > 0) {
          mensagem = `[${data[0].field}]: ${data[0].message}`
        } else if (data.message) {
          mensagem = data.message
        }
      }
      mostrarSnackbar(mensagem, 'error')
      console.error(error)
    }
  }

  function abrirConfirm (aparelho) {
    aparelhoSelecionado.value = aparelho
    confirmDialogRemover.value = true
  }

  async function confirmarRemover () {
    if (!aparelhoSelecionado.value) return

    try {
      await deviceAdminStore.removerAparelho(aparelhoSelecionado.value.id)
      mostrarSnackbar('Aparelho removido com sucesso!', 'success')
    } catch (error) {
      let mensagem = 'Erro ao remover aparelho'
      if (error.response?.data) {
        const data = error.response.data
        if (Array.isArray(data) && data.length > 0) {
          mensagem = `[${data[0].field}]: ${data[0].message}`
        } else if (data.message) {
          mensagem = data.message
        }
      }
      mostrarSnackbar(mensagem, 'error')
      console.error(error)
    } finally {
      confirmDialog.value = false
      aparelhoSelecionado.value = null
    }
  }

  function abrirConfirmInvalidar (aparelho) {
    aparelhoSelecionado.value = aparelho
    confirmDialogInvalidar.value = true
  }

  async function confirmarInvalidar () {
    if (!aparelhoSelecionado.value) return
    try {
      await deviceAdminStore.invalidarAparelho(aparelhoSelecionado.value.id)
      mostrarSnackbar('Acesso do aparelho invalidado com sucesso!', 'warning') // Snackbar amarelo
    } catch (error) {
      const mensagem = error.response?.data?.message || 'Erro ao invalidar aparelho'
      mostrarSnackbar(mensagem, 'error')
      console.error(error)
    } finally {
      confirmDialogInvalidar.value = false
      aparelhoSelecionado.value = null
    }
  }

  function mostrarSnackbar (text, color = 'success') {
    snackbar.text = text
    snackbar.color = color
    snackbar.show = true
  }

  function formatarData (date) {
    if (!date) return '—'
    return new Date(date).toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
    })
  }
</script>
