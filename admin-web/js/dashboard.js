// admin-web/js/dashboard.js
import { db } from "./firebase-config.js";
import { collection, onSnapshot } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

let allProducts = [];
let allOrders = [];

// 1. KHỞI ĐỘNG CÁC BỘ LẮNG NGHE
function initRealtimeDashboard() {
  
  // Lắng nghe Sản phẩm
  onSnapshot(collection(db, "products"), (snapshot) => {
    allProducts = snapshot.docs.map(doc => doc.data());
    updateStat("statProducts", snapshot.size);
    if (allOrders.length > 0) calculateAndRenderBestSellers(allOrders, allProducts);
  });

  // Lắng nghe Users
  onSnapshot(collection(db, "users"), (snapshot) => {
    const users = snapshot.docs.map(doc => doc.data());
    const totalCustomers = users.filter(u => u.role !== 'admin').length;
    updateStat("statCustomers", totalCustomers);
  });

  // Lắng nghe Orders
  onSnapshot(collection(db, "orders"), (snapshot) => {
    allOrders = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));

    updateStat("statOrders", snapshot.size);

    const totalRevenue = allOrders.reduce((sum, order) => {
      if (order.status === 'completed') {
        return sum + Number(order.totalPrice || 0);
      }
      return sum;
    }, 0);
    updateStat("statRevenue", new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(totalRevenue));

    renderRecentOrders(allOrders);
    if (allProducts.length > 0) calculateAndRenderBestSellers(allOrders, allProducts);
  });
}

function updateStat(id, value) {
  const el = document.getElementById(id);
  if (el) el.innerText = value;
}

// 2. BẢNG ĐƠN HÀNG GẦN ĐÂY (Có Thời gian thật)
function renderRecentOrders(orders) {
  const tableBody = document.getElementById("recentOrdersBody");
  if (!tableBody) return;

  tableBody.innerHTML = "";

  const sortedOrders = [...orders].sort((a, b) => {
    const timeA = a.createdAt || "";
    const timeB = b.createdAt || "";
    return timeB.localeCompare(timeA); 
  });

  const recentOrders = sortedOrders.slice(0, 5);

  if (recentOrders.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="4" style="text-align:center; padding:1rem; color:#888;">Chưa có đơn hàng nào</td></tr>`;
    return;
  }

  recentOrders.forEach(order => {
    const price = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalPrice || 0);
    
    let statusBadge = `<span class="badge badge-warning">Chờ</span>`;
    if (order.status === 'processing') statusBadge = `<span class="badge badge-info">Làm</span>`;
    if (order.status === 'completed') statusBadge = `<span class="badge badge-success">Xong</span>`;
    if (order.status === 'cancelled') statusBadge = `<span class="badge badge-danger">Hủy</span>`;

    // Lấy thời gian từ Firebase (createdAt)
    // Nếu chưa có thì hiện "Vừa xong"
    const timeDisplay = order.createdAt || "Vừa xong";

    const row = document.createElement("tr");
    row.innerHTML = `
      <td><b>...${order.id.slice(-5)}</b><br><span style="font-size:0.75rem; color:#888;">${timeDisplay}</span></td>
      <td>${order.customerName || "Khách lẻ"}</td>
      <td style="font-weight:600;">${price}</td>
      <td>${statusBadge}</td>
    `;
    tableBody.appendChild(row);
  });
}

// 3. TOP BÁN CHẠY (Đã bỏ chữ "ly")
function calculateAndRenderBestSellers(orders, products) {
  const listContainer = document.getElementById("bestSellerList");
  if (!listContainer) return;

  listContainer.innerHTML = "";

  const salesMap = {};

  orders.forEach(order => {
    if (order.status === 'completed' && Array.isArray(order.items)) {
      order.items.forEach(item => {
        const name = item.name;
        const qty = item.quantity || 1;
        if (!salesMap[name]) {
          salesMap[name] = { qty: 0, name: name };
        }
        salesMap[name].qty += qty;
      });
    }
  });

  let sortedSales = Object.values(salesMap).sort((a, b) => b.qty - a.qty);
  const top5 = sortedSales.slice(0, 5);

  if (top5.length === 0) {
    listContainer.innerHTML = `<div style="text-align:center; color:#888; padding:1rem;">Chưa có dữ liệu bán hàng</div>`;
    return;
  }

  top5.forEach((item, index) => {
    const productInfo = products.find(p => p.name === item.name);
    const imgUrl = productInfo ? productInfo.imageUrl : 'assets/logo.png';
    const rankClass = index === 0 ? "rank-1" : (index === 1 ? "rank-2" : (index === 2 ? "rank-3" : ""));

    const div = document.createElement("div");
    div.className = "best-seller-item";
    div.innerHTML = `
      <div class="rank-number ${rankClass}">#${index + 1}</div>
      <img src="${imgUrl}" class="user-avatar" style="border-radius: 8px; width: 45px; height: 45px;" onerror="this.src='assets/logo.png'">
      <div class="item-info">
        <span class="item-name">${item.name}</span>
        <span class="item-price" style="color: #6b7280; font-size: 0.85rem;">Đã bán: ${item.qty}</span> </div>
      <div class="sold-count" style="background: #f3f4f6; color: #374151;">Top ${index+1}</div>
    `;
    listContainer.appendChild(div);
  });
}

initRealtimeDashboard();