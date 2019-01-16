import org.apache.fop.apps.*;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class PdfGeneration {
    public static final String RESOURCES_DIR;
    public static final String OUTPUT_DIR;

    static {
        RESOURCES_DIR = "src//main//resources//";
        OUTPUT_DIR = "src//main//resources//output//";
    }

    public void convertToPDF(InputStream xmlAsStream) throws IOException, FOPException, TransformerException {
        // the XSL FO file
        File xsltFile = new File(RESOURCES_DIR + "//template.xsl");
        // the XML file which provides the input
        // new File(RESOURCES_DIR + "//escala.xml")
        StreamSource xmlSource = new StreamSource(xmlAsStream);
        // create an instance of fop factory
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        // a user agent is needed for transformation
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // Setup output

        OutputStream out = new FileOutputStream(OUTPUT_DIR + "//output.pdf");

        try {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));

            // Resulting SAX events (the generated FO) must be piped through to
            // FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            // That's where the XML is first transformed to XSL-FO and then
            // PDF is created
            transformer.transform(xmlSource, res);
            mergePDF();
        } finally {
            out.close();
        }
    }

    public ByteArrayOutputStream generate(InputStream xml, OutputStream pdfContent) {
        try {
            // setup xml input source
            StreamSource xmlSource = new StreamSource(xml);

            // setup xsl stylesheet source
            File xslFile = new File(RESOURCES_DIR + "//template.xsl");
            FileInputStream xslFileStream = new FileInputStream(xslFile);
            StreamSource xslSource = new StreamSource(xslFileStream);

            // get transformer
            TransformerFactory tfactory = TransformerFactory.newInstance();
            Transformer transformer = tfactory.newTransformer(xslSource);

            // setup FOP
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            foUserAgent.setProducer(this.getClass().getName());
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, pdfContent);

            // perform transformation
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(xmlSource, res);
        } catch (FileNotFoundException e) { e.printStackTrace();
        } catch (TransformerException e) {  e.printStackTrace();
        } catch (FOPException e) { e.printStackTrace();
        }
        return new ByteArrayOutputStream();
    }


    private void mergePDF(){
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        try {
            pdfMergerUtility.addSource(OUTPUT_DIR+ "/output.pdf");
            pdfMergerUtility.addSource(RESOURCES_DIR + "/template.pdf");
            pdfMergerUtility.setDestinationFileName(OUTPUT_DIR + "Final.pdf");
            pdfMergerUtility.mergeDocuments();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}