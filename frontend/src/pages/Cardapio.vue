<template>
  <v-app>
    <v-container class="pa-0" fluid>
      <v-app-bar
        app
        class="px-4"
        color="primary"
        :elevation="2"
        flat
        rounded="lg"
      >
        <v-app-bar-title class="font-weight-bold text-h5">🍴 Restaurante</v-app-bar-title>
        <v-spacer />
        <v-btn icon variant="text" @click="toggleTheme">
          <v-icon size="26">{{ theme.global.current.value.dark ? 'mdi-weather-sunny' : 'mdi-weather-night' }}</v-icon>
        </v-btn>
        <v-btn
          class="ml-3 font-weight-bold"
          color="secondary"
          rounded="xl"
          variant="flat"
          @click="drawer = !drawer"
        >
          Meus pedidos
        </v-btn>
      </v-app-bar>

      <v-navigation-drawer v-model="drawer" location="right" temporary width="350">
        <v-card flat>
          <v-card-title class="font-weight-bold text-h6">
            🛒 Meus Pedidos
          </v-card-title>
          <v-divider />

          <v-list>
            <v-list-item v-for="produto in pedidos" :key="produto.id">
              <template #prepend>
                <v-avatar size="40">
                  <v-img :src="produto.imagem" />
                </v-avatar>
              </template>

              <v-list-item-title>
                {{ produto.nome }}
              </v-list-item-title>
              <v-list-item-subtitle>
                {{ produto.qtd }}x - R$ {{ (produto.qtd * produto.preco).toFixed(2) }}
              </v-list-item-subtitle>

              <template #append>
                <v-btn
                  color="error"
                  icon
                  size="small"
                  variant="text"
                  @click="remover(produto)"
                >
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </template>
            </v-list-item>
          </v-list>

          <v-divider />

          <v-card-text class="d-flex justify-space-between font-weight-bold">
            <span>Total:</span>
            <span>R$ {{ totalPreco.toFixed(2) }}</span>
          </v-card-text>

          <v-card-actions>
            <v-btn
              block
              color="secondary"
              rounded="xl"
              size="large"
              variant="flat"
              @click="dialogConfirmar = true"
            >
              Finalizar pedido
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-navigation-drawer>

      <v-main>
        <v-container class="py-8">

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
                <v-card
                  class="h-100 d-flex flex-column"
                  :class="['transition-fast', 'pa-2']"
                  elevation="6"
                  hover
                  rounded="xl"
                >
                  <v-img
                    :alt="produto.nome"
                    class="rounded-xl"
                    cover
                    height="180"
                    :src="produto.imagem"
                  />

                  <v-card-title class="text-center font-weight-bold mt-2">
                    {{ produto.nome }}
                  </v-card-title>

                  <v-card-subtitle class="text-center text-medium-emphasis">
                    R$ {{ produto.preco.toFixed(2) }}
                  </v-card-subtitle>

                  <v-spacer />

                  <v-card-actions class="justify-center pb-3">
                    <v-btn
                      color="error"
                      icon
                      size="small"
                      variant="outlined"
                      @click="remover(produto)"
                    >
                      <v-icon>mdi-minus</v-icon>
                    </v-btn>

                    <span class="mx-3 font-weight-bold text-subtitle-1">{{ produto.qtd }}</span>

                    <v-btn
                      color="success"
                      icon
                      size="small"
                      variant="flat"
                      @click="adicionar(produto)"
                    >
                      <v-icon>mdi-plus</v-icon>
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-col>
            </v-row>
          </div>
        </v-container>
      </v-main>

      <v-footer
        app
        class="pa-2"
        color="primary"
        dark
        elevation="8"
      >
        <div class="text-body-1">
          🛒 Itens: <strong>{{ totalItens }}</strong> |
          💰 Total: <strong>R$ {{ totalPreco.toFixed(2) }}</strong>
        </div>
        <v-spacer />
        <v-btn
          class="font-weight-bold"
          color="secondary"
          rounded="xl"
          size="large"
          variant="flat"
          @click="dialogConfirmar = true"
        >
          Finalizar pedido
        </v-btn>
      </v-footer>

      <v-dialog v-model="dialogConfirmar" max-width="500">
        <v-card rounded="xl">
          <v-card-title class="text-h6 font-weight-bold">
            ✅ Confirmar Pedido
          </v-card-title>

          <v-divider />

          <v-card-text>
            <v-list>
              <v-list-item v-for="produto in pedidos" :key="produto.id">
                <v-list-item-title>
                  {{ produto.nome }}
                </v-list-item-title>
                <v-list-item-subtitle>
                  {{ produto.qtd }}x - R$ {{ (produto.qtd * produto.preco).toFixed(2) }}
                </v-list-item-subtitle>
              </v-list-item>
            </v-list>

            <v-divider class="my-2" />

            <div class="text-right font-weight-bold text-subtitle-1">
              Total: R$ {{ totalPreco.toFixed(2) }}
            </div>
          </v-card-text>

          <v-card-actions class="d-flex justify-end">
            <v-btn variant="text" @click="dialogConfirmar = false">Cancelar</v-btn>
            <v-btn color="success" rounded="xl" @click="confirmarPedido">
              Confirmar
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

    </v-container>
  </v-app>
</template>

<script setup>
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
