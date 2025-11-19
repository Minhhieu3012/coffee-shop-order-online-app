// admin-web/js/products.js

// 1. DỮ LIỆU GIẢ (MOCK DATA)
let productsData = [
  {
    id: "sp001",
    name: "Cà phê Đen Đá",
    category: "Cà phê",
    price: 25000,
    imageUrl: "https://img.freepik.com/free-photo/iced-coffee-glass_144627-23578.jpg",
    isAvailable: true
  },
  {
    id: "sp002",
    name: "Bạc Xỉu",
    category: "Cà phê",
    price: 29000,
    imageUrl: "https://img.freepik.com/free-photo/glass-coffee-with-milk_140725-5781.jpg",
    isAvailable: true
  },
  {
    id: "sp003",
    name: "Trà Đào Cam Sả",
    category: "Trà trái cây",
    price: 35000,
    imageUrl: "https://img.freepik.com/free-photo/glass-peach-tea-with-ice-cubes_140725-4777.jpg",
    isAvailable: false
  },
  {
    id: "sp004",
    name: "Croissant Hạnh Nhân",
    category: "Bánh ngọt",
    price: 45000,
    imageUrl: "https://img.freepik.com/free-photo/croissants-wooden-cutting-board_1150-24898.jpg",
    isAvailable: true
  }
];

// 2. HÀM RENDER BẢNG
function renderTable() {
  const tableBody = document.getElementById("productTableBody");
  if (!tableBody) return;

  tableBody.innerHTML = "";

  productsData.forEach(product => {
    const row = document.createElement("tr");

    const formattedPrice = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(product.price);
    
    const statusBadge = product.isAvailable 
      ? `<span class="badge badge-success">Còn hàng</span>` 
      : `<span class="badge badge-danger">Hết hàng</span>`;

    const imgUrl = product.imageUrl || 'assets/logo.png';

    row.innerHTML = `
      <td>
        <img src="${imgUrl}" class="product-img" alt="${product.name}" onerror="this.src='assets/logo.png'">
      </td>
      <td>
        <div style="font-weight: 500;">${product.name}</div>
        <small style="color: #888;">ID: ${product.id}</small>
      </td>
      <td>${product.category}</td>
      <td>${formattedPrice}</td>
      <td>${statusBadge}</td>
      <td>
        <button class="action-btn" onclick="window.editProduct('${product.id}')" title="Sửa">
          <i class="fa-solid fa-pen-to-square"></i>
        </button>
        <button class="action-btn" style="color: #ef4444;" onclick="window.deleteProduct('${product.id}')" title="Xóa">
          <i class="fa-solid fa-trash"></i>
        </button>
      </td>
    `;
    tableBody.appendChild(row);
  });
}

// 3. CÁC BIẾN DOM
const modal = document.getElementById("productModal");
const btnAdd = document.getElementById("btnAddProduct");
const closeElements = document.querySelectorAll(".close-modal, .close-modal-btn");
const productForm = document.getElementById("productForm");
const modalTitle = document.getElementById("modalTitle");

// 4. XỬ LÝ MỞ MODAL (THÊM MỚI)
if (btnAdd) {
  btnAdd.addEventListener("click", () => {
    // Reset form về trắng tinh
    if (productForm) productForm.reset();
    
    // Xóa ID ẩn (để code biết là đang thêm mới)
    document.getElementById("prodId").value = "";
    
    // Đổi tiêu đề
    modalTitle.innerText = "Thêm món mới";
    
    // Hiện modal
    if (modal) modal.classList.add("show");
  });
}

// 5. XỬ LÝ MỞ MODAL (SỬA / CẬP NHẬT) - QUAN TRỌNG
window.editProduct = (id) => {
  // Tìm món cần sửa trong mảng dữ liệu
  const product = productsData.find(p => p.id === id);
  
  if (product) {
    // Điền dữ liệu cũ vào các ô input
    document.getElementById("prodId").value = product.id; // Lưu ID vào ô ẩn
    document.getElementById("prodName").value = product.name;
    document.getElementById("prodCategory").value = product.category;
    document.getElementById("prodPrice").value = product.price;
    document.getElementById("prodImage").value = product.imageUrl;
    document.getElementById("prodStatus").checked = product.isAvailable;

    // Đổi tiêu đề modal
    modalTitle.innerText = "Cập nhật sản phẩm";

    // Hiện modal
    if (modal) modal.classList.add("show");
  }
};

// 6. XỬ LÝ ĐÓNG MODAL
closeElements.forEach(el => {
  el.addEventListener("click", () => {
    if (modal) modal.classList.remove("show");
  });
});

window.addEventListener("click", (e) => {
  if (e.target == modal) modal.classList.remove("show");
});

// 7. XỬ LÝ NÚT LƯU (SUBMIT FORM) - LOGIC CRUD Ở ĐÂY
if (productForm) {
  productForm.addEventListener("submit", (e) => {
    e.preventDefault();

    // Lấy dữ liệu từ form
    const id = document.getElementById("prodId").value; // Lấy ID ẩn
    const name = document.getElementById("prodName").value;
    const category = document.getElementById("prodCategory").value;
    const price = Number(document.getElementById("prodPrice").value);
    const imageUrl = document.getElementById("prodImage").value;
    const isAvailable = document.getElementById("prodStatus").checked;

    if (id) {
      // === TRƯỜNG HỢP SỬA (UPDATE) ===
      // Tìm vị trí món đó trong mảng
      const index = productsData.findIndex(p => p.id === id);
      if (index !== -1) {
        // Cập nhật dữ liệu mới
        productsData[index] = {
          ...productsData[index], // Giữ lại các thông tin cũ nếu có
          name, category, price, imageUrl, isAvailable
        };
        alert("Đã cập nhật thành công!");
      }
    } else {
      // === TRƯỜNG HỢP THÊM MỚI (CREATE) ===
      const newProduct = {
        id: "sp" + Date.now(),
        name, category, price, imageUrl, isAvailable
      };
      productsData.unshift(newProduct); // Thêm vào đầu danh sách
      alert("Đã thêm món mới!");
    }

    // Vẽ lại bảng và đóng modal
    renderTable();
    if (modal) modal.classList.remove("show");
  });
}

// 8. XỬ LÝ XÓA (DELETE)
window.deleteProduct = (id) => {
  if (confirm("Bạn có chắc chắn muốn xóa sản phẩm này không?")) {
    productsData = productsData.filter(p => p.id !== id);
    renderTable();
  }
};

// Chạy lần đầu
renderTable();