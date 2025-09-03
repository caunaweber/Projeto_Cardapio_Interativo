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
      <v-text-field
        v-model="search"
        class="mb-4"
        hide-details
        label="Pesquisar pratos..."
        prepend-inner-icon="mdi-magnify"
        rounded="xl"
        variant="outlined"
      />

      <v-data-table
        class="elevation-2 rounded-xl"
        :headers="headers"
        item-value="id"
        :items="pratos"
        :search="search"
      >
        <template #item.imagem="{ item }">
          <v-avatar rounded size="60">
            <v-img :src="item.imagem || 'no-image.png'" />
          </v-avatar>
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
            @click="removerPrato(item)"
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
  </v-container>
</template>

<script setup>
  import { reactive, ref, watch } from 'vue'
  import MenuAppBar from '@/components/MenuAppBar.vue'
  import MenuDialog from '@/components/MenuDialog.vue'

  const dialog = ref(false)
  const pratoEditando = ref(null)
  const search = ref('')

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
    { id: 3, nome: 'Sobremesa', categoria: 'Sobremesa', preco: 20, imagem: 'sobremesa.png' },
  ])

  const form = reactive({
    id: null,
    nome: '',
    categoria: '',
    preco: 0,
    imagem: '',
    file: null,
  })

  const previewUrl = ref(null)

  watch(() => form.file, file => {
    previewUrl.value = file && file instanceof File ? URL.createObjectURL(file) : null
  })

  function abrirDialog (prato = null) {
    if (prato) {
      pratoEditando.value = prato
      Object.assign(form, { ...prato, file: null })
    } else {
      pratoEditando.value = null
      Object.assign(form, { id: null, nome: '', categoria: '', preco: 0, imagem: '', file: null })
    }
    dialog.value = true
  }

  function salvarPrato () {
    if (pratoEditando.value) {
      Object.assign(pratoEditando.value, { ...form, imagem: previewUrl.value || form.imagem })
    } else {
      pratos.value.push({ ...form, id: Date.now(), imagem: previewUrl.value || form.imagem })
    }
    dialog.value = false
    previewUrl.value = null
  }

  function removerPrato (prato) {
    pratos.value = pratos.value.filter(p => p.id !== prato.id)
  }
</script>
