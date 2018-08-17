package nrls.adapter.services;

import java.text.DateFormat;
import java.util.Date;
import nrls.adapter.model.documentreference.Author;
import nrls.adapter.model.documentreference.Custodian;
import nrls.adapter.model.documentreference.DocumentReference;
import nrls.adapter.model.documentreference.MasterIdentifier;
import nrls.adapter.model.documentreference.Subject;
import nrls.adapter.model.task.Task;
import nrls.adapter.model.task.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentReferenceService {

    @Autowired
    private ValueSetValidator valueSetValidator;
    
	public DocumentReference convertTaskToDocumentReference(Task task) throws Exception {
        
		DocumentReference documentRef = new DocumentReference();
		
        // Set the masterIdentifier
        MasterIdentifier masterIdentifier = new MasterIdentifier();
        masterIdentifier.setSystem("Random");
		masterIdentifier.setValue(task.getPointerMasterIdentifier());;
        documentRef.setMasterIdentifier(masterIdentifier);
		
		documentRef.setResourceType("DocumentReference");
        
		documentRef.setStatus(task.getStatus());
        
        if(!valueSetValidator.validateCoding(task.getType().getCoding())){
            throw new Exception("Type is not valid within document reference");
        } else {
            documentRef.setType(task.getType());
        }
        
        if(!Validators.nhsNumberValid(task.getSubject().getNhsNumber())){
            throw new Exception("The NHS Number is not valid");
        }
        
        Subject subject = new Subject();
        subject.setReference("https://demographics.spineservices.nhs.uk/STU3/Patient/" + task.getSubject().getNhsNumber());
		documentRef.setSubject(subject);
		
		documentRef.setIndexed(new Date().toGMTString());
        
        Author author = new Author();
        author.setReference("https://directory.spineservices.nhs.uk/STU3/Organization/" + task.getAuthor().getOdsCode());
		documentRef.setAuthor(author);
        
        Custodian custodian = new Custodian();
        custodian.setReference("https://directory.spineservices.nhs.uk/STU3/Organization/" + task.getCustodian().getOdsCode());
		documentRef.setCustodian(custodian);
        
		documentRef.setContent(task.getContent());
        
		return documentRef;
	}
}
