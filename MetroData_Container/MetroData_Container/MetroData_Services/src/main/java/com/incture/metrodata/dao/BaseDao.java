package com.incture.metrodata.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;

import com.incture.metrodata.constant.Message;
import com.incture.metrodata.dto.BaseDto;
import com.incture.metrodata.entity.BaseDo;
import com.incture.metrodata.exceptions.ExecutionFault;
import com.incture.metrodata.exceptions.InvalidInputFault;
import com.incture.metrodata.exceptions.NoResultFault;
import com.incture.metrodata.util.DB_Operation;
import com.incture.metrodata.util.ServicesUtil;

public abstract class BaseDao<E extends BaseDo, D extends BaseDto> {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	// Persist
	public void persist(E entity) {
		getSession().saveOrUpdate(entity);
	}

	// Update
	public void update(E entity) {
		getSession().merge(entity);
	}

	public void deleteById(D dto) throws Exception {
		E e = getByKeysForFK(dto);
		if(ServicesUtil.isEmpty(e))
			throw new InvalidInputFault("No record found");
		delete(e);
	}

	// delete
	public void delete(E entity) {
		getSession().delete(entity);
	}

	// Import Generic
	public E importDto(DB_Operation op, D dto,E dos) throws Exception {
		dto.validate(op);
		return importDto(dto, dos);
	}

	// Export Generic
	public D exportDto(DB_Operation op, E dos) throws Exception {
		// TODO Auto-generated method stub
		return exportDto(dos);
	}

	abstract D exportDto(E dos);

	abstract E importDto(D dto,E dos) throws InvalidInputFault, ExecutionFault, NoResultFault,Exception;


	// Create
	public D create(D dto,E dos) throws Exception {
		// persisting the dto
		E e = importDto(DB_Operation.CREATE, dto,dos);
		persist(e);
		return exportDto(e);
	}

	// Find
	public D getByKeys(D dto) throws Exception {
		return exportDto(getByKeysForFK(dto));
	}

	public E getByKeysForFK(D dto) throws Exception {
		return find(importDto(DB_Operation.RETRIVE, dto,null));
	}

	public D findById(D dto) throws Exception {
		return exportDto(find(importDto(DB_Operation.RETRIVE, dto,null)));
	}

	@SuppressWarnings("unchecked")
	protected E find(E pojo) throws Exception {
		E result = null;
		result = (E) getSession().get(pojo.getClass(), (Serializable) pojo.getPrimaryKey());

		if (ServicesUtil.isEmpty(result))
			throw new InvalidInputFault(Message.NO_RECORD_FOUND.getValue());

		return result;
	}

	// FindAll
	@SuppressWarnings("unchecked")
	public List<D> findAll(D Dto) throws Exception {
		
		Criteria criteria = getSession().createCriteria(importDto(DB_Operation.RETRIVE, Dto,null).getClass());
		criteria.addOrder(Order.desc("createdAt"));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		List<E> listDo = (List<E>) criteria.list();
		List<D> outDtos = exportList(listDo);
		return outDtos;
	}

	// UPdate
	public D update(D dto) throws Exception {
		E dos =getByKeysForFK(dto);
		update(importDto(DB_Operation.UPDATE, dto,dos));
	   return	exportDto(dos);
		
	}

	// Delete
	public void deleteDto(D dto) throws Exception {
		delete(getByKeysForFK(dto));
	}

	public List<D> exportList(List<E> eList) {
		List<D> list = new ArrayList<D>();
		for (E e1 : eList) {
			list.add(exportDto(e1));
		}
		return list;
	}

	public Set<D> exportSet(Set<E> eList, Comparator<D> comparator) {
		Set<D> list = new TreeSet<D>(comparator);
		for (E e1 : eList) {
			list.add(exportDto(e1));
		}
		return list;
	}
	
	public List<E> importList(List<D> dList,List<E> eList) throws Exception {
		List<E> list = new ArrayList<E>();
		for (D d : dList) {
			E dos = null;
			try {
				//dos = getByKeysForFK(d);
				for(E e : eList){
				  if( d.getPrimaryKey().equals(e.getPrimaryKey())){
					  dos = e;
					  break;
				  }
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			list.add(importDto(d,dos));
		}
		return list;
	}
	
	public Set<E> importSet(Set<D> dList, Set<E> eList) throws Exception {
		Set<E> list = new HashSet<E>();
		for (D d : dList) {
			E dos = null;
			try {
				//dos = getByKeysForFK(d);
				for(E e : eList){
					  if( d.getPrimaryKey().equals(e.getPrimaryKey())){
						  dos = e;
						  break;
					  }
					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
			list.add(importDto(d,dos));
		}
		return list;
	}

}
