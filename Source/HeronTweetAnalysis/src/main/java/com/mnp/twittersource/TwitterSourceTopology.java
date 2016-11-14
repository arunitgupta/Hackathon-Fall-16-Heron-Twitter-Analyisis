package com.mnp.twittersource;

import java.util.LinkedList;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.mnp.twittersource.bolts.ExtractHashtagsBolt;
import com.mnp.twittersource.bolts.ExtractMessageBolt;
import com.mnp.twittersource.bolts.IntermediateRankingsBolt;
import com.mnp.twittersource.bolts.RollingCountBolt;
import com.mnp.twittersource.bolts.TotalRankingsBolt;
import com.mnp.twittersource.spouts.TwitterSampleSpout;
import com.mnp.herontweets.tools.DataGeneratorSource;
import com.mnp.herontweets.tools.RankableObjectWithFields;
import com.mnp.herontweets.tools.Rankings;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class TwitterSourceTopology {
	private static final int DEFAULT_RUNTIME_IN_SECONDS = 600;
    private static final int TOP_N = 10;
	
	/* public static class StdoutBolt extends BaseRichBolt {
		    OutputCollector _collector;
		    String taskName;

			public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
				// TODO Auto-generated method stub
				  _collector = collector;
			      taskName = context.getThisComponentId() + "_" + context.getThisTaskId();
			}

			public void declareOutputFields(OutputFieldsDeclarer declarer) {
				// TODO Auto-generated method stub
			     declarer.declare(new Fields("word"));
			}

			public void execute(Tuple tuple) {
				// TODO Auto-generated method stub
				 System.out.println(tuple.getValue(0));
			      _collector.emit(tuple, new Values(tuple.getValue(0)));
			      _collector.ack(tuple);
			}
		  }*/
	 public static void main(String[] args) throws Exception {
		    TopologyBuilder builder = new TopologyBuilder();
		    String twitterSpoutId = "twitterSpout";
	        String messageExtractorId = "messageExtractor";
	        String hashtagsExtractorId = "hashtagsExtractor";
	        String counterId = "counter";
	        String intermediateRankerId = "intermediateRanker";
	        String totalRankerId = "totalRanker";
		    builder.setSpout(twitterSpoutId, 
		           new TwitterSampleSpout("PqceoBRBOGvZV0VTVVMpNDFEi",
		                 "o7KrWtRtMvrbTPXW2uzaRiblOze1J4RCPbQAHElWzDzevr3EzL",
		                 "796595630-EHolo0o8su2CIATVvo63MIrtHLi7njoNo5XDgSHp",
		                 "urBbRVEa55IrcA3QWxNsArvFqCiVuHtE8UknE7qmK3bWU"), 1);

		    builder.setBolt(messageExtractorId, new ExtractMessageBolt(), 4).shuffleGrouping(twitterSpoutId);
	        builder.setBolt(hashtagsExtractorId, new ExtractHashtagsBolt(), 4).shuffleGrouping(messageExtractorId);
	        builder.setBolt(counterId, new RollingCountBolt(300, 1), 4).fieldsGrouping(hashtagsExtractorId, new Fields("hashtag"));
	        builder.setBolt(intermediateRankerId, new IntermediateRankingsBolt(TOP_N), 4).fieldsGrouping(counterId,
	            new Fields("obj"));
	        builder.setBolt(totalRankerId, new TotalRankingsBolt(TOP_N)).globalGrouping(intermediateRankerId);

		    Config conf = new Config();
		    conf.registerSerialization(Rankings.class);
		    conf.registerSerialization(RankableObjectWithFields.class);
		    conf.registerSerialization(LinkedList.class);
		    conf.registerSerialization(ImmutableList.class);
		    conf.registerSerialization(DataGeneratorSource.class);
		    conf.setDebug(true);
		    System.out.println(args.length);
		    if (args != null && args.length > 0) {
		      conf.setNumWorkers(3);
		      StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		    }
		    else {
		      LocalCluster cluster = new LocalCluster();
		      cluster.submitTopology("Tweetsource", conf, builder.createTopology());
		    }
		  }
}
