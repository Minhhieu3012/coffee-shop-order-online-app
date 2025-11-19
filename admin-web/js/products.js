// js/products.js
const tbody = document.querySelector("#page-products tbody");
const modal = document.getElementById("product-modal");
const openModalBtn = document.getElementById("open-product-modal");
const closeBtns = document.querySelectorAll(".modal-close");

export function renderProducts() {
  tbody.innerHTML = "";

  const fakeProducts = [
    { id: "SP001", name: "Trà sữa trân châu đường đen", price: 45000, category: "Trà sữa", stock: 120 },
    { id: "SP002", name: "Cafe đen đá", price: 35000, category: "Cafe", stock: 85 },
    { id: "SP003", name: "Latte nóng", price: 55000, category: "Cafe", stock: 45 },
    { id: "SP004", name: "Matcha đá xay", price: 60000, category: "Trà xanh", stock: 67 },
    { id: "SP005", name: "Sinh tố dâu", price: 55000, category: "Sinh tố", stock: 0 },
  ];

  fakeProducts.forEach(p => {
    const stockBadge = p.stock === 0 ? "badge-danger" : p.stock < 20 ? "badge-warning" : "badge-success";
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${p.id}</td>
      <td><img src="assets/logo.png" class="product-thumb" style="width:40px;border-radius:8px;"> ${p.name}</td>
      <td>${p.price.toLocaleString('vi-VN')} đ</td>
      <td>${p.category}</td>
      <td><span class="badge ${stockBadge}">${p.stock}</span></td>
      <td class="actions">
        <button class="btn-small btn-edit"><i class="fa-solid fa-edit"></i></button>
        <button class="btn-small btn-delete"><i class="fa-solid fa-trash"></i></button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}

// Modal logic (chỉ chạy 1 lần)
openModalBtn.onclick = () => modal.classList.add("show");
closeBtns.forEach(btn => btn.onclick = () => modal.classList.remove("show"));
window.onclick = (e) => { if (e.target === modal) modal.classList.remove("show"); };