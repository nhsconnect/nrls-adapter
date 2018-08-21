package nrls.adapter.helpers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import nrls.adapter.model.task.Coding;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValueSetValidator {

    private static final Logger LOG = Logger.getLogger(ValueSetValidator.class);
    
    @Value("${fhirvaluesets.path}")
    private String fhirValueSetsPath;

    private Map<String, List<ValueSet.ConceptReferenceComponent>> codeSystemCache;

    @PostConstruct
    public void init() {
        
        codeSystemCache = new HashMap<>();

        // load value sets From Files
        File filesDir = new File(fhirValueSetsPath);

        // For each file in valueset directory
        IParser parser = FhirContext.forDstu3().newXmlParser();

        for (File file : filesDir.listFiles()) {
            if (file.isFile()) {
                try {
                    String xmlValueSetString = new String(Files.readAllBytes(file.toPath()));
                    ValueSet valSet = parser.parseResource(ValueSet.class, xmlValueSetString);

                    for (ValueSet.ConceptSetComponent codeSystemeCompose : valSet.getCompose().getInclude()) {
                        codeSystemCache.put(codeSystemeCompose.getSystem(), codeSystemeCompose.getConcept());
                    }
                } catch (Exception ex) {
                    LOG.error("Error loading valueset: " + ex.getMessage());
                }
            }
        }
    }

    public Boolean validateCoding(Coding coding) throws Exception {

        boolean codingValid = false;
        
        String systemUrl = coding.getSystem();
        List<ValueSet.ConceptReferenceComponent> codeSystemComponentList = codeSystemCache.get(systemUrl);

        // If the code system is found in cache then check value and display are valid
        if (null != codeSystemComponentList) {
            for (ValueSet.ConceptReferenceComponent codeSystemComponent : codeSystemComponentList) {
                if(coding.getCode().equals(codeSystemComponent.getCode()) &&
                        coding.getDisplay().equals(codeSystemComponent.getDisplay())){
                    codingValid = true;
                    break;
                }
            }
        } else {
            codingValid = false;
        }

        return codingValid;
    }

}
