package yewu.shopping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平摊金额算法
 * 
 * <P>File name : SharePriceCalc.java </P>
 */
public class SharePriceCalc {
	/**
	 * 平摊所有类型 
	 */
	public static final int  SHARE_ALL = 0;
	/**
	 * 平摊到价格
	 */
	public static final int  SHARE_HALVE = 1;
	/**
	 * 平摊到均价
	 */
	public static final int  SHARE_HALVE_AVERAGE = 2;
	/**
	 * 需要平摊的总价格
	 */
	private BigDecimal halveTotalPrice = BigDecimal.ZERO;
	/**
	 * 平摊价格的实体
	 */
	private List<HalveItem> halveItems = new ArrayList<HalveItem>();
	/**
	 * 平摊价格实体的总价
	 */
	private BigDecimal halveItemsTotalPrice = BigDecimal.ZERO;
	/**
	 * 平摊不均的价格
	 */
	private BigDecimal sparePrice = BigDecimal.ZERO;
	/**
	 * 所有平摊实体对照表
	 */
	private Map<String,HalveItem> halveItemMap = new HashMap<String,HalveItem>();
	/**
	 * 是否执行平摊价的操作
	 */
	private boolean is_halve = false;
	/**
	 * 是否执行平摊均价的操作
	 */
	private boolean is_halve_average = false;
	
	public SharePriceCalc(BigDecimal halveTotalPrice, List<HalveItem> halveItems, int sharestatus){
		this.halveTotalPrice = halveTotalPrice;
		this.halveItems = halveItems;
		for(HalveItem item : halveItems){
			halveItemsTotalPrice = halveItemsTotalPrice.add(item.getPrice().multiply(item.getQuantityBig()));
			halveItemMap.put(item.getKey(), item);
		}
		if(sharestatus == SHARE_ALL || sharestatus == SHARE_HALVE){
			is_halve = true;
		}else{
			is_halve_average = true;
		}
		share();
	}
	private  void share(){
		if(halveTotalPrice.compareTo(BigDecimal.ZERO) == 0||halveItemsTotalPrice.compareTo(BigDecimal.ZERO) == 0) return;
		int size = halveItems.size();
		//如果平摊到某个实体下
		if(is_halve){
			BigDecimal shengHalvePrice = halveTotalPrice;
			for(int i = 0 ; i < size ; i ++ ){
				HalveItem item = halveItems.get(i);
				if( i!= size -1 ) {
					//平摊金额
					BigDecimal halvePrice = item.getPrice().multiply(item.getQuantityBig()).multiply(halveTotalPrice).divide(halveItemsTotalPrice,2, BigDecimal.ROUND_HALF_UP);
					item.setHalvePrice(halvePrice);
					shengHalvePrice = shengHalvePrice.subtract(halvePrice);
				}else{
					item.setHalvePrice(shengHalvePrice);
				}
			}
		}
		//平摊到某个实体的均价上
		if(is_halve_average){
			BigDecimal haveHalvePrice = BigDecimal.ZERO;
			for(int i = 0 ; i < size ; i ++ ){
				HalveItem item = halveItems.get(i);
				//平摊代金券金额
				BigDecimal halveAveragePrice = item.getPrice().multiply(halveTotalPrice).divide(halveItemsTotalPrice,2, BigDecimal.ROUND_HALF_UP);
				item.setHalveAveragePrice(halveAveragePrice);
				haveHalvePrice = haveHalvePrice.add(halveAveragePrice.multiply(item.getQuantityBig()));
			}
			BigDecimal shengHalvePrice = halveTotalPrice.subtract(haveHalvePrice);
			boolean isSucc = false;
			if(shengHalvePrice.compareTo(BigDecimal.ZERO) != 0){
				for(int i = 0 ; i < size ; i ++){
					HalveItem item = halveItems.get(i);
					BigDecimal halvePrice = item.getHalveAveragePrice();
					if(shengHalvePrice.multiply(new BigDecimal(100)).abs().remainder(item.getQuantityBig()).compareTo(BigDecimal.ZERO) == 0){
						item.setHalveAveragePrice(halvePrice.add(shengHalvePrice.divide(item.getQuantityBig(),2, BigDecimal.ROUND_HALF_UP)));
						isSucc = true;
						break;
					}
				}
			}else{
				isSucc = true;
			}
			if(!isSucc){
				for(int i = 0 ; i < size ; i ++){
					HalveItem item = halveItems.get(i);
					BigDecimal halvePrice = item.getHalveAveragePrice();
					BigDecimal shengHalvePrice1 = shengHalvePrice.divide(new BigDecimal(item.getQuantity()),2, BigDecimal.ROUND_DOWN);
					if(shengHalvePrice1.abs().compareTo(new BigDecimal(0.00)) != 0){
						shengHalvePrice = shengHalvePrice.subtract(shengHalvePrice1.multiply(item.getQuantityBig()));
						item.setHalveAveragePrice(halvePrice.add(shengHalvePrice1));
					}else{
						break;
					}
				}
				sparePrice = shengHalvePrice;
			}
		}
	}
	
	
	/**
	 * 获取平摊不均的价格
	 * SharePriceCalc.getSparePrice()<BR>
	 * @return
	 */
	public BigDecimal getSparePrice() {
		return sparePrice;
	}
	/**
	 * 获取实体对应的平摊价
	 * SharePriceCalc.getHalvePriceByKey()<BR>
	 * @param key
	 * @return
	 */
	public BigDecimal getHalvePriceByKey(String key){
		HalveItem item = halveItemMap.get(key);
		if(item != null){
			return item.getHalvePrice();
		}
		return BigDecimal.ZERO;
	}
	/**
	 * 获取某个实体对应的平摊均价
	 * SharePriceCalc.getHalveAveragePriceByKey()<BR>
	 * @param key
	 * @return
	 */
	public BigDecimal getHalveAveragePriceByKey(String key){
		HalveItem item = halveItemMap.get(key);
		if(item != null){
			return item.getHalveAveragePrice();
		}
		return BigDecimal.ZERO;
	}
	
	/**
	 * PricesSpit.main()<BR>
	 * @param args
	 */
	public static void main(String[] args) {
		List<HalveItem> priceItems = new ArrayList<HalveItem>();
		HalveItem item = new HalveItem("1", new BigDecimal(1.2), 2);
		HalveItem item1 = new HalveItem("2", new BigDecimal(1.1), 1);
		priceItems.add(item);
		priceItems.add(item1);
		SharePriceCalc calc = new SharePriceCalc(new BigDecimal(0), priceItems, SharePriceCalc.SHARE_HALVE_AVERAGE);
//		SharePriceCalc calc = new SharePriceCalc(new BigDecimal(2), priceItems, SharePriceCalc.SHARE_HALVE);
		//获取实体对应的平摊价 
		System.out.println(calc.getHalvePriceByKey("1"));
		System.out.println(calc.getHalvePriceByKey("2"));
		
		//获取某个实体对应的平摊均价 
		System.out.println(calc.getHalveAveragePriceByKey("1"));
		System.out.println(calc.getHalveAveragePriceByKey("2"));
		
		//平摊不均的价格
		System.out.println(calc.getSparePrice());

	}

}
