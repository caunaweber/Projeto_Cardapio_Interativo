<template>
  <v-card class="pa-3 mb-3 menu-card-border" :color="bgColorTheme" :elevation="elevation" rounded="xl">
    <div class="d-flex justify-space-between align-center mb-2">

      <div class="d-flex align-center gap-2">
        <div class="font-weight-bold text-subtitle-1 me-4">
          Mesa {{ mesa }}
        </div>
        <div class="text-body-2 text-medium-emphasis">
          <span class="text-small">comanda </span>#{{ num }}
        </div>
      </div>

      <div :class="statusClass">
        {{ status }}
      </div>

      <v-btn
        :color="actionColorTheme"
        rounded="lg"
        size="small"
        variant="flat"
        @click="$emit('action')"
      >
        {{ actionText }}
      </v-btn>
    </div>

    <v-divider />

    <v-list density="compact">
      <v-list-item v-for="(item, i) in itens" :key="i">
        <v-list-item-title>
          {{ item.qtd }}x {{ item.nome }}
        </v-list-item-title>
      </v-list-item>
    </v-list>

    <v-divider class="my-2" />

    <div class="d-flex justify-end align-center">
      <span class="mr-2 font-weight-bold">{{ tempo }}</span>
      <v-icon size="20">mdi-timer-outline</v-icon>
    </div>
  </v-card>
</template>

<script setup>
  import { computed } from 'vue'
  import { useTheme } from 'vuetify'

  defineEmits(['action'])

  const theme = useTheme()

  const props = defineProps({
    mesa: [String, Number],
    status: String,
    num: [String, Number],
    itens: {
      type: Array,
      default: () => [],
    },
    tempo: String,
    actionText: {
      type: String,
      default: 'Finalizar',
    },
    actionColor: {
      type: String,
      default: 'success',
    },
    bgColor: {
      type: String,
      default: 'surface',
    },
    elevation: {
      type: [String, Number],
      default: 2,
    },
  })

  const bgColorTheme = computed(() => theme.global.current.value.colors[props.bgColor] || props.bgColor)
  const actionColorTheme = computed(() => theme.global.current.value.colors[props.actionColor] || props.actionColor)

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

<style>
.menu-card-border {
  border: 2px solid rgba(var(--v-theme-primary), 0.75);
}
</style>
