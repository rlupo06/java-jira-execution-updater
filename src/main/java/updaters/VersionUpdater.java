package updaters;

import utils.Config;
import zephyrapi.ZephyrAPI;
import zephyrapiclasses.ListOfCycle;

public class VersionUpdater {

  private ZephyrAPI zephyrApi;

  public VersionUpdater() {
    zephyrApi = new ZephyrAPI();
  }

  public void updateCycles() {
    Config.getDevices()
        .forEach(
            device -> {
              deleteCycles(device);
              cloneCycles(device);
            });
  }

  private void deleteCycles(String device) {
    String deviceVersionName = String.format("%s %s", Config.getPORTAL(), device);
    if (!deviceVersionName.equalsIgnoreCase("QA Parent Portal Web")) {
      if (!deviceVersionName.equalsIgnoreCase("QA iOS Mobilev2")) {
        int deviceVersionId = zephyrApi.getZephyrVersionId(deviceVersionName);
        ListOfCycle cycles = zephyrApi.getListOfCycle(deviceVersionId);
        if (!deviceVersionName.equals(Config.getVersionToCopyFrom())) {
            zephyrApi.deleteTestCycles(cycles);
        }
      }
    }
  }

  private void cloneCycles(String device) {
    int qaParentPortWebId = zephyrApi.getZephyrVersionId(Config.getVersionToCopyFrom());
    String deviceVersionName = String.format("%s %s", Config.getPORTAL(), device);
    if (!deviceVersionName.equalsIgnoreCase("QA Parent Portal Web")) {
      if (!deviceVersionName.equalsIgnoreCase("QA iOS Mobilev2")) {
        int deviceVersionId = zephyrApi.getZephyrVersionId(deviceVersionName);
        ListOfCycle cyclesToClone = zephyrApi.getListOfCycle(qaParentPortWebId);
        cyclesToClone.getCycle().forEach(cycle ->{
          try {
              Thread.sleep(1000);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
                zephyrApi.cloneCycle(cycle, deviceVersionId);
        });
      }
    }
  }
}
