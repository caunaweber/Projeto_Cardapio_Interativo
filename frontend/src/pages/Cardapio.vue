<template>
  <v-container v-if="device.validated" fluid>
    <MenuAppBar title="🍴 Restaurante" @toggle-drawer="drawer = !drawer">
      <template #title-side>
        <span class="text-caption text-medium-emphasis ml-4">
          <span v-if="device.mesaNum">Mesa: #{{ device.mesaNum }}</span>
          <span v-else>Aparelho (#{{ device.deviceId }}) não configurado</span>
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
      <v-row class="my-4">
        <v-col cols="12" md="9">
          <v-text-field
            v-model="search"
            clearable
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

      <div v-if="cardapio.carregando" class="text-center pa-10">
        <v-progress-circular color="primary" indeterminate size="50" />
        <p class="mt-4 text-medium-emphasis">Carregando cardápio...</p>
      </div>

      <v-alert
        v-else-if="cardapio.error"
        border="start"
        class="ma-4"
        prominent
        type="error"
      >
        <v-alert-title>Erro ao Carregar Cardápio</v-alert-title>
        {{ cardapio.error }}
        <template #append>
          <v-btn class="ml-4" color="info" variant="tonel" @click="cardapio.carregarCardapio">
            Tentar Novamente
          </v-btn>
        </template>
      </v-alert>

      <div v-else>
        <v-alert v-if="categoriasFiltradas.length === 0" class="ma-4" type="info">
          Nenhum prato encontrado.
        </v-alert>
        <div v-for="categoria in categoriasFiltradas" :key="categoria.nome" class="mb-10">
          <h2 class="mb-6 text-h5 font-weight-bold text-primary border-b-md pb-4">
            {{ categoria.nome }}
          </h2>
          <v-row dense>
            <v-col
              v-for="produto in categoria.itensFiltrados"
              :key="produto.id"
              cols="12"
              lg="3"
              md="4"
              sm="6"
            >
              <MenuCard :item="produto" :qtd="getQtdProduto(produto.id)" @adicionar="adicionar" @remover="remover" />
            </v-col>
          </v-row>
        </div>
      </div>
    </v-container>
    <MenuSidebar
      v-model="drawer"
      action-text="Finalizar pedido"
      :items="carrinho.pedidos"
      title="🛒 Meus Pedidos"
      :total-preco="carrinho.totalPreco"
      @finalizar="abrirDialogoConfirmar"
      @remover="remover"
    />

    <MenuDialog v-model="dialogConfirmar">
      <template #title>✅ Confirmar Pedido</template>
      <template #default>
        <v-list density="compact">
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
        <v-btn
          :disabled="comandaStore.loading"
          variant="text"
          @click="dialogConfirmar = false"
        >
          Cancelar
        </v-btn>
        <v-btn
          color="success"
          :disabled="comandaStore.loading"
          :loading="comandaStore.loading"
          rounded="xl"
          variant="flat"
          @click="confirmarPedido"
        >
          Confirmar
        </v-btn>
      </template>
    </MenuDialog>

    <MenuFooter :total-itens="carrinho.totalItens" :total-preco="carrinho.totalPreco" @action="abrirDialogoConfirmar">
      <template #content>
        <v-btn color="primary" icon="mdi-arrow-up" rounded="xl" @click="scrollToTop" />
      </template>
      <template #actions>
        <v-btn
          color="success"
          density="default"
          rounded="pill"
          size="large"
          @click="abrirDialogoConfirmar"
        >
          Finalizar Pedido
        </v-btn>
      </template>
    </MenuFooter>

    <v-snackbar
      v-model="snackbar.show"
      :color="snackbar.color"
      elevation="4"
      rounded="xl"
      timeout="2500"
    >
      {{ snackbar.text }}
    </v-snackbar>

  </v-container>

  <v-container v-else class="fill-height justify-center align-center">
    <v-dialog max-width="400px" :model-value="!device.validated && !device.loading" persistent>
      <v-card rounded="lg">
        <v-card-title class="text-center pt-6">🔐 Validar dispositivo</v-card-title>
        <v-card-text class="pa-4">
          <v-text-field
            v-model="key"
            autofocus
            class="mb-2"
            :disabled="device.loading"
            :error-messages="device.error ? [device.error] : []"
            label="Chave de acesso"
            type="password"
            variant="outlined"
            @keyup.enter="handleValidate"
          />
        </v-card-text>
        <v-card-actions class="pa-4 pt-0">
          <v-spacer />
          <v-btn
            block
            color="success"
            :disabled="!key || device.loading"
            :loading="device.loading"
            rounded="xl"
            size="large"
            variant="flat"
            @click="handleValidate"
          >
            Validar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>

</template>

<script setup>
  import { computed, onMounted, reactive, ref, watch } from 'vue'
  import { useCardapioStore } from '@/stores/cardapioStore'
  import { useCarrinhoStore } from '@/stores/carrinhoStore'
  import { useComandaStore } from '@/stores/comandaStore'
  import { useDeviceStore } from '@/stores/deviceStore'

  const drawer = ref(false)
  const dialogConfirmar = ref(false)
  const search = ref('')
  const filtroCategoria = ref('Todas')
  const key = ref('')

  const snackbar = reactive({ show: false, text: '', color: 'success' })

  const cardapio = useCardapioStore()
  const carrinho = useCarrinhoStore()
  const comandaStore = useComandaStore()
  const device = useDeviceStore()

  onMounted(() => {
    device.initDevice()
    if (device.validated) {
      cardapio.carregarCardapio()
    }
  })

  watch(() => device.validated, isValidated => {
    if (isValidated && cardapio.categorias.length === 0 && !cardapio.carregando && !cardapio.error) {
      cardapio.carregarCardapio()
    }
  })

  function handleValidate () {
    if (!key.value || device.loading) return
    device.validateKey(key.value)
  }

  const categoriasFiltradas = computed(() => {
    if (cardapio.carregando || cardapio.error) return []
    const termo = search.value ? search.value.toLowerCase() : ''

    return cardapio.categorias
      .map(c => {
        const itensFiltrados = c.itens.filter(p => {
          const passaCategoria = filtroCategoria.value === 'Todas' || c.nome === filtroCategoria.value
          return (p.nome?.toLowerCase() || '').includes(termo) && passaCategoria
        })
        return { ...c, itensFiltrados }
      })
      .filter(c => c.itensFiltrados.length > 0)
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

  function abrirDialogoConfirmar () {
    if (carrinho.pedidos.length === 0) {
      mostrarSnackbar('Não há pedidos no carrinho!', 'warning')
      return
    }
    dialogConfirmar.value = true
  }

  async function confirmarPedido () {
    try {
      await comandaStore.criarComanda(carrinho.pedidos, device.mesaNum)
      carrinho.confirmarPedido()
      dialogConfirmar.value = false
      mostrarSnackbar('✅ Pedido enviado para a cozinha!', 'success')
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

  function scrollToTop () {
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  function mostrarSnackbar (text, color = 'success') {
    snackbar.text = text
    snackbar.color = color
    snackbar.show = true
  }
</script>
