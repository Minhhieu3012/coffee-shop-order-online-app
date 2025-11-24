package vn.edu.ut.hieupm9898.customermobile.utils

/**
 * Utility để xử lý tìm kiếm tiếng Việt không dấu
 */
object StringUtils {

    /**
     * Bỏ dấu tiếng Việt
     * Ví dụ: "Cà phê sữa đá" -> "ca phe sua da"
     */
    fun String.removeDiacritics(): String {
        val diacriticsMap = mapOf(
            // Chữ thường
            'à' to 'a', 'á' to 'a', 'ả' to 'a', 'ã' to 'a', 'ạ' to 'a',
            'ă' to 'a', 'ằ' to 'a', 'ắ' to 'a', 'ẳ' to 'a', 'ẵ' to 'a', 'ặ' to 'a',
            'â' to 'a', 'ầ' to 'a', 'ấ' to 'a', 'ẩ' to 'a', 'ẫ' to 'a', 'ậ' to 'a',

            'đ' to 'd',

            'è' to 'e', 'é' to 'e', 'ẻ' to 'e', 'ẽ' to 'e', 'ẹ' to 'e',
            'ê' to 'e', 'ề' to 'e', 'ế' to 'e', 'ể' to 'e', 'ễ' to 'e', 'ệ' to 'e',

            'ì' to 'i', 'í' to 'i', 'ỉ' to 'i', 'ĩ' to 'i', 'ị' to 'i',

            'ò' to 'o', 'ó' to 'o', 'ỏ' to 'o', 'õ' to 'o', 'ọ' to 'o',
            'ô' to 'o', 'ồ' to 'o', 'ố' to 'o', 'ổ' to 'o', 'ỗ' to 'o', 'ộ' to 'o',
            'ơ' to 'o', 'ờ' to 'o', 'ớ' to 'o', 'ở' to 'o', 'ỡ' to 'o', 'ợ' to 'o',

            'ù' to 'u', 'ú' to 'u', 'ủ' to 'u', 'ũ' to 'u', 'ụ' to 'u',
            'ư' to 'u', 'ừ' to 'u', 'ứ' to 'u', 'ử' to 'u', 'ữ' to 'u', 'ự' to 'u',

            'ỳ' to 'y', 'ý' to 'y', 'ỷ' to 'y', 'ỹ' to 'y', 'ỵ' to 'y',

            // Chữ hoa
            'À' to 'A', 'Á' to 'A', 'Ả' to 'A', 'Ã' to 'A', 'Ạ' to 'A',
            'Ă' to 'A', 'Ằ' to 'A', 'Ắ' to 'A', 'Ẳ' to 'A', 'Ẵ' to 'A', 'Ặ' to 'A',
            'Â' to 'A', 'Ầ' to 'A', 'Ấ' to 'A', 'Ẩ' to 'A', 'Ẫ' to 'A', 'Ậ' to 'A',

            'Đ' to 'D',

            'È' to 'E', 'É' to 'E', 'Ẻ' to 'E', 'Ẽ' to 'E', 'Ẹ' to 'E',
            'Ê' to 'E', 'Ề' to 'E', 'Ế' to 'E', 'Ể' to 'E', 'Ễ' to 'E', 'Ệ' to 'E',

            'Ì' to 'I', 'Í' to 'I', 'Ỉ' to 'I', 'Ĩ' to 'I', 'Ị' to 'I',

            'Ò' to 'O', 'Ó' to 'O', 'Ỏ' to 'O', 'Õ' to 'O', 'Ọ' to 'O',
            'Ô' to 'O', 'Ồ' to 'O', 'Ố' to 'O', 'Ổ' to 'O', 'Ỗ' to 'O', 'Ộ' to 'O',
            'Ơ' to 'O', 'Ờ' to 'O', 'Ớ' to 'O', 'Ở' to 'O', 'Ỡ' to 'O', 'Ợ' to 'O',

            'Ù' to 'U', 'Ú' to 'U', 'Ủ' to 'U', 'Ũ' to 'U', 'Ụ' to 'U',
            'Ư' to 'U', 'Ừ' to 'U', 'Ứ' to 'U', 'Ử' to 'U', 'Ữ' to 'U', 'Ự' to 'U',

            'Ỳ' to 'Y', 'Ý' to 'Y', 'Ỷ' to 'Y', 'Ỹ' to 'Y', 'Ỵ' to 'Y'
        )

        return this.map { char -> diacriticsMap[char] ?: char }.joinToString("")
    }

    /**
     * Chuẩn hóa chuỗi cho tìm kiếm
     * - Bỏ dấu
     * - Lowercase
     * - Trim khoảng trắng
     */
    fun String.normalizeForSearch(): String {
        return this.removeDiacritics().lowercase().trim()
    }

    /**
     * Kiểm tra chuỗi có chứa query không (không phân biệt dấu)
     */
    fun String.containsVietnamese(query: String): Boolean {
        return this.normalizeForSearch().contains(query.normalizeForSearch())
    }

    /**
     * Tính độ tương đồng giữa 2 chuỗi (0.0 - 1.0)
     * Dùng thuật toán Levenshtein Distance đơn giản
     */
    fun calculateSimilarity(s1: String, s2: String): Double {
        val str1 = s1.normalizeForSearch()
        val str2 = s2.normalizeForSearch()

        if (str1 == str2) return 1.0
        if (str1.isEmpty() || str2.isEmpty()) return 0.0

        val maxLength = maxOf(str1.length, str2.length)
        val distance = levenshteinDistance(str1, str2)

        return 1.0 - (distance.toDouble() / maxLength)
    }

    /**
     * Levenshtein Distance - Độ khác biệt giữa 2 chuỗi
     */
    private fun levenshteinDistance(s1: String, s2: String): Int {
        val costs = IntArray(s2.length + 1)

        for (j in costs.indices) {
            costs[j] = j
        }

        for (i in 1..s1.length) {
            costs[0] = i
            var nw = i - 1

            for (j in 1..s2.length) {
                val cj = minOf(
                    1 + minOf(costs[j], costs[j - 1]),
                    if (s1[i - 1] == s2[j - 1]) nw else nw + 1
                )
                nw = costs[j]
                costs[j] = cj
            }
        }

        return costs[s2.length]
    }
}