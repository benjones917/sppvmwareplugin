# sppvmwareplugin

* __Back end todo:__
* Refactor so everything isn't in one service class
* Ensure strategy of "login, make sppcall, logout" for each needed API call to SPP is viable
* Improve logging and exception handling
* Create objects to hold SPP SLA and VM info
* Controller and service methods needed:
  * Method to get all SLA policies from SPP
  * Method to get SLAs assigned to VM
  * Method to get VM info from SPP (arg is vm name, for use in association)
  * Method to get SLA policy info from SPP (arg is sla pol name (clicked in UI), for use in association)
  * Method to build POST data for VM/SLA association
  * Method to associate VM from SLA
  * Method of de-associate VM from SLA

* __Front end todo:__
* Seperate registration screen or dialog
* Create context/right click menu item for vm objects
   * Menu should show available SLA policies and which ones are already associated
   * User can associate or de-associate from this menu
* Create main view (show all vms with SLA associations?)
