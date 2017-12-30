data class Account(var address: String?,
                   var publicKey: String?,
                   var username: String?,
                   var balance: Long = 0,
                   var vote: Long?,
                   var votes: List<Long>?)
{
    fun applyTransaction(transaction: Transaction): Boolean
    {
        balance -= transaction.amount + transaction.fee
        return (balance > -1)
    }

    fun undoTransaction(transaction: Transaction): Boolean
    {
        balance += transaction.amount + transaction.fee
        return (balance > -1)
    }

    fun verifyTransaction(transaction: Transaction): Verification
    {
        if (balance < transaction.amount + transaction.fee)
             "Account $address does not have enough balance: $balance"

        return Verification()
    }
}

