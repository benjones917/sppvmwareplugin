# sppvmwareplugin

* __Back end todo:__
* Controller and service methods needed:
  * Restore VM
* Determine if there is a better way than using search API to get VM by name (internal vSphere ID match with SPP?)
* Improve logging and exception handling

* __Front end todo:__
* Seperate registration screen or dialog
* GET to /register API can be used to determine if SPP is setup
   * This can be used to conditionally display elements in UI
* Create context/right click menu item for vm objects
   * Menu should show available SLA policies and which ones are already associated
   * User can associate or de-associate from this menu
   * Perhaps better to just open a panel, could offer restore from same area
* Create main view/dashboard (show all vms with SLA associations?)
