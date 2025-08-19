<template>
  <v-container fluid>
    <MenuAppBar 
      :isDark="theme.global.current.value.dark" 
      @toggleTheme="toggleTheme" 
      @toggleDrawer="drawer = !drawer" 
    />

    <MenuSidebar
      v-model="drawer"
      :pedidos="pedidos"
      :totalPreco="totalPreco"
      @remover="remover"
      @finalizar="dialogConfirmar = true" 
    />

    <v-main>
      <v-container class="py-8">
        <div v-for="categoria in categorias" :key="categoria.nome" class="mb-10">
          <h2 class="mb-6 text-h5 font-weight-bold text-primary">{{ categoria.nome }}</h2>
          <v-row dense>
            <v-col v-for="produto in categoria.itens" :key="produto.id" cols="12" lg="3" md="4" sm="6">
              <MenuCard :produto="produto" @adicionar="adicionar" @remover="remover" />
            </v-col>
          </v-row>
        </div>
      </v-container>
    </v-main>

    <MenuFooter 
      :totalItens="totalItens" 
      :totalPreco="totalPreco" 
      @finalizar="dialogConfirmar = true" 
    />

    <MenuDialog 
      v-model="dialogConfirmar"
      :pedidos="pedidos"
      :totalPreco="totalPreco"
      @confirmar="confirmarPedido" 
    />
  </v-container>
</template>

<script setup>
import MenuFooter from '@/components/MenuFooter.vue'
import { computed, reactive } from 'vue'
import { useTheme } from 'vuetify'

const drawer = ref(false)

const categorias = reactive([
  {
    nome: 'Entradas',
    itens: [
      { id: 1, nome: 'Entrada 1', preco: 10, imagem: 'image.png', qtd: 0 },
      { id: 2, nome: 'Entrada 2', preco: 10, imagem: 'image.png', qtd: 0 },
      { id: 3, nome: 'Entrada 3', preco: 10, imagem: 'image.png', qtd: 0 },
      { id: 4, nome: 'Entrada 4', preco: 10, imagem: 'image.png', qtd: 0 },
    ],
  },
  {
    nome: 'Pratos principais',
    itens: [
      { id: 5, nome: 'Prato 1', preco: 30, imagem: 'image.png', qtd: 0 },
      { id: 6, nome: 'Prato 2', preco: 30, imagem: 'image.png', qtd: 0 },
    ],
  },
  {
    nome: 'Sobremesas',
    itens: [
      { id: 7, nome: 'Sobremesa 1', preco: 10, imagem: 'image.png', qtd: 0 },
      { id: 8, nome: 'Sobremesa 2', preco: 10, imagem: 'image.png', qtd: 0 },
    ],
  },
  {
    nome: 'Bebidas',
    itens: [
      { id: 9, nome: 'Bebida 1', preco: 5, imagem: 'suco.png', qtd: 0 },
      { id: 10, nome: 'Bebida 2', preco: 5, imagem: 'suco.png', qtd: 0 },
    ],
  },
])

const adicionar = produto => {
  produto.qtd++
}

const remover = produto => {
  if (produto.qtd > 0) produto.qtd--
}

const totalItens = computed(() =>
  categorias.reduce(
    (total, cat) => total + cat.itens.reduce((soma, p) => soma + p.qtd, 0),
    0,
  ),
)

const totalPreco = computed(() =>
  categorias.reduce(
    (total, cat) =>
      total + cat.itens.reduce((soma, p) => soma + p.qtd * p.preco, 0),
    0,
  ),
)

const pedidos = computed(() =>
  categorias.flatMap(cat => cat.itens.filter(p => p.qtd > 0)),
)

const dialogConfirmar = ref(false)

const confirmarPedido = () => {
  if (pedidos.value.length === 0) {
    alert('⚠️ Você não selecionou nenhum item!')
    return
  }
  alert('🍽️ Pedido enviado para a cozinha com sucesso!')

  for (const cat of categorias) {
    for (const p of cat.itens) (p.qtd = 0)
  }

  dialogConfirmar.value = false
  drawer.value = false
}

const theme = useTheme()

const toggleTheme = () => {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}
</script>

<style scoped>
.transition-fast {
  transition: all 0.2s ease-in-out;
}

.transition-fast:hover {
  transform: translateY(-4px);
}
</style>
