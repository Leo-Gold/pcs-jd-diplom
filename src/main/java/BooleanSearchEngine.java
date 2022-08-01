import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private HashMap<String, List<PageEntry>> database = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        for (File file : Objects.requireNonNull(pdfsDir.listFiles())) {
                PdfDocument doc = new PdfDocument(new PdfReader(file));

                for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                    String text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                    List<String> words = Arrays.asList(text.split("\\P{IsAlphabetic}+"));
                    Map<String, Integer> freqs = calculateFreqs(words);

                    for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                        String word = entry.getKey();
                        Integer count = entry.getValue();

                        List<PageEntry> entries = database.getOrDefault(word, new ArrayList<>());
                        entries.add(new PageEntry(file.getName(), i, count));

                        Collections.sort(entries);
                        database.put(word, entries);
                    }
                }
            }
    }

    private Map<String, Integer> calculateFreqs(List<String> words) {
        Map<String, Integer> freqs = new HashMap<>();
        for (var word : words) {
            if (word.isEmpty()) {
                continue;
            }
            freqs.put(word.toLowerCase(), freqs.getOrDefault(word.toLowerCase(), 0) + 1);
        }

        return freqs;
    }

    @Override
    public List<PageEntry> search(String word) {
        return database.getOrDefault(word, Collections.emptyList());
    }
}
