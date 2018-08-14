package nrls.adapter.services;

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jboss.logging.Logger;

public class FileHelper {

    private static final Logger LOG = Logger.getLogger(FileHelper.class);
    
    public static boolean writeObjectToFileAsXML(String filePath, Object object) {
        
        boolean success = true;
        FileWriter fileWriter = null;
        
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            fileWriter = new FileWriter(file);
            XStream xstream = new XStream();        // Using 'XStream' as it produces more readable XML output than JAXB and is quicker that XMLEncoder
            xstream.toXML(object, fileWriter);
        } catch (IOException ex) {
            LOG.error("Error converting object to XML when saving to file: " + ex.getMessage());
            success = false;
        } finally {
            try {
                if (null != fileWriter) {
                    fileWriter.close();
                }
            } catch (IOException fileCloseEx) {
                LOG.error("Error closing FileWriter after writing xml to file: " + fileCloseEx.getMessage());
                success = false;
            }
        }
        
        return success;
    }

}
