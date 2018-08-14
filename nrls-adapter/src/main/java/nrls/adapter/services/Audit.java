package nrls.adapter.services;

import java.util.HashMap;
import nrls.adapter.enums.RequestType;
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

    public Audit() {
        auditEntities = new HashMap<>();
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

    public boolean saveAuditEntity(String id, RequestType type) {
        
        AuditEntity auditEntity = auditEntities.get(id);
        
        if (null == auditEntity) {
            // The audit entity could not be found so can not be saved
            return false;
        } else {
            
            // Start by removing the audit entity from the hashmap so that the memory can be cleaned up by the garbage collector
            auditEntities.remove(id);
            
            // Store the audit entry to a file
            String filePath;
            if(type == RequestType.CONSUMER){
                filePath = auditPathConsumer + auditEntity.getNhsNumber() + "_Consumer_" + auditEntity.getTransactionId() + ".xml";
            } else {
                filePath = auditPathProvider + auditEntity.getNhsNumber() + "_Provider_" + auditEntity.getTransactionId() + ".xml";
            }
            return FileHelper.writeObjectToFileAsXML(filePath, auditEntity);
        }
    }

}
