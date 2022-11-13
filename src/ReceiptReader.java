import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

/**
 * This program parses a list of Ethereum blocks (in compressed JSON format)
 * and extracts information about TX receipts.
 * @author Matteo Loporchio
 */
public class ReceiptReader {
	public static final Gson gson = new Gson();
	public static final int BUFSIZE = 65536;
	
	public static void main(String[] args) {
		// Check and parse command-line arguments.
		if (args.length < 2) {
			System.err.println("Usage: ReceiptReader <inputFile> <outputFile>");
			System.exit(1);
		}
		final String inputFile = args[0], outputFile = args[1];
		// Read GZIP-compressed JSON file.
		try (
			JsonReader reader = new JsonReader(
					new InputStreamReader(
							new GZIPInputStream(
									new FileInputStream(inputFile), BUFSIZE)));
			PrintWriter out = new PrintWriter(new GZIPOutputStream(new FileOutputStream(outputFile)));
		) {
			reader.beginArray();
			while (reader.hasNext()) {
				// Deserialize block and extract all fields of interest.
				Block b = gson.fromJson(reader, Block.class);
				int blockId = Integer.decode(b.number);
				// If there are no transactions, just write the block id.
				if (b.transactions == null) {
					out.printf("%d\n", blockId);
					continue;
				}
				// Examine all transactions in the block.
				for (Transaction t : b.transactions) {
					String txHash = t.hash;
					// Examine all logs for that transaction.
					List<Log> txLogs = t.logs;
					if (txLogs != null) {
						StringBuilder logStr = new StringBuilder();
						logStr.append(txLogs.get(0));
						for (int i = 1; i < txLogs.size(); i++)
							logStr.append(";" + txLogs.get(i).toString());
						// Write one line for each transaction.
						out.printf("%d:%s:%s\n", blockId, txHash, logStr.toString());
					}
					// If there are no logs, just write block id and TX hash.
					else out.printf("%d:%s\n", blockId, txHash);
				}				
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
