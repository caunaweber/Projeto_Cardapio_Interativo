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
