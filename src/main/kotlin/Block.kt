class Block {
    var version: Byte = 0;

    var totalAmount: Long = 0;
    var totalFee: Long = 0;
    var reward: Long = 0;

    var previousBlock: String = "";
    var payloadHash: String = "";
    var generatorPublicKey: String = "";

    var height: Int = 0;
    var timestamp: Int = 0;
    var numberOfTransactions: Int = 0;
    var payloadLength: Int = 0;
    var size: Int = 0;

    var transactions: List<Transaction>? = null;
    var transactionIDs: List<Transaction>? = null;

    var blockSigniture: String = "";
    var id: String = "";
}