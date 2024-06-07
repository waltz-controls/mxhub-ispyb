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
//TODO do we need this class?
package ispyb.server.mx.services.ws.rest.xfefluorescencespectrum;

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;


@Stateless
public class XFEFluorescenSpectrumRestWsServiceBean extends WsServiceBean implements XFEFluorescenSpectrumRestWsService, XFEFluorescenSpectrumRestWsServiceLocal{
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;


	@Override
	public List<Map<String, Object>> getViewBySessionId(int proposalId, int sessionId) {
		String sqlQuery = "SELECT * FROM v_xfeFluorescenceSpectrum WHERE sessionId = ?1 and BLSession_proposalId=?2";
		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class)
				.setParameter(2, proposalId)
				.setParameter(1, sessionId);

        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getViewById(int proposalId, int xfeFluorescenceSpectrumId) {
		String sqlQuery = "SELECT * FROM v_xfeFluorescenceSpectrum WHERE xfeFluorescenceSpectrumId = ?1 and BLSession_proposalId=?2";
		Query query = this.entityManager.createNativeQuery(sqlQuery, Map.class)
				.setParameter(2, proposalId)
				.setParameter(1, xfeFluorescenceSpectrumId);
		

        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	

	
}
