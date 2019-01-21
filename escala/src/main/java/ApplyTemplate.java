import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;

public class ApplyTemplate {

    public static void apply(ByteArrayOutputStream btOs, String fundo, String destPDF) throws IOException, DocumentException {
        Document destDoc = new Document(PageSize.A4.rotate(), 10f, 10f, 10f, 0f);
        PdfWriter destWriter = PdfWriter.getInstance(destDoc, new FileOutputStream(destPDF));

        destDoc.open();
        PdfContentByte dcb = destWriter.getDirectContent();
        PdfContentByte ucb = destWriter.getDirectContentUnder();
        PdfReader mainDocReader = new PdfReader(new ByteArrayInputStream(btOs.toByteArray()));
        PdfReader singlePageBackgroundReader = new PdfReader(fundo);
        PdfImportedPage backgroundPage = destWriter.getImportedPage(singlePageBackgroundReader, 1);
        for (int i = 1; i <= mainDocReader.getNumberOfPages(); i++) {
            destDoc.newPage();
            PdfImportedPage mainDocPage = destWriter.getImportedPage(mainDocReader, i);
            dcb.addTemplate(mainDocPage, 0, 0);
            ucb.addTemplate(backgroundPage, 0, 0);
        }
        destDoc.close();
    }
}