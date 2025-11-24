// admin-web/js/dashboard.js
import { db } from "./firebase-config.js";
import { collection, onSnapshot } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

let allProducts = [];
let allOrders = [];

function formatDate(timestamp) {
  if (!timestamp) return "Vừa xong";
  if (timestamp.toDate) {
    // Chỉ lấy giờ phút cho gọn ở dashboard
    return timestamp.toDate().toLocaleString('vi-VN', { 
        hour: '2-digit', minute: '2-digit', day: '2-digit', month: '2-digit'
    });
  }
  return new Date(timestamp).toLocaleString('vi-VN');
}

// 1. KHỞI ĐỘNG CÁC BỘ LẮNG NGHE
function initRealtimeDashboard() {
  
  // Lắng nghe Sản phẩm (để lấy ảnh nếu cần, tuy nhiên order mới đã có ảnh rồi)
  onSnapshot(collection(db, "products"), (snapshot) => {
    allProducts = snapshot.docs.map(doc => doc.data());
    updateStat("statProducts", snapshot.size);
    if (allOrders.length > 0) calculateAndRenderBestSellers(allOrders);
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

    // Tính doanh thu (chỉ tính đơn COMPLETED)
    const totalRevenue = allOrders.reduce((sum, order) => {
      const status = order.status ? order.status.toUpperCase() : "";
      if (status === 'COMPLETED') {
        return sum + Number(order.total || 0); // Dùng field 'total'
      }
      return sum;
    }, 0);
    
    updateStat("statRevenue", new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(totalRevenue));

    renderRecentOrders(allOrders);
    calculateAndRenderBestSellers(allOrders);
  });
}

function updateStat(id, value) {
  const el = document.getElementById(id);
  if (el) el.innerText = value;
}

// 2. BẢNG ĐƠN HÀNG GẦN ĐÂY
function renderRecentOrders(orders) {
  const tableBody = document.getElementById("recentOrdersBody");
  if (!tableBody) return;

  tableBody.innerHTML = "";

  // Sort theo timestamp (seconds)
  const sortedOrders = [...orders].sort((a, b) => {
    const timeA = a.createdAt?.seconds || 0;
    const timeB = b.createdAt?.seconds || 0;
    return timeB - timeA;
  });

  const recentOrders = sortedOrders.slice(0, 5);

  if (recentOrders.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="4" style="text-align:center; padding:1rem; color:#888;">Chưa có đơn hàng nào</td></tr>`;
    return;
  }

  recentOrders.forEach(order => {
    const price = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.total || 0);
    const status = order.status ? order.status.toUpperCase() : "";
    
    let statusBadge = `<span class="badge badge-warning">Chờ</span>`;
    if (status === 'PROCESSING') statusBadge = `<span class="badge badge-info">Làm</span>`;
    if (status === 'COMPLETED') statusBadge = `<span class="badge badge-success">Xong</span>`;
    if (status === 'CANCELLED') statusBadge = `<span class="badge badge-danger">Hủy</span>`;

    const timeDisplay = formatDate(order.createdAt);
    const customerName = order.userName || "Khách lẻ";

    const row = document.createElement("tr");
    row.innerHTML = `
      <td><b>...${order.id.slice(-5)}</b><br><span style="font-size:0.75rem; color:#888;">${timeDisplay}</span></td>
      <td>${customerName}</td>
      <td style="font-weight:600;">${price}</td>
      <td>${statusBadge}</td>
    `;
    tableBody.appendChild(row);
  });
}

// 3. TOP BÁN CHẠY (Dựa trên items.productName và items.productImage của Order)
function calculateAndRenderBestSellers(orders) {
  const listContainer = document.getElementById("bestSellerList");
  if (!listContainer) return;

  listContainer.innerHTML = "";

  const salesMap = {};

  orders.forEach(order => {
    // Chỉ đếm đơn hoàn thành
    const status = order.status ? order.status.toUpperCase() : "";
    
    if (status === 'COMPLETED' && Array.isArray(order.items)) {
      order.items.forEach(item => {
        const name = item.productName; // Dữ liệu mới dùng productName
        const qty = item.quantity || 1;
        const img = item.productImage; // Dữ liệu mới có sẵn ảnh

        if (!salesMap[name]) {
          salesMap[name] = { qty: 0, name: name, image: img };
        }
        salesMap[name].qty += qty;
        // Cập nhật ảnh nếu chưa có (phòng hờ)
        if (!salesMap[name].image && img) salesMap[name].image = img;
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
    const imgUrl = item.image || 'assets/logo.png';
    const rankClass = index === 0 ? "rank-1" : (index === 1 ? "rank-2" : (index === 2 ? "rank-3" : ""));

    const div = document.createElement("div");
    div.className = "best-seller-item";
    div.innerHTML = `
      <div class="rank-number ${rankClass}">#${index + 1}</div>
      <img src="${imgUrl}" class="user-avatar" style="border-radius: 8px; width: 45px; height: 45px; object-fit: cover;" onerror="this.src='assets/logo.png'">
      <div class="item-info">
        <span class="item-name">${item.name}</span>
        <span class="item-price" style="color: #6b7280; font-size: 0.85rem;">Đã bán: ${item.qty}</span> </div>
      <div class="sold-count" style="background: #f3f4f6; color: #374151;">Top ${index+1}</div>
    `;
    listContainer.appendChild(div);
  });
}

initRealtimeDashboard();