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
// Generated Jun 25, 2013 9:51:34 AM by Hibernate Tools 3.4.0.CR1



import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Modellist3VO generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name="ModelList")
public class ModelList3VO  implements java.io.Serializable {

	 protected Integer modelListId;
	 protected String nsdFilePath;
	 protected String chi2rgFilePath;
     private Set<AbInitioModel3VO> abinitiomodel3VOs = new HashSet<AbInitioModel3VO>(0);
     protected Set<ModelToList3VO> modeltolist3VOs = new HashSet<ModelToList3VO>(0);

    public ModelList3VO() {
    }

   
     @Id @GeneratedValue(strategy=IDENTITY)
    @Column(name="modelListId", unique=true, nullable=false)
    public Integer getModelListId() {
        return this.modelListId;
    }
    
    public void setModelListId(Integer modelListId) {
        this.modelListId = modelListId;
    }

    
    @Column(name="nsdFilePath")
    public String getNsdFilePath() {
        return this.nsdFilePath;
    }
    
    public void setNsdFilePath(String nsdFilePath) {
        this.nsdFilePath = nsdFilePath;
    }

    
    @Column(name="chi2RgFilePath")
    public String getChi2rgFilePath() {
        return this.chi2rgFilePath;
    }
    
    public void setChi2rgFilePath(String chi2rgFilePath) {
        this.chi2rgFilePath = chi2rgFilePath;
    }

@OneToMany(fetch=FetchType.EAGER, mappedBy="modelList3VO")
    public Set<AbInitioModel3VO> getAbinitiomodel3VOs() {
        return this.abinitiomodel3VOs;
    }
    
    public void setAbinitiomodel3VOs(Set<AbInitioModel3VO> abinitiomodel3VOs) {
        this.abinitiomodel3VOs = abinitiomodel3VOs;
    }

@OneToMany(fetch=FetchType.EAGER, mappedBy="modelList3VO")
    public Set<ModelToList3VO> getModeltolist3VOs() {
        return this.modeltolist3VOs;
    }
    
    public void setModeltolist3VOs(Set<ModelToList3VO> modeltolist3VOs) {
        this.modeltolist3VOs = modeltolist3VOs;
    }




}


