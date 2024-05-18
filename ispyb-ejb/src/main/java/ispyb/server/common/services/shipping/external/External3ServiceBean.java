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
package ispyb.server.common.services.shipping.external;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ispyb.server.common.vos.proposals.Laboratory3VO;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;
import org.apache.log4j.Logger;

import ispyb.common.util.upload.UploadShipmentUtils;
import ispyb.server.biosaxs.services.sql.SQLQueryKeeper;
import ispyb.server.biosaxs.services.sql.SqlTableMapper;
import ispyb.server.common.services.proposals.Proposal3Service;
import ispyb.server.common.vos.proposals.Proposal3VO;
import ispyb.server.common.vos.shipping.Container3VO;
import ispyb.server.common.vos.shipping.Dewar3VO;
import ispyb.server.common.vos.shipping.Shipping3VO;
import ispyb.server.mx.services.sample.Crystal3Service;
import ispyb.server.mx.services.sample.Protein3Service;
import ispyb.server.mx.vos.collections.Position3VO;
import ispyb.server.mx.vos.sample.BLSample3VO;
import ispyb.server.mx.vos.sample.BLSampleImage3VO;
import ispyb.server.mx.vos.sample.BLSubSample3VO;
import ispyb.server.mx.vos.sample.Crystal3VO;
import ispyb.server.mx.vos.sample.DiffractionPlan3VO;
import ispyb.server.mx.vos.sample.Protein3VO;

/**
 * <p>
 * This session bean handles ISPyB Shipping3.
 * </p>
 */
@Stateless
public class External3ServiceBean implements External3Service, External3ServiceLocal {


	
	 
	 private final static Logger LOG = Logger.getLogger(External3ServiceBean.class);

	 @PersistenceContext(unitName = "ispyb_db")
	private EntityManager entityManager;

	@EJB
	private Proposal3Service proposal3service;

	@EJB
	private Protein3Service protein3Service;
	
	@EJB
	private Crystal3Service crystal3Service;

	@Override
	public Shipping3VO storeShipping(String proposalCode, String proposalNumber, Shipping3VO shipping) throws Exception {
		List<Proposal3VO> proposals = proposal3service.findByCodeAndNumber(proposalCode, proposalNumber, false, false, false);
		if (proposals != null) {
			if (proposals.size() > 0) {
				Proposal3VO proposal = proposals.get(0);
				Set<Dewar3VO> dewars = shipping.getDewarVOs();
				Shipping3VO shipping3VO = this.createShipping(shipping, proposal);
				/** Creating dewars **/
				for (Dewar3VO dewar3vo : dewars) {
					Container3VO[] containers = dewar3vo.getContainers();
					dewar3vo = this.createDewar(dewar3vo, shipping3VO);
					for (Container3VO container3vo : containers) {
						Set<BLSample3VO> samples = container3vo.getSampleVOs();
						container3vo.setSampleVOs(new HashSet<BLSample3VO>());
						container3vo.setDewarVO(dewar3vo);
						container3vo = this.entityManager.merge(container3vo);
						for (BLSample3VO blSample3VO : samples) {
							/** Creating crystals **/
							if (blSample3VO.getCrystalVO() != null) {
								Crystal3VO crystal3VO = this.createCrystal(blSample3VO.getCrystalVO(), proposals.get(0));
								blSample3VO.setCrystalVO(crystal3VO);
								blSample3VO.setContainerVO(container3vo);
							}
							/** Creating Diffraction Pla **/
							if (blSample3VO.getDiffractionPlanVO() != null) {
								DiffractionPlan3VO plan = this.createDiffractionPlan(blSample3VO.getDiffractionPlanVO());
								blSample3VO.setDiffractionPlanVO(plan);
							}

							Set<BLSubSample3VO> subSamples = blSample3VO.getBlSubSampleVOs();
							blSample3VO.setBlSubSampleVOs(new HashSet<BLSubSample3VO>());
							blSample3VO = this.entityManager.merge(blSample3VO);

							/** Creating the subsamples **/
							if (subSamples != null) {
								for (BLSubSample3VO blSubSample3VO : subSamples) {
									blSubSample3VO.setBlSampleVO(blSample3VO);
									this.entityManager.merge(blSubSample3VO);
								}
							}
						}
					}
				}
				return shipping3VO;
			}
		} else {
			throw new Exception("- Proposal not found " + proposalCode + proposalNumber);
		}
		return null;
	}
	
	@Override
	public Shipping3VO storeShippingFull(String proposalCode, String proposalNumber, Shipping3VO shipping) throws Exception {
		
		List<Proposal3VO> proposals = proposal3service.findByCodeAndNumber(proposalCode, proposalNumber, false, false, false);
		
		if (proposals != null) {
			if (proposals.size() > 0) {
				Proposal3VO proposal = proposals.get(0);
				
				Set<Dewar3VO> dewars = shipping.getDewarVOs();
				Shipping3VO shipping3VO = this.createShipping(shipping, proposal);
				
				/** Creating dewars **/
				for (Dewar3VO dewar3vo : dewars) {
					Container3VO[] containers = dewar3vo.getContainers();
					dewar3vo = this.createDewar(dewar3vo, shipping3VO);
					
					for (Container3VO container3vo : containers) {
						Set<BLSample3VO> samples = container3vo.getSampleVOs();
						container3vo = this.createContainer(container3vo, dewar3vo);
						
						for (BLSample3VO blSample3VO : samples) {
							/** Creating crystals **/
							if (blSample3VO.getCrystalVO() != null) {
								Crystal3VO crystal3VO = this.createCrystal(blSample3VO.getCrystalVO(), proposals.get(0));
								blSample3VO.setCrystalVO(crystal3VO);
							}
							/** Creating Diffraction Pla **/
							if (blSample3VO.getDiffractionPlanVO() != null) {
								DiffractionPlan3VO plan = this.createDiffractionPlan(blSample3VO.getDiffractionPlanVO());
								blSample3VO.setDiffractionPlanVO(plan);
							}

							Set<BLSubSample3VO> subSamples = blSample3VO.getBlSubSampleVOs();
							Set<BLSampleImage3VO> sampleImages = blSample3VO.getBlsampleImageVOs();
							
							this.createSample(blSample3VO, container3vo);

							/** Creating the subsamples **/
							if (subSamples != null) {
								for (BLSubSample3VO blSubSample3VO : subSamples) {
									
									/** Creating Position **/
									if (blSubSample3VO.getPositionVO() != null) {
										Position3VO position = this.createPosition(blSubSample3VO.getPositionVO());
										blSubSample3VO.setPositionVO(position);
									}
									createSubSample(blSubSample3VO, blSample3VO);
								}
							}
							blSample3VO = this.entityManager.merge(blSample3VO);
							
							/** creating blsampleImage **/
							if (sampleImages != null) {
								for (Iterator<BLSampleImage3VO> iterator = sampleImages.iterator(); iterator.hasNext();) {
									BLSampleImage3VO blSampleImage3VO = (BLSampleImage3VO) iterator.next();
									blSampleImage3VO.setBlsampleVO(blSample3VO);
									blSampleImage3VO = this.entityManager.merge(blSampleImage3VO);
								}	
							}
							
						}
						container3vo = this.entityManager.merge(container3vo);
					}
				}
				return shipping3VO;
			}
		} else {
			throw new Exception("- Proposal not found " + proposalCode + proposalNumber);
		}
		return null;
	}

	private DiffractionPlan3VO createDiffractionPlan(DiffractionPlan3VO plan) {
		return this.entityManager.merge(plan);
	}
	
	private Position3VO createPosition(Position3VO pos) {
		return this.entityManager.merge(pos);
	}

	private Crystal3VO createCrystal(Crystal3VO crystal3VO, Proposal3VO proposal) throws Exception {
		if (crystal3VO.getProteinVO() != null) {
			Protein3VO aux = crystal3VO.getProteinVO();
			List<Protein3VO> proteins = protein3Service.findByAcronymAndProposalId(proposal.getProposalId(), aux.getAcronym());
			if (proteins != null) {
				if (proteins.size() > 0) {					
					//TODO check if crystal already exist before creating a new one
					List<Crystal3VO> listCrystal = crystal3Service.findByProteinId(proteins.get(0).getProteinId()) ;
					Crystal3VO newCrystal = UploadShipmentUtils.getCrystal(listCrystal, crystal3VO)    ;
					if (newCrystal == null ) {
						crystal3VO.setProteinVO(proteins.get(0));
						newCrystal = crystal3Service.create(crystal3VO);
					}
					//newCrystal.setProteinVO(proteins.get(0));
					if (newCrystal.getDiffractionPlanVO() != null) {
						newCrystal.setDiffractionPlanVO(this.createDiffractionPlan(crystal3VO.getDiffractionPlanVO()));
					}
					return this.entityManager.merge(newCrystal);
				} else {
					LOG.info("No protein found: " + aux.getAcronym());
					System.out.println("No protein found: " + aux.getAcronym());
				}
			}
		} else {
			System.out.println("Error retrieving protein list");
		}
		return null;
	}
	
	private BLSubSample3VO createSubSample(BLSubSample3VO blSubSample3VO, BLSample3VO blSample3VO) {

		blSubSample3VO.setBlSampleVO(blSample3VO);
		return this.entityManager.merge(blSubSample3VO);
	}

	private BLSample3VO createSample(BLSample3VO blSample3VO, Container3VO cont3vo) {

		blSample3VO.setBlSubSampleVOs(new HashSet<BLSubSample3VO>());
		blSample3VO.setBlsampleImageVOs(new HashSet<BLSampleImage3VO>());
		blSample3VO.setContainerVO(cont3vo);
		return this.entityManager.merge(blSample3VO);
	}

	private Container3VO createContainer(Container3VO cont3vo, Dewar3VO dewar3vo) {
		cont3vo.setSampleVOs(new HashSet<BLSample3VO>());
		cont3vo.setDewarVO(dewar3vo);
		return this.entityManager.merge(cont3vo);
	}
	
	private Dewar3VO createDewar(Dewar3VO dewar3vo, Shipping3VO shipping3vo) {
		dewar3vo.setContainerVOs(new HashSet<Container3VO>());
		dewar3vo.setShippingVO(shipping3vo);
		return this.entityManager.merge(dewar3vo);
	}

	private Shipping3VO createShipping(Shipping3VO shipping, Proposal3VO proposalVO) {
		shipping.setDewarVOs(new HashSet<Dewar3VO>());
		shipping.setProposalVO(proposalVO);
		shipping.setCreationDate(GregorianCalendar.getInstance().getTime());
		return this.entityManager.merge(shipping);
	}

	@Override
	public List<Map<String, Object>> getDataCollectionByProposal(String proposalCode, String proposalNumber) {
		// Retrieve the SQL query string from a method or constant
		String dataCollection = "select "
				+ SqlTableMapper.getProposalTable() + ","
				+ SqlTableMapper.getCrystalTable() + ","
				+ SqlTableMapper.getProteinTable() + ","
				+ SqlTableMapper.getImageTable() + ","
				+ SqlTableMapper.getBLSampleTable()
				+ " from Proposal "
				+ " LEFT JOIN Protein on Proposal.proposalId = Protein.proposalId\r\n "
				+ " LEFT JOIN Crystal on Protein.proteinId = Crystal.proteinId\r\n"
				+ " LEFT JOIN BLSample on Crystal.crystalId = BLSample.crystalId\r\n"
				+ " LEFT JOIN DataCollectionGroup on BLSample.blSampleId = DataCollectionGroup.blSampleId\r\n"
				+ " LEFT JOIN DataCollection on DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId\r\n"
				+ " LEFT JOIN Image on Image.dataCollectionId = DataCollection.dataCollectionGroupId\r\n"
				+ " where proposalNumber = ?1 and proposalCode=?2";

		// Create a native query using the EntityManager
		Query query = entityManager.createNativeQuery(dataCollection, Map.class)
				.setParameter(2,proposalCode)
				.setParameter(1,proposalNumber);

		// Execute the query and map the results manually or use a constructor expression in the SQL query
		return query.getResultList();
	}

	/**
	 * Return session by proposal code and number
	 * @param code MX, opd, SAXS
	 * @param number 
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getSessionsByProposalCodeAndNumber(String proposalCode, String proposalNumber){

		String sessions = "select " +
				"BLSession.sessionId as BLSession_sessionId,\r\n"
				+ "BLSession.expSessionPk as BLSession_expSessionPk,\r\n"
				+ "BLSession.beamLineSetupId as BLSession_beamLineSetupId,\r\n"
				+ "BLSession.proposalId as BLSession_proposalId,\r\n"
				+ "BLSession.projectCode as BLSession_projectCode,\r\n"
				+ "BLSession.startDate as BLSession_startDate,\r\n"
				+ "BLSession.endDate as BLSession_endDate,\r\n"
				+ "BLSession.beamLineName as BLSession_beamLineName,\r\n"
				+ "BLSession.scheduled as BLSession_scheduled,\r\n"
				+ "BLSession.nbShifts as BLSession_nbShifts,\r\n"
				+ "BLSession.comments as BLSession_comments,\r\n"
				+ "BLSession.beamLineOperator as BLSession_beamLineOperator,\r\n"
				+ "BLSession.visit_number as BLSession_visit_number,\r\n"
				+ "BLSession.bltimeStamp as BLSession_bltimeStamp,\r\n"
				+ "BLSession.usedFlag as BLSession_usedFlag,\r\n"
				+ "BLSession.sessionTitle as BLSession_sessionTitle,\r\n"
				+ "BLSession.structureDeterminations as BLSession_structureDeterminations,\r\n"
				+ "BLSession.dewarTransport as BLSession_dewarTransport,\r\n"
				+ "BLSession.databackupFrance as BLSession_databackupFrance,\r\n"
				+ "BLSession.databackupEurope as BLSession_databackupEurope,\r\n"
				+ "BLSession.operatorSiteNumber as BLSession_operatorSiteNumber,\r\n"
				+ "BLSession.lastUpdate as BLSession_lastUpdate,\r\n"
				+ "BLSession.nbReimbDewars as BLSession_nbReimbDewars,\r\n"
				+ "BLSession.protectedData as BLSession_protectedData" + "," +
				SqlTableMapper.getProposalTable()
				+ " from BLSession, Proposal where Proposal.proposalId = BLSession.proposalId and Proposal.proposalCode= ?1  and Proposal.proposalNumber= ?2";

		// Create a native query using the EntityManager
		Query query = entityManager.createNativeQuery(sessions, Map.class)
				.setParameter(1,proposalCode)
				.setParameter(2,proposalNumber);

		// Execute the query and map the results manually or use a constructor expression in the SQL query
		return query.getResultList();

	}

	@Override
	public List<Map<String, Object>> getDataCollectionFromShippingId(int shippingId){
		String dataCollection = "select " +
				SqlTableMapper.getShippingTable() + " ," +
				SqlTableMapper.getDewarTable() + " ," +
				SqlTableMapper.getContainerTable() + " ," +
				SqlTableMapper.getBLSampleTable() + ", " +
				SqlTableMapper.getDataCollectionGroupTable() + ", " +
				SqlTableMapper.getDataCollectionTable() + " ," +
				SqlTableMapper.getCrystalTable()  +
				" from Shipping left join Dewar on Dewar.shippingId = Shipping.shippingId \r\n" +
				"				left join Container on Container.dewarId = Dewar.dewarId\r\n" +
				"				left join BLSample on BLSample.containerId = Container.containerId \r\n" +
				"				left join Crystal on Crystal.crystalId = BLSample.crystalId \r\n" +
				"				left join DataCollectionGroup on DataCollectionGroup.blSampleId = BLSample.blSampleId \r\n" +
				" 				left join DataCollection on DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId\r\n" +
				" where Shipping.shippingId = ?1";

		// Create a native query using the EntityManager
		Query query = entityManager.createNativeQuery(dataCollection, Map.class)
				.setParameter(1,String.valueOf(shippingId));
		// Execute the query and map the results manually or use a constructor expression in the SQL query
		return query.getResultList();

	}
	
	@Override
	public List<Map<String, Object>> getAllDataCollectionFromShippingId(int shippingId){
		String allDataCollection = "select  " +
				SqlTableMapper.getShippingTable() + " ," +
				SqlTableMapper.getDewarTable() + " ," +
				SqlTableMapper.getContainerTable() + " ," +
				SqlTableMapper.getBLSampleTable() + " ," +
				SqlTableMapper.getDataCollectionGroupTable() + " ," +
				SqlTableMapper.getDataCollectionTable() + " ," +
				SqlTableMapper.getImageTable() + " ," +
				SqlTableMapper.getWorkflowTable() + " ," +
				SqlTableMapper.getWorkflowMeshTable() + " ," +
				SqlTableMapper.getCrystalTable()  +
				" from Shipping left join Dewar on Dewar.shippingId = Shipping.shippingId \r\n"
				+ " left join Container on Container.dewarId = Dewar.dewarId \r\n"
				+ " left join BLSample  on BLSample.containerId = Container.containerId \r\n"
				+ "	left join Crystal on Crystal.crystalId = BLSample.crystalId \r\n"
				+ " left join DataCollectionGroup on DataCollectionGroup.blSampleId = BLSample.blSampleId \r\n"
				+ " left join DataCollection on DataCollection.dataCollectionGroupId = DataCollectionGroup.dataCollectionGroupId\r\n"
				+ " left join Image on Image.dataCollectionId = DataCollection.dataCollectionId\r\n"
				+ " left join Workflow on DataCollectionGroup.workflowId = Workflow.workflowId\r\n"
				+ " left join WorkflowMesh on WorkflowMesh.workflowId = Workflow.workflowId\r\n"
				+ " where Shipping.shippingId = ?1";

		// Create a native query using the EntityManager
		Query query = entityManager.createNativeQuery(allDataCollection, Map.class)
				.setParameter(1, String.valueOf(shippingId));
		// Execute the query and map the results manually or use a constructor expression in the SQL query
		return query.getResultList();
	}

	@Override
	public List<Map<String, Object>> getAutoprocResultByDataCollectionIdList(
			ArrayList<Integer> dataCollectionIdList) {
		String autoprocResult = "select " +
//				 SQLQueryKeeper.getDataCollectionTable() + " ," +
				"AutoProcIntegration.autoProcIntegrationId as AutoProcIntegration_autoProcIntegrationId,\r\n" +
				"AutoProcIntegration.dataCollectionId as AutoProcIntegration_dataCollectionId,\r\n" +
				"AutoProcIntegration.autoProcProgramId as AutoProcIntegration_autoProcProgramId,\r\n" +
				"AutoProcIntegration.startImageNumber as AutoProcIntegration_startImageNumber,\r\n" +
				"AutoProcIntegration.endImageNumber as AutoProcIntegration_endImageNumber,\r\n" +
				"AutoProcIntegration.refinedDetectorDistance as AutoProcIntegration_refinedDetectorDistance,\r\n" +
				"AutoProcIntegration.refinedXBeam as AutoProcIntegration_refinedXBeam,\r\n" +
				"AutoProcIntegration.refinedYBeam as AutoProcIntegration_refinedYBeam,\r\n" +
				"AutoProcIntegration.rotationAxisX as AutoProcIntegration_rotationAxisX,\r\n" +
				"AutoProcIntegration.rotationAxisY as AutoProcIntegration_rotationAxisY,\r\n" +
				"AutoProcIntegration.rotationAxisZ as AutoProcIntegration_rotationAxisZ,\r\n" +
				"AutoProcIntegration.beamVectorX as AutoProcIntegration_beamVectorX,\r\n" +
				"AutoProcIntegration.beamVectorY as AutoProcIntegration_beamVectorY,\r\n" +
				"AutoProcIntegration.beamVectorZ as AutoProcIntegration_beamVectorZ,\r\n" +
				"AutoProcIntegration.cell_a as AutoProcIntegration_cell_a,\r\n" +
				"AutoProcIntegration.cell_b as AutoProcIntegration_cell_b,\r\n" +
				"AutoProcIntegration.cell_c as AutoProcIntegration_cell_c,\r\n" +
				"AutoProcIntegration.cell_alpha as AutoProcIntegration_cell_alpha,\r\n" +
				"AutoProcIntegration.cell_beta as AutoProcIntegration_cell_beta,\r\n" +
				"AutoProcIntegration.cell_gamma as AutoProcIntegration_cell_gamma,\r\n" +
				"AutoProcIntegration.recordTimeStamp as AutoProcIntegration_recordTimeStamp,\r\n" +
				"AutoProcIntegration.anomalous as AutoProcIntegration_anomalous,\r\n" +
				"AutoProcScaling_has_Int.autoProcScaling_has_IntId as AutoProcScaling_has_Int_autoProcScaling_has_IntId,\r\n" +
				"AutoProcScaling_has_Int.autoProcScalingId as AutoProcScaling_has_Int_autoProcScalingId,\r\n" +
				"AutoProcScaling_has_Int.autoProcIntegrationId as AutoProcScaling_has_Int_autoProcIntegrationId,\r\n" +
				"AutoProcScaling_has_Int.recordTimeStamp as AutoProcScaling_has_Int_recordTimeStamp,\r\n" +
				"AutoProcScaling.autoProcScalingId as AutoProcScaling_autoProcScalingId,\r\n" +
				"AutoProcScaling.autoProcId as AutoProcScaling_autoProcId,\r\n" +
				"AutoProcScaling.recordTimeStamp as AutoProcScaling_recordTimeStamp,\r\n" +
				"AutoProcScalingStatistics.autoProcScalingStatisticsId as AutoProcScalingStatistics_autoProcScalingStatisticsId,\r\n" +
				"AutoProcScalingStatistics.autoProcScalingId as AutoProcScalingStatistics_autoProcScalingId,\r\n" +
				"AutoProcScalingStatistics.scalingStatisticsType as AutoProcScalingStatistics_scalingStatisticsType,\r\n" +
				"AutoProcScalingStatistics.comments as AutoProcScalingStatistics_comments,\r\n" +
				"AutoProcScalingStatistics.resolutionLimitLow as AutoProcScalingStatistics_resolutionLimitLow,\r\n" +
				"AutoProcScalingStatistics.resolutionLimitHigh as AutoProcScalingStatistics_resolutionLimitHigh,\r\n" +
				"AutoProcScalingStatistics.rMerge as AutoProcScalingStatistics_rMerge,\r\n" +
				"AutoProcScalingStatistics.rMeasWithinIPlusIMinus as AutoProcScalingStatistics_rMeasWithinIPlusIMinus,\r\n" +
				"AutoProcScalingStatistics.rMeasAllIPlusIMinus as AutoProcScalingStatistics_rMeasAllIPlusIMinus,\r\n" +
				"AutoProcScalingStatistics.rPimWithinIPlusIMinus as AutoProcScalingStatistics_rPimWithinIPlusIMinus,\r\n" +
				"AutoProcScalingStatistics.rPimAllIPlusIMinus as AutoProcScalingStatistics_rPimAllIPlusIMinus,\r\n" +
				"AutoProcScalingStatistics.fractionalPartialBias as AutoProcScalingStatistics_fractionalPartialBias,\r\n" +
				"AutoProcScalingStatistics.nTotalObservations as AutoProcScalingStatistics_nTotalObservations,\r\n" +
				"AutoProcScalingStatistics.nTotalUniqueObservations as AutoProcScalingStatistics_nTotalUniqueObservations,\r\n" +
				"AutoProcScalingStatistics.meanIOverSigI as AutoProcScalingStatistics_meanIOverSigI,\r\n" +
				"AutoProcScalingStatistics.completeness as AutoProcScalingStatistics_completeness,\r\n" +
				"AutoProcScalingStatistics.multiplicity as AutoProcScalingStatistics_multiplicity,\r\n" +
				"AutoProcScalingStatistics.anomalousCompleteness as AutoProcScalingStatistics_anomalousCompleteness,\r\n" +
				"AutoProcScalingStatistics.anomalousMultiplicity as AutoProcScalingStatistics_anomalousMultiplicity,\r\n" +
				"AutoProcScalingStatistics.recordTimeStamp as AutoProcScalingStatistics_recordTimeStamp,\r\n" +
				"AutoProcScalingStatistics.anomalous as AutoProcScalingStatistics_anomalous,\r\n" +
				"AutoProcScalingStatistics.ccHalf as AutoProcScalingStatistics_ccHalf\r\n" +
				"from DataCollection\r\n" +
				"left join AutoProcIntegration on AutoProcIntegration.dataCollectionId = DataCollection.dataCollectionId\r\n" +
				"left join AutoProcScaling_has_Int on AutoProcScaling_has_Int.autoProcIntegrationId = AutoProcIntegration.autoProcIntegrationId\r\n" +
				"left join AutoProcScaling on AutoProcScaling.autoProcScalingId = AutoProcScaling_has_Int.autoProcScalingId\r\n" +
				"left join AutoProcScalingStatistics on AutoProcScalingStatistics.autoProcScalingId = AutoProcScaling.autoProcScalingId" +
				" where DataCollection.dataCollectionId IN ?1";

		// Create a native query using the EntityManager
		Query query = entityManager.createNativeQuery(autoprocResult, Map.class)
				.setParameter(1, String.valueOf(dataCollectionIdList));
		return query.getResultList();
	}
	

	@Override
	public List<Map<String, Object>> getPhasingAnalysisByDataCollectionIdListQuery( Integer autoProcIntegrationId) {
		String phasingAnalysis = "select \r\n" +
				"phasing_has_Scaling.datasetNumber,\r\n" +
				"preparePhasingData.phasingAnalysisId as PreparePhasingData_phasingAnalysisId,\r\n" +
				"preparePhasingData.lowRes as PreparePhasingData_lowRes,\r\n" +
				"preparePhasingData.highRes as PreparePhasingData_highRes,\r\n" +
				"preparePhasingDataSpaceGroup.spaceGroupNumber as PreparePhasingDataSpaceGroup_spaceGroupNumber,\r\n" +
				"preparePhasingDataSpaceGroup.spaceGroupShortName as PreparePhasingDataSpaceGroup_spaceGroupShortName,\r\n" +
				"preparePhasingDataSpaceGroup.spaceGroupName as PreparePhasingDataSpaceGroup_spaceGroupName,\r\n" +
				"preparePhasingDataSpaceGroup.bravaisLattice as PreparePhasingDataSpaceGroup_bravaisLatticeName,\r\n" +
				"preparePhasingDataSpaceGroup.pointGroup as PreparePhasingDataSpaceGroup_pointGroup,\r\n" +
				"preparePhasingDataSpaceGroup.MX_used as PreparePhasingDataSpaceGroup_MX_user,\r\n" +
				"preparePhasingProgramRun.phasingCommandLine as PreparePhasingProgramRun_phasingCommandLine,\r\n" +
				"preparePhasingProgramRun.phasingPrograms as PreparePhasingProgramRun_phasingPrograms,\r\n" +
				"preparePhasingProgramRun.phasingStatus as PreparePhasingProgramRun_phasingStatus,\r\n" +
				"preparePhasingProgramRun.phasingMessage as PreparePhasingProgramRun_phasingMessage,\r\n" +
				"preparePhasingProgramRun.phasingStartTime as PreparePhasingProgramRun_phasingStartTime,\r\n" +
				"preparePhasingProgramRun.phasingEndTime as PreparePhasingProgramRun_phasingEndTime,\r\n" +
				"\r\n" +
				"\r\n" +
				"substructureDetermination.phasingAnalysisId as SubstructureDetermination_phasingAnalysisId,\r\n" +
				"substructureDetermination.lowRes as SubstructureDetermination_lowRes,\r\n" +
				"substructureDetermination.highRes as SubstructureDetermination_highRes,\r\n" +
				"substructureDetermination.method as SubstructureDetermination_method,\r\n" +
				"substructureDeterminationSpaceGroup.spaceGroupNumber as SubstructureDeterminationSpaceGroup_spaceGroupNumber,\r\n" +
				"substructureDeterminationSpaceGroup.spaceGroupShortName as SubstructureDeterminationSpaceGroup_spaceGroupShortName,\r\n" +
				"substructureDeterminationSpaceGroup.spaceGroupName as SubstructureDeterminationSpaceGroup_spaceGroupName,\r\n" +
				"substructureDeterminationSpaceGroup.bravaisLattice as SubstructureDeterminationSpaceGroup_bravaisLatticeName,\r\n" +
				"substructureDeterminationSpaceGroup.pointGroup as SubstructureDeterminationSpaceGroup_pointGroup,\r\n" +
				"substructureDeterminationSpaceGroup.MX_used as SubstructureDeterminationSpaceGroup_MX_user,\r\n" +
				"substructureDeterminationPhasingProgramRun.phasingCommandLine as SubstructureDeterminationPhasingProgramRun_phasingCommandLine,\r\n" +
				"substructureDeterminationPhasingProgramRun.phasingPrograms as SubstructureDeterminationPhasingProgramRun_phasingPrograms,\r\n" +
				"substructureDeterminationPhasingProgramRun.phasingStatus as SubstructureDeterminationPhasingProgramRun_phasingStatus,\r\n" +
				"substructureDeterminationPhasingProgramRun.phasingMessage as SubstructureDeterminationPhasingProgramRun_phasingMessage,\r\n" +
				"substructureDeterminationPhasingProgramRun.phasingStartTime as SubstructureDeterminationPhasingProgramRun_phasingStartTime,\r\n" +
				"substructureDeterminationPhasingProgramRun.phasingEndTime as SubstructureDeterminationPhasingProgramRun_phasingEndTime,\r\n" +
				"\r\n" +
				"\r\n" +
				"phasing.phasingAnalysisId as Phasing_phasingAnalysisId,\r\n" +
				"phasing.lowRes as Phasing_lowRes,\r\n" +
				"phasing.highRes as Phasing_highRes,\r\n" +
				"phasing.method as Phasing_method,\r\n" +
				"phasing.solventContent as Phasing_solventContent,\r\n" +
				"phasing.enantiomorph as Phasing_enantiomorph,\r\n" +
				"phasingSpaceGroup.spaceGroupNumber as PhasingSpaceGroup_spaceGroupNumber,\r\n" +
				"phasingSpaceGroup.spaceGroupShortName as PhasingSpaceGroup_spaceGroupShortName,\r\n" +
				"phasingSpaceGroup.spaceGroupName as PhasingSpaceGroup_spaceGroupName,\r\n" +
				"phasingSpaceGroup.bravaisLattice as PhasingSpaceGroup_bravaisLatticeName,\r\n" +
				"phasingSpaceGroup.pointGroup as PhasingSpaceGroup_pointGroup,\r\n" +
				"phasingSpaceGroup.MX_used as PhasingSpaceGroup_MX_user,\r\n" +
				"phasingPhasingProgramRun.phasingCommandLine as PhasingPhasingProgramRun_phasingCommandLine,\r\n" +
				"phasingPhasingProgramRun.phasingPrograms as PhasingPhasingProgramRun_phasingPrograms,\r\n" +
				"phasingPhasingProgramRun.phasingStatus as PhasingPhasingProgramRun_phasingStatus,\r\n" +
				"phasingPhasingProgramRun.phasingMessage as PhasingPhasingProgramRun_phasingMessage,\r\n" +
				"phasingPhasingProgramRun.phasingStartTime as PhasingPhasingProgramRun_phasingStartTime,\r\n" +
				"phasingPhasingProgramRun.phasingEndTime as PhasingPhasingProgramRun_phasingEndTime,\r\n" +
				"\r\n" +
				"modelBuilding.phasingAnalysisId as ModelBuilding_phasingAnalysisId,\r\n" +
				"modelBuilding.lowRes as ModelBuilding_lowRes,\r\n" +
				"modelBuilding.highRes as ModelBuilding_highRes,\r\n" +
				"modelBuildingSpaceGroup.spaceGroupNumber as ModelBuildingSpaceGroup_spaceGroupNumber,\r\n" +
				"modelBuildingSpaceGroup.spaceGroupShortName as ModelBuildingSpaceGroup_spaceGroupShortName,\r\n" +
				"modelBuildingSpaceGroup.spaceGroupName as ModelBuildingSpaceGroup_spaceGroupName,\r\n" +
				"modelBuildingSpaceGroup.bravaisLattice as ModelBuildingSpaceGroup_bravaisLatticeName,\r\n" +
				"modelBuildingSpaceGroup.pointGroup as ModelBuildingSpaceGroup_pointGroup,\r\n" +
				"modelBuildingSpaceGroup.MX_used as ModelBuildingSpaceGroup_MX_user,\r\n" +
				"modelBuildingPhasingProgramRun.phasingCommandLine as ModelBuildingPhasingProgramRun_phasingCommandLine,\r\n" +
				"modelBuildingPhasingProgramRun.phasingPrograms as ModelBuildingPhasingProgramRun_phasingPrograms,\r\n" +
				"modelBuildingPhasingProgramRun.phasingStatus as ModelBuildingPhasingProgramRun_phasingStatus,\r\n" +
				"modelBuildingPhasingProgramRun.phasingMessage as ModelBuildingPhasingProgramRun_phasingMessage,\r\n" +
				"modelBuildingPhasingProgramRun.phasingStartTime as ModelBuildingPhasingProgramRun_phasingStartTime,\r\n" +
				"modelBuildingPhasingProgramRun.phasingEndTime as ModelBuildingPhasingProgramRun_phasingEndTime\r\n" +
				"\r\n" +
				"\r\n" +
				"\r\n" +
				"from AutoProcIntegration autoProcIntegration\r\n" +
				"left join AutoProcScaling_has_Int a_h_i on autoProcIntegration.autoProcIntegrationId = a_h_i.autoProcIntegrationId\r\n" +
				"left join AutoProcScaling autoProcScaling on autoProcScaling.autoProcId = a_h_i.autoProcScalingId\r\n" +
				"left join Phasing_has_Scaling phasing_has_Scaling on phasing_has_Scaling.autoProcScalingId = autoProcScaling.autoProcScalingId\r\n" +
				"left join PhasingAnalysis phasingAnalysis on phasingAnalysis.phasingAnalysisId = phasing_has_Scaling.phasingAnalysisId\r\n" +
				"\r\n" +
				"left join PreparePhasingData preparePhasingData on preparePhasingData.phasingAnalysisId = phasingAnalysis.phasingAnalysisId\r\n" +
				"left join SpaceGroup preparePhasingDataSpaceGroup on preparePhasingData.spaceGroupId = preparePhasingDataSpaceGroup.spaceGroupId\r\n" +
				"left join PhasingProgramRun preparePhasingProgramRun on preparePhasingProgramRun.phasingProgramRunId = preparePhasingData.phasingProgramRunId\r\n" +
				"\r\n" +
				"left join SubstructureDetermination substructureDetermination on substructureDetermination.phasingAnalysisId = phasingAnalysis.phasingAnalysisId\r\n" +
				"left join SpaceGroup substructureDeterminationSpaceGroup on substructureDetermination.spaceGroupId = substructureDeterminationSpaceGroup.spaceGroupId\r\n" +
				"left join PhasingProgramRun substructureDeterminationPhasingProgramRun on substructureDeterminationPhasingProgramRun.phasingProgramRunId = substructureDetermination.phasingProgramRunId\r\n" +
				"\r\n" +
				"left join Phasing phasing on phasing.phasingAnalysisId = phasingAnalysis.phasingAnalysisId\r\n" +
				"left join SpaceGroup phasingSpaceGroup on phasing.spaceGroupId = phasingSpaceGroup.spaceGroupId\r\n" +
				"left join PhasingProgramRun phasingPhasingProgramRun on phasingPhasingProgramRun.phasingProgramRunId = phasing.phasingProgramRunId\r\n" +
				"\r\n" +
				"\r\n" +
				"left join ModelBuilding modelBuilding on modelBuilding.phasingAnalysisId = phasingAnalysis.phasingAnalysisId\r\n" +
				"left join SpaceGroup modelBuildingSpaceGroup on modelBuilding.spaceGroupId = modelBuildingSpaceGroup.spaceGroupId\r\n" +
				"left join PhasingProgramRun modelBuildingPhasingProgramRun on modelBuildingPhasingProgramRun.phasingProgramRunId = modelBuilding.phasingProgramRunId"
				+ " where autoProcIntegration.autoProcIntegrationId = ?1";

		// Create a native query using the EntityManager
		Query query = entityManager.createNativeQuery(phasingAnalysis, Map.class)
				.setParameter(1, String.valueOf(autoProcIntegrationId));
		return query.getResultList();
	}
	
	
}