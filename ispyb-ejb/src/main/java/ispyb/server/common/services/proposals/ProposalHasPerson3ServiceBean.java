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

package ispyb.server.common.services.proposals;

import java.util.Iterator;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.apache.log4j.Logger;

import ispyb.server.common.exceptions.AccessDeniedException;
import ispyb.server.common.services.AuthorisationServiceLocal;
import ispyb.server.common.vos.proposals.ProposalHasPerson3VO;

/**
 * <p>
 * This session bean handles Proposal table.
 * </p>
 * 
 */
@Stateless
public class ProposalHasPerson3ServiceBean implements ProposalHasPerson3Service, ProposalHasPerson3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	private final static Logger LOG = Logger.getLogger(ProposalHasPerson3ServiceBean.class);
	
	@EJB
	private AuthorisationServiceLocal autService;

	@Resource
	private SessionContext context;

	public ProposalHasPerson3ServiceBean() {
	};

	/**
	 * Create new entity.
	 * 
	 * @param vo
	 *            the entity to persist.
	 * @return the persisted entity.
	 */
	public ProposalHasPerson3VO create(final ProposalHasPerson3VO vo) throws Exception {
			entityManager.persist(vo);
			return vo;
	}
	
	public ProposalHasPerson3VO create(Integer proposalId, Integer personId) throws Exception {
		
		ProposalHasPerson3VO vo = new ProposalHasPerson3VO();
		vo.setProposalId(proposalId);
		vo.setPersonId(personId);
		entityManager.persist(vo);
		return vo;
}


	/**
	 * Update the entity data.
	 * 
	 * @param vo
	 *            the entity data to update.
	 * @return the updated entity.
	 */
	public ProposalHasPerson3VO update(final ProposalHasPerson3VO vo) throws Exception {
		checkChangeRemoveAccess(vo);
		entityManager.merge(vo);
		return vo;

	}

	/**
	 * Remove the entity from its pk
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void deleteByPk(final Integer pk) throws Exception {
		ProposalHasPerson3VO vo = this.findByPk(pk);
		checkChangeRemoveAccess(vo);
		entityManager.remove(vo);
	}

	/**
	 * Remove the entity
	 * 
	 * @param vo
	 *            the entity to remove.
	 */
	public void delete(final ProposalHasPerson3VO vo) throws Exception {
		checkChangeRemoveAccess(vo);
		entityManager.remove(vo);
	}
	
	@Override
	public void removeByProposalPk(Integer proposalPk) throws Exception {
		List<ProposalHasPerson3VO> toBeRemoved = this.findByProposalPk(proposalPk);
		
		for (Iterator<ProposalHasPerson3VO> iterator = toBeRemoved.iterator(); iterator.hasNext();) {
			ProposalHasPerson3VO proposalHasPerson3VO = (ProposalHasPerson3VO) iterator.next();
			delete(proposalHasPerson3VO);
		}
	}

	/**
	 * Finds an entity by its primary key and set linked value objects if necessary
	 * 
	 * @param pk
	 *            the primary key
	 * @param withLink1
	 * @param withLink2
	 * @return the Scientist value object
	 */
	@WebMethod
	public ProposalHasPerson3VO findByPk(final Integer pk) throws Exception {

		Query query = entityManager.createQuery("select vo from ProposalHasPerson3VO vo  where vo.proposalHasPersonId = :pk", ProposalHasPerson3VO.class)
				.setParameter("pk", pk);
		try {
			var result = query.getSingleResult();
			checkChangeRemoveAccess( (ProposalHasPerson3VO) result);
			return (ProposalHasPerson3VO) result;
		} catch (NoResultException e) {
			return null;
		}


	}
		
	@SuppressWarnings("unchecked")
	public List<ProposalHasPerson3VO> findByProposalPk(Integer proposalId) throws Exception {

		Query query = entityManager.createQuery("select vo from ProposalHasPerson3VO vo  where vo.proposalId = :pk", ProposalHasPerson3VO.class)
				.setParameter("pk", proposalId);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ProposalHasPerson3VO> findByPersonPk(Integer personId)throws Exception {

		Query query = entityManager.createQuery("select vo from ProposalHasPerson3VO vo  where vo.personId = :pk")
				.setParameter("pk", personId);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ProposalHasPerson3VO> findByProposalAndPersonPk(Integer proposalId, Integer personId) throws Exception {

		Query query = entityManager.createQuery("select vo from ProposalHasPerson3VO vo  where vo.personId = :personId and vo.proposalId = :proposalId")
				.setParameter("proposalId", proposalId)
				.setParameter("personId", personId);
		return query.getResultList();
	}


	/**
	 * Check if user has access rights to change and remove ProposalHasPerson3 entities. If not set throw
	 * AccessDeniedException
	 * 
	 * @throws AccessDeniedException
	 */
	private void checkChangeRemoveAccess(ProposalHasPerson3VO vo) throws AccessDeniedException {
		if (vo == null) return;
		//autService.checkUserRightToAccessProposal(vo);				
	}


}
