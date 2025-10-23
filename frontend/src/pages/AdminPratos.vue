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

    <NavSidebar v-model="drawer" :pagina-atual="'pratos'" />

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
            item-title="label"
            item-value="value"
            :items="categoriasComTodos"
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
        :items-per-page="10"
        :search="search"
      >
        <template #item.imagem="{ item }">
          <v-avatar class="my-1" rounded size="70">
            <v-img aspect-ratio="1" cover :src="getImageSrc(item.imagem)" />
          </v-avatar>
        </template>

        <template #item.preco="{ item }">
          R$ {{ Number(item.preco).toFixed(2) }}
        </template>

        <template #item.categoria="{ item }">
          {{ getCategoriaLabel(item.categoria) }}
        </template>

        <template #item.vendas="{ item }">
          <span>{{ item.vendas }}</span>
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
            Nenhum prato encontrado.
          </v-alert>
        </template>
      </v-data-table>
    </v-container>

    <MenuDialog v-model="dialog">
      <template #title>
        {{ pratoEditando.id ? '✏️ Editar Prato' : '➕ Novo Prato' }}
      </template>

      <template #default>
        <v-form ref="formRef">
          <v-text-field
            v-model="pratoEditando.nome"
            label="Nome do prato"
            :rules="[rules.required]"
          />
          <v-select
            v-model="pratoEditando.categoria"
            item-title="label"
            item-value="value"
            :items="categorias"
            label="Categoria"
            :rules="[rules.required]"
          />
          <v-text-field
            v-model.number="pratoEditando.preco"
            label="Preço"
            prefix="R$"
            :rules="[rules.required, rules.minZero]"
            step="0.01"
            type="number"
          />

          <v-row v-if="pratoEditando.imagem && pratoEditando.imagem !== '/no-image.png'" class="mb-4 align-center">
            <v-col cols="auto">
              <v-avatar rounded size="80">
                <v-img aspect-ratio="1" cover :src="pratoEditando.imagem" />
              </v-avatar>
            </v-col>
            <v-col>
              <span class="text-caption">Imagem Atual</span>
            </v-col>
          </v-row>
          <v-file-input
            v-model="pratoEditando.file"
            accept="image/*"
            clearable
            :label="pratoEditando.id ? 'Substituir imagem (opcional)' : 'Imagem do prato'"
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
      @confirm="confirmarRemover"
    />

    <NavSidebar v-model="drawer" :pagina-atual="'pratos'" />
  </v-container>
</template>

<script setup>
  import { computed, onMounted, reactive, ref, watch } from 'vue'
  import { usePratosStore } from '@/stores/pratosStore'

  const pratoStore = usePratosStore()

  const drawer = ref(false)
  const dialog = ref(false)
  const confirmDialog = ref(false)
  const pratoSelecionado = ref(null)
  const search = ref('')
  const filtroCategoria = ref('TODOS')
  const snackbar = reactive({ show: false, text: '', color: 'success' })
  const formRef = ref(null)

  const rules = {
    required: value => {
      if (value === 0) return true
      return !!value || 'Este campo é obrigatório.'
    },
    minZero: value => value >= 0 || 'O preço não pode ser negativo.',
  }

  const pratoEditando = reactive(resetPrato())

  const headers = [
    { title: 'Imagem', key: 'imagem', align: 'center', sortable: false },
    { title: 'Nome', key: 'nome' },
    { title: 'Categoria', key: 'categoria' },
    { title: 'Preço', key: 'preco', align: 'end' },
    { title: 'Vendas', key: 'vendas', align: 'center' },
    { title: 'Ações', key: 'acoes', align: 'center', sortable: false },
  ]

  const categorias = pratoStore.categorias

  const categoriasComTodos = computed(() => [
    { label: 'Todos', value: 'TODOS' },
    ...pratoStore.categorias,
  ])

  function getCategoriaLabel (value) {
    const cat = pratoStore.categorias.find(c => c.value === value)
    return cat ? cat.label : value
  }

  const pratosFiltrados = computed(() => {
    if (filtroCategoria.value === 'TODOS') return pratoStore.pratos
    return pratoStore.pratos.filter(p => p.categoria === filtroCategoria.value)
  })

  watch(() => pratoEditando.file, newFile => {
    const file = Array.isArray(newFile) ? newFile[0] : newFile

    if (file) {
      pratoEditando.file = file
      const reader = new FileReader()
      reader.addEventListener('load', e => (pratoEditando.imagem = e.target.result))
      reader.readAsDataURL(file)
    } else {
      pratoEditando.file = null

      if (pratoEditando.id) {
        const pratoOriginal = pratoStore.pratos.find(p => p.id === pratoEditando.id)
        pratoEditando.imagem = pratoOriginal?.imagem ? getImageSrc(pratoOriginal.imagem) : '/no-image.png'
      } else {
        pratoEditando.imagem = '/no-image.png'
      }
    }
  })

  onMounted(async () => {
    try {
      await pratoStore.carregarPratos()
    } catch (error) {
      let mensagem = 'Erro ao carregar pratos'
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
  })

  function resetPrato () {
    return {
      id: null,
      nome: '',
      categoria: null,
      preco: 0,
      imagem: '',
      file: null,
      vendas: 0,
    }
  }

  function abrirDialog (prato = null) {
    Object.assign(pratoEditando, resetPrato())
    if (prato) {
      Object.assign(pratoEditando, {
        ...prato,
        file: null,
        imagem: prato.imagem ? getImageSrc(prato.imagem) : '/no-image.png',
      })
    } else {
      pratoEditando.imagem = '/no-image.png'
    }
    dialog.value = true
  }

  async function salvarPrato () {
    try {
      const { valid } = await formRef.value.validate()
      if (!valid) {
        mostrarSnackbar('Por favor, preencha todos os campos obrigatórios.', 'error')
        return
      }

      if (pratoEditando.id) {
        await pratoStore.atualizarPrato(pratoEditando)
        mostrarSnackbar('Prato atualizado com sucesso!')
      } else {
        await pratoStore.adicionarPrato(pratoEditando)
        mostrarSnackbar('Prato adicionado com sucesso!')
      }

      dialog.value = false
      Object.assign(pratoEditando, resetPrato())
    } catch (error) {
      let mensagem = 'Erro ao salvar prato'
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

  function abrirConfirm (prato) {
    pratoSelecionado.value = prato
    confirmDialog.value = true
  }

  async function confirmarRemover () {
    if (!pratoSelecionado.value) return
    try {
      await pratoStore.removerPrato(pratoSelecionado.value.id)
      mostrarSnackbar('Prato removido com sucesso!', 'success')
    } catch (error) {
      let mensagem = 'Erro ao remover prato'
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
      pratoSelecionado.value = null
    }
  }

  function mostrarSnackbar (text, color = 'success') {
    snackbar.text = text
    snackbar.color = color
    snackbar.show = true
  }

  function getImageSrc (imagem) {
    if (!imagem || imagem === '/no-image.png') return '/no-im<ctrl61>age.png'
    if (imagem.startsWith('data:') || imagem.startsWith('http')) {
      return imagem
    }
    return `http://localhost:8080/uploads/${imagem}`
  }

</script>
