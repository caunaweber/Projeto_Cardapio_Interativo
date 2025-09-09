<template>
  <div class="chart-container">
    <Pie :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup>
  import { ArcElement, Chart as ChartJS, Legend, Title, Tooltip } from 'chart.js'
  import { Pie } from 'vue-chartjs'

  ChartJS.register(Title, Tooltip, Legend, ArcElement)

  const props = defineProps({ data: Object })
  const chartData = props.data

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { position: 'bottom' },
    },
  }
</script>

<style scoped>
.chart-container {
  height: 250px;
}
</style>
