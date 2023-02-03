import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class PDFTableReader extends PDFTextStripper {

    public PDFTableReader() throws IOException {
        super.setSortByPosition(true);
    }

    public static void main(String[] args) throws IOException {
        File file = new File("path/to/your/pdf/file.pdf");
        PDDocument document = PDDocument.load(file);
        PDFTableReader reader = new PDFTableReader();
        reader.stripPage(document.getPage(0));
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        float lastX = -1;
        float lastY = -1;
        List<String> line = new ArrayList<>();
        for (TextPosition text : textPositions) {
            float x = text.getXDirAdj();
            float y = text.getYDirAdj();
            if (lastX != -1 && lastY != -1) {
                // Check if we're starting a new row
                if (Math.abs(y - lastY) > text.getFontSize() * 1.5f) {
                    // Print the current line
                    System.out.println(String.join("\t", line));
                    line.clear();
                }
            }
            line.add(text.getUnicode());
            lastX = x;
            lastY = y;
        }
        // Print the last line
        System.out.println(String.join("\t", line));
    }
}
