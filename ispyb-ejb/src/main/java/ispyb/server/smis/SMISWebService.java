//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ispyb.server.smis;

import java.util.Calendar;
import java.util.List;

import generated.ws.smis.*;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;

import javax.naming.spi.ObjectFactory;

@WebService(
        name = "SMISWebService",
        targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/"
)
@XmlSeeAlso({ObjectFactory.class})
public interface SMISWebService {
    @WebMethod
    @WebResult(
            name = "ProposalPk",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "getProposalPK",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.GetProposalPK"
    )
    @ResponseWrapper(
            localName = "getProposalPKResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.GetProposalPKResponse"
    )
    long getProposalPK(@WebParam(name = "arg0",targetNamespace = "") String var1, @WebParam(name = "arg1",targetNamespace = "") Long var2) throws FinderException_Exception;

    @WebMethod
    @WebResult(
            name = "ExpSessionInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findRecentSessionsInfoLightForProposalPkAndDays",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindRecentSessionsInfoLightForProposalPkAndDays"
    )
    @ResponseWrapper(
            localName = "findRecentSessionsInfoLightForProposalPkAndDaysResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindRecentSessionsInfoLightForProposalPkAndDaysResponse"
    )
    List<ExpSessionInfoLightVO> findRecentSessionsInfoLightForProposalPkAndDays(@WebParam(name = "arg0",targetNamespace = "") Long var1, @WebParam(name = "arg1",targetNamespace = "") Integer var2) throws FinderException_Exception;

    @WebMethod
    @WebResult(
            name = "ProposalPk",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findNewMXProposalPKs",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindNewMXProposalPKs"
    )
    @ResponseWrapper(
            localName = "findNewMXProposalPKsResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindNewMXProposalPKsResponse"
    )
    List<Long> findNewMXProposalPKs(@WebParam(name = "arg0",targetNamespace = "") String var1, @WebParam(name = "arg1",targetNamespace = "") String var2) throws FinderException_Exception;

    @WebMethod
    @WebResult(
            name = "SampleSheetInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findSamplesheetInfoLightForProposalPk",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSamplesheetInfoLightForProposalPk"
    )
    @ResponseWrapper(
            localName = "findSamplesheetInfoLightForProposalPkResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSamplesheetInfoLightForProposalPkResponse"
    )
    List<SampleSheetInfoLightVO> findSamplesheetInfoLightForProposalPk(@WebParam(name = "arg0",targetNamespace = "") Long var1, @WebParam(name = "arg1",targetNamespace = "") boolean var2) throws Exception_Exception;

    @WebMethod
    @WebResult(
            name = "SampleSheetInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findSamplesheetInfoLightForSessionPk",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSamplesheetInfoLightForSessionPk"
    )
    @ResponseWrapper(
            localName = "findSamplesheetInfoLightForSessionPkResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSamplesheetInfoLightForSessionPkResponse"
    )
    List<SampleSheetInfoLightVO> findSamplesheetInfoLightForSessionPk(@WebParam(name = "arg0",targetNamespace = "") Long var1) throws Exception_Exception;

    @WebMethod
    @RequestWrapper(
            localName = "addDoiToSessionByProposalBeamlineAndDate",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.AddDoiToSessionByProposalBeamlineAndDate"
    )
    @ResponseWrapper(
            localName = "addDoiToSessionByProposalBeamlineAndDateResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.AddDoiToSessionByProposalBeamlineAndDateResponse"
    )
    void addDoiToSessionByProposalBeamlineAndDate(@WebParam(name = "arg0",targetNamespace = "") String var1, @WebParam(name = "arg1",targetNamespace = "") Integer var2, @WebParam(name = "arg2",targetNamespace = "") String var3, @WebParam(name = "arg3",targetNamespace = "") Calendar var4, @WebParam(name = "arg4",targetNamespace = "") String var5) throws Exception_Exception;

    @WebMethod
    @WebResult(
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findUserByScientistPk",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindUserByScientistPk"
    )
    @ResponseWrapper(
            localName = "findUserByScientistPkResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindUserByScientistPkResponse"
    )
    UserLightVO findUserByScientistPk(@WebParam(name = "arg0",targetNamespace = "") Long var1) throws Exception_Exception;

    @WebMethod
    @WebResult(
            name = "UserDataAccessLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "getAclsWithAllDelays",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.GetAclsWithAllDelays"
    )
    @ResponseWrapper(
            localName = "getAclsWithAllDelaysResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.GetAclsWithAllDelaysResponse"
    )
    List<UserDataAccessLightVO> getAclsWithAllDelays(@WebParam(name = "arg0",targetNamespace = "") double var1, @WebParam(name = "arg1",targetNamespace = "") double var3, @WebParam(name = "arg2",targetNamespace = "") double var5, @WebParam(name = "arg3",targetNamespace = "") double var7);

    @WebMethod
    @WebResult(
            name = "ExpSessionInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findRecentSessionsInfoLightForProposalPk",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindRecentSessionsInfoLightForProposalPk"
    )
    @ResponseWrapper(
            localName = "findRecentSessionsInfoLightForProposalPkResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindRecentSessionsInfoLightForProposalPkResponse"
    )
    List<ExpSessionInfoLightVO> findRecentSessionsInfoLightForProposalPk(@WebParam(name = "arg0",targetNamespace = "") Long var1) throws FinderException_Exception;

    @WebMethod
    @WebResult(
            name = "ExpSessionInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findSessionByProposalBeamlineAndDate",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSessionByProposalBeamlineAndDate"
    )
    @ResponseWrapper(
            localName = "findSessionByProposalBeamlineAndDateResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSessionByProposalBeamlineAndDateResponse"
    )
    ExpSessionInfoLightVO findSessionByProposalBeamlineAndDate(@WebParam(name = "arg0",targetNamespace = "") String var1, @WebParam(name = "arg1",targetNamespace = "") Integer var2, @WebParam(name = "arg2",targetNamespace = "") String var3, @WebParam(name = "arg3",targetNamespace = "") Calendar var4) throws FinderException_Exception;

    @WebMethod
    @WebResult(
            name = "UserDataAccessLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "getAclsWithEndDelay",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.GetAclsWithEndDelay"
    )
    @ResponseWrapper(
            localName = "getAclsWithEndDelayResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.GetAclsWithEndDelayResponse"
    )
    List<UserDataAccessLightVO> getAclsWithEndDelay(@WebParam(name = "arg0",targetNamespace = "") double var1);

    @WebMethod
    @WebResult(
            name = "ExpSessionInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findSessionsByBeamlineAndDates",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSessionsByBeamlineAndDates"
    )
    @ResponseWrapper(
            localName = "findSessionsByBeamlineAndDatesResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSessionsByBeamlineAndDatesResponse"
    )
    List<ExpSessionInfoLightVO> findSessionsByBeamlineAndDates(@WebParam(name = "arg0",targetNamespace = "") String var1, @WebParam(name = "arg1",targetNamespace = "") Calendar var2, @WebParam(name = "arg2",targetNamespace = "") Calendar var3) throws FinderException_Exception;

    @WebMethod
    @WebResult(
            name = "ExpSessionInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findSessionsInfoLightInRange",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSessionsInfoLightInRange"
    )
    @ResponseWrapper(
            localName = "findSessionsInfoLightInRangeResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSessionsInfoLightInRangeResponse"
    )
    List<ExpSessionInfoLightVO> findSessionsInfoLightInRange(@WebParam(name = "arg0",targetNamespace = "") Calendar var1, @WebParam(name = "arg1",targetNamespace = "") Calendar var2) throws FinderException_Exception;

    @WebMethod
    @RequestWrapper(
            localName = "addDoiToSessionBySessionPk",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.AddDoiToSessionBySessionPk"
    )
    @ResponseWrapper(
            localName = "addDoiToSessionBySessionPkResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.AddDoiToSessionBySessionPkResponse"
    )
    void addDoiToSessionBySessionPk(@WebParam(name = "arg0",targetNamespace = "") Long var1, @WebParam(name = "arg1",targetNamespace = "") String var2) throws Exception_Exception;

    @WebMethod
    @WebResult(
            name = "ProposalParticipantInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findMainProposersForProposal",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindMainProposersForProposal"
    )
    @ResponseWrapper(
            localName = "findMainProposersForProposalResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindMainProposersForProposalResponse"
    )
    List<ProposalParticipantInfoLightVO> findMainProposersForProposal(@WebParam(name = "arg0",targetNamespace = "") Long var1) throws Exception_Exception;

    @WebMethod
    @WebResult(
            name = "BeamlineDataAccessScientists",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "getScientistsWithDataAccessForAllBeamlines",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.GetScientistsWithDataAccessForAllBeamlines"
    )
    @ResponseWrapper(
            localName = "getScientistsWithDataAccessForAllBeamlinesResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.GetScientistsWithDataAccessForAllBeamlinesResponse"
    )
    List<BeamlineScientistsVO> getScientistsWithDataAccessForAllBeamlines();

    @WebMethod
    @WebResult(
            name = "ProposalParticipantInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findLatestProposalParticipantByScientistPk",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindLatestProposalParticipantByScientistPk"
    )
    @ResponseWrapper(
            localName = "findLatestProposalParticipantByScientistPkResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindLatestProposalParticipantByScientistPkResponse"
    )
    ProposalParticipantInfoLightVO findLatestProposalParticipantByScientistPk(@WebParam(name = "arg0",targetNamespace = "") Long var1) throws Exception_Exception;

    @WebMethod
    @WebResult(
            name = "ProposalParticipantInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findUsersByExpSession",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindUsersByExpSession"
    )
    @ResponseWrapper(
            localName = "findUsersByExpSessionResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindUsersByExpSessionResponse"
    )
    List<ProposalParticipantInfoLightVO> findUsersByExpSession(@WebParam(name = "arg0",targetNamespace = "") Long var1) throws Exception_Exception;

    @WebMethod
    @WebResult(
            name = "ProposalParticipantInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findScientistsForProposalByNameAndFirstName",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindScientistsForProposalByNameAndFirstName"
    )
    @ResponseWrapper(
            localName = "findScientistsForProposalByNameAndFirstNameResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindScientistsForProposalByNameAndFirstNameResponse"
    )
    List<ProposalParticipantInfoLightVO> findScientistsForProposalByNameAndFirstName(@WebParam(name = "arg0",targetNamespace = "") Long var1, @WebParam(name = "arg1",targetNamespace = "") String var2, @WebParam(name = "arg2",targetNamespace = "") String var3) throws Exception_Exception;

    @WebMethod
    @WebResult(
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findProposalInfoLightByProposalPk",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindProposalInfoLightByProposalPk"
    )
    @ResponseWrapper(
            localName = "findProposalInfoLightByProposalPkResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindProposalInfoLightByProposalPkResponse"
    )
    ProposalInfoLightVO findProposalInfoLightByProposalPk(@WebParam(name = "arg0",targetNamespace = "") Long var1) throws Exception_Exception;

    @WebMethod
    @WebResult(
            name = "UserDataAccessLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "getAcls",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.GetAcls"
    )
    @ResponseWrapper(
            localName = "getAclsResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.GetAclsResponse"
    )
    List<UserDataAccessLightVO> getAcls();

    @WebMethod
    @WebResult(
            name = "ProposalParticipantInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findScientistsByNameAndFirstName",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindScientistsByNameAndFirstName"
    )
    @ResponseWrapper(
            localName = "findScientistsByNameAndFirstNameResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindScientistsByNameAndFirstNameResponse"
    )
    List<ProposalParticipantInfoLightVO> findScientistsByNameAndFirstName(@WebParam(name = "arg0",targetNamespace = "") String var1, @WebParam(name = "arg1",targetNamespace = "") String var2, @WebParam(name = "arg2",targetNamespace = "") int var3) throws Exception_Exception;

    @WebMethod
    @WebResult(
            name = "ProposalCategoryAndCode",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findProposalsFromBeamlineLogin",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindProposalsFromBeamlineLogin"
    )
    @ResponseWrapper(
            localName = "findProposalsFromBeamlineLoginResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindProposalsFromBeamlineLoginResponse"
    )
    List<String> findProposalsFromBeamlineLogin(@WebParam(name = "arg0",targetNamespace = "") String var1) throws Exception_Exception;

    @WebMethod
    @WebResult(
            name = "ProposalParticipantInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findParticipantsForProposal",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindParticipantsForProposal"
    )
    @ResponseWrapper(
            localName = "findParticipantsForProposalResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindParticipantsForProposalResponse"
    )
    List<ProposalParticipantInfoLightVO> findParticipantsForProposal(@WebParam(name = "arg0",targetNamespace = "") Long var1) throws Exception_Exception;

    @WebMethod
    @WebResult(
            name = "TrainingValid",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "isTrainingValidForDate",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.IsTrainingValidForDate"
    )
    @ResponseWrapper(
            localName = "isTrainingValidForDateResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.IsTrainingValidForDateResponse"
    )
    boolean isTrainingValidForDate(@WebParam(name = "arg0",targetNamespace = "") String var1, @WebParam(name = "arg1",targetNamespace = "") Training var2, @WebParam(name = "arg2",targetNamespace = "") Calendar var3) throws Exception_Exception;

    @WebMethod
    @WebResult(
            name = "ExpSessionInfoLight",
            targetNamespace = ""
    )
    @RequestWrapper(
            localName = "findSessionsInfoLightForProposalPk",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSessionsInfoLightForProposalPk"
    )
    @ResponseWrapper(
            localName = "findSessionsInfoLightForProposalPkResponse",
            targetNamespace = "http://webservice.service.ejb3.smis.esrf.fr/",
            className = "generated.ws.smis.FindSessionsInfoLightForProposalPkResponse"
    )
    List<ExpSessionInfoLightVO> findSessionsInfoLightForProposalPk(@WebParam(name = "arg0",targetNamespace = "") Long var1) throws FinderException_Exception;
}
