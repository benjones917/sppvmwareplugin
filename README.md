# sppvmwareplugin

* __Back end todo:__
* Improve logging and exception handling
* Controller and service methods needed:
  * Method to build POST data for VM/SLA association
  * Method to associate VM from SLA
  * Method of de-associate VM from SLA
* Refactor so everything isn't in one service class (if need be)
* Ensure strategy of "login, make sppcall, logout" for each needed API call to SPP is viable

* __Front end todo:__
* Seperate registration screen or dialog
* GET to /register API can be used to determine if SPP is setup
   * This can be used to conditionally display elements in UI
* Create context/right click menu item for vm objects
   * Menu should show available SLA policies and which ones are already associated
   * User can associate or de-associate from this menu
* Create main view/dashboard (show all vms with SLA associations?)
