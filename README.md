# sppvmwareplugin

* __Back end todo:__
* Improve logging and exception handling
* Controller and service methods needed:
  * Method to build POST data for Folder/SLA associaton
  * Method to associate VM with SLA (POST call all that's left)
* Refactor so everything isn't in one service class (if need be)
* Determine if there is a better way than using search API to get VM by name (internal vSphere ID match with SPP?)

* __Front end todo:__
* Seperate registration screen or dialog
* GET to /register API can be used to determine if SPP is setup
   * This can be used to conditionally display elements in UI
* Create context/right click menu item for vm objects
   * Menu should show available SLA policies and which ones are already associated
   * User can associate or de-associate from this menu
* Create main view/dashboard (show all vms with SLA associations?)
