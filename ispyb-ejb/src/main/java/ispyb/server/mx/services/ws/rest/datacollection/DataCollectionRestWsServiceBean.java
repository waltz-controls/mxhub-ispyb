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

package ispyb.server.mx.services.ws.rest.datacollection;

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;


@Stateless
public class DataCollectionRestWsServiceBean extends WsServiceBean implements DataCollectionRestWsService, DataCollectionRestWsServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/DataCollectionRestWsServiceBean/getViewTableQuery.sql");
	}
	
	@Override
	public Collection<? extends Map<String, Object>> getViewDataCollectionsByWorkflowId(int proposalId, Integer workflowId) {
		String mySQLQuery = this.getViewTableQuery() + " where proposalId = :proposalId and workflowId = :workflowId  group by v_datacollection.dataCollectionId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery)
				.setParameter("proposalId", proposalId)
				.setParameter("workflowId", workflowId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	

	@Override
	public Collection<? extends Map<String, Object>> getDataCollectionByDataCollectionGroupId(int proposalId, Integer dataCollectionGroupId) {
		String mySQLQuery = this.getViewTableQuery() + " where proposalId = :proposalId and dataCollectionGroupId = :dataCollectionGroupId  group by v_datacollection.dataCollectionId";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("proposalId", proposalId)
				.setParameter("dataCollectionGroupId", dataCollectionGroupId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
}
