package com.mnp.TwitterTrends.spouts;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSampleSpout extends BaseRichSpout{
	SpoutOutputCollector _collector;
	LinkedBlockingQueue<Status> queue = null;
	TwitterStream _twitterStream;
	String _custkey;
	String _custsecret;
	String _accesstoken;
	String _accesssecret;

	public TwitterSampleSpout(String key, String secret) {
		_custkey = key;
		_custsecret = secret;
	}

	public TwitterSampleSpout(String key, String secret, String token, String tokensecret) {
		_custkey = key;
		_custsecret = secret;
		_accesstoken = token;
		_accesssecret = tokensecret;
	}

	public void nextTuple() {
		// TODO Auto-generated method stub
		Status ret = queue.poll();
        if(ret==null) {
            Utils.sleep(50);
        } else {
            _collector.emit(new Values(ret.getText()));
        }
		
	}

	public void open(Map<String, Object> conf, TopologyContext context, SpoutOutputCollector collector) {
		// TODO Auto-generated method stub
		 queue = new LinkedBlockingQueue<Status>(1000);
	        _collector = collector;

	        StatusListener listener = new StatusListener() {
				
				public void onException(Exception ex) {
					// TODO Auto-generated method stub
					
				}
				
				public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
					// TODO Auto-generated method stub
					
				}
				
				public void onStatus(Status status) {
					// TODO Auto-generated method stub
					queue.offer(status);
				}
				
				public void onStallWarning(StallWarning warning) {
					// TODO Auto-generated method stub
					
				}
				
				public void onScrubGeo(long userId, long upToStatusId) {
					// TODO Auto-generated method stub
					
				}
				
				public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
					// TODO Auto-generated method stub
					
				}
			}; 
			 ConfigurationBuilder config = 
			            new ConfigurationBuilder()
			                 .setOAuthConsumerKey(_custkey)
			                 .setOAuthConsumerSecret(_custsecret)
			                 .setOAuthAccessToken(_accesstoken)
			                 .setOAuthAccessTokenSecret(_accesssecret);

			        TwitterStreamFactory fact = 
			            new TwitterStreamFactory(config.build());

			        _twitterStream = fact.getInstance();
			        _twitterStream.addListener(listener);
			        _twitterStream.sample();
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub
		_twitterStream.shutdown();
	}
	
	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		  Config ret = new Config();
	        ret.setMaxTaskParallelism(1);
	        return ret;
	}
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		 declarer.declare(new Fields("tweet"));
	}
}
