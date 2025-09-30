<template>
  <v-container fluid>
    <MenuAppBar title="🍳 Gerenciamento - Pratos" @toggle-drawer="drawer = !drawer">
      <template #left>
        <v-btn icon @click="drawer = !drawer">
          <v-icon>mdi-menu</v-icon>
        </v-btn>
      </template>

      <template #actions>
        <v-btn color="success" rounded="xl" variant="flat" @click="abrirDialog()">
          ➕ Novo Prato
        </v-btn>
      </template>
    </MenuAppBar>

    <v-container class="py-6">
      <v-row class="mb-4">
        <v-col cols="12" md="9">
          <v-text-field
            v-model="search"
            hide-details
            label="Pesquisar pratos..."
            prepend-inner-icon="mdi-magnify"
            rounded="xl"
            variant="outlined"
          />
        </v-col>
        <v-col cols="12" md="3">
          <v-select
            v-model="filtroCategoria"
            hide-details
            :items="['Todos', ...categorias]"
            label="Filtrar por categoria"
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
        item-value="id"
        :items="pratosFiltrados"
        :search="search"
      >
        <template #item.imagem="{ item }">
          <v-avatar class="my-1" rounded size="70">
            <v-img :src="getImageSrc(item.imagem)" />
          </v-avatar>
        </template>

        <template #item.preco="{ item }">
          R$ {{ Number(item.preco).toFixed(2) }}
        </template>

        <template #item.vendas="{ item }">
          <span>{{ item.vendas }}</span>
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
        {{ pratoEditando ? '✏️ Editar Prato' : '➕ Novo Prato' }}
      </template>

      <template #default>
        <v-form ref="formRef">
          <v-text-field v-model="pratoEditando.nome" label="Nome do prato" required />
          <v-select v-model="pratoEditando.categoria" :items="categorias" label="Categoria" required />
          <v-text-field
            v-model.number="pratoEditando.preco"
            label="Preço"
            prefix="R$"
            required
            step="0.01"
            type="number"
          />
          <v-file-input
            v-model="pratoEditando.file"
            accept="image/*"
            label="Imagem do prato"
            outlined
            prepend-icon="mdi-image"
          />
        </v-form>
      </template>

      <template #actions>
        <v-btn variant="text" @click="dialog = false">Cancelar</v-btn>
        <v-btn color="success" rounded="xl" @click="salvarPrato">
          Salvar
        </v-btn>
      </template>
    </MenuDialog>

    <v-snackbar v-model="snackbar.show" :color="snackbar.color" rounded="xl" timeout="2500">
      {{ snackbar.text }}
    </v-snackbar>

    <ConfirmDialog
      v-model="confirmDialog"
      message="Tem certeza que deseja excluir este prato? Essa ação não pode ser desfeita."
      title="Remover prato"
      @confirm="removerPrato(pratoSelecionado)"
    />

    <NavSidebar v-model="drawer" :pagina-atual="'pratos'" />
  </v-container>
</template>

<script setup>
  import { computed, reactive, ref, watch } from 'vue'
  import { usePratosStore } from '@/stores/pratosStore'

  const pratoStore = usePratosStore()

  const drawer = ref(false)
  const dialog = ref(false)
  const confirmDialog = ref(false)
  const pratoSelecionado = ref(null)
  const pratoEditando = reactive({})
  const search = ref('')
  const filtroCategoria = ref('Todos')
  const snackbar = reactive({ show: false, text: '', color: 'success' })

  const headers = [
    { title: 'Imagem', key: 'imagem', align: 'center' },
    { title: 'Nome', key: 'nome' },
    { title: 'Categoria', key: 'categoria' },
    { title: 'Preço', key: 'preco', align: 'end' },
    { title: 'Vendas', key: 'vendas', align: 'center' },
    { title: 'Ações', key: 'acoes', align: 'center', sortable: false },
  ]

  const categorias = pratoStore.categorias

  const pratosFiltrados = computed(() => {
    if (filtroCategoria.value === 'Todos') return pratoStore.pratos
    return pratoStore.pratos.filter(p => p.categoria === filtroCategoria.value)
  })

  watch(() => pratoEditando.file, file => {
    if (!file) return
    const reader = new FileReader()
    reader.addEventListener('load', e => pratoEditando.imagem = e.target.result)
    reader.readAsDataURL(file)
  })

  function abrirDialog (prato = null) {
    if (prato) {
      Object.assign(pratoEditando, { ...prato, file: prato.file || null })
    } else {
      Object.assign(pratoEditando, { id: null, nome: '', categoria: '', preco: 0, imagem: '', file: null, vendas: 0 })
    }
    dialog.value = true
  }

  function salvarPrato () {
    if (pratoEditando.id) {
      pratoStore.atualizarPrato(pratoEditando)
      mostrarSnackbar('Prato atualizado com sucesso!', 'success')
    } else {
      pratoStore.adicionarPrato(pratoEditando)
      mostrarSnackbar('Prato adicionado com sucesso!', 'success')
    }
    dialog.value = false
  }

  function removerPrato (prato) {
    pratoStore.removerPrato(prato.id)
    mostrarSnackbar('Prato removido com sucesso!', 'error')
  }

  function abrirConfirm (prato) {
    pratoSelecionado.value = prato
    confirmDialog.value = true
  }

  function mostrarSnackbar (text, color = 'success') {
    snackbar.text = text
    snackbar.color = color
    snackbar.show = true
  }

  function getImageSrc (imagem) {
    if (!imagem) return '/no-image.png'
    if (typeof imagem !== 'string') return '/no-image.png'
    if (imagem.startsWith('data:') || imagem.startsWith('blob:') || imagem.startsWith('/') || imagem.startsWith('http')) return imagem
    return '/' + imagem
  }
</script>
