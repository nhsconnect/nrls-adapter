# nrls-adapter
Consumer and Provider NRLS adapters

## nrls-adapter provider details
- Tasks file name must conform to the following format: "NrlsTasks_YYYY-MM-DD.xml" as the date is used to determine the order in which the files should be processed. (The oldest files are processed first.)

## nrls-adapter consumer details

## nrls-adapter installation instructions (installing as a windows service):
 - download the latest release from [here](https://github.com/nhsconnect/nrls-adapter/releases)
 - extract the folder into a working directory.
 - open "Command prompt" or your chosen alternative.
 - navigate to the working directory.
 - run the following commands:
   - `nrls-adpater.exe install` - to install the service.
   - `nrls-adapter.exe start` - to start the service.
   - `nrls-adapter.exe stop` - to stop the service.
   - `nrls-adapter.exe uninstall` - to uninstall the service.
