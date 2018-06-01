package com.incture.metrodata.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.incture.metrodata.dto.DeliveryItemDTO;
import com.incture.metrodata.entity.DeliveryItemDo;
import com.incture.metrodata.util.ServicesUtil;

@Repository("DeliveryItemDAO")
public class DeliveryItemDAO extends BaseDao<DeliveryItemDo, DeliveryItemDTO> {

	@Override
	DeliveryItemDTO exportDto(DeliveryItemDo deliveryItemDo) {

		DeliveryItemDTO deliveryItemDTO = new DeliveryItemDTO();
		if (!ServicesUtil.isEmpty(deliveryItemDo)) {
			if (!ServicesUtil.isEmpty(deliveryItemDo.getDeliveryItemId())) {
				deliveryItemDTO.setDeliveryItemId(deliveryItemDo.getDeliveryItemId());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDo.getBatch())) {
				deliveryItemDTO.setBatch(deliveryItemDo.getBatch());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDo.getMaterial())) {
				deliveryItemDTO.setMaterial(deliveryItemDo.getMaterial());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDo.getQuantity())) {
				deliveryItemDTO.setQuantity(deliveryItemDo.getQuantity());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDo.getSerialNum())) {
				deliveryItemDTO.setSerialNum(deliveryItemDo.getSerialNum());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDo.getVolume())) {
				deliveryItemDTO.setVolume(deliveryItemDo.getVolume());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDo.getDeliveryItemId())) {
				deliveryItemDTO.setDeliveryItemId(deliveryItemDo.getDeliveryItemId());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDo.getDescription())) {
				deliveryItemDTO.setDescription(deliveryItemDo.getDescription());
			}
			

		}
		return deliveryItemDTO;
	}

	@Override
	DeliveryItemDo importDto(DeliveryItemDTO deliveryItemDTO, DeliveryItemDo deliveryItemDo) {

		if (ServicesUtil.isEmpty(deliveryItemDo))
			deliveryItemDo = new DeliveryItemDo();
		if (!ServicesUtil.isEmpty(deliveryItemDTO)) {
			if (!ServicesUtil.isEmpty(deliveryItemDTO.getDeliveryItemId())) {
				deliveryItemDo.setDeliveryItemId(deliveryItemDTO.getDeliveryItemId());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDTO.getBatch())) {
				deliveryItemDo.setBatch(deliveryItemDTO.getBatch());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDTO.getMaterial())) {
				deliveryItemDo.setMaterial(deliveryItemDTO.getMaterial());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDTO.getQuantity())) {
				deliveryItemDo.setQuantity(deliveryItemDTO.getQuantity());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDTO.getSerialNum())) {
				deliveryItemDo.setSerialNum(deliveryItemDTO.getSerialNum());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDTO.getVolume())) {
				deliveryItemDo.setVolume(deliveryItemDTO.getVolume());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDTO.getDeliveryItemId())) {
				deliveryItemDo.setDeliveryItemId(deliveryItemDTO.getDeliveryItemId());
			}
			if (!ServicesUtil.isEmpty(deliveryItemDTO.getDescription())) {
				deliveryItemDo.setDescription(deliveryItemDTO.getDescription());
			}

		}
		return deliveryItemDo;
	}
	
	public int deleteUnlinkDeliveryItems(){
		
		String sql = "DELETE FROM delivery_item WHERE delivery_item_id "
				     + " NOT IN "
				     + "(SELECT DELIVERYITEMS_DELIVERY_ITEM_ID FROM DELIVERY_HEADER_DELIVERY_ITEM )";
		Query query =  getSession().createSQLQuery(sql);
		int rowAffected = query.executeUpdate();
		return rowAffected;
	}
}
