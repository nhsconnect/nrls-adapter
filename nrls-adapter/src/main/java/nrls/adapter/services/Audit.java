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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import nrls.adapter.enums.RequestType;
import nrls.adapter.helpers.FileHelper;
import nrls.adapter.model.AuditEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Audit {

    @Value("${auditPathConsumer}")
    private String auditPathConsumer;
    
    @Value("${auditPathProvider}")
    private String auditPathProvider;
    
    private HashMap<String, AuditEntity> auditEntities;
    private SimpleDateFormat simpleDateFormat;

    public Audit() {
        auditEntities = new HashMap<>();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
    }

    // Returns the existing audit entry or creates a new one if one does not exist already
    public AuditEntity getAuditEntity(String id) {
        AuditEntity auditEntity = auditEntities.get(id);
        if (null == auditEntity) {
            auditEntity = new AuditEntity(id);
            auditEntities.put(id, auditEntity);
        }
        return auditEntity;
    }

    public boolean saveAuditEntity(String id) {
        
        AuditEntity auditEntity = auditEntities.get(id);
        
        if (null == auditEntity) {
            // The audit entity could not be found so can not be saved
            return false;
        } else {
            
            // Start by removing the audit entity from the hashmap so that the memory can be cleaned up by the garbage collector
            auditEntities.remove(id);
            
            auditEntity.setDateLogged(new Date()); // Set the audit date to todays date
            
            // Store the audit entry to a file
            String filePath;
            if(RequestType.CONSUMER == auditEntity.getType()){
                filePath = auditPathConsumer + "NrlsAudit_" + auditEntity.getNhsNumber() + "_Consumer_" + simpleDateFormat.format(auditEntity.getDateLogged()) + "_" + auditEntity.getTransactionId() + ".xml";
            } else {
                filePath = auditPathProvider + "NrlsAudit_" + auditEntity.getNhsNumber() + "_Provider_" + simpleDateFormat.format(auditEntity.getDateLogged()) + "_" + auditEntity.getTransactionId() + ".xml";
            }
            return FileHelper.writeObjectToFileAsXML(filePath, auditEntity);
        }
    }

}
