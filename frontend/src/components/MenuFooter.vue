<template>
  <div class="menu-footer">

    <div class="footer-card content-card rounded-te-lg">
      <slot name="content" />
    </div>
    <div class="actions-card rounded-ts-lg">
      <slot name="actions" />
    </div>

  </div>
</template>

<script setup>
  defineProps({
    totalItens: { type: Number, default: 0 },
    totalPreco: { type: Number, default: 0 },
  })
  defineEmits(['action'])
</script>

<style scoped>
.menu-footer {
  position: fixed;
  left: 0;
  right: 0;
  pointer-events: none;
}

.footer-card {
  pointer-events: all;
  padding: 5px 8px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
  display: inline-flex;
  align-items: center;
  font-weight: bold;
}

.content-card {
  position: fixed;
  bottom: 0px;
  left: 0px;
}

.actions-card {
  pointer-events: all;
  display: inline-flex;
  align-items: center;
  font-weight: bold;
  position: fixed;
  bottom: 20px;
  right: 10px;
  padding: 5px 8px;
}

</style>
