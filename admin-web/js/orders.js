// admin-web/js/orders.js
import { db } from "./firebase-config.js";
import { collection, onSnapshot, doc, updateDoc } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

let ordersData = [];

// --- 1. LẮNG NGHE DỮ LIỆU REALTIME TỪ FIREBASE ---
function listenOrders() {
  // Lắng nghe thay đổi ở collection 'orders'
  onSnapshot(collection(db, "orders"), (snapshot) => {
    ordersData = snapshot.docs.map(doc => ({
      id: doc.id,
      ...doc.data()
    }));

    // Sắp xếp đơn mới nhất lên đầu (Dựa vào chuỗi createdAt)
    ordersData.sort((a, b) => (b.createdAt || "").localeCompare(a.createdAt || ""));
    
    renderOrderTable(ordersData);
  }, (error) => {
    console.error("Lỗi tải đơn hàng:", error);
  });
}

// --- 2. CẬP NHẬT TRẠNG THÁI LÊN FIREBASE ---
async function updateOrderStatusOnDB(id, newStatus) {
  try {
    const btn = document.querySelector(`button[onclick*='${id}']`);
    if(btn) btn.disabled = true; // Chặn bấm liên tục

    await updateDoc(doc(db, "orders", id), { status: newStatus });
    
    let msg = "Đã cập nhật đơn hàng!";
    if(newStatus === 'processing') msg = "Đã duyệt đơn, bắt đầu pha chế!";
    if(newStatus === 'completed') msg = "Đơn hàng hoàn thành!";
    if(newStatus === 'cancelled') msg = "Đã hủy đơn hàng.";
    alert(msg);

  } catch (e) {
    console.error("Lỗi update:", e);
    alert("Lỗi: " + e.message);
  }
}

// --- 3. HÀM RENDER BẢNG ---
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
    const price = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalPrice || 0);
    let statusBadge = getStatusBadge(order.status);
    
    // Xử lý danh sách món (Lấy từ mảng 'items' trên Firebase)
    const itemsList = order.items || [];
    
    // Tóm tắt món ăn: "1x Cafe Đen (M), 2x Trà Đào"
    // Phần hiển thị Size ở đây: nếu có size thì hiện (S, M, L), không có thì thôi
    const itemsSummary = itemsList.map(i => {
        const sizeStr = i.size ? `(${i.size})` : ""; 
        return `${i.quantity}x ${i.name} ${sizeStr}`;
    }).join(", ");

    row.innerHTML = `
      <td>
        <a href="#" onclick="window.viewOrder('${order.id}')" style="color: #6a4616; font-weight: 700; text-decoration: none;">
          ${order.id.length > 8 ? "..." + order.id.slice(-5) : order.id} 
          <i class="fa-solid fa-up-right-from-square" style="font-size: 0.7rem; margin-left: 4px;"></i>
        </a>
      </td>
      <td>
        <div style="font-weight: 500;">${order.customerName || "Khách vãng lai"}</div>
        <small style="color: #6b7280; display: block; max-width: 200px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
            ${itemsSummary || "Không có món"}
        </small>
      </td>
      <td>${order.createdAt || "---"}</td>
      <td style="font-weight: 600;">${price}</td>
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

function getStatusBadge(status) {
  switch (status) {
    case 'pending': return `<span class="badge badge-warning" style="background:#fef3c7; color:#b45309;">Chờ xác nhận</span>`;
    case 'processing': return `<span class="badge badge-info" style="background:#dbeafe; color:#1e40af;">Đang pha chế</span>`;
    case 'completed': return `<span class="badge badge-success" style="background:#dcfce7; color:#15803d;">Hoàn thành</span>`;
    case 'cancelled': return `<span class="badge badge-danger" style="background:#fee2e2; color:#b91c1c;">Đã hủy</span>`;
    default: return `<span class="badge">${status}</span>`;
  }
}

// --- 4. XỬ LÝ MODAL CHI TIẾT ---
const orderModal = document.getElementById("orderModal");
const closeOrderBtn = document.getElementById("closeOrderModal");

if (closeOrderBtn) {
  closeOrderBtn.addEventListener("click", () => orderModal.classList.remove("show"));
}
window.addEventListener("click", (e) => { 
  if (e.target == orderModal) orderModal.classList.remove("show"); 
});

// Hàm xem chi tiết
window.viewOrder = (id) => {
  const order = ordersData.find(o => o.id === id);
  if (!order) return;

  document.getElementById("ordModalId").innerText = "#" + (order.id.length > 8 ? "..." + order.id.slice(-5) : order.id);
  document.getElementById("ordModalCustomer").value = order.customerName || "";
  
  // Hiển thị thời gian vào modal
  if(document.getElementById("ordModalDate")) {
      document.getElementById("ordModalDate").value = order.createdAt || ""; 
  }
  
  document.getElementById("ordModalStatus").innerHTML = getStatusBadge(order.status);

  const itemsContainer = document.getElementById("ordModalItems");
  let itemsHtml = "";
  
  const itemsList = order.items || [];

  if (itemsList.length > 0) {
    itemsList.forEach(item => {
      const itemTotal = (item.price || 0) * (item.quantity || 1);
      // Hiển thị size trong Popup
      const sizeDisplay = item.size ? `<span style="background:#eee; padding:2px 6px; border-radius:4px; font-size:0.8rem; margin-left:5px; font-weight:bold; color:#555;">Size: ${item.size}</span>` : "";
      
      itemsHtml += `
        <div class="order-item-row">
          <span>
            <b>${item.quantity}x</b> ${item.name} 
            ${sizeDisplay} 
          </span>
          <span>${new Intl.NumberFormat('vi-VN').format(itemTotal)} đ</span>
        </div>`;
    });
  } else {
    itemsHtml = "<p style='text-align:center; color:#999;'>Không có thông tin món ăn</p>";
  }
  
  itemsHtml += `
    <div class="order-total-row">
      <span>Tổng cộng:</span>
      <span>${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalPrice || 0)}</span>
    </div>`;
  itemsContainer.innerHTML = itemsHtml;

  // Nút bấm Footer
  const footer = document.getElementById("ordModalFooter");
  let buttonsHtml = `<button type="button" class="btn-secondary close-modal-btn" onclick="document.getElementById('orderModal').classList.remove('show')">Đóng</button>`;

  if (order.status === 'pending') {
    buttonsHtml = `
      <button type="button" class="btn-secondary" onclick="document.getElementById('orderModal').classList.remove('show')">Đóng</button>
      <button type="button" class="btn-primary btn-cancel" onclick="window.processOrder('${order.id}', 'cancelled')">Hủy đơn</button>
      <button type="button" class="btn-primary btn-approve" onclick="window.processOrder('${order.id}', 'processing')">Duyệt đơn</button>
    `;
  } else if (order.status === 'processing') {
    buttonsHtml = `
      <button type="button" class="btn-secondary" onclick="document.getElementById('orderModal').classList.remove('show')">Đóng</button>
      <button type="button" class="btn-primary btn-complete" onclick="window.processOrder('${order.id}', 'completed')">Hoàn thành</button>
    `;
  }
  footer.innerHTML = buttonsHtml;

  if (orderModal) orderModal.classList.add("show");
};

// Hàm xử lý nút bấm trong Modal
window.processOrder = async (id, newStatus) => {
  if(confirm("Bạn chắc chắn muốn cập nhật trạng thái đơn này?")) {
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
      normalizeStr(o.customerName || "").includes(keyword)
    );
    renderOrderTable(filteredData);
  });
}

// Khởi chạy
listenOrders();