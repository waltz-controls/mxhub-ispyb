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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;




@Stateless
public class Analysis3ServiceBean implements Analysis3Service, Analysis3ServiceLocal {
	/** The entity manager. */
	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;


	@Deprecated
	@Override
	public List<Map<String,Object>> getAllByMacromolecule(int macromoleculeId, int poposalId) {
		String mySQLQuery = SQLQueryKeeper.getAnalysisByMacromoleculeId(macromoleculeId, poposalId);
		Query query = this.entityManager.createQuery(mySQLQuery);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}

	@Deprecated
	@Override
	public List<Map<String,Object>> getAllByMacromolecule(int macromoleculeId, int bufferId, int poposalId) {
		String mySQLQuery = SQLQueryKeeper.getAnalysisByMacromoleculeId(macromoleculeId, bufferId, poposalId);
		Query query = this.entityManager.createQuery(mySQLQuery);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String,Object>> getExperimentListByProposalId(int proposalId) {
		String mySQLQuery = SQLQueryKeeper.getExperimentListByProposalId(proposalId);
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("proposalId", proposalId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String,Object>> getExperimentListByProposalId(int proposalId, String experimentType) {
		StringBuilder sb = new StringBuilder(
				SQLQueryKeeper.getExperimentListByProposalId(proposalId));
		sb.append(" and e.experimentType = :experimentType ");
		String mySQLQuery = sb.toString();
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("proposalId", proposalId)
				.setParameter("experimentType", experimentType);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	

	@Override
	public List<Map<String, Object>> getExperimentListBySessionId(
			Integer proposalId, Integer sessionId) {
		StringBuilder sb = new StringBuilder(
				SQLQueryKeeper.getExperimentListByProposalId(proposalId));
		sb.append(" and e.sessionId = :sessionId ");
		String mySQLQuery = sb.toString();

		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("proposalId", proposalId)
				.setParameter("sessionId", sessionId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getExperimentListByExperimentId(
			Integer proposalId, Integer experimentId) {
		String mySQLQuery = SQLQueryKeeper.getExperimentListByExperimentId(proposalId, experimentId);
		Query query = this.entityManager.createNativeQuery(mySQLQuery)
				.setParameter("proposalId", proposalId)
				.setParameter("experimentId", experimentId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String,Object>> getCompactAnalysisByExperimentId(int experimentId) {
		String mySQLQuery = SQLQueryKeeper.getAnalysisCompactQueryByExperimentId();
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("experimentId", experimentId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getCompactAnalysisByProposalId(Integer proposalId, Integer limit) {
		String mySQLQuery = SQLQueryKeeper.getAnalysisCompactQueryByProposalId(limit);
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("proposalId", proposalId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public List<Map<String, Object>> getCompactAnalysisByProposalId(Integer proposalId, Integer start, Integer limit) {
		String mySQLQuery = SQLQueryKeeper.getAnalysisCompactQueryByProposalId(start, limit);
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("proposalId", proposalId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public BigInteger getCountCompactAnalysisByExperimentId(Integer proposalId) {
		String mySQLQuery = SQLQueryKeeper.getCountAnalysisCompactQueryByProposalId(proposalId);
		Query query = this.entityManager.createNativeQuery(mySQLQuery, BigInteger.class)
				.setParameter("proposalId", proposalId);
		return 	(BigInteger) query.getResultList()
				.stream()
				.findAny()
				.get();//TODO NPE, class cast safety
	}
	
	@Override
	public List<Map<String, Object>> getCompactAnalysisBySubtractionId(String subtractionId) {
		String mySQLQuery = SQLQueryKeeper.getAnalysisCompactQueryBySubtractionId();
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("subtractionId", subtractionId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}


	@Deprecated
	@Override
	public List<Map<String, Object>> getAllAnalysisInformation() {
		Query query = this.entityManager.createQuery(SQLQueryKeeper.getAnalysisQuery());
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}

	@Override
	public List<Map<String, Object>> getCompactAnalysisByMacromoleculeId(Integer proposalId, Integer macromoleculeId) {
		String mySQLQuery = SQLQueryKeeper.getAnalysisCompactQueryByMacromoleculeId();
		Query query = this.entityManager.createNativeQuery(mySQLQuery, Map.class)
				.setParameter("proposalId", proposalId)
				.setParameter("macromoleculeId", macromoleculeId);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> aliasToValueMapList= query.getResultList();
		return 	aliasToValueMapList;
	}
	
	@Override
	public SaxsDataCollection3VO getDataCollection(int dataCollectionId) {
		StringBuilder ejbQLQuery = new StringBuilder();
		ejbQLQuery.append("SELECT DISTINCT(datacollection) FROM SaxsDataCollection3VO  datacollection ");
		ejbQLQuery.append(" LEFT JOIN FETCH datacollection.measurementtodatacollection3VOs measurementtodatacollection3VOs ");
		ejbQLQuery.append(" LEFT JOIN FETCH datacollection.substraction3VOs substraction3VOs ");
		ejbQLQuery.append(" LEFT JOIN FETCH substraction3VOs.sampleOneDimensionalFiles sampleOneDimensionalFiles ");
		ejbQLQuery.append(" LEFT JOIN FETCH sampleOneDimensionalFiles.frametolist3VOs sampleFrametolist3VOs ");
		ejbQLQuery.append(" LEFT JOIN FETCH substraction3VOs.bufferOneDimensionalFiles bufferOneDimensionalFiles ");
		ejbQLQuery.append(" LEFT JOIN FETCH bufferOneDimensionalFiles.frametolist3VOs bufferFrametolist3VOs ");
		ejbQLQuery.append(" WHERE datacollection.dataCollectionId = :dataCollectionId");
		TypedQuery<SaxsDataCollection3VO> query = entityManager.createQuery(ejbQLQuery.toString(), SaxsDataCollection3VO.class).setParameter("dataCollectionId", dataCollectionId);
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
