// js/dashboard.js
const statsContainer = document.getElementById("dashboard-stats");
const revenueChart = document.getElementById("revenue-chart");

export function renderDashboard() {
  statsContainer.innerHTML = "";
  revenueChart.innerHTML = "";

  // Fake stats cards
  const stats = [
    { label: "Doanh thu hôm nay", value: "16.840.000 đ", icon: "fa-sack-dollar", color: "#10b981" },
    { label: "Đơn hàng hôm nay", value: "84", icon: "fa-receipt", color: "#3b82f6" },
    { label: "Khách hàng mới", value: "12", icon: "fa-user-plus", color: "#8b5cf6" },
    { label: "Sản phẩm bán chạy", value: "Trà sữa trân châu", icon: "fa-mug-hot", color: "#f59e0b" },
  ];

  stats.forEach(stat => {
    const card = document.createElement("div");
    card.className = "stat-card";
    card.innerHTML = `
      <div class="stat-icon" style="background-color: ${stat.color}22; color: ${stat.color};"><i class="fa-solid ${stat.icon}"></i></div>
      <div class="stat-info">
        <h3>${stat.value}</h3>
        <p>${stat.label}</p>
      </div>
    `;
    statsContainer.appendChild(card);
  });

  // Fake chart
  const chartData = [12,15,11,18,14,16,20].map((rev, i) => ({
    day: ["T2","T3","T4","T5","T6","T7","CN"][i],
    revenue: rev * 1000000
  }));

  chartData.forEach(item => {
    const bar = document.createElement("div");
    bar.className = "chart-bar";
    bar.style.height = `${(item.revenue / 25000000) * 100}%`;
    bar.innerHTML = `<span class="bar-label">${item.day}</span><span class="bar-value">${(item.revenue/1000000).toFixed(1)}tr</span>`;
    revenueChart.appendChild(bar);
  });
}