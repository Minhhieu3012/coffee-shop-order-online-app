// js/orders.js
const tbody = document.querySelector("#page-orders tbody");

export function renderOrders() {
  tbody.innerHTML = "";

  const statusText = {
    pending: "Chờ xác nhận",
    confirmed: "Đã xác nhận",
    preparing: "Đang pha chế",
    delivering: "Đang giao",
    completed: "Hoàn thành",
    cancelled: "Đã hủy"
  };

  const fakeOrders = Array.from({length: 20}, (_, i) => ({
    id: `DH${String(i+1001).padStart(4,"0")}`,
    customer: ["Nguyễn Văn A", "Trần B", "Lê C", "Phạm D", "Hoàng E"][i%5],
    total: Math.floor(Math.random() * 300000 + 100000),
    status: ["pending","confirmed","preparing","delivering","completed","cancelled"][Math.floor(Math.random()*6)],
    date: new Date(Date.now() - Math.random()*7*24*60*60*1000).toLocaleDateString('vi-VN')
  }));

  fakeOrders.forEach(order => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${order.id}</td>
      <td>${order.customer}</td>
      <td>${order.total.toLocaleString('vi-VN')} đ</td>
      <td><span class="badge badge-${order.status}">${statusText[order.status]}</span></td>
      <td>${order.date}</td>
      <td class="actions">
        <button class="btn-small btn-view"><i class="fa-solid fa-eye"></i></button>
        <button class="btn-small btn-edit"><i class="fa-solid fa-pen"></i></button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}