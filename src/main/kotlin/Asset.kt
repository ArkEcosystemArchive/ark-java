/**
 * This data class contains optional fields that a Transaction object can include.
 * These fields are required for type 1, 2, and 3 transactions
 */
data class Asset(var votes: List<String>? = null,
                 var username: String? = null,
                 var signature: String? = null)