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

package ispyb.server.mx.services.ws.rest.phasing;

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;


@Stateless
public class PhasingRestWsServiceBean  extends WsServiceBean implements PhasingRestWsService, PhasingRestWsServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	private String getPhasingViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/PhasingRestWsServiceBean/getViewTableQuery.sql");
	}
	
	private String ByDataCollectionGroupId = getPhasingViewTableQuery() + " where DataCollection_dataCollectionGroupId = :dataCollectionGroupId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	private String ByDataCollectionId = getPhasingViewTableQuery() + " where DataCollection_dataCollectionId = :dataCollectionId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	private String ByAutoProcIntegrationId = getPhasingViewTableQuery() + " where AutoProcIntegration_autoProcIntegrationId = :autoprocIntegrationId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	private String ByPhasingStepId = getPhasingViewTableQuery() + " where PhasingStep_phasingStepId = :phasingStepId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	
	
	private String BySampleId = getPhasingViewTableQuery()  + " where BLSample_blSampleId = :blSampleId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	private String ByProteinId = getPhasingViewTableQuery() + " where Protein_proteinId = :Protein_proteinId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	private String BySessionId = getPhasingViewTableQuery() + " where BLSession_sessionId = :sessionId and BLSession_proposalId = :proposalId group by PhasingStep_phasingStepId";
	

	@Override
	public List<Map<String, Object>> getPhasingViewByDataCollectionGroupId(int dataCollectionGroupId, int proposalId) {
		String sqlQuery = ByDataCollectionGroupId;

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter("dataCollectionGroupId", dataCollectionGroupId); // Bind the dataCollectionGroupId parameter
		query.setParameter("proposalId", proposalId);                       // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getPhasingViewByDataCollectionId(int dataCollectionId, int proposalId) {
		String sqlQuery = ByDataCollectionId;

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter("dataCollectionId", dataCollectionId); // Bind the dataCollectionId parameter
		query.setParameter("proposalId", proposalId);             // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getPhasingViewByAutoProcIntegrationId(int autoprocIntegrationId, int proposalId) {
		String sqlQuery = ByAutoProcIntegrationId;

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter("autoprocIntegrationId", autoprocIntegrationId); // Bind the autoprocIntegrationId parameter
		query.setParameter("proposalId", proposalId);                      // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getPhasingViewByBlSampleId(int blSampleId, int proposalId) {
		String sqlQuery = BySampleId;

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter("blSampleId", blSampleId);   // Bind the blSampleId parameter
		query.setParameter("proposalId", proposalId);   // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getPhasingViewByProteinId(int proteinId,
			int proposalId) {
		String sqlQuery = ByProteinId;

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter("proteinId", proteinId);   // Bind the proteinId parameter
		query.setParameter("proposalId", proposalId); // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }

	@Override
	public List<Map<String, Object>> getPhasingViewBySessionId(int sessionId, int proposalId) {
		String sqlQuery = BySessionId;

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter("sessionId", sessionId);   // Bind the sessionId parameter
		query.setParameter("proposalId", proposalId); // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();

	}
	
	@Override
	public List<Map<String, Object>> getPhasingViewByStepId(int phasingStepId, int proposalId) {
		String sqlQuery = ByPhasingStepId;

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter("phasingStepId", phasingStepId);   // Bind the phasingStepId parameter
		query.setParameter("proposalId", proposalId);         // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }
	
	
	
	
	
	
	
	private String getPhasingFilesViewTableQuery(){
		return "select * from v_datacollection_phasing_program_run ";
	}
	
	private String FilesByPhasingStepId = getPhasingFilesViewTableQuery() + " where phasingStepId = :phasingStepId and proposalId = :proposalId";
	
	
	private String FilesByPhasingProgramAttachmentId = getPhasingFilesViewTableQuery() + " where phasingProgramAttachmentId = :phasingProgramAttachmentId and proposalId = :proposalId";
	
	@Override
	public List<Map<String, Object>> getPhasingFilesViewByStepId(int phasingStepId, int proposalId) {
		String session = FilesByPhasingStepId;
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter("phasingStepId", phasingStepId)
				.setParameter("proposalId", proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getPhasingFilesViewByPhasingProgramAttachmentId(int phasingProgramAttachmentId, int proposalId) {
		String session = FilesByPhasingProgramAttachmentId;
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter("phasingProgramAttachmentId", phasingProgramAttachmentId)
				.setParameter("proposalId", proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	


}
