package nrls.adapter.services;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import nrls.adapter.model.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class FileHelper {

    private static final Logger LOG = Logger.getLogger(FileHelper.class);
    private FileReader reader;
    private ObjectInputStream inputStream;
    
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
    
    public ObjectInputStream getObjectInputStream(String filePath) {
    	
		try {
			reader = new FileReader(filePath);
			XStream xstream = new XStream(new StaxDriver());
			xstream.alias("Task", Task.class);
			xstream.processAnnotations(Task.class);
			inputStream =  xstream.createObjectInputStream(reader);
			
		} catch (FileNotFoundException fileNotFoundEx) {
			LOG.error("Error opening File: " + fileNotFoundEx.getMessage());
		} catch (IOException openingInputStreamEx) {
			LOG.error("Error opening Input Stream: " + openingInputStreamEx.getMessage());
		}
		
		return inputStream;
    }
    
    public void closeFile() {
    	try {
    		inputStream.close();
			reader.close();
		} catch (IOException closeStreamEx) {
			// TODO Auto-generated catch block
			LOG.error("Error closing Input Stream: " + closeStreamEx.getMessage());
		}
    }

}
