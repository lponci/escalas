import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;

public class PdfGeneration {

    public static ByteArrayOutputStream generatePDF(String xml, String xsl) {
        ByteArrayOutputStream btOs = new ByteArrayOutputStream();
        try {
            PdfGeneration pdfGeneration = new PdfGeneration();
            InputStream xslInput = new FileInputStream(xsl);
            InputStream xmlInput = new ByteArrayInputStream(xml.getBytes());
            btOs.writeTo(pdfGeneration.generate(xmlInput, xslInput, btOs, MimeConstants.MIME_PDF));
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return btOs;
    }

    public OutputStream generate(InputStream xml, InputStream xsl, OutputStream pdfContent, String mimePdf) {
        try {
            StreamSource xmlSource = new StreamSource(xml);
            StreamSource xslSource = new StreamSource(xsl);

            // get transformer
            TransformerFactory tfactory = TransformerFactory.newInstance();
            Transformer transformer = tfactory.newTransformer(xslSource);

            // setup FOP
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            foUserAgent.setProducer(this.getClass().getName());
            Fop fop = fopFactory.newFop(mimePdf, foUserAgent, pdfContent);

            // perform transformation
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(xmlSource, res);
        } catch (FOPException | TransformerException e) {
            e.printStackTrace();
        }
       return pdfContent;
    }
}