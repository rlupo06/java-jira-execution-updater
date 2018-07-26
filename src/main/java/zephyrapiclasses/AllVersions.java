package zephyrapiclasses;

import java.util.List;

public class AllVersions {
	private String type;
	private String hasAccessToSoftware;
	private List<UnreleasedVersions> unreleasedVersions;
	private List<UnreleasedVersions> releasedVersions;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHasAccessToSoftware() {
		return hasAccessToSoftware;
	}

	public void setHasAccessToSoftware(String hasAccessToSoftware) {
		this.hasAccessToSoftware = hasAccessToSoftware;
	}

	public List<UnreleasedVersions> getUnreleasedVersions() {
		return unreleasedVersions;
	}

	public void setUnreleasedVersions(List<UnreleasedVersions> unreleasedVersions) {
		this.unreleasedVersions = unreleasedVersions;
	}

	public List<UnreleasedVersions> getReleasedVersions() {
		return releasedVersions;
	}

	public void setReleasedVersions(List<UnreleasedVersions> releasedVersions) {
		this.releasedVersions = releasedVersions;
	}

	public class UnreleasedVersions {
		private int value;
		private boolean archived;
		private String label;

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public boolean isArchived() {
			return archived;
		}

		public void setArchived(boolean archived) {
			this.archived = archived;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
	}

}
