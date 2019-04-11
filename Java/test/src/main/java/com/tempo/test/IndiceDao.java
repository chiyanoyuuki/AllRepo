package com.tempo.test;

import java.util.List;

public interface IndiceDao 
{
	public List<Indice> getIndices(String type);
	public List<IndiceVal> getIndicesVals(int ID, String TIME,String type);
	public List<IndiceVal> getIndicesNewVals(int ID, String DATE,String type);
	public List<String> getIndicesTotal(int ID,String type);
}
