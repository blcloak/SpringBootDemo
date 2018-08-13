package com.onekeyweb.dao;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class CarScore {
	@PrimaryKey
	private String carId;
	private String name;  //车型名
	private String model;  //车型code
	
	private Long price;  //车价(按车价差公式计算结果降权即可，不作为硬性索引条件)
	
	@SecondaryKey(relate = Relationship.MANY_TO_ONE) private String brand;   //品牌
	@SecondaryKey(relate = Relationship.MANY_TO_ONE) private String series;  //车系
	private Integer year;  //年限(上牌年份)
	@SecondaryKey(relate = Relationship.MANY_TO_ONE) private String carType;  //车形
	@SecondaryKey(relate = Relationship.MANY_TO_ONE) private String color;	//颜色
	@SecondaryKey(relate = Relationship.MANY_TO_ONE) private String capacity;  //排量
	@SecondaryKey(relate = Relationship.MANY_TO_ONE) private Long miles;   //里程
	@SecondaryKey(relate = Relationship.MANY_TO_ONE) private String geerbox;  //变速箱
	@SecondaryKey(relate = Relationship.MANY_TO_ONE) private String country;  //国别
	
	//
	private String status;  //状态
	private String level;  //车辆等级
	private String upShelfDate;  //第一次上架时间
	private String licenseDate;  //上牌时间
	private Double score;
	
	public CarScore(){}
	
	//getter & setter method....

}
