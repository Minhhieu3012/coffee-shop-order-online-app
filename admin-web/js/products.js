// admin-web/js/products.js
import { db } from "./firebase-config.js";
import { 
  collection, addDoc, getDocs, doc, deleteDoc, updateDoc 
} from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

// Biến chứa dữ liệu
let productsData = [];

// --- API FIREBASE ---
async function fetchProducts() {
  try {
    const querySnapshot = await getDocs(collection(db, "products"));
    productsData = querySnapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
    renderTable(productsData);
  } catch (error) {
    console.error("Lỗi lấy sản phẩm:", error);
  }
}

async function createProduct(productObj) {
  try {
    await addDoc(collection(db, "products"), productObj);
    alert("Đã lưu thành công lên Firebase!");
    fetchProducts();
    return true;
  } catch (e) {
    alert("Lỗi: " + e.message);
    return false;
  }
}

async function updateProduct(id, productObj) {
  try {
    await updateDoc(doc(db, "products", id), productObj);
    alert("Đã cập nhật thành công!");
    fetchProducts();
    return true;
  } catch (e) {
    alert("Lỗi cập nhật: " + e.message);
    return false;
  }
}

async function removeProduct(id) {
  try {
    await deleteDoc(doc(db, "products", id));
    fetchProducts();
  } catch (e) {
    alert("Lỗi xóa: " + e.message);
  }
}

// --- RENDER & LOGIC ---
function normalizeStr(str) {
  if (!str) return "";
  return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/đ/g, 'd').replace(/Đ/g, 'D').toLowerCase().trim();
}

function renderTable(data = productsData) {
  const tableBody = document.getElementById("productTableBody");
  if (!tableBody) return;
  tableBody.innerHTML = "";

  if (data.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="6" style="text-align:center; padding: 2rem;">Không có dữ liệu</td></tr>`;
    return;
  }

  data.forEach(product => {
    const row = document.createElement("tr");
    const formattedPrice = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(product.price);
    const statusBadge = product.isAvailable ? `<span class="badge badge-success">Còn hàng</span>` : `<span class="badge badge-danger">Hết hàng</span>`;
    const imgUrl = product.imageUrl || 'assets/logo.png';

    row.innerHTML = `
      <td><img src="${imgUrl}" class="product-img" onerror="this.src='assets/logo.png'"></td>
      <td><div style="font-weight: 500;">${product.name}</div><small style="color: #888;">ID: ...${product.id.slice(-5)}</small></td>
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

// Search
const searchInput = document.getElementById("searchProduct");
if (searchInput) {
  searchInput.addEventListener("input", (e) => {
    const keyword = normalizeStr(e.target.value);
    const filteredData = productsData.filter(p => normalizeStr(p.name).includes(keyword));
    renderTable(filteredData);
  });
}

// Modal & Form
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

closeElements.forEach(el => el.addEventListener("click", () => modal.classList.remove("show")));
window.addEventListener("click", (e) => { if (e.target == modal) modal.classList.remove("show"); });

if (productForm) {
  productForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = document.getElementById("prodId").value;
    const productObj = {
      name: document.getElementById("prodName").value,
      category: document.getElementById("prodCategory").value,
      price: Number(document.getElementById("prodPrice").value),
      imageUrl: document.getElementById("prodImage").value,
      isAvailable: document.getElementById("prodStatus").checked
    };

    let success = false;
    if (id) {
      success = await updateProduct(id, productObj);
    } else {
      success = await createProduct(productObj);
    }

    if (success && modal) modal.classList.remove("show");
  });
}

window.deleteProduct = async (id) => {
  if (confirm("Xóa sản phẩm này?")) {
    await removeProduct(id);
  }
};

fetchProducts();