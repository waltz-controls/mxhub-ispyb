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

import java.util.ArrayList;
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

import ispyb.common.util.StringUtils;
import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.vos.proposals.Laboratory3VO;

/**
 * <p>
 * This session bean handles ISPyB Laboratory3.
 * </p>
 */
@Stateless
public class Laboratory3ServiceBean implements Laboratory3Service, Laboratory3ServiceLocal {

	private final static Logger LOG = Logger.getLogger(Laboratory3ServiceBean.class);

	// Generic HQL request to find instances of Laboratory3 by pk

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	public Laboratory3ServiceBean() {
	};

	public Laboratory3VO merge(Laboratory3VO detachedInstance) {
		try {
			Laboratory3VO result = entityManager.merge(detachedInstance);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/**
	 * Create new Laboratory3.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public Laboratory3VO create(final Laboratory3VO vo) throws Exception {
		return this.merge(vo);
	}

	/**
	 * Update the Laboratory3 data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public Laboratory3VO update(final Laboratory3VO vo) throws Exception {
		return this.merge(vo);
	}

	/**
	 * Remove the Laboratory3 from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		Laboratory3VO vo = findByPk(pk);
		checkCreateChangeRemoveAccess();
		delete(vo);
	}

	/**
	 * Remove the Laboratory3
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final Laboratory3VO vo) throws Exception {
		entityManager.remove(vo);
	}

	/**
	 * Finds a labo entity by its primary key 
	 * 
	 * @param pk
	 *            the primary key
	 * @return the Laboratory3 value object
	 */
	public Laboratory3VO findByPk(final Integer pk) throws Exception {
		try {
			return (Laboratory3VO) entityManager.createQuery("SELECT Laboratory3VO FROM Laboratory3VO vo  where vo.laboratoryId = :pk")
					.setParameter("pk", pk).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Finds a labo entity by its extLabPk
	 * @param laboExtPk  the ext labo key
	 * @return the Laboratory3 value object
	 */
	@SuppressWarnings("unchecked")
	public Laboratory3VO findByLaboratoryExtPk(final Integer laboExtPk) {
		String query = "SELECT Laboratory3VO FROM Laboratory3VO vo  where vo.laboratoryExtPk = :labExtPk order by vo.laboratoryId desc";
		List<Laboratory3VO> listVOs =  this.entityManager.createQuery(query, Laboratory3VO.class)
				.setParameter("labExtPk", laboExtPk)
				.getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
			
		return (Laboratory3VO) listVOs.toArray()[0];
		
	}

	//TODO test
	@SuppressWarnings("unchecked")
	public Laboratory3VO findLaboratoryByProposalCodeAndNumber(String code, String number) {
		
		String query = "SELECT l.laboratoryId, l.laboratoryUUID, l.name, l.address, "
				+ "l.city, l.country, l.url, l.organization, l.laboratoryExtPk  "
				+ " FROM Laboratory l, Person p, Proposal pro "
				+ "WHERE l.laboratoryId = p.laboratoryId AND p.personId = pro.personId AND pro.proposalCode like :code AND pro.proposalNumber = :number "
				.replace(":code", "'" + code + "'")
				.replace(":number", "'" + number + "'");
		List<Laboratory3VO> listVOs = this.entityManager.createNativeQuery(query, Laboratory3VO.class).getResultList();
		if (listVOs == null || listVOs.isEmpty())
			return null;
		
		return (Laboratory3VO) listVOs.toArray()[0];
	}

	@SuppressWarnings("unchecked")
	public List<Laboratory3VO> findFiltered(String laboratoryName, String city, String country) {

		// Get the CriteriaBuilder from the EntityManager
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

// Create a CriteriaQuery object for Laboratory3VO
		CriteriaQuery<Laboratory3VO> criteriaQuery = criteriaBuilder.createQuery(Laboratory3VO.class);

// Define the root of the query (the main entity to query from)
		Root<Laboratory3VO> root = criteriaQuery.from(Laboratory3VO.class);

// List to hold Predicate objects for query conditions
		List<Predicate> predicates = new ArrayList<>();

// Add conditions based on method parameters
		if (!StringUtils.isEmpty(laboratoryName)) {
			predicates.add(criteriaBuilder.like(root.get("name"), "%" + laboratoryName + "%"));
		}
		if (!StringUtils.isEmpty(city)) {
			predicates.add(criteriaBuilder.like(root.get("city"), "%" + city + "%"));
		}
		if (!StringUtils.isEmpty(country)) {
			predicates.add(criteriaBuilder.like(root.get("country"), "%" + country + "%"));
		}

// Apply the predicates to the CriteriaQuery
		criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

// Make sure results are distinct
		criteriaQuery.distinct(true);

// Prepare the query to be executed
		CriteriaQuery<Laboratory3VO> select = criteriaQuery.select(root);

// Execute the query
		List<Laboratory3VO> result = entityManager.createQuery(select).getResultList();

		return result;

	}

	public List<Laboratory3VO> findByNameAndCityAndCountry(final String laboratoryName, final String city,
			final String country) throws Exception {
		
		return this.findFiltered(laboratoryName, city, country);
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Laboratory3VO getLightLaboratoryVO(Laboratory3VO vo) throws CloneNotSupportedException {
		if (vo == null) return null;
		Laboratory3VO otherVO = (Laboratory3VO) vo.clone();
		return otherVO;
	}

	/**
	 * Get all lights entities
	 * 
	 * @param localEntities
	 * @return
	 * @throws CloneNotSupportedException
	 */
	private Laboratory3VO getLaboratoryVO(Laboratory3VO vo) {
		return vo;
	}

	/**
	 * Check if user has access rights to create, change and remove Laboratory3 entities. If not set rollback only and
	 * throw AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkCreateChangeRemoveAccess() throws Exception {
		//TODO
		return;
	}


}