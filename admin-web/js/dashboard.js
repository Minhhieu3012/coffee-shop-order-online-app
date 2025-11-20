// admin-web/js/dashboard.js
import { db } from "./firebase-config.js";
import { collection, getDocs } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

// ===================================================
// 1. HÀM LOAD DỮ LIỆU THỐNG KÊ TỔNG HỢP
// ===================================================
async function loadDashboardStats() {
  try {
    // --- A. LẤY DỮ LIỆU TỪ FIREBASE ---
    // Sử dụng Promise.all để chạy song song 3 lệnh lấy dữ liệu cho nhanh
    const [productsSnap, usersSnap, ordersSnap] = await Promise.all([
      getDocs(collection(db, "products")),
      getDocs(collection(db, "users")),
      getDocs(collection(db, "orders"))
    ]);

    // --- B. XỬ LÝ SỐ LIỆU TỔNG QUAN ---
    const productsData = productsSnap.docs.map(doc => doc.data());
    const usersData = usersSnap.docs.map(doc => doc.data());
    
    // Lấy danh sách đơn hàng và thêm ID để xử lý
    const ordersData = ordersSnap.docs.map(doc => ({ id: doc.id, ...doc.data() }));

    // 1. Tổng Sản phẩm
    const totalProducts = productsSnap.size;

    // 2. Tổng Khách hàng (Lọc: chỉ đếm role 'customer', trừ 'admin')
    const totalCustomers = usersData.filter(u => u.role !== 'admin').length;

    // 3. Tổng Đơn hàng
    const totalOrders = ordersSnap.size;

    // 4. Tổng Doanh thu (Chỉ tính đơn 'completed' - Hoàn thành)
    // Cần đảm bảo field totalPrice là số (nếu lưu string thì phải parse)
    const totalRevenue = ordersData.reduce((sum, order) => {
      if (order.status === 'completed') {
        return sum + Number(order.totalPrice || 0);
      }
      return sum;
    }, 0);

    // --- C. HIỂN THỊ SỐ LIỆU LÊN HTML ---
    updateStat("statRevenue", new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(totalRevenue));
    updateStat("statOrders", totalOrders);
    updateStat("statCustomers", totalCustomers);
    updateStat("statProducts", totalProducts);

    // --- D. XỬ LÝ 2 BẢNG DƯỚI ---
    renderRecentOrders(ordersData);
    calculateAndRenderBestSellers(ordersData, productsData);

  } catch (error) {
    console.error("Lỗi tải Dashboard:", error);
  }
}

// Hàm phụ để gán text an toàn
function updateStat(id, value) {
  const el = document.getElementById(id);
  if (el) el.innerText = value;
}

// ===================================================
// 2. BẢNG ĐƠN HÀNG GẦN ĐÂY (RECENT ORDERS)
// ===================================================
function renderRecentOrders(orders) {
  const tableBody = document.getElementById("recentOrdersBody");
  if (!tableBody) return;

  tableBody.innerHTML = "";

  // Sắp xếp đơn mới nhất lên đầu (Dựa vào createdAt)
  // Lưu ý: createdAt trên Firebase thường là String hoặc Timestamp. 
  // Đây là so sánh chuỗi đơn giản, nếu cần chính xác tuyệt đối nên dùng Timestamp.
  const sortedOrders = [...orders].sort((a, b) => {
    const timeA = a.createdAt || "";
    const timeB = b.createdAt || "";
    return timeB.localeCompare(timeA); 
  });

  // Lấy 5 đơn đầu tiên
  const recentOrders = sortedOrders.slice(0, 5);

  if (recentOrders.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="4" style="text-align:center; padding:1rem; color:#888;">Chưa có đơn hàng nào</td></tr>`;
    return;
  }

  recentOrders.forEach(order => {
    const price = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalPrice || 0);
    
    // Badge trạng thái
    let statusBadge = `<span class="badge badge-warning">Chờ</span>`; // Mặc định
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
// 3. TÍNH TOÁN & HIỂN THỊ BEST SELLER (TOP BÁN CHẠY)
// ===================================================
function calculateAndRenderBestSellers(orders, products) {
  const listContainer = document.getElementById("bestSellerList");
  if (!listContainer) return;

  listContainer.innerHTML = "";

  // --- Bước 1: Đếm số lượng bán của từng món ---
  // Tạo map: { "Tên Món": { qty: 10, revenue: 500000 } }
  const salesMap = {};

  orders.forEach(order => {
    // Chỉ tính các đơn Đã Hoàn Thành (completed)
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

  // --- Bước 2: Chuyển Map thành Array và Sắp xếp ---
  let sortedSales = Object.values(salesMap).sort((a, b) => b.qty - a.qty);

  // Lấy Top 5
  const top5 = sortedSales.slice(0, 5);

  if (top5.length === 0) {
    listContainer.innerHTML = `<div style="text-align:center; color:#888; padding:1rem;">Chưa có dữ liệu bán hàng</div>`;
    return;
  }

  // --- Bước 3: Hiển thị ra màn hình ---
  top5.forEach((item, index) => {
    // Tìm ảnh của sản phẩm trong danh sách products (để hiển thị cho đẹp)
    // Nếu không tìm thấy thì dùng logo mặc định
    const productInfo = products.find(p => p.name === item.name);
    const imgUrl = productInfo ? productInfo.imageUrl : 'assets/logo.png';

    // Class màu cho Top 1, 2, 3
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

// Chạy hàm chính khi file load
loadDashboardStats();