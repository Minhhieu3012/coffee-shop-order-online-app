// admin-web/js/orders.js
import { db } from "./firebase-config.js";
import { collection, onSnapshot, doc, updateDoc } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

let ordersData = [];

// --- LẮNG NGHE DỮ LIỆU REALTIME ---
function listenOrders() {
  // Lắng nghe thay đổi ở collection 'orders'
  onSnapshot(collection(db, "orders"), (snapshot) => {
    ordersData = snapshot.docs.map(doc => ({
      id: doc.id,
      ...doc.data()
    }));
    // Sắp xếp đơn mới nhất lên đầu (dựa vào createdAt nếu có)
    // ordersData.sort((a, b) => b.createdAt - a.createdAt); 
    
    renderOrderTable(ordersData);
  });
}

// Cập nhật trạng thái lên Firebase
async function updateOrderStatusOnDB(id, newStatus) {
  try {
    await updateDoc(doc(db, "orders", id), { status: newStatus });
    alert("Đã cập nhật trạng thái!");
  } catch (e) {
    alert("Lỗi: " + e.message);
  }
}

// --- RENDER & LOGIC (Giữ nguyên như cũ) ---
function normalizeStr(str) {
  if (!str) return "";
  return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/đ/g, 'd').replace(/Đ/g, 'D').toLowerCase().trim();
}

function renderOrderTable(data = ordersData) {
  const tableBody = document.getElementById("orderTableBody");
  if (!tableBody) return;
  tableBody.innerHTML = "";

  if (data.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="6" style="text-align:center; padding: 2rem;">Chưa có đơn hàng nào</td></tr>`;
    return;
  }

  data.forEach(order => {
    const row = document.createElement("tr");
    const price = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalPrice || 0);
    let statusBadge = getStatusBadge(order.status);
    
    // Nếu items là mảng (từ firebase) thì map, nếu không có thì để trống
    const itemsSummary = Array.isArray(order.items) 
      ? order.items.map(i => `${i.quantity}x ${i.name}`).join(", ")
      : "Chi tiết trong xem thêm...";

    row.innerHTML = `
      <td><b>...${order.id.slice(-5)}</b></td>
      <td>
        <div style="font-weight: 500;">${order.customerName || "Khách lẻ"}</div>
        <small style="color: #6b7280;">${itemsSummary}</small>
      </td>
      <td>${order.createdAt || "N/A"}</td>
      <td style="font-weight: 600;">${price}</td>
      <td>${statusBadge}</td>
      <td>
        <button class="action-btn" onclick="window.viewOrder('${order.id}')" title="Xem"><i class="fa-solid fa-eye"></i> Xem</button>
      </td>
    `;
    tableBody.appendChild(row);
  });
}

function getStatusBadge(status) {
  switch (status) {
    case 'pending': return `<span class="badge badge-warning" style="background:#fef3c7; color:#b45309;">Chờ xác nhận</span>`;
    case 'processing': return `<span class="badge badge-info" style="background:#dbeafe; color:#1e40af;">Đang pha chế</span>`;
    case 'completed': return `<span class="badge badge-success" style="background:#dcfce7; color:#15803d;">Hoàn thành</span>`;
    case 'cancelled': return `<span class="badge badge-danger" style="background:#fee2e2; color:#b91c1c;">Đã hủy</span>`;
    default: return status;
  }
}

// MODAL
const orderModal = document.getElementById("orderModal");
const closeOrderBtn = document.getElementById("closeOrderModal");
if (closeOrderBtn) closeOrderBtn.addEventListener("click", () => orderModal.classList.remove("show"));
window.addEventListener("click", (e) => { if (e.target == orderModal) orderModal.classList.remove("show"); });

window.viewOrder = (id) => {
  const order = ordersData.find(o => o.id === id);
  if (!order) return;

  document.getElementById("ordModalId").innerText = "#" + order.id;
  document.getElementById("ordModalCustomer").value = order.customerName || "";
  document.getElementById("ordModalStatus").innerHTML = getStatusBadge(order.status);

  const itemsContainer = document.getElementById("ordModalItems");
  let itemsHtml = "";
  if (Array.isArray(order.items)) {
    order.items.forEach(item => {
      const itemTotal = (item.price || 0) * (item.quantity || 1);
      itemsHtml += `
        <div class="order-item-row">
          <span><b>${item.quantity}x</b> ${item.name}</span>
          <span>${new Intl.NumberFormat('vi-VN').format(itemTotal)} đ</span>
        </div>`;
    });
  }
  itemsHtml += `
    <div class="order-total-row">
      <span>Tổng cộng:</span>
      <span>${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalPrice || 0)}</span>
    </div>`;
  itemsContainer.innerHTML = itemsHtml;

  const footer = document.getElementById("ordModalFooter");
  let buttonsHtml = `<button type="button" class="btn-secondary" onclick="document.getElementById('orderModal').classList.remove('show')">Đóng</button>`;

  if (order.status === 'pending') {
    buttonsHtml += `
      <button type="button" class="btn-primary btn-cancel" onclick="window.processOrder('${order.id}', 'cancelled')">Hủy đơn</button>
      <button type="button" class="btn-primary btn-approve" onclick="window.processOrder('${order.id}', 'processing')">Duyệt đơn</button>
    `;
  } else if (order.status === 'processing') {
    buttonsHtml += `
      <button type="button" class="btn-primary btn-complete" onclick="window.processOrder('${order.id}', 'completed')">Hoàn thành</button>
    `;
  }
  footer.innerHTML = buttonsHtml;
  if (orderModal) orderModal.classList.add("show");
};

window.processOrder = async (id, newStatus) => {
  await updateOrderStatusOnDB(id, newStatus);
  if (orderModal) orderModal.classList.remove("show");
};

const searchInput = document.getElementById("searchOrder");
if (searchInput) {
  searchInput.addEventListener("input", (e) => {
    const keyword = normalizeStr(e.target.value);
    const filteredData = ordersData.filter(o => normalizeStr(o.id).includes(keyword) || normalizeStr(o.customerName).includes(keyword));
    renderOrderTable(filteredData);
  });
}

listenOrders();