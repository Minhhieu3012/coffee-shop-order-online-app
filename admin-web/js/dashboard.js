// admin-web/js/dashboard.js

// 1. DỮ LIỆU GIẢ BEST SELLER
const bestSellers = [
  { name: "Cà phê Đen Đá", price: 25000, sold: 150, img: "https://img.freepik.com/free-photo/iced-coffee-glass_144627-23578.jpg" },
  { name: "Bạc Xỉu", price: 29000, sold: 120, img: "https://img.freepik.com/free-photo/glass-coffee-with-milk_140725-5781.jpg" },
  { name: "Trà Đào Cam Sả", price: 35000, sold: 98, img: "https://img.freepik.com/free-photo/glass-peach-tea-with-ice-cubes_140725-4777.jpg" },
  { name: "Croissant", price: 45000, sold: 75, img: "https://img.freepik.com/free-photo/croissants-wooden-cutting-board_1150-24898.jpg" },
  { name: "Espresso", price: 22000, sold: 60, img: "assets/logo.png" }
];

// 2. DỮ LIỆU GIẢ ĐƠN MỚI (Lấy tạm vài cái)
const recentOrders = [
  { id: "DH005", customer: "Nguyễn Văn A", total: 55000, status: "pending" },
  { id: "DH004", customer: "Phạm Thị D", total: 35000, status: "processing" },
  { id: "DH003", customer: "Lê Văn C", total: 120000, status: "completed" }
];

function loadDashboardStats() {
  // --- Phần thống kê số liệu (Cũ) ---
  const stats = { revenue: 2550000, orders: 15, customers: 45, products: 28 };
  document.getElementById("statRevenue").innerText = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(stats.revenue);
  document.getElementById("statOrders").innerText = stats.orders;
  document.getElementById("statCustomers").innerText = stats.customers;
  document.getElementById("statProducts").innerText = stats.products;

  // --- Phần 1: Render Đơn hàng gần đây ---
  const recentBody = document.getElementById("recentOrdersBody");
  if (recentBody) {
    recentBody.innerHTML = "";
    recentOrders.forEach(order => {
      let statusBadge = `<span class="badge badge-warning">Chờ</span>`;
      if(order.status === 'processing') statusBadge = `<span class="badge badge-info">Làm</span>`;
      if(order.status === 'completed') statusBadge = `<span class="badge badge-success">Xong</span>`;
      
      const price = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.total);

      const row = document.createElement("tr");
      row.innerHTML = `
        <td><b>${order.id}</b></td>
        <td>${order.customer}</td>
        <td style="font-weight:600">${price}</td>
        <td>${statusBadge}</td>
      `;
      recentBody.appendChild(row);
    });
  }

  // --- Phần 2: Render Best Seller ---
  const bestSellerList = document.getElementById("bestSellerList");
  if (bestSellerList) {
    bestSellerList.innerHTML = "";
    bestSellers.forEach((item, index) => {
      const rankClass = index === 0 ? "rank-1" : (index === 1 ? "rank-2" : (index === 2 ? "rank-3" : ""));
      const imgUrl = item.img || 'assets/logo.png';
      
      const div = document.createElement("div");
      div.className = "best-seller-item";
      div.innerHTML = `
        <div class="rank-number ${rankClass}">#${index + 1}</div>
        <img src="${imgUrl}" class="user-avatar" style="border-radius: 8px;"> <div class="item-info">
          <span class="item-name">${item.name}</span>
          <span class="item-price">${item.sold} đã bán</span>
        </div>
        <div class="sold-count">+${item.sold}</div>
      `;
      bestSellerList.appendChild(div);
    });
  }
}

loadDashboardStats();
export { loadDashboardStats };