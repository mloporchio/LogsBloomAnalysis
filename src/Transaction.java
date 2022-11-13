import java.util.List;

/**
 * Represents an Ethereum transaction.
 * 
 * @author Matteo Loporchio
 */
public class Transaction {
	public final String hash;
	public final String blockNumber;
	public final String blockHash;
	public final List<Log> logs;
	
	public Transaction(String hash, String blockNumber, String blockHash, List<Log> logs) {
		this.hash = hash;
		this.blockNumber = blockNumber;
		this.blockHash = blockHash;
		this.logs = logs;
	}
	
}
