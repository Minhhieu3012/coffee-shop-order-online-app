// admin-web/js/users.js
import { db } from "./firebase-config.js";
import { collection, getDocs, deleteDoc, doc } from "https://www.gstatic.com/firebasejs/10.8.0/firebase-firestore.js";

let usersData = [];

async function fetchUsers() {
  try {
    const querySnapshot = await getDocs(collection(db, "users"));
    usersData = querySnapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
    renderUserTable(usersData);
  } catch (error) {
    console.error("Lỗi lấy users:", error);
  }
}

async function removeUser(id) {
  try {
    await deleteDoc(doc(db, "users", id));
    fetchUsers();
  } catch (e) { alert("Lỗi xóa: " + e.message); }
}

function normalizeStr(str) {
  if (!str) return "";
  return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').replace(/đ/g, 'd').replace(/Đ/g, 'D').toLowerCase().trim();
}

function renderUserTable(data = usersData) {
  const tableBody = document.getElementById("userTableBody");
  if (!tableBody) return;
  tableBody.innerHTML = "";

  if (data.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; padding: 2rem;">Không có user nào</td></tr>`;
    return;
  }

  data.forEach(user => {
    const row = document.createElement("tr");
    const roleBadge = user.role === 'admin' ? `<span class="badge badge-admin" style="background:#e0e7ff; color:#3730a3;">Admin</span>` : `<span class="badge badge-customer" style="background:#f3f4f6; color:#374151;">Khách</span>`;
    const avatarSrc = user.photoUrl || 'assets/logo.png';

    row.innerHTML = `
      <td>
        <div style="display: flex; align-items: center; gap: 12px;">
          <img src="${avatarSrc}" class="user-avatar" onerror="this.src='assets/logo.png'">
          <div><div style="font-weight: 600;">${user.displayName || "Chưa đặt tên"}</div><small>${user.email}</small></div>
        </div>
      </td>
      <td>${roleBadge}</td>
      <td>${user.phoneNumber || "Trống"}</td>
      <td style="text-align: center;">
        ${user.role !== 'admin' ? `<button class="action-btn text-red" onclick="window.deleteUser('${user.id}')"><i class="fa-solid fa-trash"></i></button>` : ''}
      </td>
    `;
    tableBody.appendChild(row);
  });
}

const searchInput = document.getElementById("searchUser");
if (searchInput) {
  searchInput.addEventListener("input", (e) => {
    const keyword = normalizeStr(e.target.value);
    const filtered = usersData.filter(u => normalizeStr(u.displayName).includes(keyword) || normalizeStr(u.email).includes(keyword));
    renderUserTable(filtered);
  });
}

window.deleteUser = async (id) => {
  if (confirm("Xóa user này khỏi database?")) {
    await removeUser(id);
  }
};

fetchUsers();