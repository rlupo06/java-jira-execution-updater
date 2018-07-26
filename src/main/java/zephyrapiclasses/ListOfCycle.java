package zephyrapiclasses;

import com.google.gson.JsonElement;
import java.util.List;
import java.util.Map;

public class ListOfCycle {

    public ListOfCycle(List<Cycle> cycles){
        this.cycles = cycles;
    }

   private List<Cycle> cycles;

    public List<Cycle> getCycle() {
        return cycles;
    }

    public void setCycle(Map<String, JsonElement> cycle) {
        this.cycles = cycles;
    }


    public class Cycle {

        private String versionName;
        private int versionId;
        private String name;
        private int cycleId;

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionId() {
            return versionId;
        }

        public void setVersionId(int versionId) {
            this.versionId = versionId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCycleId() {
            return cycleId;
        }

        public void setCycleId(int cycleId) {
            this.cycleId = cycleId;
        }
    }

}



