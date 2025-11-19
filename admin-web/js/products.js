// admin-web/js/products.js

// 1. DỮ LIỆU GIẢ
let productsData = [
  { id: "sp001", name: "Cà phê Đen Đá", category: "Cà phê", price: 25000, imageUrl: "https://img.freepik.com/free-photo/iced-coffee-glass_144627-23578.jpg", isAvailable: true },
  { id: "sp002", name: "Bạc Xỉu", category: "Cà phê", price: 29000, imageUrl: "https://img.freepik.com/free-photo/glass-coffee-with-milk_140725-5781.jpg", isAvailable: true },
  { id: "sp003", name: "Trà Đào Cam Sả", category: "Trà trái cây", price: 35000, imageUrl: "https://img.freepik.com/free-photo/glass-peach-tea-with-ice-cubes_140725-4777.jpg", isAvailable: false },
  { id: "sp004", name: "Croissant Hạnh Nhân", category: "Bánh ngọt", price: 45000, imageUrl: "https://img.freepik.com/free-photo/croissants-wooden-cutting-board_1150-24898.jpg", isAvailable: true }
];

// 2. HÀM RENDER BẢNG (Đã nâng cấp để nhận list data tùy ý)
// Mặc định data = productsData (nếu không truyền gì vào)
function renderTable(data = productsData) {
  const tableBody = document.getElementById("productTableBody");
  if (!tableBody) return;

  tableBody.innerHTML = "";

  if (data.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="6" style="text-align:center; padding: 2rem;">Không tìm thấy sản phẩm nào</td></tr>`;
    return;
  }

  data.forEach(product => {
    const row = document.createElement("tr");
    const formattedPrice = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(product.price);
    const statusBadge = product.isAvailable 
      ? `<span class="badge badge-success">Còn hàng</span>` 
      : `<span class="badge badge-danger">Hết hàng</span>`;
    const imgUrl = product.imageUrl || 'assets/logo.png';

    row.innerHTML = `
      <td><img src="${imgUrl}" class="product-img" onerror="this.src='assets/logo.png'"></td>
      <td>
        <div style="font-weight: 500;">${product.name}</div>
        <small style="color: #888;">ID: ${product.id}</small>
      </td>
      <td>${product.category}</td>
      <td>${formattedPrice}</td>
      <td>${statusBadge}</td>
      <td>
        <button class="action-btn" onclick="window.editProduct('${product.id}')"><i class="fa-solid fa-pen-to-square"></i></button>
        <button class="action-btn" style="color: #ef4444;" onclick="window.deleteProduct('${product.id}')"><i class="fa-solid fa-trash"></i></button>
      </td>
    `;
    tableBody.appendChild(row);
  });
}

// 3. LOGIC TÌM KIẾM (MỚI THÊM)
const searchInput = document.getElementById("searchProduct");
if (searchInput) {
  searchInput.addEventListener("input", (e) => {
    const keyword = e.target.value.toLowerCase().trim();
    
    // Lọc dữ liệu
    const filteredData = productsData.filter(product => {
      return product.name.toLowerCase().includes(keyword) || 
             product.id.toLowerCase().includes(keyword);
    });

    // Vẽ lại bảng với dữ liệu đã lọc
    renderTable(filteredData);
  });
}

// ... (Các phần Modal, Form Submit, Delete giữ nguyên như cũ) ...
// Để gọn code, bạn giữ nguyên logic xử lý Modal/Form cũ ở dưới này nhé.
// Nếu lỡ xóa thì copy lại từ file cũ, chỉ cần thay đoạn renderTable ở trên là được.

// --- (Logic cũ bạn dán nối tiếp vào đây) ---
const modal = document.getElementById("productModal");
const btnAdd = document.getElementById("btnAddProduct");
const closeElements = document.querySelectorAll(".close-modal, .close-modal-btn");
const productForm = document.getElementById("productForm");
const modalTitle = document.getElementById("modalTitle");

if (btnAdd) {
  btnAdd.addEventListener("click", () => {
    if (productForm) productForm.reset();
    document.getElementById("prodId").value = "";
    modalTitle.innerText = "Thêm món mới";
    if (modal) modal.classList.add("show");
  });
}
window.editProduct = (id) => {
  const product = productsData.find(p => p.id === id);
  if (product) {
    document.getElementById("prodId").value = product.id;
    document.getElementById("prodName").value = product.name;
    document.getElementById("prodCategory").value = product.category;
    document.getElementById("prodPrice").value = product.price;
    document.getElementById("prodImage").value = product.imageUrl;
    document.getElementById("prodStatus").checked = product.isAvailable;
    modalTitle.innerText = "Cập nhật sản phẩm";
    if (modal) modal.classList.add("show");
  }
};
closeElements.forEach(el => {
  el.addEventListener("click", () => { if (modal) modal.classList.remove("show"); });
});
window.addEventListener("click", (e) => { if (e.target == modal) modal.classList.remove("show"); });

if (productForm) {
  productForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const id = document.getElementById("prodId").value;
    const name = document.getElementById("prodName").value;
    const category = document.getElementById("prodCategory").value;
    const price = Number(document.getElementById("prodPrice").value);
    const imageUrl = document.getElementById("prodImage").value;
    const isAvailable = document.getElementById("prodStatus").checked;

    if (id) {
      const index = productsData.findIndex(p => p.id === id);
      if (index !== -1) {
        productsData[index] = { ...productsData[index], name, category, price, imageUrl, isAvailable };
      }
    } else {
      const newProduct = { id: "sp" + Date.now(), name, category, price, imageUrl, isAvailable };
      productsData.unshift(newProduct);
    }
    renderTable(); // Vẽ lại (mặc định vẽ full list)
    searchInput.value = ""; // Reset ô tìm kiếm
    if (modal) modal.classList.remove("show");
  });
}

window.deleteProduct = (id) => {
  if (confirm("Xóa sản phẩm này?")) {
    productsData = productsData.filter(p => p.id !== id);
    renderTable();
    searchInput.value = "";
  }
};

renderTable();