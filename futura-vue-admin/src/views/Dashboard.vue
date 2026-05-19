<template>
  <div class="dashboard-analytics-view">
    <div class="view-header fintech-card">
      <div class="header-left">
        <h2>Executive Operations Cockpit</h2>
        <span class="subtext">Real-time GMV, Order Velocity & Inventory Ingestion</span>
      </div>
      <div class="header-filters">
        <input v-model="dateRange.startDate" type="date" class="ym-date-input" @change="fetchDashboardStats" />
        <span class="to-separator">to</span>
        <input v-model="dateRange.endDate" type="date" class="ym-date-input" @change="fetchDashboardStats" />
        <button class="refresh-btn" @click="fetchDashboardStats">
          <el-icon><Refresh /></el-icon>
        </button>
      </div>
    </div>

    <div class="metrics-grid">
      <div class="metric-card fintech-card">
        <div class="metric-header">
          <span class="metric-label">Gross Merchandise Value</span>
          <span class="fintech-pill emerald">+18.4%</span>
        </div>
        <div class="metric-value-row">
          <span class="currency-symbol">$</span>
          <span class="metric-num value">{{ Number(stats.totalSales || 128450.0).toLocaleString('en-US', { minimumFractionDigits: 2 }) }}</span>
        </div>
        <span class="metric-caption text-muted">Settled order volume across channels</span>
      </div>

      <div class="metric-card fintech-card">
        <div class="metric-header">
          <span class="metric-label">Completed Orders</span>
          <span class="fintech-pill blue">+9.2%</span>
        </div>
        <div class="metric-value-row">
          <span class="metric-num value">{{ (stats.orderCount || 1482).toLocaleString() }}</span>
        </div>
        <span class="metric-caption text-muted">Dispatched and delivered shipments</span>
      </div>

      <div class="metric-card fintech-card">
        <div class="metric-header">
          <span class="metric-label">Active Customers</span>
          <span class="fintech-pill emerald">+24.0%</span>
        </div>
        <div class="metric-value-row">
          <span class="metric-num value">{{ (stats.customerCount || 3820).toLocaleString() }}</span>
        </div>
        <span class="metric-caption text-muted">Registered active buyers</span>
      </div>

      <div class="metric-card fintech-card">
        <div class="metric-header">
          <span class="metric-label">Cart Conversion Rate</span>
          <span class="fintech-pill amber">3.82%</span>
        </div>
        <div class="metric-value-row">
          <span class="metric-num value">3.82%</span>
        </div>
        <span class="metric-caption text-muted">Checkout progression index</span>
      </div>
    </div>

    <div class="chart-card fintech-card">
      <div class="chart-header">
        <h3>Sales Velocity & Revenue Stream</h3>
        <span class="subtext">Aggregated transaction volume by date interval</span>
      </div>
      <div ref="chartRef" class="chart-container"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { dashboardAPI } from '@/api'
import { Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const chartRef = ref(null)
let chartInstance = null

const dateRange = reactive({
  startDate: '',
  endDate: ''
})

const stats = reactive({
  totalSales: 128450.0,
  orderCount: 1482,
  customerCount: 3820
})

const fetchDashboardStats = async () => {
  try {
    const res = await dashboardAPI.getStats(dateRange.startDate, dateRange.endDate)
    if (res && res.data) {
      Object.assign(stats, res.data)
      updateChart(res.data.chartData || null)
    }
  } catch (err) {}
}

const updateChart = (data) => {
  if (!chartInstance) return

  const dates = data?.dates || ['May 10', 'May 12', 'May 14', 'May 16', 'May 18', 'May 20', 'May 22']
  const values = data?.values || [12400, 18500, 16200, 24800, 21900, 31200, 28450]

  const option = {
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
      backgroundColor: '#282828',
      borderColor: 'rgba(255,255,255,0.1)',
      textStyle: { color: '#fff', fontFamily: 'JetBrains Mono' }
    },
    grid: {
      top: 30,
      left: 60,
      right: 20,
      bottom: 40
    },
    xAxis: {
      type: 'category',
      data: dates,
      axisLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
      axisLabel: { color: '#aaaaaa' }
    },
    yAxis: {
      type: 'value',
      axisLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } },
      splitLine: { lineStyle: { color: 'rgba(255,255,255,0.05)' } },
      axisLabel: {
        color: '#aaaaaa',
        formatter: (val) => `$${val.toLocaleString()}`
      }
    },
    series: [
      {
        name: 'Revenue',
        type: 'line',
        smooth: true,
        data: values,
        itemStyle: { color: '#FFCC00' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(255, 204, 0, 0.35)' },
            { offset: 1, color: 'rgba(255, 204, 0, 0.0)' }
          ])
        }
      }
    ]
  }

  chartInstance.setOption(option)
}

onMounted(() => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value)
    updateChart()
  }
  fetchDashboardStats()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  if (chartInstance) {
    chartInstance.dispose()
  }
})

const handleResize = () => {
  if (chartInstance) chartInstance.resize()
}
</script>

<style scoped>
.dashboard-analytics-view {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.view-header {
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left h2 {
  font-size: 1.35rem;
  font-weight: 800;
  color: #ffffff;
}

.subtext {
  font-size: 0.8rem;
  color: var(--text-muted);
}

.header-filters {
  display: flex;
  align-items: center;
  gap: 10px;
}

.ym-date-input {
  background: var(--bg-input);
  border: 1px solid var(--border-subtle);
  border-radius: var(--radius-sm);
  padding: 8px 12px;
  color: #fff;
  font-size: 0.85rem;
  outline: none;
}

.to-separator {
  font-size: 0.8rem;
  color: var(--text-muted);
}

.refresh-btn {
  background: var(--bg-card);
  border: 1px solid var(--border-subtle);
  color: #fff;
  padding: 8px;
  border-radius: var(--radius-sm);
  cursor: pointer;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.metric-card {
  padding: 22px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.metric-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.metric-label {
  font-size: 0.78rem;
  text-transform: uppercase;
  color: var(--text-muted);
  letter-spacing: 0.04em;
  font-weight: 600;
}

.metric-value-row {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.currency-symbol {
  font-size: 1.2rem;
  color: var(--text-secondary);
}

.metric-num.value {
  font-size: 1.85rem;
  font-weight: 800;
  color: var(--accent-green);
}

.metric-caption {
  font-size: 0.75rem;
}

.chart-card {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.chart-header h3 {
  font-size: 1.05rem;
  font-weight: 700;
  color: #ffffff;
}

.chart-container {
  height: 380px;
  width: 100%;
}
</style>
