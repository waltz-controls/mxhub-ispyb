package ispyb.server.common.services.admin;

import ispyb.common.util.StringUtils;
import ispyb.server.common.vos.admin.SchemaStatusVO;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.log4j.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(value= TransactionAttributeType.NEVER)
public class SchemaStatusServiceBean implements SchemaStatusService, SchemaStatusServiceLocal {

	private final static Logger LOG = Logger.getLogger(SchemaStatusServiceBean.class);

	@PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;
	
	public SchemaStatusServiceBean() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SchemaStatusVO> findFiltered(Integer schemaStatusId, String scriptName, String schemaStatus)
			throws Exception {
		EntityManager em = this.entityManager;
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<SchemaStatusVO> cq = cb.createQuery(SchemaStatusVO.class);
		Root<SchemaStatusVO> root = cq.from(SchemaStatusVO.class);

		List<Predicate> predicates = new ArrayList<>();

// Applying conditions based on the input
		if (!StringUtils.isEmpty(scriptName)) {
			predicates.add(cb.like(root.get("scriptName"), "%" + scriptName + "%"));
		}

		if (!StringUtils.isEmpty(schemaStatus)) {
			predicates.add(cb.like(root.get("schemaStatus"), "%" + schemaStatus + "%"));
		}

		if (schemaStatusId != null) {
			predicates.add(cb.equal(root.get("schemaStatusId"), schemaStatusId));
		}

		cq.where(cb.and(predicates.toArray(new Predicate[0])));

// Ensuring distinct results
		cq.distinct(true);

		List<SchemaStatusVO> results = em.createQuery(cq).getResultList();
		return results;

	};
	

	@Override
	public List<SchemaStatusVO> findAll() throws Exception {
			return this.findFiltered(null, null, null);
	}


}
