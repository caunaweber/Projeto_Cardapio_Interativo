<template>
  <v-card
    class="pa-3 mb-4 menu-card-border d-flex flex-column"
    :color="cardColor"
    :elevation="elevation"
    rounded="xl"
    style="min-height: 260px"
  >
    <div class="d-flex justify-space-between align-center mb-2">
      <div>
        <div class="font-weight-bold text-subtitle-1">Mesa {{ mesa }}</div>
        <div class="text-body-2 text-medium-emphasis">
          <span class="text-small">Comanda </span>#{{ num }}
        </div>
      </div>

      <div :class="statusClass">{{ status }}</div>

      <v-btn
        :color="actionColorTheme"
        rounded="xl"
        size="small"
        variant="flat"
        @click="$emit('action')"
      >
        {{ actionText }}
      </v-btn>
    </div>

    <v-divider />

    <div class="items-columns flex-grow-1" :style="columnsStyle">
      <div
        v-for="(item, i) in itens"
        :key="i"
        class="item-text"
      >
        {{ item.qtd }}x {{ item.nome }}
      </div>
    </div>

    <v-divider class="my-2" />

    <div class="mt-auto d-flex justify-end align-center">
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
      default: 3,
    },
  })

  const cardColor = computed(() => {
    const isDark = theme.global.current.value.dark
    if (!isDark) return '#fdfdd7ff'
    const darkToken = props.bgColor || 'surface'
    return theme.global.current.value.colors[darkToken] || darkToken
  })

  const actionColorTheme = computed(
    () => theme.global.current.value.colors[props.actionColor] || props.actionColor,
  )

  const statusClass = computed(() => {
    switch (props.status) {
      case 'A Entregar': {
        return 'text-warning font-weight-bold'
      }
      case 'Entregue': {
        return 'text-success font-weight-bold'
      }
      case 'Em Preparo': {
        return 'text-primary font-weight-bold'
      }
      default: {
        return 'text-body-2'
      }
    }
  })

  const columnsStyle = computed(() => {
    const totalItens = props.itens.length
    const maxRows = 6
    const colCount = Math.ceil(totalItens / maxRows) || 1
    return {
      columnCount: colCount,
      columnGap: '0.5rem',
    }
  })
</script>

<style scoped>
.menu-card-border {
  border: 2px solid rgba(var(--v-theme-primary), 0.75);
}

.items-columns {
  max-height: 8.5em;
  overflow: hidden;
}

.item-text {
  display: block;
  margin-bottom: 0.25rem;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
</style>
