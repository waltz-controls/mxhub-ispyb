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
package ispyb.server.mx.services.autoproc;

import ispyb.server.common.util.ejb.EJBAccessCallback;
import ispyb.server.common.util.ejb.EJBAccessTemplate;

import ispyb.server.mx.vos.autoproc.AutoProcIntegration3VO;
import ispyb.server.mx.vos.autoproc.AutoProcScalingHasInt3VO;

import java.util.List;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.*;
import org.apache.log4j.Logger;

/**
 * <p>
 *  This session bean handles ISPyB AutoProcScalingHasInt3.
 * </p>
 */
@Stateless
public class AutoProcScalingHasInt3ServiceBean implements AutoProcScalingHasInt3Service,
		AutoProcScalingHasInt3ServiceLocal {

	private final static Logger LOG = Logger
			.getLogger(AutoProcScalingHasInt3ServiceBean.class);
	
	// Generic HQL request to find instances of AutoProcScalingHasInt3 by pk
	// TODO choose between left/inner join
	private static final String FIND_BY_PK() {
		return "from AutoProcScalingHasInt3VO vo "  + "where vo.autoProcScalingHasIntId = :pk";
	}

	// Generic HQL request to find all instances of AutoProcScalingHasInt3
	// TODO choose between left/inner join
	private static final String FIND_ALL() {
		return "from AutoProcScalingHasInt3VO vo " ;
	}

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public AutoProcScalingHasInt3ServiceBean() {
	};

	/**
	 * Create new AutoProcScalingHasInt3.
	 * @param vo the entity to persist.
	 * @return the persisted entity.
	 */
	public AutoProcScalingHasInt3VO create(final AutoProcScalingHasInt3VO vo) throws Exception {
	
		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, true);
		this.entityManager.persist(vo);
		return vo;
	}
	
	/**
	 * Update the AutoProcScalingHasInt3 data.
	 * @param vo the entity data to update.
	 * @return the updated entity.
	 */
	public AutoProcScalingHasInt3VO update(final AutoProcScalingHasInt3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		this.checkAndCompleteData(vo, false);
		return entityManager.merge(vo);
	}

	/**
	 * Remove the AutoProcScalingHasInt3 from its pk
	 * @param vo the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		AutoProcScalingHasInt3VO vo = findByPk(pk);
		// TODO Edit this business code				
		delete(vo);
	}

	/**
	 * Remove the AutoProcScalingHasInt3
	 * @param vo the entity to remove.
	 */
	public void delete(final AutoProcScalingHasInt3VO vo) throws Exception {

		checkCreateChangeRemoveAccess();
		// TODO Edit this business code
		entityManager.remove(vo);
	}

	/**
	 * Finds a Scientist entity by its primary key and set linked value objects if necessary
	 * @param pk the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the AutoProcScalingHasInt3 value object
	 */
	public AutoProcScalingHasInt3VO findByPk(final Integer pk) throws Exception {

		checkCreateChangeRemoveAccess();
		try{
			return (AutoProcScalingHasInt3VO) entityManager.createQuery(FIND_BY_PK())
					.setParameter("pk", pk).getSingleResult();
			}catch(NoResultException e){
				return null;
			}
	}

	// TODO remove following method if not adequate
	/**
	 * Find all AutoProcScalingHasInt3s and set linked value objects if necessary
	 * @param withLink1
	 * @param withLink2
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcScalingHasInt3VO> findAll()
			throws Exception {

		List<AutoProcScalingHasInt3VO> foundEntities = entityManager.createQuery(FIND_ALL()).getResultList();
		return foundEntities;
	}

	/**
	 * Check if user has access rights to create, change and remove AutoProcScalingHasInt3 entities. If not set rollback only and throw AccessDeniedException
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		
				//AuthorizationServiceLocal autService = (AuthorizationServiceLocal) ServiceLocator.getInstance().getService(AuthorizationServiceLocalHome.class);			// TODO change method to the one checking the needed access rights
				//autService.checkUserRightToChangeAdminData();
	}
	
	/**
	 * returns the list of AutoProcScaling for a given autoProcIntegrationId
	 * @param autoProcIntegrationId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AutoProcScalingHasInt3VO> findFiltered(final Integer autoProcIntegrationId) throws Exception{
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

		CriteriaQuery<AutoProcScalingHasInt3VO> cq = cb.createQuery(AutoProcScalingHasInt3VO.class);
		Root<AutoProcScalingHasInt3VO> root = cq.from(AutoProcScalingHasInt3VO.class);

// Joining with AutoProcIntegrationVO
		Join<AutoProcScalingHasInt3VO, AutoProcIntegration3VO> autoProcIntegrationJoin = root.join("autoProcIntegrationVO", JoinType.INNER);

// Applying conditions
		if (autoProcIntegrationId != null) {
			Predicate condition = cb.equal(autoProcIntegrationJoin.get("autoProcIntegrationId"), autoProcIntegrationId);
			cq.where(condition);
		}

// Ordering
		cq.orderBy(cb.asc(root.get("autoProcScalingHasIntId")));

// Ensuring distinct results
		cq.distinct(true);

		List<AutoProcScalingHasInt3VO> foundEntities = this.entityManager.createQuery(cq).getResultList();
		return foundEntities;
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
	private void checkAndCompleteData(AutoProcScalingHasInt3VO vo, boolean create) throws Exception {

		if (create) {
			if (vo.getAutoProcScalingHasIntId() != null) {
				throw new IllegalArgumentException(
						"Primary key is already set! This must be done automatically. Please, set it to null!");
			}
		} else {
			if (vo.getAutoProcScalingHasIntId() == null) {
				throw new IllegalArgumentException("Primary key is not set for update!");
			}
		}
		// check value object
		vo.checkValues(create);
		// TODO check primary keys for existence in DB
	}
}
