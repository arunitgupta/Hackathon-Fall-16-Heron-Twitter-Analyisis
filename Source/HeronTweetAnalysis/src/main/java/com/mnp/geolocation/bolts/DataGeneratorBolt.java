package com.mnp.geolocation.bolts;

import java.util.Map;

import com.mnp.herontweets.tools.DataGeneratorGeo;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class DataGeneratorBolt extends BaseRichBolt {
	private OutputCollector collector;
	private DataGeneratorGeo g = new DataGeneratorGeo();
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
	}

	public void execute(Tuple tuple) {
		// TODO Auto-generated method stub
		Double lat = (Double) tuple.getValue(0);
		Double lng = (Double) tuple.getValue(1);
		String s = "{lat: "+lat+", lng:"+lng+"}";
		g.dataGenerator(s);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
//		declarer.declare(new Fields("message","source"));
	}
}
