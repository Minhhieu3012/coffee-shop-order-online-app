import { onDocumentCreated } from "firebase-functions/v2/firestore";
import { setGlobalOptions } from "firebase-functions/v2";
import * as admin from "firebase-admin";
import { FieldValue } from "firebase-admin/firestore";

// Khởi tạo Firebase Admin SDK
admin.initializeApp();

// Tạo các "lối tắt"
const db = admin.firestore();
const fcm = admin.messaging();

setGlobalOptions({ region: "asia-southeast1" });

export const onordercreated = onDocumentCreated(
  "orders/{orderId}",

  // 2. Hàm xử lý (event)
  async (event) => {
    // 'event' (sự kiện) bao gồm mọi thứ (snapshot, context, params)

    const snapshot = event.data;
    if (!snapshot) {
      console.log("Không có snapshot, có thể document đã bị xóa?");
      return null;
    }

    // Lấy orderId từ 'event.params'
    const orderId = event.params.orderId;

    // 1. Lấy dữ liệu đơn hàng mới
    const orderData = snapshot.data();

    if (!orderData) {
      console.log("Không có dữ liệu đơn hàng.");
      return null;
    }

    console.log(`Đang xử lý đơn hàng mới: ${orderId}`);

    // 2. Tìm tất cả Admin
    const adminQuery = await db
      .collection("users")
      .where("role", "==", "admin")
      .get();

    if (adminQuery.empty) {
      console.log("Không tìm thấy admin nào để gửi thông báo.");
      return null;
    }

    // Tạo nội dung thông báo
    const payload: admin.messaging.Notification = {
      title: "📣 Có đơn hàng mới!",
      body: `Khách hàng "${
        orderData.customerName
      }" vừa đặt đơn #${orderId.substring(0, 5)}...`,
    };

    // Mảng chứa các token của tất cả admin
    const tokens: string[] = [];

    // 3. Lấy fcmToken của từng admin và lưu notification
    for (const doc of adminQuery.docs) {
      const adminUser = doc.data();
      if (adminUser.fcmToken) {
        // Thêm token của admin vào mảng
        tokens.push(adminUser.fcmToken);

        // 5. Lưu vào collection 'notifications' cho từng admin
        // (await) Đảm bảo việc ghi vào DB hoàn tất trước khi tiếp tục
        await db.collection("notifications").add({
          userId: doc.id,
          title: payload.title,
          body: payload.body,
          orderId: orderId,
          isRead: false,
          createdAt: FieldValue.serverTimestamp(),
        });
      }
    }

    if (tokens.length === 0) {
      console.log("Không có admin nào có fcmToken để gửi.");
      return null;
    }

    // 4. Gửi thông báo FCM
    try {
      // Dùng sendEachForMulticast (gửi cho nhiều người) là chính xác
      const message: admin.messaging.MulticastMessage = {
        notification: payload,
        data: {
          orderId: orderId,
          click_action: "FLUTTER_NOTIFICATION_CLICK",
        },
        tokens: tokens,
      };

      const response = await fcm.sendEachForMulticast(message);

      console.log(
        `Gửi thông báo thành công. Số token: ${tokens.length},
         Thành công: ${response.successCount}, 
         Thất bại: ${response.failureCount}`
      );
    } catch (error) {
      console.error("Lỗi khi gửi thông báo FCM:", error);
    }

    return null;
  }
);
