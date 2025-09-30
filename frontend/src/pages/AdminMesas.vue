<template>
  <v-container fluid>
    <MenuAppBar title="🍳 Gerenciamento - Mesas">
      <template #left>
        <v-btn icon @click="drawer = !drawer">
          <v-icon>mdi-menu</v-icon>
        </v-btn>
      </template>

      <template #actions>
        <v-btn color="success" rounded="xl" variant="flat" @click="abrirDialog()">
          ➕ Nova Mesa
        </v-btn>
      </template>
    </MenuAppBar>

    <NavSidebar v-model="drawer" :pagina-atual="'mesas'" />

    <v-container class="py-6">
      <v-row class="mb-4">
        <v-col cols="12" md="12">
          <v-text-field
            v-model="search"
            hide-details
            label="Pesquisar mesas..."
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
        item-value="id"
        :items="mesasFiltradas"
        :search="search"
      >
        <template #item.numero="{ item }">
          Mesa {{ item.numero }}
        </template>

        <template #item.IDvinc="{ item }">
          <v-chip
            v-if="item.IDvinc !== 'Indefinido'"
            color="primary"
            size="small"
            text-color="white"
            variant="outlined"
          >
            {{ item.IDvinc }}
          </v-chip>
          <span v-else>Indefinido</span>
        </template>

        <template #item.acoes="{ item }">
          <v-btn
            color="primary"
            icon
            size="small"
            variant="text"
            @click="abrirDialog(item)"
          >
            <v-icon>mdi-pencil</v-icon>
          </v-btn>
          <v-btn
            color="error"
            icon
            size="small"
            variant="text"
            @click="abrirConfirm(item)"
          >
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </template>
      </v-data-table>
    </v-container>

    <MenuDialog v-model="dialog">
      <template #title>
        {{ mesaEditando ? '✏️ Editar Mesa' : '➕ Nova Mesa' }}
      </template>

      <template #default>
        <v-form ref="formRef">
          <v-text-field v-model.number="form.numero" label="Número da mesa" required type="number" />
          <v-select
            v-model="form.IDvinc"
            :items="['Indefinido', '#1', '#2', '#3', '#4', '#5']"
            label="Identificador Vinculado"
            required
          />
        </v-form>
      </template>

      <template #actions>
        <v-btn variant="text" @click="dialog = false">Cancelar</v-btn>
        <v-btn color="success" rounded="xl" @click="salvarMesa">Salvar</v-btn>
      </template>
    </MenuDialog>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" rounded="xl" timeout="2500">
      {{ snackbar.text }}
    </v-snackbar>

    <ConfirmDialog
      v-model="confirmDialog"
      message="Tem certeza que deseja excluir esta mesa? Essa ação não pode ser desfeita."
      title="Remover mesa"
      @confirm="removerMesa(mesaSelecionada)"
    />
  </v-container>
</template>

<script setup>
  import { computed, reactive, ref } from 'vue'
  import { useMesasStore } from '@/stores/mesasStore'

  const drawer = ref(false)
  const search = ref('')
  const dialog = ref(false)
  const mesaEditando = ref(null)
  const confirmDialog = ref(false)
  const mesaSelecionada = ref(null)

  const mesasStore = useMesasStore()

  const headers = [
    { title: 'Número', key: 'numero' },
    { title: 'ID vinculado', key: 'IDvinc', align: 'center' },
    { title: 'Ações', key: 'acoes', align: 'center', sortable: false },
  ]

  const mesasFiltradas = computed(() => {
    if (!search.value) return mesasStore.mesas
    const termo = search.value.toLowerCase()
    return mesasStore.mesas.filter(m =>
      m.numero.toString().includes(termo)
      || m.IDvinc.toLowerCase().includes(termo),
    )
  })

  const formRef = ref(null)

  const form = reactive({
    id: null,
    numero: null,
    IDvinc: 'Indefinido',
  })

  function abrirDialog (mesa = null) {
    if (mesa) {
      mesaEditando.value = mesa
      Object.assign(form, mesa)
    } else {
      mesaEditando.value = null
      Object.assign(form, { id: null, numero: null, IDvinc: 'Indefinido' })
    }
    dialog.value = true
  }

  async function salvarMesa () {
    const isValid = await formRef.value?.validate()
    if (!isValid) {
      mostrarSnackbar('Preencha todos os campos obrigatórios!', 'error')
      return
    }

    if (mesaEditando.value) {
      mesasStore.atualizarMesa({ ...form })
      mostrarSnackbar('Mesa atualizada com sucesso!', 'success')
    } else {
      mesasStore.adicionarMesa({ ...form })
      mostrarSnackbar('Mesa adicionada com sucesso!', 'success')
    }

    dialog.value = false
  }

  function abrirConfirm (mesa) {
    mesaSelecionada.value = mesa
    confirmDialog.value = true
  }

  function removerMesa (mesa) {
    mesasStore.removerMesa(mesa.id)
    mostrarSnackbar('Mesa removida com sucesso!', 'error')
  }

  function mostrarSnackbar (text, color = 'success') {
    snackbar.text = text
    snackbar.color = color
    snackbar.show = true
  }

  const snackbar = reactive({ show: false, text: '', color: 'success' })
</script>
