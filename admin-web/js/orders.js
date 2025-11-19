// admin-web/js/orders.js

// DỮ LIỆU GIẢ
let ordersData = [
  {
    id: "DH001",
    customerName: "Nguyễn Văn A",
    totalPrice: 55000,
    status: "pending", 
    itemsDetail: [
      { name: "Cafe Đen", quantity: 1, price: 25000 },
      { name: "Bạc Xỉu", quantity: 1, price: 30000 }
    ]
  },
  {
    id: "DH002",
    customerName: "Trần Thị B",
    totalPrice: 45000,
    status: "processing",
    itemsDetail: [
      { name: "Croissant", quantity: 1, price: 25000 },
      { name: "Espresso", quantity: 1, price: 20000 }
    ]
  },
  {
    id: "DH003",
    customerName: "Lê Văn C",
    totalPrice: 120000,
    status: "completed",
    itemsDetail: [
      { name: "Trà Đào", quantity: 4, price: 30000 }
    ]
  },
  {
    id: "DH004",
    customerName: "Phạm Thị D",
    totalPrice: 35000,
    status: "cancelled",
    itemsDetail: [
      { name: "Trà Sữa", quantity: 1, price: 35000 }
    ]
  }
];

// HÀM HỖ TRỢ: Xóa dấu Tiếng Việt
function normalizeStr(str) {
  if (!str) return "";
  return str.normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .replace(/đ/g, 'd').replace(/Đ/g, 'D')
            .toLowerCase().trim();
}

// HÀM RENDER BẢNG
function renderOrderTable(data = ordersData) {
  const tableBody = document.getElementById("orderTableBody");
  if (!tableBody) return;

  tableBody.innerHTML = "";

  if (data.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; padding: 2rem;">Không tìm thấy đơn hàng nào</td></tr>`;
    return;
  }

  data.forEach(order => {
    const row = document.createElement("tr");
    const price = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalPrice);
    let statusBadge = getStatusBadge(order.status);
    const itemsSummary = order.itemsDetail.map(i => `${i.quantity}x ${i.name}`).join(", ");

    row.innerHTML = `
      <td><b>${order.id}</b></td>
      <td>
        <div style="font-weight: 500;">${order.customerName}</div>
        <small style="color: #6b7280;">${itemsSummary}</small>
      </td>
      <td style="font-weight: 600;">${price}</td>
      <td>${statusBadge}</td>
      <td>
        <button class="action-btn" onclick="window.viewOrder('${order.id}')" title="Xem & Xử lý">
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
    default: return status;
  }
}

// TÌM KIẾM THÔNG MINH
const searchInput = document.getElementById("searchOrder");
if (searchInput) {
  searchInput.addEventListener("input", (e) => {
    const keyword = normalizeStr(e.target.value);

    const filteredData = ordersData.filter(order => 
      normalizeStr(order.id).includes(keyword) || 
      normalizeStr(order.customerName).includes(keyword)
    );
    renderOrderTable(filteredData);
  });
}

// LOGIC MODAL & XỬ LÝ ĐƠN
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

  document.getElementById("ordModalId").innerText = "#" + order.id;
  document.getElementById("ordModalCustomer").value = order.customerName;
  document.getElementById("ordModalStatus").innerHTML = getStatusBadge(order.status);

  const itemsContainer = document.getElementById("ordModalItems");
  let itemsHtml = "";
  order.itemsDetail.forEach(item => {
    const itemTotal = item.price * item.quantity;
    itemsHtml += `
      <div class="order-item-row">
        <span><b>${item.quantity}x</b> ${item.name}</span>
        <span>${new Intl.NumberFormat('vi-VN').format(itemTotal)} đ</span>
      </div>
    `;
  });
  itemsHtml += `
    <div class="order-total-row">
      <span>Tổng cộng:</span>
      <span>${new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(order.totalPrice)}</span>
    </div>
  `;
  itemsContainer.innerHTML = itemsHtml;

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

window.processOrder = (id, newStatus) => {
  const index = ordersData.findIndex(o => o.id === id);
  if (index !== -1) {
    ordersData[index].status = newStatus;
    orderModal.classList.remove("show");
    renderOrderTable();
    let msg = "Đã cập nhật đơn hàng!";
    if(newStatus === 'processing') msg = "Đã duyệt đơn!";
    if(newStatus === 'completed') msg = "Đơn hàng đã hoàn thành!";
    if(newStatus === 'cancelled') msg = "Đã hủy đơn.";
    alert(msg);
  }
};

renderOrderTable();
export { renderOrderTable };