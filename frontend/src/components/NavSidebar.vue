<template>
  <v-navigation-drawer
    v-model="drawer"
    app
    class="elevation-2"
    color="surface"
    :rail="mini"
    temporary
  >
    <v-list-item class="py-4">
      <v-list-item-title v-if="!mini" class="font-weight-bold">
        Administração
      </v-list-item-title>
    </v-list-item>

    <v-divider />

    <v-list density="comfortable" nav>
      <v-list-item
        v-for="link in links"
        :key="link.value"
        :prepend-icon="link.icon"
        rounded="xl"
        :title="!mini ? link.title : ''"
        :to="link.to"
      />
    </v-list>

    <template #append>
      <v-divider />
      <v-list-item
        prepend-icon="mdi-logout"
        rounded="xl"
        title="Sair"
        @click="handleLogout"
      />
    </template>
  </v-navigation-drawer>
</template>

<script setup>
  import { ref, watch } from 'vue'
  import { useRouter } from 'vue-router'

  const router = useRouter()

  const props = defineProps({
    paginaAtual: String,
    modelValue: Boolean,
  })

  const emit = defineEmits(['update:modelValue', 'logout'])

  const drawer = ref(props.modelValue)
  watch(() => props.modelValue, val => drawer.value = val)
  watch(drawer, val => emit('update:modelValue', val))

  const mini = ref(false)

  const links = [
    { title: 'Gerenciamento de Pratos', value: 'pratos', to: '/admin/pratos', icon: 'mdi-silverware-fork-knife' },
    { title: 'Gerenciamento de Mesas', value: 'mesas', to: '/admin/mesas', icon: 'mdi-table-chair' },
    { title: 'Métricas de administração', value: 'metricas', to: '/admin/metricas', icon: 'mdi-chart-bar' },
  ]

  function handleLogout () {
    router.push('/login')
  }
</script>
