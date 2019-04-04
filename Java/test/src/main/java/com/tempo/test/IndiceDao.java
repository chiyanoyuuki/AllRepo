package com.tempo.test;

import java.util.List;

public interface IndiceDao 
{
	public List<Indice> getIndices();
	public List<IndiceVal> getIndicesVals(int ID);
	public List<IndiceVal> getIndicesNewVals(int ID, String DATE);
}
