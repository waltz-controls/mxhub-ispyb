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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;

@Stateless
public class Stats3ServiceBean extends WsServiceBean implements Stats3Service,
		Stats3ServiceLocal {

	private String GET_EXPERIMENT_COUNT_BY_DATE = " select count(*) from Experiment "
			+ "										where experimentType = :TYPE and creationDate > :START and creationDate < :END";

	private String GET_FRAMES_COUNT_BY_DATE = " select count(*) from Frame "
			+ "										where creationDate > :START and creationDate < :END";

	private String GET_SAMPLES_COUNT_BY_DATE = "select count(*) from Specimen spe, Experiment exp where exp.experimentId = spe.experimentId and exp.creationDate > :START and exp.creationDate < :END";

	private String GET_SESSIONS = "select count(distinct(sessionId)) from Experiment where  creationDate > :START and creationDate < :END";

	private String AUTOPROCSTATS_QUERY = "select * from v_mx_autoprocessing_stats where startTime >= :START and startTime <= :END and scalingStatisticsType = :TYPE";

	private String DATACOLLECTIONSTATS_QUERY ="select count(DC.dataCollectionId) as \"Datasets\", "
												+"	  count(distinct(case when right(DC.imageDirectory,1) = '/' then left(DC.imageDirectory,length(DC.imageDirectory)-1) "
												+"						  else DC.imageDirectory end)) as \"Samples\" "
												+"from DataCollection DC "
												+"join DataCollectionGroup DCG on DCG.dataCollectionGroupId = DC.dataCollectionGroupId "
												+"join BLSession BLS on BLS.sessionId = DCG.sessionId "
												+"join Proposal P on P.proposalId = BLS.proposalId "
												+"where DC.startTime >= :START "
												+"and DC.startTime <= :END "
												+"and DC.numberOfImages <= :LIMITIMAGES "
												+"and DCG.comments not in (' Data collection failed!\n') "
												+"and P.proposalNumber not in (:TESTPROPOSALS) ";

	private String EXPERIMENTSTATS_QUERY ="select * from v_mx_experiment_stats where startTime >= :START and startTime <= :END and comments not in (' Data collection failed!\\n') and proposalNumber not in (:TESTPROPOSALS) ";

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@Override
	public List getSamplesBy(String start, String end) {
		String queryString = GET_SAMPLES_COUNT_BY_DATE;

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter("START", start); // Bind the start parameter
		query.setParameter("END", end);     // Bind the end parameter

		return query.getResultList();
	}

	@Override
	public List getSessionsBy(String start, String end) {
		String queryString = GET_SESSIONS;

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter("START", start); // Bind the start date parameter
		query.setParameter("END", end);     // Bind the end date parameter

		return query.getResultList();
	}

	@Override
	public List getExperimentsBy(String type, String start, String end) {
		String queryString = GET_EXPERIMENT_COUNT_BY_DATE;

		Query query = this.entityManager.createNativeQuery(queryString);
		query.setParameter("TYPE", type);  // Bind the type parameter
		query.setParameter("START", start); // Bind the start date parameter
		query.setParameter("END", end);     // Bind the end date parameter

		return query.getResultList();
	}

	@Override
	public List getFramesBy(String start, String end) {
		String queryString = GET_FRAMES_COUNT_BY_DATE;

		Query query = this.entityManager.createNativeQuery(queryString);
		query.setParameter("START", start); // Bind the start date parameter
		query.setParameter("END", end);     // Bind the end date parameter

		return query.getResultList();

	}

	@Override
	public List<Map<String, Object>> getAutoprocStatsByDate(
			String autoprocStatisticsType, Date startDate, Date endDate) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		String queryString = AUTOPROCSTATS_QUERY;

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter("START", dt1.format(startDate)); // Bind the start date parameter
		query.setParameter("END", dt1.format(endDate));     // Bind the end date parameter
		query.setParameter("TYPE", autoprocStatisticsType); // Bind the type parameter

		return query.getResultList();
    }
	
	@Override
	public List<Map<String, Object>> getAutoprocStatsByDate(
			String autoprocStatisticsType, Date startDate, Date endDate, String beamline) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		String queryString = AUTOPROCSTATS_QUERY
				+ " and beamLineName = :BEAMLINENAME"; // Use a parameter placeholder for the beamline name.

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter("START", dt1.format(startDate));
		query.setParameter("END", dt1.format(endDate));
		query.setParameter("TYPE", autoprocStatisticsType);
		query.setParameter("BEAMLINENAME", beamline); // Set the parameter for the beamline name safely

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

		String queryString = DATACOLLECTIONSTATS_QUERY;

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter("START", formattedStart);
		query.setParameter("END", formattedEnd);
		query.setParameter("LIMITIMAGES", Integer.parseInt(formattedImages));
		query.setParameter("TESTPROPOSALS", proposalList); // Assumes proposalList is a collection type compatible with the query

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

		String queryString = DATACOLLECTIONSTATS_QUERY
				.concat(" and beamLineName = :BEAMLINENAME ");

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter("START", formattedStart);
		query.setParameter("END", formattedEnd);
		query.setParameter("LIMITIMAGES", formattedImages);
		query.setParameter("TESTPROPOSALS", proposalList);
		query.setParameter("BEAMLINENAME", beamline); // Setting the parameter for BEAMLINENAME as before

		return query.getResultList();
    }

	@Override
	public List<Map<String, Object>> getExperimentStatsByDate(
			Date startDate, Date endDate, String[] datacollectionTestProposals) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedStart = dt1.format(startDate);
		String formattedEnd = dt1.format(endDate);
		String proposalList = String.join(", ", datacollectionTestProposals);

		String queryString = EXPERIMENTSTATS_QUERY;

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter("START", formattedStart);
		query.setParameter("END", formattedEnd);
		query.setParameter("TESTPROPOSALS", proposalList);

		return query.getResultList();
    }

	@Override
	public List<Map<String, Object>> getExperimentStatsByDate(
			Date startDate, Date endDate, String[] datacollectionTestProposals, String beamline) {
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedStart = dt1.format(startDate);
		String formattedEnd = dt1.format(endDate);
		String proposalList = String.join(", ", datacollectionTestProposals);

		String queryString = EXPERIMENTSTATS_QUERY + " and beamLineName = :BEAMLINENAME";

		Query query = this.entityManager.createNativeQuery(queryString, Map.class);
		query.setParameter("START", formattedStart);
		query.setParameter("END", formattedEnd);
		query.setParameter("TESTPROPOSALS", proposalList);
		query.setParameter("BEAMLINENAME", beamline);

		return query.getResultList();
    }
}
