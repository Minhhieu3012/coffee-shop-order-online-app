package vn.edu.ut.hieupm9898.customermobile.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import vn.edu.ut.hieupm9898.customermobile.data.model.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    companion object {
        private const val TAG = "OrderRepository"
        private const val ORDERS_COLLECTION = "orders"
    }

    /**
     * Lấy danh sách đơn hàng đang xử lý
     * Status: PENDING, CONFIRMED, PREPARING, READY, DELIVERING
     *
     * ✅ FIXED: Không dùng whereIn + orderBy để tránh cần composite index
     */
    suspend fun getOngoingOrders(): Result<List<Order>> {
        return try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Result.failure(Exception("❌ User not logged in"))

            val ongoingStatuses = setOf(
                "PENDING", "CONFIRMED", "PREPARING", "READY", "DELIVERING"
            )

            // ✅ Chỉ dùng whereEqualTo, filter status ở client-side
            val snapshot = firestore.collection(ORDERS_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val orders = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Order::class.java)?.apply {
                        orderId = doc.id
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Error parsing order ${doc.id}: ${e.message}")
                    null
                }
            }
                .filter { it.status in ongoingStatuses } // Filter ở client
                .sortedByDescending { it.createdAt?.toDate()?.time ?: 0 } // Sort ở client

            Log.d(TAG, "✅ Loaded ${orders.size} ongoing orders")
            Result.success(orders)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error loading ongoing orders: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Lấy lịch sử đơn hàng
     * Status: COMPLETED, CANCELLED, FAILED
     *
     * ✅ FIXED: Không dùng whereIn + orderBy để tránh cần composite index
     */
    suspend fun getOrderHistory(): Result<List<Order>> {
        return try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Result.failure(Exception("❌ User not logged in"))

            val historyStatuses = setOf(
                "COMPLETED", "CANCELLED", "FAILED"
            )

            // ✅ Chỉ dùng whereEqualTo, filter status ở client-side
            val snapshot = firestore.collection(ORDERS_COLLECTION)
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val orders = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Order::class.java)?.apply {
                        orderId = doc.id
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Error parsing order ${doc.id}: ${e.message}")
                    null
                }
            }
                .filter { it.status in historyStatuses } // Filter ở client
                .sortedByDescending { it.createdAt?.toDate()?.time ?: 0 } // Sort ở client

            Log.d(TAG, "✅ Loaded ${orders.size} history orders")
            Result.success(orders)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error loading history orders: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Lấy tất cả đơn hàng của user
     */
    suspend fun getAllOrders(): Result<List<Order>> {
        return try {
            val userId = firebaseAuth.currentUser?.uid
                ?: return Result.failure(Exception("❌ User not logged in"))

            val snapshot = firestore.collection(ORDERS_COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val orders = snapshot.documents.mapNotNull { doc ->
                try {
                    doc.toObject(Order::class.java)?.apply {
                        orderId = doc.id
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Error parsing order ${doc.id}: ${e.message}")
                    null
                }
            }

            Log.d(TAG, "✅ Loaded ${orders.size} total orders")
            Result.success(orders)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error loading all orders: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Lấy chi tiết 1 đơn hàng
     */
    suspend fun getOrderById(orderId: String): Result<Order> {
        return try {
            val snapshot = firestore.collection(ORDERS_COLLECTION)
                .document(orderId)
                .get()
                .await()

            val order = snapshot.toObject(Order::class.java)?.apply {
                this.orderId = snapshot.id
            } ?: return Result.failure(Exception("❌ Order not found"))

            Log.d(TAG, "✅ Loaded order: $orderId")
            Result.success(order)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error loading order $orderId: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Hủy đơn hàng
     */
    suspend fun cancelOrder(orderId: String): Result<Unit> {
        return try {
            firestore.collection(ORDERS_COLLECTION)
                .document(orderId)
                .update(
                    mapOf(
                        "status" to "CANCELLED",
                        "updatedAt" to com.google.firebase.Timestamp.now()
                    )
                )
                .await()

            Log.d(TAG, "✅ Order cancelled: $orderId")
            Result.success(Unit)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Error cancelling order $orderId: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Observe orders real-time
     */
    fun observeUserOrders(): Flow<Result<List<Order>>> = callbackFlow {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            trySend(Result.failure(Exception("❌ User not logged in")))
            close()
            return@callbackFlow
        }

        val listenerRegistration = firestore.collection(ORDERS_COLLECTION)
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val orders = snapshot.documents.mapNotNull { doc ->
                        try {
                            doc.toObject(Order::class.java)?.apply {
                                orderId = doc.id
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "❌ Error parsing order ${doc.id}: ${e.message}")
                            null
                        }
                    }
                    trySend(Result.success(orders))
                    Log.d(TAG, "✅ Real-time: ${orders.size} orders")
                }
            }

        awaitClose {
            listenerRegistration.remove()
            Log.d(TAG, "🔴 Stopped observing orders")
        }
    }
}