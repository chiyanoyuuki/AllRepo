package com.tempo.managerImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tempo.bean.Indice;
import com.tempo.dao.IndiceDao;
import com.tempo.manager.IndiceManager;

@Service ("indiceManager")
public class IndiceManagerImpl implements IndiceManager
{
	@Autowired
	private IndiceDao indiceDao;
	
	public List<Indice> getIndices(){return indiceDao.getIndices();}
}
