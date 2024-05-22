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

package ispyb.server.biosaxs.services.stats;

import ispyb.server.mx.services.ws.rest.WsServiceBean;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;

@Stateless
public class Stats3ServiceBean extends WsServiceBean implements Stats3Service,
		Stats3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Override
	public List getSamplesBy(String start, String end) {
		String queryString = "select count(*) from Specimen spe, Experiment exp where exp.experimentId = spe.experimentId and exp.creationDate > ?1 and exp.creationDate < ?2";

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter(1, start); // Bind the start parameter
		query.setParameter(2, end);     // Bind the end parameter

		return query.getResultList();
	}

	@Override
	public List getSessionsBy(String start, String end) {
		String queryString = "select count(distinct(sessionId)) from Experiment where  creationDate > ?1 and creationDate < ?2";

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter(1, start); // Bind the start date parameter
		query.setParameter(2, end);     // Bind the end date parameter

		return query.getResultList();
	}

	@Override
	public List getExperimentsBy(String type, String start, String end) {
		String queryString = " select count(*) from Experiment "
				+ "										where experimentType = ?1 and creationDate > ?2 and creationDate < ?3";

		Query query = this.entityManager.createNativeQuery(queryString);
		query.setParameter(1, type);  // Bind the type parameter
		query.setParameter(2, start); // Bind the start date parameter
		query.setParameter(3, end);     // Bind the end date parameter

		return query.getResultList();
	}

	@Override
	public List getFramesBy(String start, String end) {
		String queryString = " select count(*) from Frame "
				+ "										where creationDate > ?1 and creationDate < ?2";

		Query query = this.entityManager.createNativeQuery(queryString);
		query.setParameter(1, start); // Bind the start date parameter
		query.setParameter(2, end);     // Bind the end date parameter

		return query.getResultList();

	}

	@Override
	public List<Map<String, Object>> getAutoprocStatsByDate(
			String autoprocStatisticsType, Date startDate, Date endDate) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		String queryString = "select * from v_mx_autoprocessing_stats where startTime >= ?1 and startTime <= ?2 and scalingStatisticsType = ?3";

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter(1, dt1.format(startDate)); // Bind the start date parameter
		query.setParameter(2, dt1.format(endDate));     // Bind the end date parameter
		query.setParameter(3, autoprocStatisticsType); // Bind the type parameter

		return query.getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getAutoprocStatsByDate(
			String autoprocStatisticsType, Date startDate, Date endDate, String beamline) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		String queryString = "select * from v_mx_autoprocessing_stats where startTime >= ?1 and startTime <= ?2 and scalingStatisticsType = ?3"
				+ " and beamLineName = ?4"; // Use a parameter placeholder for the beamline name.

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter(1, dt1.format(startDate));
		query.setParameter(2, dt1.format(endDate));
		query.setParameter(3, autoprocStatisticsType);
		query.setParameter(4, beamline); // Set the parameter for the beamline name safely

		return query.getResultList();
    }

	@Override
	public List<Map<String, Object>> getDatacollectionStatsByDate(
			String datacollectionImages, Date startDate, Date endDate, String[] datacollectionTestProposals) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedStart = dt1.format(startDate);
		String formattedEnd = dt1.format(endDate);
		String formattedImages = datacollectionImages;
		String proposalList = String.join(", ", datacollectionTestProposals);

		String queryString = "select count(DC.dataCollectionId) as \"Datasets\", "
													+"	  count(distinct(case when right(DC.imageDirectory,1) = '/' then left(DC.imageDirectory,length(DC.imageDirectory)-1) "
													+"						  else DC.imageDirectory end)) as \"Samples\" "
													+"from DataCollection DC "
													+"join DataCollectionGroup DCG on DCG.dataCollectionGroupId = DC.dataCollectionGroupId "
													+"join BLSession BLS on BLS.sessionId = DCG.sessionId "
													+"join Proposal P on P.proposalId = BLS.proposalId "
													+"where DC.startTime >= ?1 "
													+"and DC.startTime <= ?2 "
													+"and DC.numberOfImages <= ?3 "
													+"and DCG.comments not in (' Data collection failed!\n') "
													+"and P.proposalNumber not in (?4) ";

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter(1, formattedStart);
		query.setParameter(2, formattedEnd);
		query.setParameter(3, Integer.parseInt(formattedImages));
		query.setParameter(4, proposalList); // Assumes proposalList is a collection type compatible with the query

		return query.getResultList();
    }

	@Override
	public List<Map<String, Object>> getDatacollectionStatsByDate(
			String datacollectionImages, Date startDate, Date endDate, String[] datacollectionTestProposals, String beamline) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedStart = dt1.format(startDate);
		String formattedEnd = dt1.format(endDate);
		String formattedImages = datacollectionImages;
		String proposalList = String.join(", ", datacollectionTestProposals);

		String queryString = "select count(DC.dataCollectionId) as \"Datasets\", "
														+"	  count(distinct(case when right(DC.imageDirectory,1) = '/' then left(DC.imageDirectory,length(DC.imageDirectory)-1) "
														+"						  else DC.imageDirectory end)) as \"Samples\" "
														+"from DataCollection DC "
														+"join DataCollectionGroup DCG on DCG.dataCollectionGroupId = DC.dataCollectionGroupId "
														+"join BLSession BLS on BLS.sessionId = DCG.sessionId "
														+"join Proposal P on P.proposalId = BLS.proposalId "
														+"where DC.startTime >= ?1 "
														+"and DC.startTime <= ?2 "
														+"and DC.numberOfImages <= ?3 "
														+"and DCG.comments not in (' Data collection failed!\n') "
														+"and P.proposalNumber not in (?4) and beamLineName = ?5 ";

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter(1, formattedStart);
		query.setParameter(2, formattedEnd);
		query.setParameter(3, formattedImages);
		query.setParameter(4, proposalList);
		query.setParameter(5, beamline); // Setting the parameter for BEAMLINENAME as before

		return query.getResultList();
    }

	@Override
	public List<Map<String, Object>> getExperimentStatsByDate(
			Date startDate, Date endDate, String[] datacollectionTestProposals) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedStart = dt1.format(startDate);
		String formattedEnd = dt1.format(endDate);
		String proposalList = String.join(", ", datacollectionTestProposals);

		String queryString = "select * from v_mx_experiment_stats where startTime >= ?1 and startTime <= ?2 and comments not in (' Data collection failed!\\n') and proposalNumber not in (?3) ";

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter(1, formattedStart);
		query.setParameter(2, formattedEnd);
		query.setParameter(3, proposalList);

		return query.getResultList();
    }

	@Override
	public List<Map<String, Object>> getExperimentStatsByDate(
			Date startDate, Date endDate, String[] datacollectionTestProposals, String beamline) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedStart = dt1.format(startDate);
		String formattedEnd = dt1.format(endDate);
		String proposalList = String.join(", ", datacollectionTestProposals);

		String queryString = "select * from v_mx_experiment_stats where startTime >= ?1 and startTime <= ?2 and comments not in (' Data collection failed!\\n') and proposalNumber not in (?3) " + " and beamLineName = ?4";

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter(1, formattedStart);
		query.setParameter(2, formattedEnd);
		query.setParameter(3, proposalList);
		query.setParameter(4, beamline);

		return query.getResultList();
    }
}
