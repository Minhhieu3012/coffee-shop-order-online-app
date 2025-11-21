// admin-web/js/dashboard.js
import { db } from "./firebase-config.js";
import { collection, onSnapshot } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

// Biến toàn cục để lưu dữ liệu mới nhất (Dùng để tính toán chéo)
let allProducts = [];
let allOrders = [];

// ===================================================
// 1. KHỞI ĐỘNG CÁC BỘ LẮNG NGHE (REALTIME LISTENERS)
// ===================================================
function initRealtimeDashboard() {
  
  // --- LẮNG NGHE SẢN PHẨM (Products) ---
  onSnapshot(collection(db, "products"), (snapshot) => {
    // 1. Cập nhật biến toàn cục
    allProducts = snapshot.docs.map(doc => doc.data());
    
    // 2. Cập nhật số lượng trên thẻ
    updateStat("statProducts", snapshot.size);

    // 3. Tính toán lại Best Seller (Vì nhỡ đâu vừa sửa ảnh/tên món)
    if (allOrders.length > 0) calculateAndRenderBestSellers(allOrders, allProducts);
  });

  // --- LẮNG NGHE NGƯỜI DÙNG (Users) ---
  onSnapshot(collection(db, "users"), (snapshot) => {
    const users = snapshot.docs.map(doc => doc.data());
    // Chỉ đếm khách hàng
    const totalCustomers = users.filter(u => u.role !== 'admin').length;
    updateStat("statCustomers", totalCustomers);
  });

  // --- LẮNG NGHE ĐƠN HÀNG (Orders) - QUAN TRỌNG NHẤT ---
  onSnapshot(collection(db, "orders"), (snapshot) => {
    // 1. Cập nhật biến toàn cục
    allOrders = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));

    // 2. Cập nhật Tổng đơn hàng
    updateStat("statOrders", snapshot.size);

    // 3. Tính Tổng doanh thu (Chỉ tính đơn completed)
    const totalRevenue = allOrders.reduce((sum, order) => {
      if (order.status === 'completed') {
        return sum + Number(order.totalPrice || 0);
      }
      return sum;
    }, 0);
    updateStat("statRevenue", new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(totalRevenue));

    // 4. Cập nhật 2 bảng dữ liệu bên dưới
    renderRecentOrders(allOrders);
    // Cần cả data sản phẩm để hiển thị ảnh trong Best Seller
    if (allProducts.length > 0) calculateAndRenderBestSellers(allOrders, allProducts);
  });
}

// Hàm phụ cập nhật text
function updateStat(id, value) {
  const el = document.getElementById(id);
  if (el) el.innerText = value;
}

// ===================================================
// 2. BẢNG ĐƠN HÀNG GẦN ĐÂY
// ===================================================
function renderRecentOrders(orders) {
  const tableBody = document.getElementById("recentOrdersBody");
  if (!tableBody) return;

  tableBody.innerHTML = "";

  // Sắp xếp mới nhất lên đầu
  const sortedOrders = [...orders].sort((a, b) => {
    const timeA = a.createdAt || "";
    const timeB = b.createdAt || "";
    return timeB.localeCompare(timeA); 
  });

  // Lấy 5 đơn
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

    const row = document.createElement("tr");
    row.innerHTML = `
      <td><b>...${order.id.slice(-5)}</b></td>
      <td>${order.customerName || "Khách lẻ"}</td>
      <td style="font-weight:600;">${price}</td>
      <td>${statusBadge}</td>
    `;
    tableBody.appendChild(row);
  });
}

// ===================================================
// 3. BẢNG TOP BÁN CHẠY (BEST SELLER)
// ===================================================
function calculateAndRenderBestSellers(orders, products) {
  const listContainer = document.getElementById("bestSellerList");
  if (!listContainer) return;

  listContainer.innerHTML = "";

  const salesMap = {};

  orders.forEach(order => {
    // Chỉ tính đơn đã hoàn thành
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
    // Tìm ảnh sản phẩm
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
        <span class="item-price" style="color: #6b7280; font-size: 0.85rem;">Đã bán: ${item.qty} ly</span>
      </div>
      <div class="sold-count" style="background: #f3f4f6; color: #374151;">Top ${index+1}</div>
    `;
    listContainer.appendChild(div);
  });
}

// Kích hoạt chế độ Realtime
initRealtimeDashboard();