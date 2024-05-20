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

package ispyb.server.biosaxs.services.core.analysis;


import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;
import ispyb.server.biosaxs.vos.datacollection.SaxsDataCollection3VO;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;


@Stateless
public class Analysis3ServiceBean implements Analysis3Service, Analysis3ServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public static String getSelectClause() {
		return " select   Subtraction.volume as volumePorod, "
				+ "Run.creationDate as runCreationDate, "
				+ "Measurement.code as measurementCode, "
				+ "Macromolecule.acronym as macromoleculeAcronym, "
				+ "Buffer.acronym as bufferAcronym, "
				+ "exp.*, "
				+ "Specimen.specimenId , "
				// + "Specimen.experimentId as specimen_experimentId, "
				+ "Specimen.safetyLevelId, "
				// + "Specimen.stockSolutionId as specimen_stockSolutionId, "
				// + "Specimen.code as code, "
				+ "Specimen.concentration as concentration, "
				+ "Specimen.volume as volume, "
				+ "Specimen.comments as specimen_comments, "
				//
				+ "Buffer.bufferId as buffer_bufferId, "
				//
				+ "Macromolecule.macromoleculeId, "

				+ "Measurement.measurementId, "
				+ "Measurement.code, "
				+ "Measurement.priorityLevelId, "
				+ "Measurement.exposureTemperature, "
				+ "Measurement.viscosity, "
				+ "Measurement.flow, "
				+ "Measurement.extraFlowTime, "
				+ "Measurement.volumeToLoad, "
				+ "Measurement.waitTime, "
				+ "Measurement.transmission, "
				+ "Measurement.comments as measurement_comments, "

				+ "MeasurementToDataCollection.measurementToDataCollectionId, "
				+ "MeasurementToDataCollection.dataCollectionOrder, "

				+ "SaxsDataCollection.dataCollectionId, "

				+ "Subtraction.subtractionId, "
				+ "Subtraction.rg, "
				+ "Subtraction.rgStdev, "
				+ "Subtraction.I0, "
				+ "Subtraction.I0Stdev, "
				+ "Subtraction.firstPointUsed, "
				+ "Subtraction.lastPointUsed, "
				+ "Subtraction.quality, "
				+ "Subtraction.isagregated, "

				+ "Subtraction.gnomFilePath, "
				+ "Subtraction.rgGuinier, "
				+ "Subtraction.rgGnom, "
				+ "Subtraction.dmax, "
				+ "Subtraction.total, "
				+ "Subtraction.volume as subtraction_volume, "
				+ "Subtraction.creationTime as subtraction_creationTime, "
				+ "Subtraction.kratkyFilePath, "
				+ "Subtraction.scatteringFilePath, "
				+ "Subtraction.guinierFilePath, "
				+ "Subtraction.substractedFilePath, "
				+ "Subtraction.gnomFilePathOutput, "
				+ "Subtraction.sampleOneDimensionalFiles, "
				+ "Subtraction.bufferOnedimensionalFiles, "
				+ "Subtraction.sampleAverageFilePath, "
				+ "Subtraction.bufferAverageFilePath, "

				+ "Merge.mergeId, "
				+ "Merge.discardedFrameNameList, "
				+ "Merge.averageFilePath, "
				+ "Merge.framesCount, "
				+ "Merge.framesMerge, "

				+ " (select count(*) from FitStructureToExperimentalData as f where f.subtractionId = Subtraction.subtractionId) as fitCount,\r\n"
				+ "(select count(*) from Superposition as f where f.subtractionId = Subtraction.subtractionId) as superposisitionCount,\r\n"
				+ "(select count(*) from RigidBodyModeling as f where f.subtractionId = Subtraction.subtractionId) as rigidbodyCount,\r\n"
				+ "(select count(*) from SubtractionToAbInitioModel as f where f.subtractionId = Subtraction.subtractionId) as abinitioCount ";
	}

	public static String getFromClause() {
		return "  from Experiment exp\r\n"
				+ "  LEFT JOIN Specimen on Specimen.experimentId = exp.experimentId\r\n"
				+ "  LEFT JOIN Buffer on Buffer.bufferId = Specimen.bufferId\r\n"
				+ "  LEFT JOIN Macromolecule on Macromolecule.macromoleculeId = Specimen.macromoleculeId\r\n"
				+ "  LEFT JOIN Measurement on Measurement.specimenId = Specimen.specimenId\r\n"
				+ "  LEFT JOIN Run on Measurement.runId = Run.runId\r\n"
				+ "  LEFT JOIN Merge on Merge.measurementId = Measurement.measurementId\r\n"
				+ "  LEFT JOIN SaxsDataCollection on SaxsDataCollection.experimentId = exp.experimentId\r\n"
				+ "  LEFT JOIN MeasurementToDataCollection on MeasurementToDataCollection.dataCollectionId = SaxsDataCollection.dataCollectionId and MeasurementToDataCollection.measurementId = Measurement.measurementId  \r\n"
				+ "  LEFT JOIN Subtraction on Subtraction.dataCollectionId = SaxsDataCollection.dataCollectionId\r\n";
		// "				 LEFT JOIN SubtractionToAbInitioModel on SubtractionToAbInitioModel.subtractionId = Subtraction.subtractionId\r\n"
		// +
		// "                LEFT JOIN AbInitioModel on AbInitioModel.abInitioModelId = SubtractionToAbInitioModel.abInitioId\r\n"
		// +
		// "                LEFT JOIN Model reference on reference.modelId = AbInitioModel.averagedModelId\r\n"
		// +
		// "                LEFT JOIN Model refined on refined.modelId = AbInitioModel.shapeDeterminationModelId\r\n"
		// +
		// "				 LEFT JOIN FitStructureToExperimentalData on FitStructureToExperimentalData.subtractionId = Subtraction.subtractionId and FitStructureToExperimentalData.fitStructureToExperimentalDataId in (select max(f2.fitStructureToExperimentalDataId) from FitStructureToExperimentalData f2  where f2.subtractionId = Subtraction.subtractionId)  ";
	}

	public static String getExperimentListByProposalId() {
		return ""
				+ "select *, "
				+ " ( "
				+ "  select count(*) "
				+ " from  Specimen s "
				+ " where s.experimentId = e.experimentId "
				+ ") as specimenCount, "
				+ "( "
				+ "  select count(*) "
				+ "  from  Measurement m, Specimen s "
				+ "  where s.experimentId = e.experimentId and m.specimenId = s.specimenId "
				+ ") as measurementCount, "
				+ "( "
				+ "  select count(*) "
				+ "  from  Measurement m, Specimen s "
				+ "  where s.experimentId = e.experimentId and m.specimenId = s.specimenId and m.runId is not null "
				+ ") as measurementDoneCount, "
				+ "( "
				+ "  select count(*) "
				+ "  from  SaxsDataCollection sdc "
				+ "  where sdc.experimentId = e.experimentId "
				+ ") as dataCollectionCount, "
				+ "( "
				+ "  select count(*) "
				+ "  from  SaxsDataCollection sdc, Subtraction sub "
				+ "  where sdc.experimentId = e.experimentId and sub.dataCollectionId = sdc.dataCollectionId "
				+ ") as dataCollectionDoneCount, "
				+ "( "
				+ "  select count(*) "
				+ "  from  Measurement m, Specimen s, Merge me "
				+ "  where s.experimentId = e.experimentId and m.specimenId = s.specimenId and me.measurementId = m.measurementId "
				+ ") as measurementAveragedCount, "
				+ "(  "
				+ "	    select group_concat(distinct(acronym) separator ', ')  "
				+ "	    from Macromolecule ma, Specimen sp, Experiment exp "
				+ "	    where ma.macromoleculeId = sp.macromoleculeId and sp.experimentId = e.experimentId "
				+ "	) as macromolecules "
				+ " from Experiment e where e.proposalId = ?1 ";
	}


	@Deprecated
	@Override
	public List<Map<String,Object>> getAllByMacromolecule(int macromoleculeId, int poposalId) {
		String sb = SQLQueryKeeper.getAnalysisQuery()
				+ " and s.macromoleculeId = ?1"
				+ " and p.proposalId = ?2";
		Query query = this.entityManager.createQuery(sb)
				.setParameter(1, macromoleculeId)
				.setParameter(2, poposalId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}

	@Deprecated
	@Override
	public List<Map<String,Object>> getAllByMacromolecule(int macromoleculeId, int bufferId, int poposalId) {
		String sb1 = SQLQueryKeeper.getAnalysisQuery()
				+ " and s.macromoleculeId = ?1 "
				+ " and p.proposalId = ?2"
				+ " and bu.bufferId = ?3";
		Query query = this.entityManager.createQuery(sb1)
				.setParameter(1, macromoleculeId)
				.setParameter(2, poposalId)
				.setParameter(3, bufferId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String,Object>> getExperimentListByProposalId(int proposalId) {
		String mySQLQuery = getExperimentListByProposalId();
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, proposalId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String,Object>> getExperimentListByProposalId(int proposalId, String experimentType) {
		String mySQLQuery = getExperimentListByProposalId()
				+ " AND e.experimentType = ?2 ";
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, proposalId)
				.setParameter(2, experimentType);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	

	@Override
	public List<Map<String, Object>> getExperimentListBySessionId(
			Integer proposalId, Integer sessionId) {
		String mySQLQuery = getExperimentListByProposalId()
				+ " AND e.sessionId = ?2 ";

		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter(1, proposalId)
				.setParameter(2, sessionId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getExperimentListByExperimentId(
			Integer proposalId, Integer experimentId) {
		String mySQLQuery = getExperimentListByProposalId()
				+ " AND e.experimentId = ?2 ";
		Query query = this.entityManager.createNativeQuery(mySQLQuery)
				.setParameter(1, proposalId)
				.setParameter(2, experimentId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String,Object>> getCompactAnalysisByExperimentId(int experimentId) {

		String sb = getSelectClause()
				+ getFromClause()
				+ " where exp.experimentId = ?1 and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId \r\n"
				+ " order by exp.experimentId ASC, Measurement.priorityLevelId ASC\r\n";
		Query query = this.entityManager.createNativeQuery(sb, Map.class)
				.setParameter(1, experimentId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getCompactAnalysisByProposalId(Integer proposalId, Integer limit) {

		String sb = getSelectClause()
				+ getFromClause()
				+ " where exp.proposalId = ?1 and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId and exp.experimentType != \"TEMPLATE\" \r\n"
				+ " order by exp.experimentId DESC, Measurement.priorityLevelId DESC, Merge.mergeId DESC\r\n"
				+ " limit " + limit + "\r\n";
		Query query = this.entityManager.createNativeQuery(sb, Map.class)
				.setParameter(1, proposalId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getCompactAnalysisByProposalId(Integer proposalId, Integer start, Integer limit) {

		String sb = getSelectClause()
				+ getFromClause()
				+ " where exp.proposalId = ?1 and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId and exp.experimentType != \"TEMPLATE\" \r\n"
				+ " order by exp.experimentId DESC, Measurement.priorityLevelId DESC, Merge.mergeId DESC\r\n"
				+ " limit " + start + "," + limit + "\r\n";
		Query query = this.entityManager.createNativeQuery(sb, Map.class)
				.setParameter(1, proposalId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public BigInteger getCountCompactAnalysisByExperimentId(Integer proposalId) {
		String sb = "Select Count(*) "
				+ getFromClause()
				+ " where exp.proposalId = ?1 and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId and exp.experimentType != \"TEMPLATE\" \r\n"
				+ " order by exp.experimentId DESC, Measurement.priorityLevelId DESC, Merge.mergeId DESC\r\n";

		Query query = this.entityManager.createNativeQuery(sb, BigInteger.class)
				.setParameter(1, proposalId);
		return 	(BigInteger) query.getResultList()
				.stream()
				.findAny()
				.get();//TODO NPE, class cast safety
	}
	
	@Override
	public List<Map<String, Object>> getCompactAnalysisBySubtractionId(String subtractionId) {

		String sb = getSelectClause()
				+ getFromClause()
				+ " where Subtraction.subtractionId = ?1 and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId \r\n"
				+ " order by exp.experimentId DESC, Measurement.priorityLevelId DESC, Merge.mergeId DESC\r\n";

		Query query = this.entityManager.createNativeQuery(sb, Map.class)
				.setParameter(1, subtractionId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}


	@Deprecated
	@Override
	public List<Map<String, Object>> getAllAnalysisInformation() {
		Query query = this.entityManager.createNativeQuery(SQLQueryKeeper.getAnalysisQuery());
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}

	@Override
	public List<Map<String, Object>> getCompactAnalysisByMacromoleculeId(Integer proposalId, Integer macromoleculeId) {
		String sb = getSelectClause()
				+ getFromClause()
				+ " where SaxsDataCollection.dataCollectionId in (select dc.dataCollectionId from SaxsDataCollection dc, MeasurementToDataCollection mtd, Measurement m, Specimen s where m.specimenId = s.specimenId and mtd.measurementId = m.measurementId and dc.dataCollectionId = mtd.dataCollectionId and s.macromoleculeId = ?1 ) and exp.proposalId = ?2 and  SaxsDataCollection.dataCollectionId = MeasurementToDataCollection.dataCollectionId and exp.experimentType != \"TEMPLATE\" \r\n"
				+ " order by exp.experimentId DESC, Measurement.priorityLevelId DESC, Merge.mergeId DESC\r\n";
		Query query = this.entityManager.createNativeQuery(sb, Map.class)
				.setParameter(2, proposalId)
				.setParameter(1, macromoleculeId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public SaxsDataCollection3VO getDataCollection(int dataCollectionId) {
			String ejbQLQuery = "SELECT DISTINCT(datacollection) FROM SaxsDataCollection3VO  datacollection " +
					" LEFT JOIN FETCH datacollection.measurementtodatacollection3VOs " +
					" LEFT JOIN FETCH datacollection.substraction3VOs LEFT JOIN datacollection.substraction3VOs substraction3VOs " +
					" LEFT JOIN FETCH substraction3VOs.sampleOneDimensionalFiles LEFT JOIN substraction3VOs.sampleOneDimensionalFiles sampleOneDimensionalFiles " +
					" LEFT JOIN FETCH sampleOneDimensionalFiles.frametolist3VOs " +
					" LEFT JOIN FETCH substraction3VOs.bufferOneDimensionalFiles LEFT JOIN substraction3VOs.bufferOneDimensionalFiles bufferOneDimensionalFiles " +
					" LEFT JOIN FETCH bufferOneDimensionalFiles.frametolist3VOs " +
					" WHERE datacollection.dataCollectionId = :dataCollectionId";
			TypedQuery<SaxsDataCollection3VO> query = entityManager.createQuery(ejbQLQuery, SaxsDataCollection3VO.class)
					.setParameter("dataCollectionId", dataCollectionId);
			return query.getSingleResult();
	}
	
	@Override
	public List<SaxsDataCollection3VO> getDataCollections(List<Integer> dataCollectionIdList) {
		 List<SaxsDataCollection3VO> result = new ArrayList<SaxsDataCollection3VO>();
		 for (Integer id : dataCollectionIdList) {
			result.add(this.getDataCollection(id));
		}
		 return result;
	}
}
