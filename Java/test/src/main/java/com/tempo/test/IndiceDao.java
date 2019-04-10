package com.tempo.test;

import java.util.List;

public interface IndiceDao 
{
	public List<Indice> getIndices();
	public List<IndiceVal> getIndicesVals(int ID, String TIME);
	public List<IndiceVal> getIndicesNewVals(int ID, String DATE);
	public List<String> getIndicesTotal(int ID);
}
