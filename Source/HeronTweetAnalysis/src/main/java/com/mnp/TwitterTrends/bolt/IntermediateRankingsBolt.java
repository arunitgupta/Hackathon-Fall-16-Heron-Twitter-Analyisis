package com.mnp.TwitterTrends.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.google.common.collect.ImmutableList;
import com.mnp.herontweets.tools.Rankable;
import com.mnp.herontweets.tools.RankableObjectWithFields;
import com.mnp.herontweets.tools.Rankings;


/**
 * This bolt ranks incoming objects by their count.
 * 
 * It assumes the input tuples to adhere to the following format: (object, object_count, additionalField1,
 * additionalField2, ..., additionalFieldN).
 * 
 */
public final class IntermediateRankingsBolt extends AbstractRankerBolt {

    private static final long serialVersionUID = -1369800530256637409L;
   
    public IntermediateRankingsBolt() {
        super();
    }

    public IntermediateRankingsBolt(int topN) {
        super(topN);
    }

    public IntermediateRankingsBolt(int topN, int emitFrequencyInSeconds) {
        super(topN, emitFrequencyInSeconds);
    }

    @Override
    void updateRankingsWithTuple(Tuple tuple) {
        Rankable rankable = RankableObjectWithFields.from(tuple);
        super.getRankings().updateWith(rankable);
    }

    
    @Override
    public void emitRankings(BasicOutputCollector collector) {
    	collector.emit(new Values(getClonedRankings()));
    	
    }
    
    private Rankings getClonedRankings() {
    	Rankings rankings = getRankings();
    	Rankings clonedRankings = new Rankings(rankings.maxSize(), rankings.getRankings());
    	return clonedRankings;
    }
}
