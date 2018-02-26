package com.albertodepaola.logparser.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class SalesLogEntry extends LogEntry {

	private Long salesId;
	private String salesItemList;
	private String salesmanName;
	private List<SaleDetail> saleDetails;
	private MathContext mc = MathContext.DECIMAL64;
	
	private BigDecimal saleAmount;

	public LOG_ENTRY_TYPE getType() {
		return LOG_ENTRY_TYPE.SALES;
		
	}

	public Long getSalesId() {
		return salesId;
	}

	public void setSalesId(Long salesId) {
		this.salesId = salesId;
	}

	public String getSalesItemList() {
		return salesItemList;
	}

	public void setSalesItemList(String salesItemList) {
		this.salesItemList = salesItemList;
	}

	public String getSalesmanName() {
		return salesmanName;
	}

	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	public List<SaleDetail> getSaleDetails() {
		return saleDetails;
	}

	public void setSaleDetails(List<SaleDetail> saleDetails) {
		this.saleDetails = saleDetails;
	}
	
	public BigDecimal getSaleAmount() {
		if(saleDetails == null) {
			processSalesItems();
		}
		
		return saleAmount;
	}

	private void processSalesItems() {
		
		saleDetails = new ArrayList<>();
		
		String[] salesList = salesItemList.replaceAll("(\\[|\\])", "").split(",");
		this.saleAmount = BigDecimal.ZERO;
		
		for (String string : salesList) {
			String[] saleDetail = string.split("-");
			
			Long id = Long.valueOf(saleDetail[0]);
			// TODO colocar em parametros
			
//			BigDecimal quantity = new BigDecimal(saleDetail[1], mc).setScale(2, mc.getRoundingMode());
//			BigDecimal price = new BigDecimal(saleDetail[2], mc).setScale(2, mc.getRoundingMode());
			BigDecimal quantity = new BigDecimal(saleDetail[1], mc);
			BigDecimal price = new BigDecimal(saleDetail[2], mc);
			
			SaleDetail sd = new SaleDetail(id, quantity, price);
			saleDetails.add(sd);
			
			saleAmount = saleAmount.add(quantity.multiply(price));
			
		}
		saleAmount = saleAmount.setScale(4, mc.getRoundingMode());
	}

	public class SaleDetail {
		
		private Long itemId;
		private BigDecimal quantity;
		private BigDecimal price;

		public SaleDetail() {
			
		}

		public SaleDetail(Long itemId, BigDecimal quantity, BigDecimal price) {
			super();
			this.itemId = itemId;
			this.quantity = quantity;
			this.price = price;
		}
		
		public Long getItemId() {
			return itemId;
		}

		public void setItemId(Long itemId) {
			this.itemId = itemId;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}
	}

}
