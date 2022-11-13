import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.zip.GZIPInputStream;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 * Java class that parses a list of Ethereum blocks (in compressed JSON format)
 * and computes statistics about TX receipts.
 * The output is a CSV file with one line for each block.
 * Each line is formatted as follows:
 * 
 * <blockId>,<txCount>,<numLogs>,<numKeys>
 * 
 * @author Matteo Loporchio
 */
public class ReceiptStats {
	public static final Gson gson = new Gson();
	public static final int BUFSIZE = 65536;

	public static void main(String[] args) {
		// Check and parse command-line arguments.
		if (args.length < 2) {
			System.err.println("Usage: ReceiptStats <inputFile> <outputFile>");
			System.exit(1);
		}
		final String inputFile = args[0], outputFile = args[1];
		// Read GZIP-compressed JSON file.
		try (
			JsonReader reader = new JsonReader(
				new InputStreamReader(
					new GZIPInputStream(
						new FileInputStream(inputFile), BUFSIZE)));
			PrintWriter out = new PrintWriter(outputFile);
		) {
			reader.beginArray();
			while (reader.hasNext()) {
				// Deserialize block and extract all fields of interest.
				Block b = gson.fromJson(reader, Block.class);
				int blockId = Integer.decode(b.number), txCount = 0, numLogs = 0, numKeys = 0;
				// Examine all transactions in the block (if any).
				if (b.transactions != null) {
					txCount = b.transactions.size();
					// For each transaction, check its logs.
					for (Transaction t : b.transactions) {
						if (t.logs != null) {
							numLogs += t.logs.size();
							for (Log l : t.logs) {
								if (l.address == null) 
									System.err.printf("Null log address: blockId=%d, txId=%s, logIndex=%s\n", blockId, t.hash, l.logIndex);
								else
									numKeys += 1;
								if (l.topics == null) 
									System.err.printf("Null log topics: blockId=%d, txId=%s, logIndex=%s\n", blockId, t.hash, l.logIndex);
								else
									numKeys += l.topics.size();
							}
						}
					}
				}
				out.printf("%d,%d,%d,%d\n", blockId, txCount, numLogs, numKeys);
			}
			reader.endArray();
		}
		catch (Exception e) {
			System.err.printf("Error: %s\n", e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

}
