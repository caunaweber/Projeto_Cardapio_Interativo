<template>
  <v-container fluid>
    <MenuAppBar title="🍳 Administração - Pratos">
      <template #actions>
        <v-btn
          color="success"
          rounded="xl"
          variant="flat"
          @click="abrirDialog()"
        >
          Novo Prato
        </v-btn>
      </template>
    </MenuAppBar>

    <v-container class="py-6">
      <v-row class="mb-4" dense>
        <v-col cols="12" md="10">
          <v-text-field
            v-model="search"
            hide-details
            label="Pesquisar pratos..."
            prepend-inner-icon="mdi-magnify"
            rounded="xl"
            variant="outlined"
          />
        </v-col>
        <v-col cols="12" md="2">
          <v-select
            v-model="filtroCategoria"
            :items="['Todos', ...categorias]"
            hide-details
            label="Filtrar por categoria"
            prepend-inner-icon="mdi-filter"
            rounded="xl"
            variant="outlined"
          />
        </v-col>
      </v-row>

      <v-data-table
        class="elevation-2 rounded-xl"
        :headers="headers"
        item-value="id"
        :items="pratosFiltrados"
        :search="search"
      >
        <template #item.imagem="{ item }">
          <v-avatar rounded size="60">
            <v-img :src="item.imagem || 'no-image.png'" />
          </v-avatar>
        </template>

        <template #item.preco="{ item }">
          R$ {{ Number(item.preco).toFixed(2) }}
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
          <v-text-field v-model="form.nome" label="Nome do prato" required />

          <v-select
            v-model="form.categoria"
            :items="categorias"
            label="Categoria"
            required
          />

          <v-text-field
            v-model.number="form.preco"
            label="Preço"
            prefix="R$"
            required
            type="number"
            step="0.01"
          />

          <v-file-input
            v-model="form.file"
            accept="image/*"
            label="Imagem do prato"
            outlined
            prepend-icon="mdi-image"
          />

          <div v-if="form.file" class="mt-4 text-center">
            <v-avatar rounded size="120">
              <v-img :src="previewUrl" />
            </v-avatar>
          </div>
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
      title="Remover prato"
      message="Tem certeza que deseja excluir este prato? Essa ação não pode ser desfeita."
      @confirm="removerPrato(pratoSelecionado)"
    />
  </v-container>
</template>

<script setup>
import { reactive, ref, watch, computed } from 'vue'
import MenuAppBar from '@/components/MenuAppBar.vue'
import MenuDialog from '@/components/MenuDialog.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'

const confirmDialog = ref(false)
const pratoSelecionado = ref(null)
const dialog = ref(false)
const pratoEditando = ref(null)
const search = ref('')
const filtroCategoria = ref('Todos')

const headers = [
  { title: 'Imagem', key: 'imagem', align: 'center' },
  { title: 'Nome', key: 'nome' },
  { title: 'Categoria', key: 'categoria' },
  { title: 'Preço', key: 'preco', align: 'end' },
  { title: 'Ações', key: 'acoes', align: 'center', sortable: false },
]

const categorias = ['Entrada', 'Prato Principal', 'Bebida', 'Sobremesa']

const pratos = ref([
  { id: 1, nome: 'Prato 1', categoria: 'Prato Principal', preco: 25, imagem: 'image.png' },
  { id: 2, nome: 'Suco de Laranja', categoria: 'Bebida', preco: 12, imagem: 'suco.png' },
  { id: 3, nome: 'Petit Gateau', categoria: 'Sobremesa', preco: 20, imagem: 'sobremesa.png' },
])

const pratosFiltrados = computed(() => {
  if (filtroCategoria.value === 'Todos' || !filtroCategoria.value) return pratos.value
  return pratos.value.filter(p => p.categoria === filtroCategoria.value)
})

const form = reactive({
  id: null,
  nome: '',
  categoria: '',
  preco: 0,
  imagem: '',
  file: null,
})

const previewUrl = ref(null)

const snackbar = reactive({
  show: false,
  text: '',
  color: 'success',
})

watch(() => form.file, file => {
  previewUrl.value = file && file instanceof File ? URL.createObjectURL(file) : null
})

function abrirDialog(prato = null) {
  if (prato) {
    pratoEditando.value = prato
    Object.assign(form, { ...prato, file: null })
  } else {
    pratoEditando.value = null
    Object.assign(form, { id: null, nome: '', categoria: '', preco: 0, imagem: '', file: null })
  }
  dialog.value = true
}

function salvarPrato() {
  if (pratoEditando.value) {
    Object.assign(pratoEditando.value, { ...form, imagem: previewUrl.value || form.imagem })
    mostrarSnackbar('Prato atualizado com sucesso!', 'success')
  } else {
    pratos.value.push({ ...form, id: Date.now(), imagem: previewUrl.value || form.imagem })
    mostrarSnackbar('Prato adicionado com sucesso!', 'success')
  }
  dialog.value = false
  previewUrl.value = null
}

function removerPrato(prato) {
  pratos.value = pratos.value.filter(p => p.id !== prato.id)
  mostrarSnackbar('Prato removido com sucesso!', 'error')
}

function abrirConfirm(prato) {
  pratoSelecionado.value = prato
  confirmDialog.value = true
}

function mostrarSnackbar(text, color = 'success') {
  snackbar.text = text
  snackbar.color = color
  snackbar.show = true
}
</script>
