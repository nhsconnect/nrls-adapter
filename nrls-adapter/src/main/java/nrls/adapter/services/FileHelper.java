package nrls.adapter.services;

import com.thoughtworks.xstream.XStream;
import java.io.FileWriter;
import java.io.IOException;
import org.jboss.logging.Logger;

public class FileHelper {

    private static final Logger LOG = Logger.getLogger(FileHelper.class);
    
    public static void writeObjectToFileAsXML(String filePath, Object object) {
        
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath);
            XStream xstream = new XStream();        // Using 'XStream' as it produces more readable XML output than JAXB and is quicker that XMLEncoder
            xstream.toXML(object, fileWriter);
        } catch (IOException ex) {
            LOG.error("Error converting object to XML when saving to file: " + ex.getMessage());
        } finally {
            try {
                if (null != fileWriter) {
                    fileWriter.close();
                }
            } catch (IOException fileCloseEx) {
                LOG.error("Error closing FileWriter after writing xml to file: " + fileCloseEx.getMessage());
            }
        }
    }

}
