package com.mnp.geolocation;

import java.util.LinkedList;

import com.google.common.collect.ImmutableList;
import com.mnp.geolocation.bolts.DataGeneratorBolt;
import com.mnp.geolocation.spouts.TwitterGeolocationCount;
import com.mnp.herontweets.tools.DataGeneratorGeo;
import com.mnp.herontweets.tools.DataGeneratorSource;
import com.mnp.herontweets.tools.RankableObjectWithFields;
import com.mnp.herontweets.tools.Rankings;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class TwitterGeolocationTopology {
	 public static void main(String[] args) throws Exception {
		    TopologyBuilder builder = new TopologyBuilder();
		    String twitterSpoutId = "twitterSpout";
	        String dataExtractorId = "messageExtractor";
	        builder.setSpout(twitterSpoutId, 
		           new TwitterGeolocationCount("PqceoBRBOGvZV0VTVVMpNDFEi",
		                 "o7KrWtRtMvrbTPXW2uzaRiblOze1J4RCPbQAHElWzDzevr3EzL",
		                 "796595630-EHolo0o8su2CIATVvo63MIrtHLi7njoNo5XDgSHp",
		                 "urBbRVEa55IrcA3QWxNsArvFqCiVuHtE8UknE7qmK3bWU"), 1);

		    builder.setBolt(dataExtractorId, new DataGeneratorBolt(), 4).shuffleGrouping(twitterSpoutId);
	        
		    Config conf = new Config();
		    conf.registerSerialization(DataGeneratorGeo.class);
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
