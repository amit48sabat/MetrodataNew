package com.incture.metrodata.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.constant.DeliveryNoteStatus;
import com.incture.metrodata.constant.RoleConstant;
import com.incture.metrodata.dto.DeliveryHeaderDTO;
import com.incture.metrodata.dto.DeliveryItemDTO;
import com.incture.metrodata.dto.WareHouseDetailsDTO;
import com.incture.metrodata.entity.DeliveryHeaderDo;
import com.incture.metrodata.entity.DeliveryItemDo;
import com.incture.metrodata.entity.WareHouseDetailsDo;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.util.PaginationUtil;
import com.incture.metrodata.util.ServicesUtil;

@Repository("DeliveryHeaderDao")
public class DeliveryHeaderDAO extends BaseDao<DeliveryHeaderDo, DeliveryHeaderDTO> {
	@Autowired
	DeliveryItemDAO deliveryItemDAO;

	@Autowired
	WareHouseDAO wareHouseDetailDao;

	@Override
	public DeliveryHeaderDo importDto(DeliveryHeaderDTO deliveryHeaderDTO, DeliveryHeaderDo deliveryHeaderDo)
			throws Exception {
		if (ServicesUtil.isEmpty(deliveryHeaderDo))
			deliveryHeaderDo = new DeliveryHeaderDo();
		if (!ServicesUtil.isEmpty(deliveryHeaderDTO)) {
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getDeliveryNoteId())) {
				deliveryHeaderDo.setDeliveryNoteId(deliveryHeaderDTO.getDeliveryNoteId());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getAreaCode())) {
				deliveryHeaderDo.setAreaCode(deliveryHeaderDTO.getAreaCode());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getCity())) {
				deliveryHeaderDo.setCity(deliveryHeaderDTO.getCity());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getInstructionForDelivery())) {
				deliveryHeaderDo.setInstructionForDelivery(deliveryHeaderDTO.getInstructionForDelivery());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getPurchaseOrder())) {
				deliveryHeaderDo.setPurchaseOrder(deliveryHeaderDTO.getPurchaseOrder());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getSalesGroup())) {
				deliveryHeaderDo.setSalesGroup(deliveryHeaderDTO.getSalesGroup());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getShippingType())) {
				deliveryHeaderDo.setShippingType(deliveryHeaderDTO.getShippingType());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getSoldToAddress())) {
				deliveryHeaderDo.setSoldToAddress(deliveryHeaderDTO.getSoldToAddress());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getShipToAddress())) {
				deliveryHeaderDo.setShipToAddress(deliveryHeaderDTO.getShipToAddress());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getLongitude())) {
				deliveryHeaderDo.setLongitude(deliveryHeaderDTO.getLongitude());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getLatitude())) {
				deliveryHeaderDo.setLatitude(deliveryHeaderDTO.getLatitude());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getStorageLocation())) {
				deliveryHeaderDo.setStorageLocation(deliveryHeaderDTO.getStorageLocation());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getTelephone())) {
				deliveryHeaderDo.setTelephone(deliveryHeaderDTO.getTelephone());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getTripped())) {
				deliveryHeaderDo.setTripped(deliveryHeaderDTO.getTripped());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getCreatedDate())) {
				deliveryHeaderDo.setCreatedDate(deliveryHeaderDTO.getCreatedDate());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getRefNo())) {
				deliveryHeaderDo.setRefNo(deliveryHeaderDTO.getRefNo());
			}

			// validate the delivery note status if invalid throw expection
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getStatus())) {
				if (checkDnStatus(deliveryHeaderDTO.getStatus()))
					deliveryHeaderDo.setStatus(deliveryHeaderDTO.getStatus());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getStartedAt())) {
				deliveryHeaderDo.setStartedAt(deliveryHeaderDTO.getStartedAt());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getEndedAt())) {
				deliveryHeaderDo.setEndedAt(deliveryHeaderDTO.getEndedAt());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getDeliveryOrder())) {
				deliveryHeaderDo.setDeliveryOrder(deliveryHeaderDTO.getDeliveryOrder());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getReasonForCancellation())) {
				deliveryHeaderDo.setReasonForCancellation(deliveryHeaderDTO.getReasonForCancellation());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getSignatureDocId())) {

				deliveryHeaderDo.setSignatureDocId(deliveryHeaderDTO.getSignatureDocId());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getInvalidateIds())) {

				deliveryHeaderDo.setInvalidateIds(deliveryHeaderDTO.getInvalidateIds());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getRating())) {
				deliveryHeaderDo.setRating(deliveryHeaderDTO.getRating());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getReceiverName())) {
				deliveryHeaderDo.setReceiverName(deliveryHeaderDTO.getReceiverName());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getCreatedAt())) {
				deliveryHeaderDo.setCreatedAt(deliveryHeaderDTO.getCreatedAt());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getUpdatedAt())) {
				deliveryHeaderDo.setUpdatedAt(deliveryHeaderDTO.getUpdatedAt());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getAssignedUser())) {
				deliveryHeaderDo.setAssignedUser(deliveryHeaderDTO.getAssignedUser());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getWareHouseDetails())) {
				WareHouseDetailsDo wareHouseDo = new WareHouseDetailsDo();
				try {
					wareHouseDo = wareHouseDetailDao.getByKeysForFK(deliveryHeaderDTO.getWareHouseDetails());
				} catch (Exception e) {
					// TODO: handle exception
				}
				deliveryHeaderDo.setWareHouseDetails(wareHouseDetailDao.importDto(deliveryHeaderDTO.getWareHouseDetails(), wareHouseDo));

			}

			// importing delivery items
			if (!ServicesUtil.isEmpty(deliveryHeaderDTO.getDeliveryItems())) {

				List<DeliveryItemDo> itemDos = deliveryItemDAO.importList(deliveryHeaderDTO.getDeliveryItems(),
						deliveryHeaderDo.getDeliveryItems());
				deliveryHeaderDo.setDeliveryItems(itemDos);
			}
		}

		return deliveryHeaderDo;

	}

	@Override
	DeliveryHeaderDTO exportDto(DeliveryHeaderDo deliveryHeaderDo) {
		DeliveryHeaderDTO deliveryHeaderDTO = new DeliveryHeaderDTO();
		if (!ServicesUtil.isEmpty(deliveryHeaderDo)) {
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getDeliveryNoteId())) {
				deliveryHeaderDTO.setDeliveryNoteId(deliveryHeaderDo.getDeliveryNoteId());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getAreaCode())) {
				deliveryHeaderDTO.setAreaCode(deliveryHeaderDo.getAreaCode());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getCity())) {
				deliveryHeaderDTO.setCity(deliveryHeaderDo.getCity());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getInstructionForDelivery())) {
				deliveryHeaderDTO.setInstructionForDelivery(deliveryHeaderDo.getInstructionForDelivery());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getPurchaseOrder())) {
				deliveryHeaderDTO.setPurchaseOrder(deliveryHeaderDo.getPurchaseOrder());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getSalesGroup())) {
				deliveryHeaderDTO.setSalesGroup(deliveryHeaderDo.getSalesGroup());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getShippingType())) {
				deliveryHeaderDTO.setShippingType(deliveryHeaderDo.getShippingType());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getSoldToAddress())) {
				deliveryHeaderDTO.setSoldToAddress(deliveryHeaderDo.getSoldToAddress());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getShipToAddress())) {
				deliveryHeaderDTO.setShipToAddress(deliveryHeaderDo.getShipToAddress());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getLongitude())) {
				deliveryHeaderDTO.setLongitude(deliveryHeaderDo.getLongitude());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getLatitude())) {
				deliveryHeaderDTO.setLatitude(deliveryHeaderDo.getLatitude());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getStorageLocation())) {
				deliveryHeaderDTO.setStorageLocation(deliveryHeaderDo.getStorageLocation());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getTelephone())) {
				deliveryHeaderDTO.setTelephone(deliveryHeaderDo.getTelephone());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getTripped())) {
				deliveryHeaderDTO.setTripped(deliveryHeaderDo.getTripped());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getCreatedDate())) {
				deliveryHeaderDTO.setCreatedDate(deliveryHeaderDo.getCreatedDate());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getRefNo())) {
				deliveryHeaderDTO.setRefNo(deliveryHeaderDo.getRefNo());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getStatus())) {
				deliveryHeaderDTO.setStatus(deliveryHeaderDo.getStatus());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getStartedAt())) {
				deliveryHeaderDTO.setStartedAt(deliveryHeaderDo.getStartedAt());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getEndedAt())) {
				deliveryHeaderDTO.setEndedAt(deliveryHeaderDo.getEndedAt());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getReasonForCancellation())) {
				deliveryHeaderDTO.setReasonForCancellation(deliveryHeaderDo.getReasonForCancellation());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getSignatureDocId())) {
				deliveryHeaderDTO.setSignatureDocId(deliveryHeaderDo.getSignatureDocId());
			}

			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getInvalidateIds())) {

				deliveryHeaderDTO.setInvalidateIds(deliveryHeaderDo.getInvalidateIds());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getDeliveryOrder())) {
				deliveryHeaderDTO.setDeliveryOrder(deliveryHeaderDo.getDeliveryOrder());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getRating())) {
				deliveryHeaderDTO.setRating(deliveryHeaderDo.getRating());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getReceiverName())) {
				deliveryHeaderDTO.setReceiverName(deliveryHeaderDo.getReceiverName());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getCreatedAt())) {
				deliveryHeaderDTO.setCreatedAt(deliveryHeaderDo.getCreatedAt());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getUpdatedAt())) {
				deliveryHeaderDTO.setUpdatedAt(deliveryHeaderDo.getUpdatedAt());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getAssignedUser())) {
				deliveryHeaderDTO.setAssignedUser(deliveryHeaderDo.getAssignedUser());
			}
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getWareHouseDetails())) {
				deliveryHeaderDTO
						.setWareHouseDetails(wareHouseDetailDao.exportDto(deliveryHeaderDo.getWareHouseDetails()));

			}
			// exporting delivery items
			if (!ServicesUtil.isEmpty(deliveryHeaderDo.getDeliveryItems())) {
				List<DeliveryItemDTO> itemDtos = deliveryItemDAO.exportList(deliveryHeaderDo.getDeliveryItems());
				deliveryHeaderDTO.setDeliveryItems(itemDtos);
			}

		}
		return deliveryHeaderDTO;
	}

	/**
	 * Return all the delivery headers who's tripped is not planned till yet.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DeliveryHeaderDTO> findAllDeliveryHeaderDtos() {
		Criteria criteria = getSession().createCriteria(DeliveryHeaderDo.class);
		criteria.add(Restrictions.eq("tripped", false));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List<DeliveryHeaderDo> resultDos = criteria.list();
		List<DeliveryHeaderDTO> resultDtos = exportList(resultDos);
		return resultDtos;
	}

	/**
	 * Return the count of delivery notes by as per their status
	 * 
	 */

	@SuppressWarnings("unchecked")
	public List<Object> getDeliveryNoteCountAsPerStatus() {
		String hql = "SELECT status,COUNT(deliveryNoteId) AS count FROM DeliveryHeaderDo where tripped = true GROUP BY status";
		Query query = getSession().createQuery(hql);
		List<Object> results = query.list();
		return results;
	}

	/*
	 * count delivery header's status
	 * 
	 */
	public Map<String, Long> getHeaderCounts(List<DeliveryHeaderDo> headerList) {
		Map<String, Long> map = new TreeMap<>();
		Long count = null;
		for (DeliveryHeaderDo dos : headerList) {
			count = map.get(dos.getStatus());
			if (ServicesUtil.isEmpty(count))
				count = 1L;
			else
				count++;
			map.put(dos.getStatus(), count);
		}
		return map;
	}

	private boolean checkDnStatus(String status) throws InvalidInputFault {

		if (DeliveryNoteStatus.DELIVERY_NOTE_CREATED.getValue().equals(status)
				|| DeliveryNoteStatus.DRIVER_DN_INVALIDATED.getValue().equals(status)
				|| DeliveryNoteStatus.ADMIN_DN_INVALIDATED.getValue().equals(status)
				|| DeliveryNoteStatus.RFC_DN_INVALIDATED.getValue().equals(status)
				|| DeliveryNoteStatus.DELIVERY_NOTE_PARTIALLY_REJECTED.getValue().equals(status)
				|| DeliveryNoteStatus.DELIVERY_NOTE_REJECTED.getValue().equals(status)
				|| DeliveryNoteStatus.DELIVERY_NOTE_COMPLETED.getValue().equals(status)
				|| DeliveryNoteStatus.DELIVERY_NOTE_STARTED.getValue().equals(status)
				|| DeliveryNoteStatus.DELIVERY_NOTE_VALIDATED.getValue().equals(status)) {
			return true;
		} else {
			throw new InvalidInputFault("delivery_note status '" + status + "' is invalid status code");
		}
	}

	@SuppressWarnings("unchecked")
	public List<DeliveryHeaderDTO> getAllDeliveryNoteByAdminsWareHouse(String adminId, String roleName,
			Set<WareHouseDetailsDTO> wareHouseDetails) {
		List<String> wareHouseIds = new ArrayList<String>();
		for (WareHouseDetailsDTO wareHouse : wareHouseDetails)
			wareHouseIds.add(wareHouse.getWareHouseId());
		boolean isSuperAdmin = false;
		String hql = "";
		// get all the user list if role is super_admin or sales_admin
		if (roleName.equals(RoleConstant.SUPER_ADMIN.getValue())
				|| roleName.equals(RoleConstant.SALES_ADMIN.getValue())) {
			hql = "SELECT d FROM DeliveryHeaderDo AS d WHERE d.tripped = false AND d.status != 'rfc_del_note_invalidated' ORDER BY d.createdAt desc";
			isSuperAdmin = true;
		} else
			hql = "SELECT d FROM DeliveryHeaderDo AS d inner join d.wareHouseDetails w WHERE d.tripped = false  AND d.status != 'rfc_del_note_invalidated' AND w.wareHouseId IN (:warehouselist) ORDER BY d.createdAt desc";
		Query query = getSession().createQuery(hql);
		query.setFirstResult(PaginationUtil.FIRST_RESULT);
		query.setMaxResults(PaginationUtil.MAX_RESULT);
		if (!isSuperAdmin) {
			// send no data on if warehouse if is empty
			if (ServicesUtil.isEmpty(wareHouseIds))
				return new ArrayList<DeliveryHeaderDTO>();

			query.setParameterList("warehouselist", wareHouseIds);
		}

		ArrayList<DeliveryHeaderDo> result = (ArrayList<DeliveryHeaderDo>) query.list();
		return exportList(result);

	}
	
	
   public boolean removeTripDeliveryNoteMapping(DeliveryHeaderDTO dto){
	   String sql = "DELETE FROM TRIP_DELIVERY_HEADER_MAPPING WHERE DELIVERY_NOTE_ID = "+dto.getDeliveryNoteId();
	   SQLQuery query = getSession().createSQLQuery(sql);
	   query.executeUpdate();
	   return false;
   }

@SuppressWarnings("unchecked")
public Object getDeliveryNoteByStatus(String userId, String roleName, Set<WareHouseDetailsDTO> wareHouseDetails, String deliveryNoteStatus) {
	List<String> wareHouseIds = new ArrayList<String>();
	for (WareHouseDetailsDTO wareHouse : wareHouseDetails)
		wareHouseIds.add(wareHouse.getWareHouseId());
	boolean isSuperAdmin = false;
	String hql = "";
	// get all the user list if role is super_admin or sales_admin
	if (roleName.equals(RoleConstant.SUPER_ADMIN.getValue())
			|| roleName.equals(RoleConstant.SALES_ADMIN.getValue())) {
		hql = "SELECT d FROM DeliveryHeaderDo AS d WHERE d.status = :status  ORDER BY d.createdAt desc";
		isSuperAdmin = true;
	} else
		hql = "SELECT d FROM DeliveryHeaderDo AS d inner join d.wareHouseDetails w WHERE d.status = :status AND w.wareHouseId IN (:warehouselist) ORDER BY d.createdAt desc";
	Query query = getSession().createQuery(hql);
	query.setParameter("status", deliveryNoteStatus);
	if (!isSuperAdmin) {
		// send no data on if warehouse if is empty
		if (ServicesUtil.isEmpty(wareHouseIds))
			return new ArrayList<DeliveryHeaderDTO>();

		query.setParameterList("warehouselist", wareHouseIds);
	}

	ArrayList<DeliveryHeaderDo> result = (ArrayList<DeliveryHeaderDo>) query.list();
	return exportList(result);
}

}
