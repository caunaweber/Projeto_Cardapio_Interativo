<template>
  <v-container fluid>

    <MenuAppBar @toggle-drawer="drawer = !drawer" />

    <v-main>
      <v-container class="py-1">
        <div v-for="categoria in categorias" :key="categoria.nome" class="mb-10">
          <h2 class="mb-6 text-h5 font-weight-bold text-primary">{{ categoria.nome }}</h2>
          <v-row dense>
            <v-col
              v-for="produto in categoria.itens"
              :key="produto.id"
              cols="12"
              lg="3"
              md="4"
              sm="6"
            >
              <MenuCard :item="produto" @adicionar="adicionar" @remover="remover" />
            </v-col>
          </v-row>
        </div>
      </v-container>
    </v-main>

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
      <template #title>
        ✅ Confirmar Pedido
      </template>

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

    <MenuFooter :total-itens="totalItens" :total-preco="totalPreco" @action="dialogConfirmar = true" />

  </v-container>
</template>

<script setup>
  import { computed, reactive, ref } from 'vue'
  import MenuFooter from '@/components/MenuFooter.vue'

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

</script>

<style scoped>
.transition-fast {
  transition: all 0.2s ease-in-out;
}

.transition-fast:hover {
  transform: translateY(-4px);
}
</style>
