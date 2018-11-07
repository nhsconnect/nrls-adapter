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

package nrls.adapter.services;

import org.hl7.fhir.dstu3.model.DocumentReference;
import org.hl7.fhir.dstu3.model.DocumentReference.DocumentReferenceContentComponent;
import org.hl7.fhir.dstu3.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Reference;

import java.util.Date;

import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;

import nrls.adapter.helpers.Validators;
import nrls.adapter.helpers.ValueSetValidator;
import nrls.adapter.model.task.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;

@Service
public class DocumentReferenceService {

	@Autowired
	private ValueSetValidator valueSetValidator;
	
	@Value("${nrls.api.pointer.system}")
    private String nrlsPointerSystem;

	public String convertTaskToDocument(Task task) throws Exception {
		DocumentReference doc = new DocumentReference();

		// Set 'Master Identifier'
		Identifier masterIdentifier = new Identifier();
		masterIdentifier.setSystem(nrlsPointerSystem);
		masterIdentifier.setValue(task.getPointerMasterIdentifier());
		doc.setMasterIdentifier(masterIdentifier);

		// Set 'Status'
		doc.setStatus(DocumentReferenceStatus.CURRENT);

		// Set 'Type'
		if (!valueSetValidator.validateCoding(task.getType().getCoding())) {
			throw new Exception("Type is not valid within document reference");
		} else {
			CodeableConcept code = new CodeableConcept();
			Coding coding = new Coding();
			coding.setSystem(task.getType().getCoding().getSystem());
			coding.setCode(task.getType().getCoding().getCode());
			coding.setDisplay(task.getType().getCoding().getDisplay());
			code.addCoding(coding);
			doc.setType(code);
		}

		// Set 'Subject'
		if (!Validators.nhsNumberValid(task.getSubject().getNhsNumber())) {
			throw new Exception("The NHS Number is not valid");
		}
		Reference subjectRef = new Reference();
		subjectRef.setReference("https://demographics.spineservices.nhs.uk/STU3/Patient/" + task.getSubject().getNhsNumber());
		doc.setSubject(subjectRef);

		// Set 'Author'
		Reference authorRef = new Reference();
		authorRef.setReference("https://directory.spineservices.nhs.uk/STU3/Organization/" + task.getAuthor().getOdsCode());
		doc.getAuthor().add(authorRef);

		// Set 'Custodian'
		Reference custodianRef = new Reference();
		custodianRef.setReference("https://directory.spineservices.nhs.uk/STU3/Organization/" + task.getCustodian().getOdsCode());
		doc.setCustodian(custodianRef);

		// Set 'Content'
		Attachment attachment = new Attachment();
		attachment.setContentType(task.getContent().getAttchment().getContentType());
		attachment.setCreationElement(new DateTimeType(task.getContent().getAttchment().getCreation()));
		attachment.setTitle(task.getContent().getAttchment().getTitle());
		attachment.setUrl(task.getContent().getAttchment().getUrl());
		doc.addContent(new DocumentReferenceContentComponent(attachment));
		
		//Set 'Created'/'Indexed'
		doc.setCreated(new Date());
		doc.setIndexed(new Date());

		FhirContext ctx = FhirContext.forDstu3();

		return ctx.newJsonParser().encodeResourceToString(doc);
	}
}
