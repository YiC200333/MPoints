package me.yic.mpoints.utils;

import me.yic.mpoints.data.DataFormat;
import me.yic.mpoints.data.caches.PointsCache;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

public class Points {
	private final String sign;
	private final BigDecimal initialbal;
	private final Boolean enablebaltop;
	private final Boolean allowpay;
	private final Boolean enablebc;
	private final String singularname;
	private final String pluralname;
	private final Boolean integerbal;
	private final DecimalFormat decimalFormat;
	private final String displayformat;
	private final BigDecimal maxnumber;

	public Points(String sign,String initialbal,Boolean enablebaltop,Boolean allowpay,Boolean enablebc,
				  String singularname,String pluralname,
				  Boolean integerbal,String separator,String displayformat,String maxnumber) {
		this.sign = sign;
		this.initialbal = formatString(integerbal,initialbal);
		this.enablebaltop = enablebaltop;
		this.allowpay = allowpay;
		this.enablebc = enablebc;
		this.singularname = singularname;
		this.pluralname = pluralname;
		this.integerbal = integerbal;
		this.decimalFormat = DataFormat.setformat(integerbal,separator);
		this.displayformat = displayformat;
		this.maxnumber = DataFormat.setmaxnumber(maxnumber);
	}

	private BigDecimal formatString(Boolean isInteger ,String am) {
		BigDecimal bigDecimal = new BigDecimal(am);
		if (isInteger) {
			return bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
		} else {
			return bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
		}
	}

	public String getsign() {
		return sign;
	}

	public BigDecimal getinitialbal() {
		return initialbal;
	}

	public Boolean getenablebaltop() {
		return enablebaltop;
	}

	public Boolean getallowpay() {
		return allowpay;
	}

	public Boolean getenablebc() {
		return enablebc;
	}

	public String getsingularname() {
		return singularname;
	}

	public String getpluralname() {
		return pluralname;
	}

	public Boolean getintegerbal() {
		return integerbal;
	}

	public DecimalFormat getdecimalFormat() {
		return decimalFormat;
	}

	public String getdisplayformat() {
		return displayformat;
	}

	public BigDecimal getmaxnumber() {
		return maxnumber;
	}

}
