package ai.smarthome.util.xmlrpc;

import java.util.Map;

/**
 *
 * @author pasquale
 *
 * Classe che definisce una istanza della coppia (Classe, Accuratezza).
 */
public class Classification {

	private String clazz;
	private Double accuracy;

	public Classification(String clazz, Double accuracy) {
		this.setClazz(clazz);
		this.setAccuracy(accuracy);
	}

	public Classification(Map<String, Object> vals) {
		this.setClazz((String) vals.get("Class"));
		this.setAccuracy((Double) vals.get("Accuracy"));
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getClazz() {
		return clazz;
	}

	public void setAccuracy(Double accuracy) {
		this.accuracy = accuracy;
	}

	public Double getAccuracy() {
		return accuracy;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accuracy == null) ? 0 : accuracy.hashCode());
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Classification other = (Classification) obj;
		if (accuracy == null) {
			if (other.accuracy != null)
				return false;
		} else if (!accuracy.equals(other.accuracy))
			return false;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		return true;
	}

	public String toString() {
		return "Classification [clazz=" + clazz + ", accuracy=" + accuracy + "]";
	}
}
