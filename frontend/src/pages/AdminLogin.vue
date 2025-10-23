<template>
  <v-container
    class="d-flex align-center justify-center login-background"
    fluid
    style="height: 100vh;"
  >
    <v-card class="pa-10 rounded-xl elevation-6 login-card" width="400">
      <div class="text-center mb-6">
        <v-avatar color="primary" size="64">
          <v-icon large>mdi-shield-account</v-icon>
        </v-avatar>
      </div>
      <h2 class="text-center mb-6 text-h4" style="color: var(--v-theme-primary)">
        🔒 login
      </h2>

      <v-form ref="formRef" @submit.prevent="fazerLogin">
        <v-text-field
          v-model="email"
          class="mb-4"
          label="Email"
          prepend-inner-icon="mdi-account"
          required
          rounded="xl"
          type="text"
          variant="outlined"
        />

        <v-text-field
          v-model="senha"
          class="mb-6"
          label="Senha"
          prepend-inner-icon="mdi-lock"
          required
          rounded="xl"
          type="password"
          variant="outlined"
        />

        <v-btn
          block
          class="login-btn"
          color="primary"
          rounded="xl"
          type="submit"
        >
          Entrar
        </v-btn>
      </v-form>

      <v-alert
        v-if="erro"
        class="mt-6 rounded-lg"
        dense
        prominent
        type="error"
      >
        {{ erro }}
      </v-alert>
    </v-card>
  </v-container>
</template>

<script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { useAuthStore } from '@/stores/authStore'

  const router = useRouter()
  const email = ref('')
  const senha = ref('')
  const erro = ref('')

  const auth = useAuthStore()

  function fazerLogin () {
    if (auth.login(email.value, senha.value)) {
      router.push({ name: 'AdminMetricas' })
    } else {
      erro.value = 'Email ou senha incorretos.'
    }
  }
</script>

<style scoped>
.login-background {
  background: linear-gradient(to right, #f5f7fa, #c3cfe2);
}

.login-card {
  animation: fadeIn 0.6s ease-in-out;
}

.login-btn {
  transition: all 0.3s ease;
}
.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 15px rgba(0,0,0,0.15);
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
