import java.util.List;

/**
 * Represents a TX log.
 * 
 * @author Matteo Loporchio
 */
public class Log {
	public final String address;
	public final List<String> topics;
	public final String logIndex;
	
	public Log(String address, List<String> topics, String logIndex) {
		this.address = address;
		this.topics = topics;
		this.logIndex = logIndex;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (address != null) sb.append(address);
		if (topics != null) {
			for (int i = 0; i < topics.size(); i++) {
				sb.append("," + topics.get(i));
			}
		}
		return sb.toString();
	}
}
