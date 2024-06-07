/** This file is part of ISPyB.
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

package ispyb.ws.soap.mx;

import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ispyb.common.util.Constants;
import ispyb.common.util.StringUtils;
import ispyb.server.common.services.proposals.Laboratory3Service;
import ispyb.server.common.services.proposals.Person3Service;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.services.sessions.Session3Service;
import ispyb.server.common.services.shipping.Shipping3Service;
import ispyb.server.common.util.ejb.Ejb3ServiceLocator;
import ispyb.server.common.vos.proposals.Laboratory3VO;
import ispyb.server.common.vos.proposals.PersonWS3VO;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.proposals.ProposalWS3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.vos.collections.Session3VO;
import ispyb.server.mx.vos.collections.SessionWS3VO;

/**
 * Web services for Shipment / proposal
 * 
 * @author BODIN
 * 
 */
@WebService(name = "ToolsForShippingWebService", serviceName = "ispybWS", targetNamespace = "http://ispyb.ejb3.webservices.shipping")
@SOAPBinding(style = Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@Stateless(name="ToolsForShippingWebService")
@RolesAllowed({ "WebService", "User", "Industrial"}) // allow special access roles
//@SecurityDomain("ispyb")
//@WebContext(authMethod="BASIC",  secureWSDLAccess=false, transportGuarantee="NONE")
public class ToolsForShippingWebService {
	private final static Logger LOG = Logger.getLogger(ToolsForShippingWebService.class);


	@WebMethod(operationName = "echo")
	@WebResult(name = "echo")
	public String echo(){
		return "echo from server...";
	}
	
	@WebMethod
	@WebResult(name = "person")
	public PersonWS3VO findPersonBySessionId(@WebParam(name = "sessionId") Integer sessionId) throws Exception {
		try {
			LOG.debug("findPersonBySessionId");
			PersonWS3VO personValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Person3Service personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);

			personValue = personService.findForWSPersonBySessionId(sessionId);
			if (personValue != null)
				LOG.debug("findPersonBySessionId " + personValue.getFamilyName());
			return personValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findPersonBySessionId - " + StringUtils.getCurrentDate() + " - " + sessionId);
			throw e;
		}
	}

	// 03/07/12: returns the person corresponding to the beamlineOperator of the given session
	@WebMethod
	@WebResult(name = "person")
	public PersonWS3VO findPersonBySessionIdLocalContact(@WebParam(name = "sessionId") Integer sessionId)
			throws Exception {
		try {
			LOG.debug("findPersonBySessionIdLocalContact");
			PersonWS3VO personValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Person3Service personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);
			Session3Service sessionService = (Session3Service) ejb3ServiceLocator
					.getLocalService(Session3Service.class);
			Session3VO session = sessionService.findByPk(sessionId, false, false, false);
			String[] tab = session.getBeamlineOperatorLastNameAndFirstNameLetter();
			if (tab != null) {
				// Get first letter of firstName + *
				String lastName = tab[0];
				String firstNameLetter = tab[1];
				if (lastName == null && firstNameLetter == null)
					return null;
				personValue = personService.findForWSPersonByLastNameAndFirstNameLetter(lastName, firstNameLetter);
				LOG.debug("findPersonBySessionIdLocalContact " + lastName);
				return personValue;

			} else
				return null;
		} catch (Exception e) {
			LOG.error("WS ERROR: findPersonBySessionIdLocalContact - " + StringUtils.getCurrentDate() + " - "
					+ sessionId);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "proposal")
	public ProposalWS3VO findProposalByCodeAndNumber(String code, Integer number) throws Exception {
		try {
			LOG.debug("findProposalByCodeAndNumber");
			ProposalWS3VO proposalValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
					.getLocalService(Proposal3Service.class);

			proposalValue = proposalService.findForWSByCodeAndNumber(StringUtils.getProposalCode(code), number+"");
			if (proposalValue != null)
				LOG.debug("findProposalByCodeAndNumber " + proposalValue.getProposalId());
			return proposalValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findProposalByCodeAndNumber - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "proposal")
	public ProposalWS3VO findProposal(String code, String number) throws Exception {
		try {
			LOG.debug("findProposal");
			ProposalWS3VO proposalValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
					.getLocalService(Proposal3Service.class);

			proposalValue = proposalService.findForWSByCodeAndNumber(StringUtils.getProposalCode(code), number);
			if (proposalValue != null)
				LOG.debug("findProposal " + proposalValue.getProposalId());
			return proposalValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findProposal - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}


	@WebMethod
	@WebResult(name = "person")
	public PersonWS3VO findPersonByCodeAndNumber(String code, Integer number) throws Exception {
		try {
			LOG.debug("findPersonByCodeAndNumber");
			PersonWS3VO personValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Person3Service personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);

			personValue = personService.findForWSPersonByProposalCodeAndNumber(StringUtils.getProposalCode(code),
					number+"");
			if (personValue != null)
				LOG.debug("findPersonByCodeAndNumber " + personValue.getFamilyName());
			return personValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findPersonByCodeAndNumber - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "person")
	public PersonWS3VO findPersonByProposal(String code, String number) throws Exception {
		try {
			LOG.debug("findPersonByProposal");
			PersonWS3VO personValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Person3Service personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);

			personValue = personService.findForWSPersonByProposalCodeAndNumber(StringUtils.getProposalCode(code),
					number);
			if (personValue != null)
				LOG.debug("findPersonByProposal " + personValue.getFamilyName());
			return personValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findPersonByProposal - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "person")
	public PersonWS3VO findPersonByProteinAcronym(Integer proposalId, String acronym) throws Exception {
		try {
			LOG.debug("findPersonByProteinAcronym");
			PersonWS3VO personValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Person3Service personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);

			personValue = personService.findForWSPersonByProteinAcronym(proposalId,
					acronym);
			if (personValue != null)
				LOG.debug("findPersonByProteinAcronym " + personValue.getFamilyName());
			return personValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findPersonByProteinAcronym - " + StringUtils.getCurrentDate() + " - for proposalId:" + proposalId + ", acronym:"
					+ acronym);
			throw e;
		}
	}

	@WebMethod
	@WebResult(name = "laboratory")
	public Laboratory3VO findLaboratoryByCodeAndNumber(String code, Integer number) throws Exception {
		try {
			LOG.debug("findLaboratoryByCodeAndNumber " + code + number);
			Laboratory3VO laboratoryValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Laboratory3Service laboratoryService = (Laboratory3Service) ejb3ServiceLocator
					.getLocalService(Laboratory3Service.class);

			laboratoryValue = laboratoryService.findLaboratoryByProposalCodeAndNumber(
					StringUtils.getProposalCode(code), number+"");
			if (laboratoryValue != null)
				LOG.debug("findLaboratoryByCodeAndNumber " + laboratoryValue.getName());

			return laboratoryValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findLaboratoryByCodeAndNumber - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "laboratory")
	public Laboratory3VO findLaboratoryByProposal(String code, String number) throws Exception {
		try {
			LOG.debug("findLaboratoryByProposal " + code + number);
			Laboratory3VO laboratoryValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Laboratory3Service laboratoryService = (Laboratory3Service) ejb3ServiceLocator
					.getLocalService(Laboratory3Service.class);

			laboratoryValue = laboratoryService.findLaboratoryByProposalCodeAndNumber(
					StringUtils.getProposalCode(code), number);
			if (laboratoryValue != null)
				LOG.debug("findLaboratoryByProposal " + laboratoryValue.getName());

			return laboratoryValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findLaboratoryByProposal - " + StringUtils.getCurrentDate() + " - " + code + ", "
					+ number);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "person")
	public PersonWS3VO findPersonByLogin(String login) throws Exception {
		try {
			LOG.debug("findPersonByLogin");
			PersonWS3VO personValue = null;

			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Person3Service personService = (Person3Service) ejb3ServiceLocator.getLocalService(Person3Service.class);

			personValue  = personService.findForWSPersonByLogin(login);
			if (personValue != null)
				LOG.debug("findPersonByLogin " + personValue.getFamilyName());
			return personValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findPersonByLogin - " + StringUtils.getCurrentDate() + " - " + login);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "proposal")
	public ProposalWS3VO findProposalByLoginAndBeamline(String login, String beamline) throws Exception {
		try {
			LOG.debug("findProposalByLoginAndBeamline");
			ProposalWS3VO proposalValue = null;

			List<ProposalWS3VO> proposalValues = new ArrayList<ProposalWS3VO>();
			
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service proposalService = (Proposal3Service) ejb3ServiceLocator
					.getLocalService(Proposal3Service.class);
			
			List<String> groups = null;
			if(Constants.SITE_IS_DESY()) {
				List<Proposal3VO> props = proposalService.findByLoginName(login);
				if(props.size() >= 1){
					Proposal3VO prop = props.get(0);
					ProposalWS3VO wsProposal = new ProposalWS3VO(prop);
					wsProposal.setProteinVOs(null);
					wsProposal.setSessionVOs(null);
					wsProposal.setShippingVOs(null);
					wsProposal.setParticipants(null);
					wsProposal.setPersonVO(null);
					wsProposal.setPersonId(prop.getPersonVOId());
					//ProposalWS3VO wsProposal = proposalService.findForWSByCodeAndNumber("MX", prop.getNumber());
					
					proposalValues.add(wsProposal);
				}
			}
			
			if (proposalValues != null && !proposalValues.isEmpty()) {
				LOG.debug("findProposalByLoginAndBeamline: "+proposalValues.size() +" proposals found in the ISPyB database");
				LOG.debug("findProposalByLoginAndBeamline: check if there is a proposal with a current active session (only for accademic experiments)");
				Session3Service sessionService = (Session3Service) ejb3ServiceLocator.getLocalService(Session3Service.class);
				boolean stopSearch = Boolean.FALSE;
				for (ProposalWS3VO proposal : proposalValues) {
					//check if there is a proposal with a current session if not in house or industrial
					LOG.debug("findProposalByLoginAndBeamline: Check proposal with code:" +proposal.getProposalCode());
					if (!(proposal.getProposalCode().startsWith(Constants.PROPOSAL_CODE_MXIHR) || proposal.getProposalCode().startsWith(Constants.PROPOSAL_CODE_FX))){
						SessionWS3VO[] sessions = sessionService.findForWSByProposalCodeAndNumber(proposal.getProposalCode(), proposal.getProposalNumber(), beamline);
						if (!stopSearch) {
							if (sessions != null && sessions.length == 1) {
								// Only an active session
								stopSearch = true;
								proposalValue = proposal;
							} else if ((sessions != null && sessions.length == 0) || sessions == null) {
								//No active session
								proposalValue = proposal;
								LOG.debug("findProposalByLoginAndBeamline: The user " +login +" has no active session for the proposal " +proposal.getProposalCode() + "-" +proposal.getProposalNumber());
							}
						}
					} else {
						LOG.debug("findProposalByLoginAndBeamline: proposal with code " +proposal.getProposalCode() +" is a in house research / industrial");
						proposalValue = proposal;
					}
				}
				if (proposalValue != null){
					LOG.debug("findProposalByLoginAndBeamline: return the proposal for the current session... " + proposalValue.getProposalCode() + "-" +proposalValue.getProposalNumber());
				}
			} else {
				LOG.debug("findProposalByLoginAndBeamline: no proposals found");
			}
			
			return proposalValue;
		} catch (Exception e) {
			LOG.error("WS ERROR: findProposalByLoginAndBeamline - " + StringUtils.getCurrentDate() + " - Login:" + login + " - Beamline:" +beamline);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "findProposalsByLoginName")
	public String findProposalsByLoginName(
			@WebParam(name = "loginName") String loginName
			) throws Exception {

		/** Logging **/
		@SuppressWarnings("unused")
		long id = 0;
		try {
			/** Logging params **/
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userName", String.valueOf(loginName));
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Proposal3Service service = (Proposal3Service) ejb3ServiceLocator.getLocalService(Proposal3Service.class);
			List<Proposal3VO> proposals = service.findByLoginName(loginName);
			
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
			
			for (Proposal3VO proposal3vo : proposals) {
				HashMap<String, String> entry = new HashMap<String, String>();
				entry.put("title", proposal3vo.getTitle());
				entry.put("code", proposal3vo.getProposalCode());
				entry.put("number", proposal3vo.getProposalNumber());
				entry.put("type", proposal3vo.getType());
				entry.put("proposalId", proposal3vo.getProposalId().toString());
				entry.put("timeStamp", dateFormat.format(proposal3vo.getTimeStamp()).toString());
				result.add(entry);
			}
			return new Gson().toJson(result);
		} catch (Exception e) {
			LOG.error("WS ERROR: findProposalsByLoginName - " + StringUtils.getCurrentDate() + " - Login:" + loginName);
			throw e;
		}
	}
	
	@WebMethod
	@WebResult(name = "findShippingById")
	public String findShippingById(@WebParam(name = "shippingId") Integer shippingId, 
			@WebParam(name = "withDewars") String withDewars,
			@WebParam(name = "withContainers") String withContainers,
			@WebParam(name = "withSamples") String withSamples,
			@WebParam(name = "withSubSamples") String withSubSamples) throws Exception {
				
		try {
			Ejb3ServiceLocator ejb3ServiceLocator = Ejb3ServiceLocator.getInstance();
			Shipping3Service service = (Shipping3Service) ejb3ServiceLocator.getLocalService(Shipping3Service.class);
			
			String ret = "no shipping found";
			
			//Shipping3VO shipping = service.findByPk(shippingId, true, true, true, true);
			ret = service.findSerialShippingByPk(shippingId, new Boolean(withDewars), new Boolean(withContainers), new Boolean(withSamples), new Boolean(withSubSamples));
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("WS ERROR: findShippingById - " + StringUtils.getCurrentDate() + " - ship: " + shippingId);
			throw e;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static String serialize(Shipping3VO shipping) {
		Gson gson =  new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).serializeNulls().create();		
		return  gson.toJson(shipping);
	}

}
