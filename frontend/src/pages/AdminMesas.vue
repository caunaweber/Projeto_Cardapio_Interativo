<template>
  <v-container fluid>
    <MenuAppBar title="🍳 Gerenciamento - Pratos" @toggle-drawer="drawer = !drawer">
      <template #left>
        <v-btn icon @click="drawer = !drawer">
          <v-icon>mdi-menu</v-icon>
        </v-btn>

      </template>

      <template #actions>
        <v-btn
          color="success"
          rounded="xl"
          variant="flat"
          @click="abrirDialog()"
        >
          ➕ Novo Prato
        </v-btn>
      </template>
    </MenuAppBar>

    <NavSidebar v-model="drawer" :pagina-atual="'mesas'" @logout="logout" />
  </v-container>
</template>

<script setup>
  import { ref } from 'vue'
  import MenuAppBar from '@/components/MenuAppBar.vue'
  import NavSidebar from '@/components/NavSidebar.vue'

  const drawer = ref(false)

  function logout () {
    // Lógica de logout aqui
    console.log('Logout acionado')
  }
</script>
