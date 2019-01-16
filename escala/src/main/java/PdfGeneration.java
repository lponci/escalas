import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class PdfGeneration {

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