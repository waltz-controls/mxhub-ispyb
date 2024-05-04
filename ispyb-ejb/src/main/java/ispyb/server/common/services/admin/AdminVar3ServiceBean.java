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
package ispyb.server.common.services.admin;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.log4j.Logger;

import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.admin.AdminVar3VO;

/**
 * <p>
 * This session bean handles ISPyB AdminVar3.
 * </p>
 */
@Stateless
public class AdminVar3ServiceBean implements AdminVar3Service, AdminVar3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(AdminVar3ServiceBean.class);

	// Generic HQL request to find instances of AdminVar3 by pk
	private static final String FIND_BY_PK() {
		return "from AdminVar3VO vo where vo.adminVarId = :pk";
	}

	// Generic HQL request to find all instances of AdminVar3
	private static final String FIND_ALL() {
		return "from AdminVar3VO vo ";
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AdminVar3ServiceBean() {
	};

	/**
	 * Create new AdminVar3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public AdminVar3VO create(final AdminVar3VO vo) throws Exception {
		
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}
	
	/**
	 * Update the AdminVar3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public AdminVar3VO update(final AdminVar3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();	
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AdminVar3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		AdminVar3VO vo = findByPk(pk);
		delete(vo);	
	}

	/**
	 * Remove the AdminVar3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final AdminVar3VO vo) throws Exception {
		
		checkCreateChangeRemoveAccess();
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the AdminVar3 value object
	 */
	public AdminVar3VO findByPk(final Integer pk) throws Exception {
		
		checkCreateChangeRemoveAccess();
		try {
			return (AdminVar3VO) entityManager.createQuery(FIND_BY_PK()).setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * loads the vo with all the linked object in eager fetch mode
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public AdminVar3VO loadEager(AdminVar3VO vo) throws Exception {
		
		AdminVar3VO newVO = this.findByPk(vo.getAdminVarId());
		return newVO;
	}

	/**
	 * Find all AdminVar3s and set linked value objects if necessary
	 * 
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<AdminVar3VO> findAll() throws Exception {
		
		List<AdminVar3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	@SuppressWarnings("unchecked")
	public List<AdminVar3VO> findByName(final String name) throws Exception {

		// Obtain the CriteriaBuilder from the EntityManager
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		// Create a CriteriaQuery object for AdminVar3VO
		CriteriaQuery<AdminVar3VO> query = cb.createQuery(AdminVar3VO.class);

		// Define the root of the query (i.e., AdminVar3VO)
		Root<AdminVar3VO> root = query.from(AdminVar3VO.class);

		// Create a CriteriaQuery object
		query.select(root).distinct(true); // Enable distinct results

		if (name != null && !name.isEmpty()) {
			// Convert the name to lower case and create a 'like' predicate
			String n = name.toLowerCase();
			Predicate nameLike = cb.like(cb.lower(root.get("name")), "%" + n + "%");
			query.where(nameLike); // Add the where clause to the query
		}

		// Execute the query and return the result list
		return entityManager.createQuery(query).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AdminVar3VO> findByAction(final String statusLogon) throws Exception {

		// Obtain the CriteriaBuilder from the EntityManager
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();

		// Create a CriteriaQuery object for AdminVar3VO
		CriteriaQuery<AdminVar3VO> query = cb.createQuery(AdminVar3VO.class);

		// Define the root of the query (i.e., AdminVar3VO)
		Root<AdminVar3VO> root = query.from(AdminVar3VO.class);

		// Create a CriteriaQuery object
		query.select(root).distinct(true); // Enable distinct results

		if (statusLogon != null && !statusLogon.isEmpty()) {
			// Convert the statusLogon to lower case and create a 'like' predicate
			String v = statusLogon.toLowerCase();
			Predicate statusLogonLike = cb.like(cb.lower(root.get("value")), "%" + v + "%");
			query.where(statusLogonLike); // Add the where clause to the query
		}

		// Execute the query and return the result list
		return entityManager.createQuery(query).getResultList();
	}

	/**
	 * Check if user has access rights to create, change and remove AdminVar3 entities. If not set rollback only and
	 * throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		
				// AuthorizationServiceLocal autService = (AuthorizationServiceLocal)
				// ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class); // TODO change method
				// to the one checking the needed access rights
				// autService.checkUserRightToChangeAdminData();
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
	private void checkAndCompleteData(AdminVar3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAdminVarId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAdminVarId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
	
}