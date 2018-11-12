# nrls-adapter
Consumer and Provider NRLS adapters

## nrls-adapter installation instructions (installing as a windows service):
 - Download the latest release from [here](https://github.com/nhsconnect/nrls-adapter/releases)
 - Extract the folder into a working directory.
 - Do the following:
   - `open start.bat` - to install and start the service.
   - `open stop.bat` - to stop and uninstall the service.
## nrls-adapter provider details
- Tasks file name must conform to the following format: "NrlsTasks_YYYY-MM-DD.xml" as the date is used to determine the order in which the files should be processed. (The oldest files are processed first.)
- `provider.enabled=true` should be set to true.
- `task.schedule.cron=0 0 0 * * ?` should be configured (default is at midnight everyday).
- `task.folder.location=provider` should be configured to point to the directory in which the "NrlsTasks_YYYY-MM-DD.xml" files will be dumped.
- see [nrls-adapter configuration guide](https://github.com/nhsconnect/nrls-adapter/blob/master/README.md#nrls-adapter-configuration-guide-applicationproperties) for more general configuration instructions.
## nrls-adapter consumer details
- `provider.enabled=false` should be set to false.
- see [nrls-adapter configuration guide](https://github.com/nhsconnect/nrls-adapter/blob/master/README.md#nrls-adapter-configuration-guide-applicationproperties) for more general configuration instructions.
## nrls-adapter configuration guide (application.properties):
#### configuration
 - `provider.enabled=true` - determines if the adapter will be used as a provider, if disabled the the adapter will be configured to work as a consumer
#### cache configuration
 - `spring.cache.cache-names: pointers` - the names of the cache used by the adapter, should not be adjusted.
 - `spring.cache.caffeine.spec: maximumSize=500, expireAfterWrite=30m` -  the maximum size and length of time the cache will persist after caching a response.
#### task schedule
 - `task.schedule.cron=0 0 0 * * ?` - the schedule for processing the "NrlsTasks_YYYY-MM-DD.xml" file, currently 00:00 every day.
 - `task.folder.location=provider` - the location the folder containing the "NrlsTasks_YYYY-MM-DD.xml" files.
#### logging location
 - `task.failed.task.location=provider/failed/` - the location in which the failed "NrlsTasks_YYYY-MM-DD.xml" files should be deposited.
 - `auditPathConsumer=audit/consumer/` - the location in which the consumer audit logs should be deposited.
 - `auditPathProvider=audit/provider/` - the location in which the provider audit logs should be deposited.
#### nrls connection configuration
 - `adapter.asid=200000000117` - the client system ASID
 - `spine.asid=999999999999` - the spine ASID
 - `fhirvaluesets.path=valuesets/` - the location containing the value-sets used to validate the "type" field of each task. it should not need to be adjusted.
#### nrls api configuration
 - `nrls.api.get.pointer.url=http://127.0.0.1:4848/DocumentReference` - the nrls api url for "Get" requests.
 - `nrls.api.get.pointer.url.subject=?subject=https://demographics.spineservices.nhs.uk/STU3/Patient/` - the nrls api subject parameter for "Get" requests.
 - `nrls.api.get.pointer.url.count=&_summary=count` - the nrls api summary parameter for "Get" count requests.
 - `nrls.api.get.pointer.url.identifier=&identifier=` - the nrls api identifier parameter for "Get" requests.
 - `nrls.api.post.pointer.url=http://127.0.0.1:4848/DocumentReference` - the nrls api url for "Post" requests.
 - `nrls.api.delete.pointer.url=http://127.0.0.1:4848/DocumentReference?_id=` - the nrls api url for "Delete" requests.
 - `nrls.api.delete.pointer.system="urn:ietf:rfc:3986"` - the nrls api system parameter for "Delete" requests.
#### email server and account details
 - `spring.mail.host=smtp.gmail.com` - mail host url
 - `spring.mail.port=587` - mail port
 - `spring.mail.username=<username>` - username/email address
 - `spring.mail.password=<password>` - password
 - `spring.mail.properties.mail.smtp.auth=true` - 
 - `spring.mail.properties.mail.smtp.starttls.enable=true` -
#### notification configuration
 - `batch.report.recipient.email=<email.address@test.com>` - the email address that reports should be sent to.
 - `error.report.recipient.email=<email.address@test.com>` - the email address that error reports should be sent to.
 - `error.report.interval.mins=60` - error reports are grouped and sent in a single email at every interval, to avoid overwhelming an inbox with error reports.
