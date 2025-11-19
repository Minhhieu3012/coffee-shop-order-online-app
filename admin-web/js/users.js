// admin-web/js/users.js

// 1. DỮ LIỆU GIẢ
let usersData = [
  { id: "admin_01", displayName: "Admin (Bạn)", email: "admin@bros.com", photoUrl: "assets/logo.png", role: "admin", phoneNumber: "0909.123.456" },
  { id: "u001", displayName: "Nguyễn Văn A", email: "khach.a@gmail.com", photoUrl: "https://i.pravatar.cc/150?img=3", role: "customer", phoneNumber: "0912.345.678" },
  { id: "u002", displayName: "Trần Thị B", email: "khach.b@gmail.com", photoUrl: "https://i.pravatar.cc/150?img=5", role: "customer", phoneNumber: "0933.444.555" }
];

// 2. HÀM RENDER (Nhận tham số data)
function renderUserTable(data = usersData) {
  const tableBody = document.getElementById("userTableBody");
  if (!tableBody) return;

  tableBody.innerHTML = "";
  
  if (data.length === 0) {
    tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center; padding: 2rem;">Không tìm thấy user nào</td></tr>`;
    return;
  }

  data.forEach(user => {
    const row = document.createElement("tr");
    const roleBadge = user.role === 'admin' 
      ? `<span class="badge badge-admin" style="background:#e0e7ff; color:#3730a3;">Quản trị viên</span>` 
      : `<span class="badge badge-customer" style="background:#f3f4f6; color:#374151;">Khách hàng</span>`;
    const avatarSrc = user.photoUrl || 'assets/logo.png';

    row.innerHTML = `
      <td>
        <div style="display: flex; align-items: center; gap: 12px;">
          <img src="${avatarSrc}" class="user-avatar" onerror="this.src='assets/logo.png'">
          <div>
            <div style="font-weight: 600; color: #111827;">${user.displayName}</div>
            <small style="color: #6b7280;">${user.email}</small>
          </div>
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

// 3. LOGIC TÌM KIẾM USER (MỚI THÊM)
const searchInput = document.getElementById("searchUser");
if (searchInput) {
  searchInput.addEventListener("input", (e) => {
    const keyword = e.target.value.toLowerCase().trim();

    const filteredUsers = usersData.filter(user => {
      return user.displayName.toLowerCase().includes(keyword) ||
             user.email.toLowerCase().includes(keyword) ||
             (user.phoneNumber && user.phoneNumber.includes(keyword));
    });

    renderUserTable(filteredUsers);
  });
}

window.deleteUser = (id) => {
  if (confirm("Xóa người dùng này?")) {
    usersData = usersData.filter(u => u.id !== id);
    renderUserTable();
    searchInput.value = "";
  }
};

renderUserTable();
export { renderUserTable };