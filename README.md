# sppvmwareplugin

* __Back end todo:__
* Controller and service methods needed:
  * Dashboard Info Service
  * Check/Register vSphere provider in SPP
  * VM/Folder version service
* Determine VMWare unique ID (also accessible in SPP data) to ensure proper match
  * Will need to be passed into appropriate service functions from UI

* __Front end todo:__
* Seperate registration screen or dialog
   * Link to this from main view screen
* Add list of backups to SLA policy select screen on VMs and Folders
   * This is a call to the /vmversions and /folderversions plugin service endpoints
   * Must pass in SPP HyperVisor ID and VM/Folder ID ... this is available in the vm and folder responses
* Create main view/dashboard (show all vms with SLA associations?)
