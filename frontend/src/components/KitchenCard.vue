<template>
  <v-card class="pa-3 mb-3" :color="bgColorTheme" :elevation="elevation" rounded="xl">
    <div class="d-flex justify-space-between align-center mb-2">
      <div class="font-weight-bold text-subtitle-1">
        Mesa {{ mesa }}
      </div>

      <div :class="statusClass">
        {{ status }}
      </div>

      <v-btn
        :color="actionColorTheme"
        rounded="lg"
        size="small"
        variant="flat"
      >
        {{ actionText }}
      </v-btn>
    </div>

    <v-divider />

    <!-- Itens da comanda -->
    <v-list density="compact">
      <v-list-item v-for="(item, i) in itens" :key="i">
        <v-list-item-title>
          {{ item.qtd }}x {{ item.nome }}
        </v-list-item-title>
      </v-list-item>
    </v-list>

    <v-divider class="my-2" />

    <!-- Rodapé -->
    <div class="d-flex justify-end align-center">
      <span class="mr-2 font-weight-bold">{{ tempo }}</span>
      <v-icon size="20">mdi-timer-outline</v-icon>
    </div>
  </v-card>
</template>

<script setup>
  import { computed } from 'vue'
  import { useTheme } from 'vuetify'

  const theme = useTheme()

  const props = defineProps({
    mesa: [String, Number],
    status: String,
    itens: {
      type: Array,
      default: () => [],
    },
    tempo: String,
    actionText: {
      type: String,
      default: 'Finalizado',
    },
    actionColor: {
      type: String,
      default: 'success', // cor do tema
    },
    bgColor: {
      type: String,
      default: 'surface', // cor base do card, respeita tema
    },
    elevation: {
      type: [String, Number],
      default: 2,
    },
  })

  // Pega a cor do tema diretamente
  const bgColorTheme = computed(() => theme.global.current.value.colors[props.bgColor] || props.bgColor)
  const actionColorTheme = computed(() => theme.global.current.value.colors[props.actionColor] || props.actionColor)

  // Classe dinâmica para status
  const statusClass = computed(() => {
    switch (props.status) {
      case 'A Entregar': { return 'text-warning font-weight-bold'
      }
      case 'Entregue': { return 'text-success font-weight-bold'
      }
      case 'Em Preparo': { return 'text-primary font-weight-bold'
      }
      default: { return 'text-body-2'
      }
    }
  })
</script>
