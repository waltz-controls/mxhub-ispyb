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

package ispyb.server.common.services.login;


import ispyb.server.common.vos.login.Login3VO;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


@Stateless
public class Login3ServiceBean implements Login3Service, Login3ServiceLocal {

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	@Override
	public void persist(Login3VO transientInstance) {
		try {
			entityManager.persist(transientInstance);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@Override
	public Login3VO findByToken(String token) {
		// Obtain the entity manager and criteria builder
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

// Create a criteria query for the Login3VO class
		CriteriaQuery<Login3VO> criteriaQuery = criteriaBuilder.createQuery(Login3VO.class);

// Define the root of the query (from where we start)
		Root<Login3VO> root = criteriaQuery.from(Login3VO.class);

// Specify the selection (typically, you might retrieve more complex structures)
		criteriaQuery.select(root);

// Add a where clause to the query
		criteriaQuery.where(criteriaBuilder.equal(root.get("token"), token));

// Execute the query
		List<Login3VO> loginList = entityManager.createQuery(criteriaQuery).getResultList();

		if (loginList.size() == 1){
			return loginList.get(0);
		}
		/** If loginList size is different to 1 means that either there is no token on the database or that there are several and in both cases it is not valid **/
		return null;
	}
	
	@Override
	public Login3VO findBylastByLogin(String login) {
		// Obtain the entity manager and criteria builder
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

// Create a criteria query for the Login3VO class
		CriteriaQuery<Login3VO> criteriaQuery = criteriaBuilder.createQuery(Login3VO.class);

// Define the root of the query (from where we start)
		Root<Login3VO> root = criteriaQuery.from(Login3VO.class);

// Create the conditions (where clauses)
		Predicate loginCondition = criteriaBuilder.equal(root.get("login"), login);

// Add the conditions to the query
		criteriaQuery.select(root).where(loginCondition);

// Add an order by clause
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("loginId")));

// Execute the query
		List<Login3VO> loginList = entityManager.createQuery(criteriaQuery).getResultList();
		if (loginList != null && loginList.size() > 0){
			return loginList.get(0);
		}
		/** If loginList size is different to 1 means that either there is no token on the database or that there are several and in both cases it is not valid **/
		return null;
	}

}