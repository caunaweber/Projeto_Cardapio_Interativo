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
        <v-col cols="12" md="12">
          <v-text-field
            v-model="search"
            hide-details
            label="Pesquisar aparelhos..."
            prepend-inner-icon="mdi-magnify"
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
        :search="search"
      >
        <template #item.mesaNum="{ item }">
          Mesa {{ item.mesaNum ?? '—' }}
        </template>

        <template #item.dataRegistro="{ item }">
          <span>{{ formatarData(item.dataRegistro) }}</span>
        </template>

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
          <v-alert border="start" class="ma-4" type="primary">
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
            v-model="form.mesaNum"
            label="Número da Mesa"
            required
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
      v-model="confirmDialog"
      message="Tem certeza que deseja excluir este aparelho? Essa ação não pode ser desfeita."
      title="Remover aparelho"
      @confirm="removerAparelho(aparelhoSelecionado)"
    />
  </v-container>
</template>

<script setup>
  import { computed, reactive, ref } from 'vue'
  import { useAparelhosStore } from '@/stores/aparelhosStore'

  const drawer = ref(false)
  const search = ref('')
  const dialog = ref(false)
  const aparelhoSelecionado = ref(null)
  const confirmDialog = ref(false)

  const aparelhosStore = useAparelhosStore()

  const headers = [
    { title: 'Número da Mesa', key: 'mesaNum' },
    { title: 'Device ID', key: 'deviceId', align: 'center' },
    { title: 'Data de Criação', key: 'dataRegistro', align: 'center' },
    { title: 'Ações', key: 'acoes', align: 'center', sortable: false },
  ]

  const aparelhosFiltrados = computed(() => {
    if (!search.value) return aparelhosStore.aparelhos
    const termo = search.value.toLowerCase()
    return aparelhosStore.aparelhos.filter(a =>
      a.mesaNum?.toString().includes(termo)
      || a.deviceId?.toLowerCase().includes(termo),
    )
  })

  const formRef = ref(null)
  const form = reactive({
    id: null,
    mesaNum: null,
    deviceId: '',
  })

  function abrirDialog (aparelho) {
    aparelhoSelecionado.value = aparelho
    Object.assign(form, aparelho)
    dialog.value = true
  }

  async function salvarEdicao () {
    const isValid = await formRef.value?.validate()
    if (!isValid) {
      mostrarSnackbar('Preencha todos os campos obrigatórios!', 'error')
      return
    }

    aparelhosStore.atualizarAparelho({ ...form })
    mostrarSnackbar('Aparelho atualizado com sucesso!', 'success')
    dialog.value = false
  }

  function abrirConfirm (aparelho) {
    aparelhoSelecionado.value = aparelho
    confirmDialog.value = true
  }

  function removerAparelho (aparelho) {
    aparelhosStore.removerAparelho(aparelho.id)
    mostrarSnackbar('Aparelho removido com sucesso!', 'error')
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

  const snackbar = reactive({ show: false, text: '', color: 'success' })
</script>
