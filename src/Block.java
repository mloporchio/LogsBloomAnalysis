import java.util.List;

/**
 * Represents an Ethereum block.
 * 
 * @author Matteo Loporchio
 */
public class Block {
	public final String number;
	public final String logsBloom;
	public final String timestamp;
	public final List<Transaction> transactions;
	
	public Block(String number, String logsBloom, String timestamp, List<Transaction> transactions) {
		this.number = number;
		this.logsBloom = logsBloom;
		this.timestamp = timestamp;
		this.transactions = transactions;
	}
}