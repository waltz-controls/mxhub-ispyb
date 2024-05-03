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

package ispyb.server.biosaxs.vos.datacollection;

// Generated Feb 5, 2013 6:43:17 PM by Hibernate Tools 3.4.0.CR1

import static jakarta.persistence.GenerationType.IDENTITY;





import ispyb.server.biosaxs.vos.advanced.FitStructureToExperimentalData3VO;
import ispyb.server.biosaxs.vos.advanced.RigidBodyModeling3VO;
import ispyb.server.biosaxs.vos.advanced.Superposition3VO;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Substraction3VO generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "Subtraction")
public class Subtraction3VO implements java.io.Serializable {

	protected Integer subtractionId;
//	protected SaxsDataCollection3VO SaxsDataCollection3VO;
	protected int dataCollectionId;
	protected String rg;
	protected String rgStdev;
	protected String i0;
	protected String i0stdev;
	protected String firstPointUsed;
	protected String lastPointUsed;
	protected String quality;
	protected String isagregated;
	protected String concentration;
	protected String rgGuinier;
	protected String rgGnom;
	protected String dmax;
	protected String total;
	protected String volume;
	protected Date creationTime;
	
	protected String gnomFilePath;
	protected String kratkyFilePath;
	protected String scatteringFilePath;
	protected String guinierFilePath;
	
	protected String substractedFilePath;
	protected String gnomFilePathOutput;

	protected Set<SubtractiontoAbInitioModel3VO> substractionToAbInitioModel3VOs = new HashSet<SubtractiontoAbInitioModel3VO>(0);
	protected Set<FitStructureToExperimentalData3VO> fitStructureToExperimentalData3VOs = new HashSet<FitStructureToExperimentalData3VO>(0);
	protected Set<Superposition3VO> superposition3VOs = new HashSet<Superposition3VO>(0);
	protected Set<RigidBodyModeling3VO> rigidBodyModeling3VOs = new HashSet<RigidBodyModeling3VO>(0);
	
	protected Framelist3VO sampleOneDimensionalFiles;
	protected Framelist3VO bufferOneDimensionalFiles;
	
	protected String sampleAverageFilePath;
	protected String bufferAverageFilePath;
	
	public Subtraction3VO() {
	}
	
	@Column(name = "sampleAverageFilePath")
	public String getSampleAverageFilePath() {
		return sampleAverageFilePath;
	}
	
	
	public void setSampleAverageFilePath(String sampleAverageFilePath) {
		this.sampleAverageFilePath = sampleAverageFilePath;
	}
	
	@Column(name = "bufferAverageFilePath")
	public String getBufferAverageFilePath() {
		return bufferAverageFilePath;
	}
	
	
	public void setBufferAverageFilePath(String bufferAverageFilePath) {
		this.bufferAverageFilePath = bufferAverageFilePath;
	}

	
	@ManyToOne
	@JoinColumn(name = "bufferOneDimensionalFiles")
	public Framelist3VO getBufferOneDimensionalFiles() {
		return bufferOneDimensionalFiles;
	}

	public void setBufferOneDimensionalFiles(Framelist3VO bufferOneDimensionalFiles) {
		this.bufferOneDimensionalFiles = bufferOneDimensionalFiles;
	}
	
	@ManyToOne
	@JoinColumn(name = "sampleOneDimensionalFiles")
	public Framelist3VO getSampleOneDimensionalFiles() {
		return sampleOneDimensionalFiles;
	}


	public void setSampleOneDimensionalFiles(Framelist3VO sampleOneDimensionalFiles) {
		this.sampleOneDimensionalFiles = sampleOneDimensionalFiles;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "subtractionId", unique = true, nullable = false)
	public Integer getSubtractionId() {
		return this.subtractionId;
	}

	public void setSubtractionId(Integer substractionId) {
		this.subtractionId = substractionId;
	}

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "dataCollectionId", nullable = false)
//	public SaxsDataCollection3VO getSaxsDataCollection3VO() {
//		return this.SaxsDataCollection3VO;
//	}
//
//	public void setSaxsDataCollection3VO(
//			SaxsDataCollection3VO SaxsDataCollection3VO) {
//		this.SaxsDataCollection3VO = SaxsDataCollection3VO;
//	}
	
	
	
	@Column(name = "dataCollectionId")
	public int getDataCollectionId() {
		return dataCollectionId;
	}

	public void setDataCollectionId(int dataCollectionId) {
		this.dataCollectionId = dataCollectionId;
	}
	

	@Column(name = "rg", length = 45)
	public String getRg() {
		return this.rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	@Column(name = "rgStdev", length = 45)
	public String getRgStdev() {
		return this.rgStdev;
	}

	public void setRgStdev(String rgStdev) {
		this.rgStdev = rgStdev;
	}

	@Column(name = "I0", length = 45)
	public String getI0() {
		return this.i0;
	}

	public void setI0(String i0) {
		this.i0 = i0;
	}

	@Column(name = "I0Stdev", length = 45)
	public String getI0stdev() {
		return this.i0stdev;
	}

	public void setI0stdev(String i0stdev) {
		this.i0stdev = i0stdev;
	}

	@Column(name = "firstPointUsed", length = 45)
	public String getFirstPointUsed() {
		return this.firstPointUsed;
	}

	public void setFirstPointUsed(String firstPointUsed) {
		this.firstPointUsed = firstPointUsed;
	}

	@Column(name = "lastPointUsed", length = 45)
	public String getLastPointUsed() {
		return this.lastPointUsed;
	}

	public void setLastPointUsed(String lastPointUsed) {
		this.lastPointUsed = lastPointUsed;
	}

	@Column(name = "quality", length = 45)
	public String getQuality() {
		return this.quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	@Column(name = "isagregated", length = 45)
	public String getIsagregated() {
		return this.isagregated;
	}

	public void setIsagregated(String isagregated) {
		this.isagregated = isagregated;
	}

	@Column(name = "concentration", length = 45)
	public String getConcentration() {
		return this.concentration;
	}

	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}

	@Column(name = "gnomFilePath", length = 255)
	public String getGnomFilePath() {
		return this.gnomFilePath;
	}

	public void setGnomFilePath(String gnomFilePath) {
		this.gnomFilePath = gnomFilePath;
	}

	@Column(name = "rgGuinier", length = 45)
	public String getRgGuinier() {
		return this.rgGuinier;
	}

	public void setRgGuinier(String rgGuinier) {
		this.rgGuinier = rgGuinier;
	}

	@Column(name = "rgGnom", length = 45)
	public String getRgGnom() {
		return this.rgGnom;
	}

	public void setRgGnom(String rgGnom) {
		this.rgGnom = rgGnom;
	}

	@Column(name = "dmax", length = 45)
	public String getDmax() {
		return this.dmax;
	}

	public void setDmax(String dmax) {
		this.dmax = dmax;
	}

	@Column(name = "total", length = 45)
	public String getTotal() {
		return this.total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	@Column(name = "volume", length = 45)
	public String getVolume() {
		return this.volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creationTime", length = 0)
	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Column(name = "kratkyFilePath", length = 255)
	public String getKratkyFilePath() {
		return kratkyFilePath;
	}


	public void setKratkyFilePath(String kratkyFilePath) {
		this.kratkyFilePath = kratkyFilePath;
	}

	@Column(name = "scatteringFilePath", length = 255)
	public String getScatteringFilePath() {
		return scatteringFilePath;
	}


	public void setScatteringFilePath(String scatteringFilePath) {
		this.scatteringFilePath = scatteringFilePath;
	}

	@Column(name = "guinierFilePath", length = 255)
	public String getGuinierFilePath() {
		return guinierFilePath;
	}


	public void setGuinierFilePath(String guinierFilePath) {
		this.guinierFilePath = guinierFilePath;
	}

	@Column(name = "substractedFilePath", length = 255)
	public String getSubstractedFilePath() {
		return substractedFilePath;
	}


	public void setSubstractedFilePath(String substractedFilePath) {
		this.substractedFilePath = substractedFilePath;
	}

	@Column(name = "gnomFilePathOutput", length = 255)
	public String getGnomFilePathOutput() {
		return gnomFilePathOutput;
	}


	public void setGnomFilePathOutput(String gnomFilePathOutput) {
		this.gnomFilePathOutput = gnomFilePathOutput;
	}



	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "subtractionId")
	public Set<SubtractiontoAbInitioModel3VO> getSubstractionToAbInitioModel3VOs() {
		return substractionToAbInitioModel3VOs;
	}


	public void setSubstractionToAbInitioModel3VOs(
			Set<SubtractiontoAbInitioModel3VO> substractionToAbInitioModel3VOs) {
		this.substractionToAbInitioModel3VOs = substractionToAbInitioModel3VOs;
	}

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "subtractionId")
	public Set<FitStructureToExperimentalData3VO> getFitStructureToExperimentalData3VOs() {
		return fitStructureToExperimentalData3VOs;
	}

	public void setFitStructureToExperimentalData3VOs(Set<FitStructureToExperimentalData3VO> fitStructureToExperimentalData3VOs) {
		this.fitStructureToExperimentalData3VOs = fitStructureToExperimentalData3VOs;
	}

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "subtractionId")
	public Set<Superposition3VO> getSuperposition3VOs() {
		return superposition3VOs;
	}

	public void setSuperposition3VOs(Set<Superposition3VO> superposition3vOs) {
		superposition3VOs = superposition3vOs;
	}

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "subtractionId")
	public Set<RigidBodyModeling3VO> getRigidBodyModeling3VOs() {
		return rigidBodyModeling3VOs;
	}
	
	public void setRigidBodyModeling3VOs(Set<RigidBodyModeling3VO> rigidBodyModeling3VOs) {
		this.rigidBodyModeling3VOs = rigidBodyModeling3VOs;
	}




}
