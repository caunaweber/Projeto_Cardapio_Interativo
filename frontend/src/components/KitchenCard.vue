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
        <div class="font-weight-bold text-subtitle-1">
          <v-icon class="mr-1 text-primary" size="18">mdi-table-furniture</v-icon>
          Mesa {{ mesa }}
        </div>
        <div class="text-body-2 text-medium-emphasis">
          <span class="text-small">Comanda </span>#{{ num }}
        </div>
      </div>

      <div class="flex-grow-1 text-center">
        <div class="d-inline-flex align-center px-3 py-1 rounded-xl" :class="statusClass" style="background-color: rgba(0,0,0,0.05)">
          <v-icon class="mr-1" size="16">mdi-information-outline</v-icon>
          {{ status }}
        </div>
      </div>

      <div class="d-flex align-center justify-end">
        <v-tooltip location="bottom" text="Editar comanda">
          <template #activator="{ props: tooltipProps }">
            <v-btn
              v-bind="tooltipProps"
              class="mr-2"
              color="info"
              icon
              size="small"
              variant="text"
              @click.stop="$emit('edit')"
            >
              <v-icon size="18">mdi-pencil-outline</v-icon>
            </v-btn>
          </template>
        </v-tooltip>

        <v-tooltip location="bottom" text="Excluir comanda">
          <template #activator="{ props: tooltipProps }">
            <v-btn
              v-bind="tooltipProps"
              class="mr-2"
              color="error"
              icon
              size="small"
              variant="text"
              @click.stop="$emit('delete')"
            >
              <v-icon size="18">mdi-delete-outline</v-icon>
            </v-btn>
          </template>
        </v-tooltip>

        <v-btn
          v-if="status === 'A Entregar'"
          :color="actionColorTheme"
          rounded="xl"
          size="small"
          variant="flat"
          @click="$emit('action')"
        >
          {{ actionText }}
        </v-btn>
      </div>
    </div>

    <v-divider class="my-2" />

    <div class="items-columns flex-grow-1" :style="columnsStyle">
      <div v-for="(item, i) in itens" :key="i" class="item-text">
        {{ item.nome }} <span class="text-medium-emphasis">{{ item.qtd }}x</span>
      </div>
    </div>

    <v-divider class="my-2" />

    <div class="mt-auto d-flex justify-end align-center">
      <v-icon class="mr-1 text-primary" size="20">mdi-timer-outline</v-icon>
      <span class="font-weight-bold">{{ tempo }}</span>
    </div>
  </v-card>
</template>

<script setup>
  import { computed } from 'vue'
  import { useTheme } from 'vuetify'

  defineEmits(['action', 'edit', 'delete'])

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
      default: {
        return 'text-body-2'
      }
    }
  })

  const columnsStyle = computed(() => {
    const totalItens = props.itens.length
    const maxRows = 6
    const colCount = Math.ceil(totalItens / maxRows) || 1
    return { columnCount: colCount, columnGap: '0.5rem' }
  })
</script>

<style scoped>
.menu-card-border {
  border: 2px solid rgba(var(--v-theme-primary), 0.6);
  transition: 0.25s ease;
}

.menu-card-border:hover {
  box-shadow: 0 4px 10px rgba(var(--v-theme-primary), 0.25);
  transform: translateY(-2px);
}

.items-columns {
  max-height: 8.5em;
  overflow: hidden;
}

.item-text {
  display: block;
  margin-bottom: 0.3rem;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  font-size: 0.9rem;
}
</style>
