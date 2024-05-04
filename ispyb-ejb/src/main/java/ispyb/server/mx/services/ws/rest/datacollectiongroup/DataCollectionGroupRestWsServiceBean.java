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

package ispyb.server.mx.services.ws.rest.datacollectiongroup;

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;


@Stateless
public class DataCollectionGroupRestWsServiceBean extends WsServiceBean implements DataCollectionGroupRestWsService, DataCollectionGroupRestWsServiceLocal  {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;


	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/DataCollectionGroupRestWsServiceBean/getViewTableQuery.sql");
	}
	
	@Override
	public List<Map<String, Object>> getViewDataCollectionBySessionId(int proposalId, int sessionId) {
		String mySQLQuery = getViewTableQuery() + " where DataCollectionGroup_sessionId = :sessionId and BLSession_proposalId = :proposalId ";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId order by DataCollection_startTime desc ";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("sessionId", sessionId)
				.setParameter("proposalId", proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getViewDataCollectionBySessionIdHavingImages(int proposalId, int sessionId) {
		String mySQLQuery = getViewTableQuery() + " where DataCollectionGroup_sessionId = :sessionId and BLSession_proposalId = :proposalId ";
		mySQLQuery = mySQLQuery + " and DataCollection_numberOfImages is not null ";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId order by DataCollection_startTime desc ";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("sessionId", sessionId)
				.setParameter("proposalId", proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getViewDataCollectionByProteinAcronym(int proposalId, String proteinAcronym) {
		String mySQLQuery = getViewTableQuery() + " where BLSession_proposalId = :proposalId and Protein_acronym = :proteinAcronym";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("proposalId", proposalId)
				.setParameter("proteinAcronym", proteinAcronym);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

	@Override
	public List<Map<String, Object>> getViewDataCollectionBySampleId(int proposalId, int sampleId) {
		String mySQLQuery = getViewTableQuery() + " where BLSession_proposalId = :proposalId and DataCollectionGroup_blSampleId = :sampleId";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("proposalId", proposalId)
				.setParameter("sampleId", sampleId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

	@Override
	public List<Map<String, Object>> getViewDataCollectionBySampleName(int proposalId, String name) {
		String mySQLQuery = getViewTableQuery() + " where BLSession_proposalId = :proposalId and BLSample_name = :name";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("proposalId", proposalId)
				.setParameter("name", name);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getViewDataCollectionByImagePrefix(int proposalId, String prefix) {
		String mySQLQuery = getViewTableQuery() + " where BLSession_proposalId = :proposalId and DataCollection_imagePrefix = :prefix";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("proposalId", proposalId)
				.setParameter("prefix", prefix);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

	@Override
	public Collection<? extends Map<String, Object>> getViewDataCollectionByDataCollectionId(int proposalId, int dataCollectionId) {
		String mySQLQuery = getViewTableQuery() + " where DataCollection_dataCollectionId = :dataCollectionId and BLSession_proposalId = :proposalId ";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("dataCollectionId", dataCollectionId)
				.setParameter("proposalId", proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

	@Override
	public List<Map<String, Object>> getViewDataCollectionByWorkflowId(Integer proposalId, Integer workflowId) {
		String mySQLQuery = getViewTableQuery() + " where Workflow_workflowId = :Workflow_workflowId and BLSession_proposalId = :proposalId ";
		mySQLQuery = mySQLQuery + " group by v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId, v_datacollection_summary.DataCollectionGroup_dataCollectionGroupId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("Workflow_workflowId", workflowId)
				.setParameter("proposalId", proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

	
	

	
}
