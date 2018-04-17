package com.incture.metrodata.dao;

import org.springframework.stereotype.Repository;

import com.incture.metrodata.dto.ContainerDetailsDTO;
import com.incture.metrodata.entity.ContainerDetailsDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.util.ServicesUtil;

@Repository("containerDao")
public class ContainerDAO extends BaseDao<ContainerDetailsDo, ContainerDetailsDTO> {

	@Override
	ContainerDetailsDo importDto(ContainerDetailsDTO dto, ContainerDetailsDo dos)
			throws InvalidInputFault, ExecutionFault, NoResultFault, Exception {
		
		if(ServicesUtil.isEmpty(dos))
			dos = new  ContainerDetailsDo();
		
		if(!ServicesUtil.isEmpty(dto)){
			if(!ServicesUtil.isEmpty(dto.getId())){
				dos.setId(dto.getId());
			}
			if(!ServicesUtil.isEmpty(dto.getDELIVNO())){
				dos.setDELIVNO(dto.getDELIVNO());
			}
			if(!ServicesUtil.isEmpty(dto.getCREATEDT())){
				dos.setCREATEDT(dto.getCREATEDT());
			}
			if(!ServicesUtil.isEmpty(dto.getCREATETM())){
				dos.setCREATETM(dto.getCREATETM());
			}
			if(!ServicesUtil.isEmpty(dto.getSALESGRP())){
				dos.setSALESGRP(dto.getSALESGRP());
			}
			if(!ServicesUtil.isEmpty(dto.getPURCHORD())){
				dos.setPURCHORD(dto.getPURCHORD());
			}
			if(!ServicesUtil.isEmpty(dto.getREFNO())){
				dos.setREFNO(dto.getREFNO());
			}
			if(!ServicesUtil.isEmpty(dto.getSLOC())){
				dos.setSLOC(dto.getSLOC());
			}
			if(!ServicesUtil.isEmpty(dto.getSHIPADD())){
				dos.setSHIPADD(dto.getSHIPADD());
			}
			if(!ServicesUtil.isEmpty(dto.getCITY())){
				dos.setCITY(dto.getCITY());
			}
			if(!ServicesUtil.isEmpty(dto.getAREACODE())){
				dos.setCITY(dto.getCITY());
			}
			if(!ServicesUtil.isEmpty(dto.getTELP())){
				dos.setTELP(dto.getTELP());
			}
			if(!ServicesUtil.isEmpty(dto.getSOLDADD())){
				dos.setSOLDADD(dto.getSOLDADD());
			}
			if(!ServicesUtil.isEmpty(dto.getSHIPTYP())){
				dos.setSHIPTYP(dto.getSHIPTYP());
			}
			if(!ServicesUtil.isEmpty(dto.getINSTDELV())){
				dos.setINSTDELV(dto.getINSTDELV());
			}
			if(!ServicesUtil.isEmpty(dto.getSERNUM())){
				dos.setSERNUM(dto.getSERNUM());
			}
			if(!ServicesUtil.isEmpty(dto.getMAT())){
				dos.setMAT(dto.getMAT());
			}
			if(!ServicesUtil.isEmpty(dto.getBATCH())){
				dos.setBATCH(dto.getBATCH());
			}
			if(!ServicesUtil.isEmpty(dto.getDESC())){
				dos.setDESC(dto.getDESC());
			}
			if(!ServicesUtil.isEmpty(dto.getQTY())){
				dos.setQTY(dto.getQTY());
			}
			if(!ServicesUtil.isEmpty(dto.getVOL())){
				dos.setVOL(dto.getVOL());
			}
			if(!ServicesUtil.isEmpty(dto.getSTAT())){
				dos.setSTAT(dto.getSTAT());
			}
		}
		
		return dos;
	}

	@Override
	ContainerDetailsDTO exportDto(ContainerDetailsDo dos) {
		
		ContainerDetailsDTO dto = new  ContainerDetailsDTO();
		
		if(!ServicesUtil.isEmpty(dos)){
			if(!ServicesUtil.isEmpty(dos.getId())){
				dto.setId(dos.getId());
			}
			if(!ServicesUtil.isEmpty(dos.getDELIVNO())){
				dto.setDELIVNO(dos.getDELIVNO());
			}
			if(!ServicesUtil.isEmpty(dos.getCREATEDT())){
				dto.setCREATEDT(dos.getCREATEDT());
			}
			if(!ServicesUtil.isEmpty(dos.getCREATETM())){
				dto.setCREATETM(dos.getCREATETM());
			}
			if(!ServicesUtil.isEmpty(dos.getSALESGRP())){
				dto.setSALESGRP(dos.getSALESGRP());
			}
			if(!ServicesUtil.isEmpty(dos.getPURCHORD())){
				dto.setPURCHORD(dos.getPURCHORD());
			}
			if(!ServicesUtil.isEmpty(dos.getREFNO())){
				dto.setREFNO(dos.getREFNO());
			}
			if(!ServicesUtil.isEmpty(dos.getSLOC())){
				dto.setSLOC(dos.getSLOC());
			}
			if(!ServicesUtil.isEmpty(dos.getSHIPADD())){
				dto.setSHIPADD(dos.getSHIPADD());
			}
			if(!ServicesUtil.isEmpty(dos.getCITY())){
				dto.setCITY(dos.getCITY());
			}
			if(!ServicesUtil.isEmpty(dos.getAREACODE())){
				dto.setCITY(dos.getCITY());
			}
			if(!ServicesUtil.isEmpty(dos.getTELP())){
				dto.setTELP(dos.getTELP());
			}
			if(!ServicesUtil.isEmpty(dos.getSOLDADD())){
				dto.setSOLDADD(dos.getSOLDADD());
			}
			if(!ServicesUtil.isEmpty(dos.getSHIPTYP())){
				dto.setSHIPTYP(dos.getSHIPTYP());
			}
			if(!ServicesUtil.isEmpty(dos.getINSTDELV())){
				dto.setINSTDELV(dos.getINSTDELV());
			}
			if(!ServicesUtil.isEmpty(dos.getSERNUM())){
				dto.setSERNUM(dos.getSERNUM());
			}
			if(!ServicesUtil.isEmpty(dos.getMAT())){
				dto.setMAT(dos.getMAT());
			}
			if(!ServicesUtil.isEmpty(dos.getBATCH())){
				dto.setBATCH(dos.getBATCH());
			}
			if(!ServicesUtil.isEmpty(dos.getDESC())){
				dto.setDESC(dos.getDESC());
			}
			if(!ServicesUtil.isEmpty(dos.getQTY())){
				dto.setQTY(dos.getQTY());
			}
			if(!ServicesUtil.isEmpty(dos.getVOL())){
				dto.setVOL(dos.getVOL());
			}
			if(!ServicesUtil.isEmpty(dto.getSTAT())){
				dos.setSTAT(dto.getSTAT());
			}
		}
		
		return dto;
	}

}
