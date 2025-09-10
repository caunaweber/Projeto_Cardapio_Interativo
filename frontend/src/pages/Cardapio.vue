<template>
  <v-container class="menu-bg" fluid>

    <MenuAppBar title="🍴 Restaurante - Cardápio" @toggle-drawer="drawer = !drawer">
      <template #title-side>
        <span class="text-caption text-medium-emphasis ml-4">
          Aparelho não configurado ID: #{{ 12 }}
        </span>
      </template>

      <template #actions="{ toggleDrawer }">
        <v-btn
          class="font-weight-bold"
          color="success"
          rounded="xl"
          variant="flat"
          @click="toggleDrawer()"
        >
          Meus pedidos
        </v-btn>
      </template>
    </MenuAppBar>

    <v-container class="py-1">
      <v-row>
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
            :items="categoriasNomes"
            label="Filtrar por categoria"
            prepend-inner-icon="mdi-filter"
            rounded="xl"
            variant="outlined"
          />
        </v-col>
      </v-row>
    </v-container>

    <v-container class="py-1">
      <div v-for="categoria in categoriasFiltradas" :key="categoria.nome" class="mb-10">
        <h2 class="mb-6 text-h5 font-weight-bold text-primary border-b-md pb-4">{{ categoria.nome }}</h2>
        <v-row dense>
          <v-col
            v-for="produto in categoria.itensFiltrados"
            :key="produto.id"
            cols="12"
            lg="3"
            md="4"
            sm="6"
          >
            <MenuCard
              :item="produto"
              :qtd="getQtdProduto(produto.id)"
              @adicionar="adicionar"
              @remover="remover"
            />
          </v-col>
        </v-row>
      </div>
    </v-container>

    <MenuSidebar
      v-model="drawer"
      action-text="Finalizar pedido"
      :items="pedidos"
      title="🛒 Meus Pedidos"
      :total-preco="totalPreco"
      @finalizar="dialogConfirmar = true"
      @remover="remover"
    />

    <MenuDialog v-model="dialogConfirmar">
      <template #title>✅ Confirmar Pedido</template>
      <template #default>
        <v-list>
          <v-list-item v-for="produto in pedidos" :key="produto.id">
            <v-list-item-title>{{ produto.nome }}</v-list-item-title>
            <v-list-item-subtitle>
              {{ produto.qtd }}x - R$ {{ (produto.qtd * produto.preco).toFixed(2) }}
            </v-list-item-subtitle>
          </v-list-item>
        </v-list>
        <v-divider class="my-2" />
        <div class="text-right font-weight-bold text-subtitle-1">
          Total: R$ {{ totalPreco.toFixed(2) }}
        </div>
      </template>
      <template #actions>
        <v-btn variant="text" @click="dialogConfirmar = false">Cancelar</v-btn>
        <v-btn color="success" rounded="xl" @click="confirmarPedido">Confirmar</v-btn>
      </template>
    </MenuDialog>

    <MenuFooter :total-itens="totalItens" :total-preco="totalPreco" @action="dialogConfirmar = true">
      <template #content>
        <v-btn
          color="primary"
          large
          rounded="xl"
          @click="scrollToTop"
        >
          <v-icon left>mdi-arrow-up</v-icon>
        </v-btn>
      </template>

      <template #actions>
        <v-btn
          color="success"
          density="default"
          rounded="pill"
          size="large"
          @click="dialogConfirmar = true"
        >
          Finalizar Pedido
        </v-btn>
      </template>
    </MenuFooter>

    <v-snackbar
      v-model="snackbar"
      color="success"
      elevation="4"
      rounded="xl"
      timeout="2500"
    >
      ✅ Pedido enviado para a cozinha!
    </v-snackbar>
  </v-container>
</template>

<script setup>
  const drawer = ref(false)
  const dialogConfirmar = ref(false)
  const search = ref('')
  const filtroCategoria = ref('Todas')
  const snackbar = ref(false)

  const categorias = ref([
    { nome: 'Entradas', itens: [
      { id: 1, nome: 'Salada Caesar', preco: 12.5, imagem: 'image.png' },
      { id: 2, nome: 'Bruschetta', preco: 9, imagem: 'image.png' },
    ] },
    { nome: 'Pratos Principais', itens: [
      { id: 3, nome: 'Filé Mignon', preco: 35, imagem: 'image.png' },
      { id: 4, nome: 'Strogonoff de Frango', preco: 28, imagem: 'image.png' },
    ] },
    { nome: 'Bebidas', itens: [
      { id: 5, nome: 'Suco Natural', preco: 7.5, imagem: 'suco.png' },
      { id: 6, nome: 'Coca-Cola', preco: 5, imagem: 'suco.png' },
    ] },
  ])

  const categoriasNomes = computed(() => ['Todas', ...categorias.value.map(c => c.nome)])

  const categoriasFiltradas = computed(() => {
    return categorias.value.map(c => {
      const itensFiltrados = c.itens.filter(p => {
        const passaCategoria = filtroCategoria.value === 'Todas' || c.nome === filtroCategoria.value
        const termo = search.value.toLowerCase()
        return p.nome.toLowerCase().includes(termo) && passaCategoria
      })
      return { ...c, itensFiltrados }
    }).filter(c => c.itensFiltrados.length > 0)
  })

  const pedidos = ref([])
  const totalPreco = computed(() => pedidos.value.reduce((sum, p) => sum + p.qtd * p.preco, 0))
  const totalItens = computed(() => pedidos.value.reduce((sum, p) => sum + p.qtd, 0))

  function adicionar (id) {
    const produto = categorias.value.flatMap(c => c.itens).find(p => p.id === id)
    const item = pedidos.value.find(p => p.id === id)
    if (item) item.qtd++
    else pedidos.value.push({ ...produto, qtd: 1 })
  }

  function remover (id) {
    const index = pedidos.value.findIndex(p => p.id === id)
    if (index !== -1) {
      if (pedidos.value[index].qtd > 1) pedidos.value[index].qtd--
      else pedidos.value.splice(index, 1)
    }
  }

  function getQtdProduto (id) {
    const item = pedidos.value.find(p => p.id === id)
    return item ? item.qtd : 0
  }

  function confirmarPedido () {
    pedidos.value = []
    dialogConfirmar.value = false
    snackbar.value = true
  }

  function scrollToTop () {
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

</script>
