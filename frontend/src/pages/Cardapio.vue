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
            :items="cardapio.categoriasNomes"
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
      :items="carrinho.pedidos"
      title="🛒 Meus Pedidos"
      :total-preco="carrinho.totalPreco"
      @finalizar="dialogConfirmar = true"
      @remover="remover"
    />

    <MenuDialog v-model="dialogConfirmar">
      <template #title>✅ Confirmar Pedido</template>
      <template #default>
        <v-list>
          <v-list-item v-for="produto in carrinho.pedidos" :key="produto.id">
            <v-list-item-title>{{ produto.nome }}</v-list-item-title>
            <v-list-item-subtitle>
              {{ produto.qtd }}x - R$ {{ (produto.qtd * produto.preco).toFixed(2) }}
            </v-list-item-subtitle>
          </v-list-item>
        </v-list>
        <v-divider class="my-2" />
        <div class="text-right font-weight-bold text-subtitle-1">
          Total: R$ {{ carrinho.totalPreco.toFixed(2) }}
        </div>
      </template>
      <template #actions>
        <v-btn variant="text" @click="dialogConfirmar = false">Cancelar</v-btn>
        <v-btn color="success" rounded="xl" @click="confirmarPedido">Confirmar</v-btn>
      </template>
    </MenuDialog>

    <MenuFooter :total-itens="carrinho.totalItens" :total-preco="carrinho.totalPreco" @action="dialogConfirmar = true">
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

    <v-snackbar
      v-model="snackbarDanger"
      color="primary"
      elevation="4"
      rounded="xl"
      timeout="2500"
    >
      Não há pedidos no carrinho!
    </v-snackbar>
  </v-container>
</template>

<script setup>
  import { computed, ref } from 'vue'
  import { useCardapioStore } from '@/stores/cardapioStore'
  import { useCarrinhoStore } from '@/stores/carrinhoStore'
  import { useComandaStore } from '@/stores/comandaStore'

  const drawer = ref(false)
  const dialogConfirmar = ref(false)
  const search = ref('')
  const filtroCategoria = ref('Todas')
  const snackbar = ref(false)
  const snackbarDanger = ref(false)

  const cardapio = useCardapioStore()
  const carrinho = useCarrinhoStore()
  const comandaStore = useComandaStore()

  onMounted(() => {
    cardapio.carregarCardapio()
  })

  const categoriasFiltradas = computed(() => {
    return cardapio.categorias.map(c => {
      const itensFiltrados = c.itens.filter(p => {
        const passaCategoria = filtroCategoria.value === 'Todas' || c.nome === filtroCategoria.value
        const termo = search.value.toLowerCase()
        return p.nome.toLowerCase().includes(termo) && passaCategoria
      })
      return { ...c, itensFiltrados }
    }).filter(c => c.itensFiltrados.length > 0)
  })

  function adicionar (id) {
    carrinho.adicionar(id, cardapio.categorias)
  }

  function remover (id) {
    carrinho.remover(id)
  }

  function getQtdProduto (id) {
    return carrinho.getQtdProduto(id)
  }

  function confirmarPedido () {
    if (carrinho.pedidos.length === 0) return snackbarDanger.value = true

    comandaStore.criarComanda(carrinho.pedidos)
    carrinho.confirmarPedido()

    dialogConfirmar.value = false
    snackbar.value = true
  }

  function scrollToTop () {
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
</script>
