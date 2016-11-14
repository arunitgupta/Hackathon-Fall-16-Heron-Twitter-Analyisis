package com.mnp.twittersource.bolts;

import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.twitter.Extractor;

public class ExtractHashtagsBolt extends BaseRichBolt {

	private static final long serialVersionUID = 5537727428628598519L;
	private OutputCollector collector;

	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
	}

	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub
		String tweetText = (String)tuple.getValue(0);
		String tweetSource = tuple.getValue(1).toString();
		Extractor extractor = new Extractor();
		List<String> hashtags = extractor.extractHashtags(tweetText);	
		for(String hashtag: hashtags) {
			collector.emit(tuple, new Values(hashtag,tweetSource));
        }
		collector.ack(tuple);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("hashtag","source"));
	}

}
