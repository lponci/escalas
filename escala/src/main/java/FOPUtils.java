/*
 * Copyright (c) 1999-2010 Touch Tecnologia e Informatica Ltda.
 *
 * R. Gomes de Carvalho, 1666, 3o. Andar, Vila Olimpia, Sao Paulo, SP, Brasil.
 *
 * Todos os direitos reservados.
 * Este software e confidencial e de propriedade da Touch Tecnologia e Informatica Ltda. (Informacao Confidencial)
 * As informacoes contidas neste arquivo nao podem ser publicadas, e seu uso esta limitado de acordo
 * com os termos do contrato de licenca.
 */

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xmlgraphics.util.MimeConstants;
import org.jdom.Document;
import org.jdom.transform.JDOMSource;

import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Classe utilitária para realizar transformações utilizando a FOP
 * 
 * @uthor Fernando Kasten Peinado
 * @author $Author$
 * @version $Revision$ Modificado $Date$
 */
public class FOPUtils {

	/***/
	public static final String PDF = MimeConstants.MIME_PDF;
	/***/
	public static final String PCL = MimeConstants.MIME_PCL;
	/***/
	public static final String PS = MimeConstants.MIME_POSTSCRIPT;
	/***/
	public static final String PLAIN_TEXT = MimeConstants.MIME_PLAIN_TEXT;

//	private static Logger LOGGER = Logger.getLogger(FOPUtils.class);

	/**
	 * Transforma um xml em PDF usando o xsl dado. A saida, é armazenada no stream out.
	 * 
	 * @param xml
	 *            O stream que contém o xml em forma de texto
	 * @param xsl
	 *            O stream que contém o xsl em forma de texto
	 * @param out
	 *            O stream que armazenará a saida gerada
	 * @exception TransformerException
	 * @exception FOPException
	 * @exception IOException
	 *
	 * 	FOPUtils foputils = new FOPUtils();
	 * 	ByteArrayInputStream in = new ByteArrayInputStream(xmlSaida.getBytes());
	 * 	ByteArrayOutputStream out = new ByteArrayOutputStream();
	 *
	 */
	public void transformPDF(InputStream xml, InputStream xsl, OutputStream out) throws TransformerException, FOPException, IOException {

		// Realiza a transformação
		this.transform(xml, xsl, out, PDF);
	}

	/**
	 * Transforma um xml em PDF usando o xsl dado. A saida, é armazenada no stream out.
	 * 
	 * @param xml
	 *            O stream que contém o xml em forma de texto
	 * @param xsl
	 *            O strean que contém o xsl em forma de texto
	 * @param out
	 *            O strean que armazenará a saida gerada
	 * @exception TransformerException
	 * @exception FOPException
	 * @exception IOException
	 */
	public void transformDocumentPDF(Document xml, InputStream xsl, OutputStream out) throws TransformerException, FOPException, IOException {

		// Realiza a transformação
		this.transformDocument(xml, xsl, out, PDF);
	}

	/**
	 * Transforma um xml para um formato especificado.
	 * 
	 * @param xml
	 *            O stream que contém o xml em forma de texto
	 * @param xsl
	 *            O stream que contém o xsl em forma de texto
	 * @param out
	 *            O stream que armazenará a saida gerada
	 * @param format
	 *            O formato do arquivo de saida: PDF, PCL ou PS
	 * @exception TransformerException
	 * @exception FOPException
	 * @exception IOException
	 */
	public void transform(InputStream xml, InputStream xsl, OutputStream out, String format)
			throws TransformerException, FOPException, IOException {

		ByteArrayOutputStream outFo = new ByteArrayOutputStream();

		// Realiza a transformação para .fo
		TransformerFactory.newInstance().newTransformer(new StreamSource(xsl)).transform(new StreamSource(xml), new StreamResult(outFo));

		this.transformFO(outFo, out, format);
	}

	/**
	 * Transforma um xml para um formato especificado.
	 * 
	 * @param xml
	 *            O Document xml com o laudo
	 * @param xsl
	 *            O stream que contém o xsl em forma de texto
	 * @param out
	 *            O stream que armazenará a saida gerada
	 * @param format
	 *            O formato do arquivo de saida: PDF, PCL ou PS
	 * @exception TransformerException
	 * @exception FOPException
	 * @exception IOException
	 */
	public void transformDocument(Document xml, InputStream xsl, OutputStream out, String format)
			throws TransformerException, FOPException, IOException {

		ByteArrayOutputStream outFo = new ByteArrayOutputStream();

		Source template = new StreamSource(xsl);
		Source source = new JDOMSource(xml);
		Result result = new StreamResult(outFo);

		// Realiza a transformação para .fo
		TransformerFactory factory = TransformerFactory.newInstance();
		factory.setErrorListener(new DefaultErrorHandler(true));
		Transformer transformer = factory.newTransformer(template);
		transformer.transform(source, result);

		// Realiza a transformação de .fo para o resultado desejado
		this.transformFO(outFo, out, format);
	}

	/**
	 * Transforma o conteudo do FO para PDF colocado no Out.
	 * 
	 * @param fo
	 * @param out
	 * @param format
	 * @throws IOException
	 * @throws FOPException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 * @throws TransformerConfigurationException
	 */
	public void transformFO(ByteArrayOutputStream fo, OutputStream out, String format) throws IOException, FOPException, TransformerException {

		// Realiza a transformação de .fo para o resultado desejado
		FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
		
//		try {
//			InputStream configIS = Thread.currentThread().getContextClassLoader().getResourceAsStream("fop.xconf");
//			if (configIS != null) {
//				DefaultConfigurationBuilder configBuilder = new DefaultConfigurationBuilder();
//                Configuration config = configBuilder.build(configIS);
//                fopFactory.setUserConfig(config);
//			}
//		} catch (Exception e) {
//			LOGGER.warn("Erro ao ler arquivo customizado de configuracoes da FOP");
//			LOGGER.warn(e.getMessage(), e);
//		}
		
		Fop fop = fopFactory.newFop(format, out);

		System.out.println("--------------------INICIO FO------------------------");
		System.out.println(new String(fo.toByteArray()));
		System.out.println("--------------------FIM FO------------------------");

		Source source = new StreamSource(new ByteArrayInputStream(fo.toByteArray()));
		Result result = new SAXResult(fop.getDefaultHandler());

		TransformerFactory.newInstance().newTransformer().transform(source, result);

		fo.close();

	}

}
