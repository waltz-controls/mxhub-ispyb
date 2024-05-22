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

package ispyb.server.biosaxs.services.ws.rest.datacollection;

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;

/**
 * TODO this bean does not work as there is no such table: pydb.v_saxs_datacollection
 */
@Deprecated(forRemoval = true)
@Stateless
public class SaxsDataCollectionRestWsServiceBean extends WsServiceBean implements SaxsDataCollectionRestWsService, SaxsDataCollectionRestWsServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;



	@Override
	public Collection<? extends Map<String, Object>> getDataCollectionByExperimentId(int proposalId, Integer experimentId) {
		String session = "select v_saxs_datacollection.*,\n"
				+ "(select SubtractionToAbInitioModel.abinitioId from SubtractionToAbInitioModel where SubtractionToAbInitioModel.subtractionId = v_saxs_datacollection.Subtraction_subtractionId) as SubtractionToAbInitioModel_abinitioId\n"
				+ "from v_saxs_datacollection "
				+ " where Experiment_proposalId = ?1 and Experiment_experimentId = ?2 group by MeasurementToDataCollection_measurementToDataCollectionId order by Measurement_measurementId DESC;";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, proposalId)
				.setParameter(2, experimentId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public Collection<? extends Map<String, Object>> getDataCollectionBySessionId(int proposalId, Integer sessionId) {
		String session = "select v_saxs_datacollection.*,\n"
				+ "(select SubtractionToAbInitioModel.abinitioId from SubtractionToAbInitioModel where SubtractionToAbInitioModel.subtractionId = v_saxs_datacollection.Subtraction_subtractionId) as SubtractionToAbInitioModel_abinitioId\n"
				+ "from v_saxs_datacollection "
				+ " where Experiment_proposalId = ?1 and Experiment_sessionId = ?2 group by MeasurementToDataCollection_measurementToDataCollectionId order by Measurement_measurementId DESC;";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, proposalId)
				.setParameter(2, sessionId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public Collection<? extends Map<String, Object>> getDataCollectionByMacromoleculeId(int proposalId, Integer macromoleculeId) {
		String session = "select v_saxs_datacollection.*,\n"
				+ "(select SubtractionToAbInitioModel.abinitioId from SubtractionToAbInitioModel where SubtractionToAbInitioModel.subtractionId = v_saxs_datacollection.Subtraction_subtractionId) as SubtractionToAbInitioModel_abinitioId\n"
				+ "from v_saxs_datacollection "
				+ "  where Experiment_proposalId = ?1 and MeasurementToDataCollection_dataCollectionId IN (select MeasurementToDataCollection_dataCollectionId from v_saxs_datacollection where Macromolecule_macromoleculeId = ?2) group by MeasurementToDataCollection_measurementToDataCollectionId order by Measurement_measurementId DESC;";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, proposalId)
				.setParameter(2, macromoleculeId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
	@Override
	public Collection<? extends Map<String, Object>> getDataCollectionByDataCollectionId(int proposalId, Integer dataCollectionId) {
		String session = "select v_saxs_datacollection.*,\n"
				+ "(select SubtractionToAbInitioModel.abinitioId from SubtractionToAbInitioModel where SubtractionToAbInitioModel.subtractionId = v_saxs_datacollection.Subtraction_subtractionId) as SubtractionToAbInitioModel_abinitioId\n"
				+ "from v_saxs_datacollection "
				+ " where Experiment_proposalId = ?1 and MeasurementToDataCollection_dataCollectionId = ?2 group by MeasurementToDataCollection_measurementToDataCollectionId order by Measurement_measurementId DESC;";
		Query query = this.entityManager.createNativeQuery(session, Map.class)
				.setParameter(1, proposalId)
				.setParameter(2, dataCollectionId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	

	
}
