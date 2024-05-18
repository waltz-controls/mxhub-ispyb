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

package ispyb.server.common.services.proposals;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ispyb.server.common.vos.proposals.Proposal3VO;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import jakarta.persistence.criteria.*;
import org.apache.log4j.Logger;

import ispyb.server.common.vos.proposals.LabContact3VO;



/**
 * <p>
 * The data access object for LabContact3 objects (rows of table TableName).
 * </p>
 * 
 */
@Stateless
public class LabContact3ServiceBean implements LabContact3Service, LabContact3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(LabContact3ServiceBean.class);


	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public LabContact3ServiceBean() {
	};

	/* Creation/Update methods ---------------------------------------------- */
	
	/**
	 * Create new LabContact3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	
	public LabContact3VO create(final LabContact3VO vo) throws Exception {
		
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}
	
	/**
	 * Update the LabContact3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public LabContact3VO update(final LabContact3VO vo) throws Exception {
		
		this.checkAndCompleteData(vo, false);
		return this.entityManager.merge(vo);
	}

	/**
	 * Remove the LabContact3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		LabContact3VO vo = findByPk(pk);
		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		delete(vo);
	}


	/**
	 * Remove the LabContact3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final LabContact3VO vo) throws Exception {
		
		entityManager.remove(vo);
	}
	
		

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the LabContact3 value object
	 */
	public LabContact3VO findByPk(final Integer pk) throws Exception {
		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		try {
			return entityManager.createQuery("SELECT LabContact3VO FROM LabContact3VO vo WHERE vo.labContactId = :pk", LabContact3VO.class)
					.setParameter("pk", pk)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all LabContact3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<LabContact3VO> findAll() throws Exception {
        String query = "SELECT LabContact3VO FROM LabContact3VO vo ";
		List<LabContact3VO> foundEntities = this.entityManager.createQuery(query, LabContact3VO.class)
				.getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<LabContact3VO> findFiltered(Integer proposalId, String cardName)  {
		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<LabContact3VO> cq = cb.createQuery(LabContact3VO.class);
		Root<LabContact3VO> root = cq.from(LabContact3VO.class);

		List<Predicate> predicates = new ArrayList<>();

// Applying conditions based on the input
		if (cardName != null && !cardName.isEmpty()) {
			predicates.add(cb.like(cb.lower(root.get("cardName")), "%" + cardName.toLowerCase() + "%"));
		}

		if (proposalId != null) {
			Join<LabContact3VO, Proposal3VO> proposalJoin = root.join("proposalVO", JoinType.INNER);
			predicates.add(cb.equal(proposalJoin.get("proposalId"), proposalId));
		}

		cq.where(cb.and(predicates.toArray(new Predicate[0])));

// Ensuring distinct results
		cq.distinct(true);

// Ordering the results
		cq.orderBy(cb.desc(root.get("labContactId")));

// Executing the query
		List<LabContact3VO> results = em.createQuery(cq).getResultList();
		return results;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LabContact3VO> findByPersonIdAndProposalId(final Integer personId, final Integer proposalId) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

// Create the criteria query object specifying the result type
		CriteriaQuery<LabContact3VO> cq = cb.createQuery(LabContact3VO.class);
		Root<LabContact3VO> labContact = cq.from(LabContact3VO.class);

// Define predicates for the query parameters
		Predicate personIdPredicate = cb.equal(labContact.get("personId"), personId);
		Predicate proposalIdPredicate = cb.equal(labContact.get("proposalId"), proposalId);

// Add the predicates to the criteria query
		cq.select(labContact)
				.where(cb.and(personIdPredicate, proposalIdPredicate))
				.distinct(true);  // Ensuring that the results are distinct

// Execute the query
		List<LabContact3VO> results = em.createQuery(cq).getResultList();
		return results;


	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LabContact3VO> findByCardName(final String cardNameWithNum) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

// Create the criteria query object specifying the result type
		CriteriaQuery<LabContact3VO> cq = cb.createQuery(LabContact3VO.class);
		Root<LabContact3VO> labContact = cq.from(LabContact3VO.class);

// Define the condition for the query
		Predicate cardNamePredicate = cb.equal(labContact.get("cardName"), cardNameWithNum);

// Add the condition to the criteria query
		cq.select(labContact)
				.where(cardNamePredicate);

// Execute the query and get the results
		List<LabContact3VO> results = em.createQuery(cq).getResultList();
		return results;

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
	private void checkAndCompleteData(LabContact3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getLabContactId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getLabContactId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
	public Integer hasShipping(final Integer labContactId) throws Exception {

		String sqlQuery = "SELECT COUNT(shipping) FROM Shipping3VO shipping WHERE shipping.sendingLabContactVO.labContactId = :labContactId OR shipping.returnLabContactVO.labContactId = :labContactId";
		Query query = entityManager.createQuery(sqlQuery)
				.setParameter("labContactId", labContactId);
		try{
			BigInteger res = (BigInteger) query.getSingleResult();
			return res.intValue();
		}catch(NoResultException e){
			System.out.println("ERROR in hasShipping - NoResultException: "+labContactId);
			e.printStackTrace();
			return 0;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LabContact3VO> findByProposalId(final Integer proposalId) throws Exception {

		// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
		// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
		// to the one checking the needed access rights
		// autService.checkUserRightToChangeAdminData();
		EntityManager em = this.entityManager;

		//TODO test proposalId
// Create a typed query using JPQL (Java Persistence Query Language)
		TypedQuery<LabContact3VO> query = em.createQuery(
						"SELECT DISTINCT lc FROM LabContact3VO lc WHERE lc.proposalVO.proposalId = :proposalId", LabContact3VO.class)
				.setParameter("proposalId", proposalId);

// Execute the query and collect the results
		List<LabContact3VO> results = query.getResultList();
		return results;


	}

}