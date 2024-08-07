/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************************************************************/

package ispyb.server.biosaxs.vos.dataAcquisition;

// Generated May 25, 2012 9:27:44 AM by Hibernate Tools 3.4.0.CR1
import static jakarta.persistence.GenerationType.IDENTITY;
import ispyb.server.biosaxs.vos.datacollection.Merge3VO;
import ispyb.server.biosaxs.vos.datacollection.Run3VO;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;

import com.google.gson.annotations.Expose;

/**
 * Specimen3VO generated by hbm2java
 */
@Entity
@Table(name = "Measurement")
@Access(AccessType.FIELD)
public class Measurement3VO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	protected Integer measurementId;
	protected int specimenId;
	protected Boolean flow;
	protected Integer priority;
	protected String exposureTemperature;
	protected String viscosity;
	protected String extraFlowTime;
	protected String volumeToLoad;
	protected String comments;
	protected String code;
	protected String transmission;
	protected String waitTime;
	protected String imageDirectory;
	protected String pathToH5;
	
	
	@Column(name = "imageDirectory")
	public String getImageDirectory() {
		return imageDirectory;
	}


	public void setImageDirectory(String imageDirectory) {
		this.imageDirectory = imageDirectory;
	}

	@Expose
	@XmlTransient
	@ManyToOne(fetch=FetchType.EAGER)
//	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="runId")
	protected Run3VO run3VO;
	@Expose
	@XmlTransient
//    @OneToMany(fetch=FetchType.EAGER)
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name = "measurementId")
	protected Set<Merge3VO> merge3VOs = new HashSet<Merge3VO>(0);
	 
	@Transient
	public String dataCollectionOrder;
	
	public Measurement3VO() {
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "measurementId", unique = true, nullable = false)
	public Integer getMeasurementId() {
		return this.measurementId;
	}

	public void setMeasurementId(Integer measurementId) {
		this.measurementId = measurementId;
	}

	@Column(name = "exposureTemperature", length = 45)
	public String getExposureTemperature() {
		return this.exposureTemperature;
	}

	public void setExposureTemperature(String exposureTemperature) {
		this.exposureTemperature = exposureTemperature;
	}

	@Column(name = "viscosity", length = 45)
	public String getViscosity() {
		return this.viscosity;
	}

	public void setViscosity(String viscosity) {
		this.viscosity = viscosity;
	}

	@XmlElement(nillable=true)
	@Column(name = "flow", length = 45)
	public Boolean getFlow() {
		return this.flow;
	}

	public void setFlow(Boolean flow) {
		this.flow = flow;
	}
	
	@XmlElement(nillable=true)
	@Column(name = "extraFlowTime", length = 45)
	public String getExtraFlowTime() {
		return this.extraFlowTime;
	}

	public void setExtraFlowTime(String extraFlowTime) {
		this.extraFlowTime = extraFlowTime;
	}

//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "creationDate", length = 0)
//	public Date getCreationDate() {
//		return this.creationDate;
//	}
//
//	public void setCreationDate(Date creationDate) {
//		this.creationDate = creationDate;
//	}

//	@Column(name = "concentration", length = 45)
//	public String getConcentration() {
//		return this.concentration;
//	}
	
	@Transient
	public String getConcentration(Experiment3VO experiment) {
		return experiment.getSampleById(this.getSpecimenId()).getConcentration();
	}
//
//	public void setConcentration(String concentration) {
//		this.concentration = concentration;
//	}

	@Column(name = "volumeToLoad", length = 45)
	public String getVolumeToLoad() {
		return this.volumeToLoad;
	}

	public void setVolumeToLoad(String volumeToLoad) {
		this.volumeToLoad = volumeToLoad;
	}

	@Column(name = "comments", length = 512)
	public String getComment() {
		return this.comments;
	}

	public void setComment(String comment) {
		this.comments = comment;
	}
	
	@Column(name = "code", length = 512)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	@Column(name = "priorityLevelId", nullable = true)
	public Integer getPriority() {
		return priority;
	}


	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Column(name = "transmission", nullable = true)
	public String getTransmission() {
		return transmission;
	}


	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	@Column(name = "pathToH5", nullable = true)
	public String getPathToH5() {
		return pathToH5;
	}


	public void setPathToH5(String pathToH5) {
		this.pathToH5 = pathToH5;
	}
	
	@Column(name = "waitTime", nullable = true)
	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	@Column(name = "specimenId")
	public int getSpecimenId() {
		return specimenId;
	}


	public void setSpecimenId(int specimenId) {
		this.specimenId = specimenId;
	}
	

    public Run3VO getRun3VO() {
        return this.run3VO;
    }
    
    public void setRun3VO(Run3VO run3VO) {
        this.run3VO = run3VO;
    }
    

    public Set<Merge3VO> getMerge3VOs() {
        return this.merge3VOs;
    }
    
    public void setMerge3VOs(Set<Merge3VO> merge3VOs) {
        this.merge3VOs = merge3VOs;
    }
    
    @Transient
    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("Measurement id: ");
    	sb.append(this.getMeasurementId());
    	sb.append("\t SpecimenId: ");
    	sb.append(this.getSpecimenId());
    	return sb.toString();
    }
    	
}
