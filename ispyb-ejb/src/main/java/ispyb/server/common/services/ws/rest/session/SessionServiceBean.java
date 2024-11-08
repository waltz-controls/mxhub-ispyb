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

package ispyb.server.common.services.ws.rest.session;

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.util.List;
import java.util.Map;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;


@Stateless
public class SessionServiceBean extends WsServiceBean  implements SessionService, SessionServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/session/getViewTableQuery.sql");
	}
	
	
	@Override
	public List<Map<String, Object>> getSessionViewBySessionId(int proposalId, int sessionId) {
		String session = getViewTableQuery() + " where v_session.sessionId = ?1 and proposalId = ?2 order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, sessionId)
				.setParameter(2, proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	
	@Override
	public List<Map<String, Object>> getSessionViewByProposalId(int proposalId) {
		String session = getViewTableQuery() + " where v_session.proposalId = ?1 order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getSessionViewByDates(String startDate, String endDate) {
		String session = getViewTableQuery() + " where " + "((BLSession_startDate >= ?1 and BLSession_startDate <= ?2) "
				+ "or "
				+ " (BLSession_endDate >= ?1 and BLSession_endDate <= ?2)"
				+ "or "
				+ " (BLSession_endDate >= ?2 and BLSession_startDate <= ?1)"
				+ "or "
				+ " (BLSession_endDate <= ?2 and BLSession_startDate >= ?1))"
				+ " order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, startDate)
				.setParameter(2, endDate);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getSessionViewByProposalAndDates(int proposalId, String startDate, String endDate) {
		String session = getViewTableQuery() + " where v_session.proposalId = ?1 and " + "((BLSession_startDate >= ?2 and BLSession_startDate <= ?3) "
				+ "or "
				+ " (BLSession_endDate >= ?2 and BLSession_endDate <= ?3)"
				+ "or "
				+ " (BLSession_endDate >= ?3 and BLSession_startDate <= ?2)"
				+ "or "
				+ " (BLSession_endDate <= ?3 and BLSession_startDate >= ?2))"
				+ " order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(2, startDate)
				.setParameter(3, endDate)
				.setParameter(1, String.valueOf(proposalId));
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

	@Override
	public List<Map<String, Object>> getSessionViewByBeamlineOperator( String beamlineOperator) {
		String session = getViewTableQuery()
				+ " where v_session.beamLineOperator LIKE ?1 order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, "%" +  beamlineOperator + "%");
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

	@Override
	public List<Map<String, Object>> getSessionViewByDates(String startDate, String endDate, String siteId) {
		String session = getViewTableQuery() + " where " + "((BLSession_startDate >= ?1 and BLSession_startDate <= ?2) "
				+ "or "
				+ " (BLSession_endDate >= ?1 and BLSession_endDate <= ?2)"
				+ "or "
				+ " (BLSession_endDate >= ?2 and BLSession_startDate <= ?1)"
				+ "or "
				+ " (BLSession_endDate <= ?2 and BLSession_startDate >= ?1))"
				+ " and v_session.operatorSiteNumber=?3 order by v_session.sessionId DESC";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, startDate)
				.setParameter(2, endDate)
				.setParameter(3, siteId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

}