package yewu.shopping;

import java.math.BigDecimal;

public class HalveItem {
	/**
	 * 唯一key
	 */
	private String key;
	/**
	 * 单价
	 */
	private BigDecimal price;
	/**
	 * 数量
	 */
	private long quantity;	
	/**
	 * 数量的bigDecimal格式
	 */
	private BigDecimal quantityBig;
	/**
	 * 平摊均价
	 */
	private BigDecimal halveAveragePrice = BigDecimal.ZERO;
	/**
	 * 平摊价格
	 */
	private BigDecimal halvePrice = BigDecimal.ZERO;
	
	public HalveItem(String key, BigDecimal price, long quantity) {
		super();
		this.key = key;
		this.price = price;
		this.quantity = quantity;
		this.quantityBig = new BigDecimal(quantity);
	}
	public String getKey() {
		return key;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public long getQuantity() {
		return quantity;
	}
	public BigDecimal getQuantityBig() {
		return quantityBig;
	}
	public BigDecimal getHalveAveragePrice() {
		return halveAveragePrice;
	}
	public void setHalveAveragePrice(BigDecimal halveAveragePrice) {
		this.halveAveragePrice = halveAveragePrice;
	}
	public BigDecimal getHalvePrice() {
		return halvePrice;
	}
	public void setHalvePrice(BigDecimal halvePrice) {
		this.halvePrice = halvePrice;
	}
	@Override
	public String toString() {
		return "HalveItem [key=" + key + ", price=" + price + ", quantity="
				+ quantity + ", quantityBig=" + quantityBig
				+ ", halveAveragePrice=" + halveAveragePrice + ", halvePrice="
				+ halvePrice + "]";
	}
	
	

}
