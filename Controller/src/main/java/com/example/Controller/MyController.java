package com.example.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by raymondvargas on 9/4/17.
 */
@RestController
@RefreshScope
 class MyController {

	private final String value ;
	private final boolean featureXFlag;


	@Autowired
	public MyController(@Value("${message}") String value, @Value("${myawesomefeature.flag}") boolean featureXFlag){
		this.value=value;
		this.featureXFlag = featureXFlag;
	}


	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public String myEndpoint(){
		return value;
	}

	@RequestMapping(value="/x/flag", method = RequestMethod.GET)
	public boolean featureFlagValueCheck(){
		return featureXFlag;
	}

	@RequestMapping(value="/x", method = RequestMethod.GET)
	public String featureFlagCheck(){
		if (featureXFlag)
			return "The feature is now available!";

		else if (!featureXFlag)
			return"The feature is not yet ready for reveal, and is hidden!";

		return "Couldn't determine state of feature flag";
	}

}
