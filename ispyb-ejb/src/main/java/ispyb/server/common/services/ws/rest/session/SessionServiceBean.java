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

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;


@Stateless
public class SessionServiceBean extends WsServiceBean  implements SessionService, SessionServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	/** SQL common clauses **/
	private String dateClause = "((BLSession_startDate >= :startDate and BLSession_startDate <= :endDate) "
			+ "or "
			+ " (BLSession_endDate >= :startDate and BLSession_endDate <= :endDate)"
			+ "or "
			+ " (BLSession_endDate >= :endDate and BLSession_startDate <= :startDate)"
			+ "or "
			+ " (BLSession_endDate <= :endDate and BLSession_startDate >= :startDate))";
	                            
	/** SQL QUERIES **/
	private  String BySessionId = getViewTableQuery() + " where v_session.sessionId = :sessionId and proposalId = :proposalId order by v_session.sessionId DESC"; 
	private  String ByProposalId = getViewTableQuery() + " where v_session.proposalId = :proposalId order by v_session.sessionId DESC";
	private  String ByDates = getViewTableQuery() + " where " + dateClause + " order by v_session.sessionId DESC";
	
	private  String ByDatesAndSiteId = getViewTableQuery() + " where " + dateClause + " and v_session.operatorSiteNumber=:siteId order by v_session.sessionId DESC";
	
	private  String ByProposalAndDates = getViewTableQuery() + " where v_session.proposalId = :proposalId and " + dateClause + " order by v_session.sessionId DESC";
	
	private  String ByBeamlineOperator = getViewTableQuery() + " where v_session.beamLineOperator LIKE :beamlineOperator order by v_session.sessionId DESC";
	
	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/session/getViewTableQuery.sql");
	}
	
	
	@Override
	public List<Map<String, Object>> getSessionViewBySessionId(int proposalId, int sessionId) {
		String session = BySessionId;
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter("sessionId", sessionId)
				.setParameter("proposalId", proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	
	@Override
	public List<Map<String, Object>> getSessionViewByProposalId(int proposalId) {
		String session = ByProposalId;
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter("proposalId", proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getSessionViewByDates(String startDate, String endDate) {
		String session = ByDates;
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getSessionViewByProposalAndDates(int proposalId, String startDate, String endDate) {
		String session = ByProposalAndDates;
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("proposalId", String.valueOf(proposalId));
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

	@Override
	public List<Map<String, Object>> getSessionViewByBeamlineOperator( String beamlineOperator) {
		String session = ByBeamlineOperator;
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter("beamlineOperator", "%" +  beamlineOperator + "%");
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

	@Override
	public List<Map<String, Object>> getSessionViewByDates(String startDate, String endDate, String siteId) {
		String session = ByDatesAndSiteId;
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.setParameter("siteId", siteId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }

}