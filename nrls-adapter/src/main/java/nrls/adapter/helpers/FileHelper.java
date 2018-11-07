/* Copyright 2018 NHS Digital

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package nrls.adapter.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import nrls.adapter.enums.RequestType;
import nrls.adapter.model.Nrls;
import nrls.adapter.model.task.Task;
import nrls.adapter.services.LoggingService;

@Service
public class FileHelper {

    private FileReader reader;
    private ObjectInputStream inputStream;
    
    @Autowired
    public static LoggingService loggingService;

    public static boolean writeObjectToFileAsXML(String filePath, Object object) {

        boolean success = true;
        FileWriter fileWriter = null;

        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            fileWriter = new FileWriter(file);
            XStream xstream = new XStream();        // Using 'XStream' as it produces more readable XML output than JAXB and is quicker that XMLEncoder
            xstream.processAnnotations(Nrls.class);
            xstream.toXML(object, fileWriter);
        } catch (IOException ex) {
        	loggingService.error("Error converting object to XML when saving to file: " + ex.getMessage(), RequestType.PROVIDER);
            success = false;
        } finally {
            try {
                if (null != fileWriter) {
                    fileWriter.close();
                }
            } catch (IOException fileCloseEx) {
            	loggingService.error("Error closing FileWriter after writing xml to file: " + fileCloseEx.getMessage(), RequestType.PROVIDER);
                success = false;
            }
        }

        return success;
    }
    
    // Extract date from file name and return file list in order of oldest first.
    public ArrayList<Path> getFileList(String folderPath) {
    	ArrayList<Path> files = new ArrayList<Path>();
    	try (Stream<Path> filePathStream=Files.walk(Paths.get(folderPath),1)) {
    	    filePathStream.forEach(filePath -> {
    	        if (Files.isRegularFile(filePath)) {
    	            files.add(filePath);
    	        }
    	    });
    	    filePathStream.close();
    	} catch (IOException readingEx) {
    		loggingService.error("Error reading folder: " + readingEx.getMessage(), RequestType.PROVIDER);
		}
    	files.sort(dateComparator);
    	return files;
    }
    
	public static Comparator<Path> dateComparator = new Comparator<Path>() {
		public int compare(Path path1, Path path2) {
			
			LocalDate fileDate1 = null;
			LocalDate fileDate2 = null;
			Matcher m1 = Pattern.compile(".*_(.*?)\\..*", Pattern.CASE_INSENSITIVE).matcher(path1.getFileName().toString());
	        while (m1.find()) {
	            fileDate1 = LocalDate.parse(m1.group(1));
	        }
	        
	        Matcher m2 = Pattern.compile(".*_(.*?)\\..*", Pattern.CASE_INSENSITIVE).matcher(path2.getFileName().toString());
	        while (m2.find()) {
				fileDate2 = LocalDate.parse(m2.group(1));
	        }

			// oldest first
			return fileDate1.compareTo(fileDate2);

			// latest first
			// return fileDate2.compareTo(fileDate1);
		}
	};

    public ObjectInputStream getObjectInputStream(String filePath) {

        try {
            reader = new FileReader(filePath);
            XStream xstream = new XStream(new StaxDriver());
            xstream.alias("Task", Task.class);
            xstream.processAnnotations(Task.class);
            inputStream = xstream.createObjectInputStream(reader);

        } catch (FileNotFoundException fileNotFoundEx) {
        	loggingService.error("Error opening File: " + fileNotFoundEx.getMessage(), RequestType.PROVIDER);
        } catch (IOException openingInputStreamEx) {
        	loggingService.error("Error opening Input Stream: " + openingInputStreamEx.getMessage(), RequestType.PROVIDER);
        }

        return inputStream;
    }

    public void closeFile() {
        try {
            inputStream.close();
            reader.close();
        } catch (IOException closeStreamEx) {
        	loggingService.error("Error closing Input Stream: " + closeStreamEx.getMessage(), RequestType.PROVIDER);
        }
    }

    public static void archiveFile(String filePath) {

        File fromFile = new File(filePath);
        String fileName = fromFile.getName();

        String newFilePath = filePath.replace(fileName, "archive/" + fileName + "_" + formatCurrentDate());
        File toFile = new File(newFilePath);
        toFile.getParentFile().mkdirs();

        if (!fromFile.renameTo(toFile)) {
        	loggingService.error("Error archiving Tasks file (" + filePath + ")", RequestType.PROVIDER);
        }
    }
    
    public static String formatCurrentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
        return simpleDateFormat.format(new Date());
    }
    
    public static String formatDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss.SSS");
        return simpleDateFormat.format(new Date());
    }

}
