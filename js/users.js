// js/users.js
const tbody = document.querySelector("#page-users tbody");

export function renderUsers() {
  tbody.innerHTML = "";

  const fakeUsers = [
    { id: "U001", name: "Nguyễn Văn Admin", email: "admin@broscafe.vn", role: "admin", joined: "2024-11-01" },
    { id: "U002", name: "Trần Thị Nhân Viên", email: "nv1@broscafe.vn", role: "staff", joined: "2025-01-15" },
    { id: "U003", name: "Lê Văn Khách", email: "khach@gmail.com", role: "customer", joined: "2025-03-10" },
    { id: "U004", name: "Phạm Ngọc Ánh", email: "anhpn@broscafe.vn", role: "staff", joined: "2025-02-20" },
    { id: "U005", name: "Hoàng Minh Quân", email: "quan@gmail.com", role: "customer", joined: "2025-04-05" },
  ];

  fakeUsers.forEach(user => {
    const roleText = user.role === "admin" ? "Quản trị" : user.role === "staff" ? "Nhân viên" : "Khách hàng";
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${user.id}</td>
      <td>${user.name}</td>
      <td>${user.email}</td>
      <td><span class="badge badge-${user.role}">${roleText}</span></td>
      <td>${new Date(user.joined).toLocaleDateString('vi-VN')}</td>
      <td class="actions">
        <button class="btn-small btn-edit"><i class="fa-solid fa-edit"></i></button>
        <button class="btn-small btn-delete"><i class="fa-solid fa-trash"></i></button>
      </td>
    `;
    tbody.appendChild(tr);
  });
}