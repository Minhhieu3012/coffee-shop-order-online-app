// admin-web/js/orders.js
import { db } from "./firebase-config.js";
import { collection, onSnapshot, doc, updateDoc } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

let ordersData = [];

// --- HELPER: FORMAT DATE (Xử lý Timestamp Firebase) ---
function formatDate(timestamp) {
  if (!timestamp) return "";
  // Nếu là Firebase Timestamp (có hàm toDate)
  if (timestamp.toDate) {
    return timestamp.toDate().toLocaleString('vi-VN', { 
        day: '2-digit', month: '2-digit', year: 'numeric', 
        hour: '2-digit', minute: '2-digit' 
    });
  }
  // Nếu là string hoặc Date
  return new Date(timestamp).toLocaleString('vi-VN');
}

// --- HELPER: FORMAT TIỀN ---
function formatMoney(amount) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount || 0);
}

// --- HELPER: TRANSLATE STATUS (Xử lý in hoa/thường) ---
function getStatusBadge(status) {
  if (!status) return `<span class="badge">---</span>`;
  const s = status.toUpperCase(); // Chuyển về in hoa để so sánh

  switch (s) {
    case 'PENDING': 
        return `<span class="badge badge-warning" style="background:#fef3c7; color:#b45309;">Chờ xác nhận</span>`;
    case 'PROCESSING': 
        return `<span class="badge badge-info" style="background:#dbeafe; color:#1e40af;">Đang pha chế</span>`;
    case 'COMPLETED': 
        return `<span class="badge badge-success" style="background:#dcfce7; color:#15803d;">Hoàn thành</span>`;
    case 'CANCELLED': 
        return `<span class="badge badge-danger" style="background:#fee2e2; color:#b91c1c;">Đã hủy</span>`;
    case 'PAID':
        return `<span class="badge badge-success">Đã thanh toán</span>`;
    default: 
        return `<span class="badge">${status}</span>`;
  }
}

// --- 1. LẮNG NGHE DỮ LIỆU ---
function listenOrders() {
  onSnapshot(collection(db, "orders"), (snapshot) => {
    ordersData = snapshot.docs.map(doc => ({
      id: doc.id,
      ...doc.data()
    }));

    // Sắp xếp: Mới nhất lên đầu (dựa trên seconds của timestamp)
    ordersData.sort((a, b) => {
        const timeA = a.createdAt?.seconds || 0;
        const timeB = b.createdAt?.seconds || 0;
        return timeB - timeA;
    });
    
    renderOrderTable(ordersData);
  }, (error) => {
    console.error("Lỗi tải đơn hàng:", error);
  });
}

// --- 2. CẬP NHẬT TRẠNG THÁI ---
async function updateOrderStatusOnDB(id, newStatus) {
  try {
    // Disable nút bấm tạm thời
    const btn = document.querySelector(`button[onclick*='${id}']`);
    if(btn) btn.disabled = true; 

    // Mobile App dùng uppercase (PENDING, PROCESSING...)
    await updateDoc(doc(db, "orders", id), { status: newStatus.toUpperCase() });
    
    alert("Đã cập nhật trạng thái: " + newStatus);
  } catch (e) {
    console.error("Lỗi update:", e);
    alert("Lỗi: " + e.message);
  }
}

// --- 3. RENDER BẢNG ---
function renderOrderTable(data = ordersData) {
  const tableBody = document.getElementById("orderTableBody");
  if (!tableBody) return;
  tableBody.innerHTML = "";

  if (data.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="6" style="text-align:center; padding: 2rem; color: #888;">Chưa có đơn hàng nào</td></tr>`;
    return;
  }

  data.forEach(order => {
    const row = document.createElement("tr");
    
    // Dữ liệu từ mobile: userName, total
    const customer = order.userName || "Khách vãng lai";
    const total = formatMoney(order.total);
    const timeStr = formatDate(order.createdAt);
    const statusBadge = getStatusBadge(order.status);
    
    const itemsList = order.items || [];
    // Mobile dùng productName
    const itemsSummary = itemsList.map(i => {
        return `${i.quantity}x ${i.productName} (${i.size})`;
    }).join(", ");

    row.innerHTML = `
      <td>
        <a href="#" onclick="window.viewOrder('${order.id}')" style="color: #6a4616; font-weight: 700; text-decoration: none;">
          ...${order.id.slice(-5)} 
          <i class="fa-solid fa-up-right-from-square" style="font-size: 0.7rem; margin-left: 4px;"></i>
        </a>
      </td>
      <td>
        <div style="font-weight: 500;">${customer}</div>
        <small style="color: #6b7280; display: block; max-width: 200px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
            ${itemsSummary || "Không có món"}
        </small>
      </td>
      <td>${timeStr}</td>
      <td style="font-weight: 600;">${total}</td>
      <td>${statusBadge}</td>
      <td>
        <button class="action-btn" onclick="window.viewOrder('${order.id}')" title="Xem chi tiết">
            <i class="fa-solid fa-eye"></i> Xem
        </button>
      </td>
    `;
    tableBody.appendChild(row);
  });
}

// --- 4. MODAL CHI TIẾT ---
const orderModal = document.getElementById("orderModal");
const closeOrderBtn = document.getElementById("closeOrderModal");

if (closeOrderBtn) {
  closeOrderBtn.addEventListener("click", () => orderModal.classList.remove("show"));
}
window.addEventListener("click", (e) => { 
  if (e.target == orderModal) orderModal.classList.remove("show"); 
});

window.viewOrder = (id) => {
  const order = ordersData.find(o => o.id === id);
  if (!order) return;

  // Header Info
  document.getElementById("ordModalId").innerText = "#" + order.id.slice(-5);
  document.getElementById("ordModalCustomer").innerText = (order.userName || "Khách vãng lai") + (order.userPhone ? ` - ${order.userPhone}` : "");
  document.getElementById("ordModalDate").innerText = formatDate(order.createdAt);

  // Items List
  const itemsContainer = document.getElementById("ordModalItems");
  let itemsHtml = "";
  
  if (order.items && order.items.length > 0) {
    order.items.forEach(item => {
      itemsHtml += `
        <div style="display:flex; justify-content:space-between; padding: 8px 0; border-bottom: 1px dashed #eee; align-items: flex-start;">
          <div style="display:flex; gap: 10px;">
            <img src="${item.productImage}" style="width: 40px; height: 40px; border-radius: 4px; object-fit: cover;" onerror="this.src='assets/logo.png'">
            <div>
                <div style="font-weight: 500;">${item.productName}</div>
                <div style="font-size: 0.8rem; color: #666;">
                    Size: <span style="background: #eee; padding: 2px 6px; border-radius: 4px;">${item.size}</span> x${item.quantity}
                </div>
            </div>
          </div>
          <span style="font-weight:500;">${formatMoney(item.lineTotal || item.price * item.quantity)}</span>
        </div>`;
    });
  } else {
    itemsHtml = "<p style='text-align:center; color:#999;'>Không có thông tin món ăn</p>";
  }
  itemsContainer.innerHTML = itemsHtml;

  // Footer & Payment Info (New Fields)
  document.getElementById("ordModalSubtotal").innerText = formatMoney(order.subtotal);
  document.getElementById("ordModalShip").innerText = formatMoney(order.deliveryFee);
  document.getElementById("ordModalTotal").innerText = formatMoney(order.total);
  
  // Payment Method & Note
  const payMethod = order.paymentMethod === 'BANK_TRANSFER' ? 'Chuyển khoản' : (order.paymentMethod === 'CASH' ? 'Tiền mặt' : order.paymentMethod);
  document.getElementById("ordModalPayment").innerText = `${payMethod} (${order.paymentStatus || 'UNPAID'})`;
  document.getElementById("ordModalNote").innerText = order.note || "Không có";
  document.getElementById("ordModalStatus").innerHTML = getStatusBadge(order.status);

  // Action Buttons logic
  const footer = document.getElementById("ordModalFooter");
  const status = order.status ? order.status.toUpperCase() : "";
  
  let buttonsHtml = `<button type="button" class="btn-secondary close-modal-btn" onclick="document.getElementById('orderModal').classList.remove('show')">Đóng</button>`;

  if (status === 'PENDING') {
    buttonsHtml = `
      <button type="button" class="btn-secondary" onclick="document.getElementById('orderModal').classList.remove('show')">Đóng</button>
      <button type="button" class="btn-primary btn-cancel" onclick="window.processOrder('${order.id}', 'CANCELLED')">Hủy đơn</button>
      <button type="button" class="btn-primary btn-approve" onclick="window.processOrder('${order.id}', 'PROCESSING')">Duyệt đơn</button>
    `;
  } else if (status === 'PROCESSING') {
    buttonsHtml = `
      <button type="button" class="btn-secondary" onclick="document.getElementById('orderModal').classList.remove('show')">Đóng</button>
      <button type="button" class="btn-primary btn-complete" onclick="window.processOrder('${order.id}', 'COMPLETED')">Hoàn thành</button>
    `;
  }
  footer.innerHTML = buttonsHtml;

  if (orderModal) orderModal.classList.add("show");
};

window.processOrder = async (id, newStatus) => {
  if(confirm(`Bạn chắc chắn muốn chuyển trạng thái thành ${newStatus}?`)) {
      await updateOrderStatusOnDB(id, newStatus);
      if (orderModal) orderModal.classList.remove("show");
  }
};

// --- 5. TÌM KIẾM ---
function normalizeStr(str) {
  if (!str) return "";
  return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/đ/g, 'd').replace(/Đ/g, 'D').toLowerCase().trim();
}

const searchInput = document.getElementById("searchOrder");
if (searchInput) {
  searchInput.addEventListener("input", (e) => {
    const keyword = normalizeStr(e.target.value);
    const filteredData = ordersData.filter(o => 
      normalizeStr(o.id).includes(keyword) || 
      normalizeStr(o.userName || "").includes(keyword)
    );
    renderOrderTable(filteredData);
  });
}

listenOrders();