/*************************************************************************************************
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
 ****************************************************************************************************/
package ispyb.server.mx.services.sample;


import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.mx.services.ws.rest.WsServiceBean;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import jakarta.persistence.criteria.*;
import org.apache.log4j.Logger;

/**
 * <p>
 * This session bean handles ISPyB Protein3.
 * </p>
 */
@Stateless
public class Protein3ServiceBean extends WsServiceBean implements Protein3Service, Protein3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Protein3ServiceBean.class);

	// Generic HQL request to find instances of Protein3 by pk
	// TODO choose between left/inner join

	// Generic HQL request to find all instances of Protein3
	// TODO choose between left/inner join

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Resource
	private SessionContext context;

	public Protein3ServiceBean() {
	};

	/**
	 * Create new Protein3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Protein3VO create(final Protein3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}

	/**
	 * Update the Protein3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Protein3VO update(final Protein3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the Protein3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		Protein3VO vo = findByPk(pk, false);
		// TODO Edit this business code
		delete(vo);
	}



	/**
	 * Remove the Protein3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Protein3VO vo) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		entityManager.remove(vo);
	}
	
	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Protein3 value object
	 */
	public Protein3VO findByPk(final Integer pk, final boolean withLink1) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		// TODO Edit this business code
		try {
			String qlString = "SELECT vo from Protein3VO vo "
					+ (withLink1 ? " left join fetch vo.crystalVOs " : "")
					+ " where vo.proteinId = :pk";
			return entityManager.createQuery(qlString, Protein3VO.class)
					.setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all Protein3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<Protein3VO> findAll(final boolean withLink1) throws Exception {

		String qlString = "SELECT vo from Protein3VO vo "
				+ (withLink1 ? "left join fetch vo.crystalVOs " : "");
        return entityManager.createQuery(qlString, Protein3VO.class).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Protein3VO> findByAcronymAndProposalId(final Integer proposalId, final String acronym, final boolean withCrystal, final boolean sortByAcronym) throws Exception {

		EntityManager entityManager = this.entityManager;  // Assuming EntityManager is injected or retrieved beforehand
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Protein3VO> cq = cb.createQuery(Protein3VO.class);
		Root<Protein3VO> root = cq.from(Protein3VO.class);

// Join with Proposal
		Join<Protein3VO, Proposal3VO> proposalJoin = root.join("proposalVO");

		if (proposalId != null) {
			cq.where(cb.equal(proposalJoin.get("proposalId"), proposalId));
		}

		if (acronym != null && !acronym.isEmpty()) {
			cq.where(cb.like(cb.upper(root.get("acronym")), acronym.toUpperCase()));
		}

// Handling fetch join with Crystal based on the withCrystal flag
		if (withCrystal) {
			Fetch<Protein3VO, Crystal3VO> crystalFetch = root.fetch("crystalVOs");
		}

// Sorting
		if (sortByAcronym) {
			cq.orderBy(cb.asc(root.get("acronym")));
		} else {
			cq.orderBy(cb.desc(root.get("proteinId")));
		}

		cq.distinct(true);  // Ensures that the results returned are distinct

// Execute the query
		List<Protein3VO> foundEntities = entityManager.createQuery(cq).getResultList();
		return foundEntities;
	}
	
	public List<Protein3VO> findByAcronymAndProposalId(final Integer proposalId, final String acronym) throws Exception {
		return findByAcronymAndProposalId(proposalId, acronym, false, false);
	}

	public Protein3VO loadEager(Protein3VO vo) throws Exception {
		Protein3VO newVO = this.findByPk(vo.getProteinId(), true);
		return newVO;
	}
	

	private String getViewTableQuery(){
		return this.getQueryFromResourceFile("/queries/ProteinServiceBean/getViewTableQuery.sql");
	}


	/**
	 * update the proposalId, returns the nb of rows updated
	 * 
	 * @param newProposalId
	 * @param oldProposalId
	 * @return
	 * @throws Exception
	 */
	public Integer updateProposalId(final Integer newProposalId, final Integer oldProposalId) throws Exception{

		String sqlString = " update Protein  set proposalId = ?1 WHERE proposalId = ?2";
		Query query = entityManager.createNativeQuery(sqlString)
				.setParameter(1, newProposalId)
				.setParameter(2, oldProposalId);
		return query.executeUpdate();
	}

	@Override
	public List<Protein3VO> findByProposalId(Integer proposalId)
			throws Exception {
		return this.findByAcronymAndProposalId(proposalId, null, false, false);
	}
	
	public List<Protein3VO> findByProposalId(final Integer proposalId, final boolean withCrystal, boolean sortByAcronym) throws Exception{
		return this.findByAcronymAndProposalId(proposalId, null, withCrystal, sortByAcronym);
	}


	/* Private methods ------------------------------------------------------ */

	/**
	 * Checks the data for integrity. E.g. if references and categories exist.
	 * 
	 * @param vo
	 *            the data to check
	 * @param create
	 *            should be true if the value object is just being created in the DB, this avoids some checks like
	 *            testing the primary key
	 * @exception VOValidateException
	 *                if data is not correct
	 */
	private void checkAndCompleteData(Protein3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getProteinId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getProteinId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}

	
	@Override
	public List<Map<String, Object>>  getStatsByProposal(int proposalId) {
		String mySQLQuery = getViewTableQuery()
				+ " where proposalId = ?1";
		Query query = this.entityManager.createNativeQuery(mySQLQuery)
				.setParameter(1, proposalId);
        return (List<Map<String, Object>>) ((Query) query).getResultList();
    }
	
}