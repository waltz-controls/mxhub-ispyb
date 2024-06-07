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


	@Override
	public List<Map<String, Object>> getPhasingViewByDataCollectionGroupId(int dataCollectionGroupId, int proposalId) {
		String sqlQuery = getPhasingViewTableQuery()
				+ " where DataCollection_dataCollectionGroupId = ?1 and BLSession_proposalId = ?2 group by PhasingStep_phasingStepId";

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter(1, dataCollectionGroupId); // Bind the dataCollectionGroupId parameter
		query.setParameter(2, proposalId);                       // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getPhasingViewByDataCollectionId(int dataCollectionId, int proposalId) {
		String sqlQuery = getPhasingViewTableQuery()
				+ " where DataCollection_dataCollectionId = ?1 and BLSession_proposalId = ?2 group by PhasingStep_phasingStepId";

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter(1, dataCollectionId); // Bind the dataCollectionId parameter
		query.setParameter(2, proposalId);             // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getPhasingViewByAutoProcIntegrationId(int autoprocIntegrationId, int proposalId) {
		String sqlQuery = getPhasingViewTableQuery()
				+ " where AutoProcIntegration_autoProcIntegrationId = ?1 and BLSession_proposalId = ?2 group by PhasingStep_phasingStepId";

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter(1, autoprocIntegrationId); // Bind the autoprocIntegrationId parameter
		query.setParameter(2, proposalId);                      // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getPhasingViewByBlSampleId(int blSampleId, int proposalId) {
		String sqlQuery = getPhasingViewTableQuery()
				+ " where BLSample_blSampleId = ?1 and BLSession_proposalId = ?2 group by PhasingStep_phasingStepId";

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter(1, blSampleId);   // Bind the blSampleId parameter
		query.setParameter(2, proposalId);   // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getPhasingViewByProteinId(int proteinId,
			int proposalId) {
		String sqlQuery = getPhasingViewTableQuery()
				+ " where Protein_proteinId = ?1 and BLSession_proposalId = ?2 group by PhasingStep_phasingStepId";

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter(1, proteinId);   // Bind the proteinId parameter
		query.setParameter(2, proposalId); // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }

	@Override
	public List<Map<String, Object>> getPhasingViewBySessionId(int sessionId, int proposalId) {
		String sqlQuery = getPhasingViewTableQuery()
				+ " where BLSession_sessionId = ?1 and BLSession_proposalId = ?2 group by PhasingStep_phasingStepId";

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter(1, sessionId);   // Bind the sessionId parameter
		query.setParameter(2, proposalId); // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();

	}
	
	@Override
	public List<Map<String, Object>> getPhasingViewByStepId(int phasingStepId, int proposalId) {
		String sqlQuery = getPhasingViewTableQuery()
				+ " where PhasingStep_phasingStepId = ?1 and BLSession_proposalId = ?2 group by PhasingStep_phasingStepId";

		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class);
		query.setParameter(1, phasingStepId);   // Bind the phasingStepId parameter
		query.setParameter(2, proposalId);         // Bind the proposalId parameter

		return (List<Map<String, Object>>) query.getResultList();
    }


	@Deprecated(forRemoval = true)
	@Override
	public List<Map<String, Object>> getPhasingFilesViewByStepId(int phasingStepId, int proposalId) {
		String session = "select * from v_datacollection_phasing_program_run "
				+ " where phasingStepId = ?1 and proposalId = ?2";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, phasingStepId)
				.setParameter(2, proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

	@Deprecated(forRemoval = true)
	@Override
	public List<Map<String, Object>> getPhasingFilesViewByPhasingProgramAttachmentId(int phasingProgramAttachmentId, int proposalId) {
		String session = "select * from v_datacollection_phasing_program_run "
				+ " where phasingProgramAttachmentId = ?1 and proposalId = ?2";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, phasingProgramAttachmentId)
				.setParameter(2, proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	


}
