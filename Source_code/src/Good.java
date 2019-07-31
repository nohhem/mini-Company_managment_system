
public class Good {
	private String code,name,unit,section,description;
	private double quantity,PurchasePrice,SellPrice;
	
	public Good(){
		super();
		
	}
	public Good(String code, String name,  double quantity,String unit,
			double PurchasePrice,double SellPrice) {
		super();
		this.code = code;
		this.name = name;
		this.unit = unit;
		this.quantity = quantity;
		this.PurchasePrice = PurchasePrice;
		this.setSellPrice(SellPrice);
	}
	
	
	public Good(String code, String name,double quantity, String unit,double purchasePrice, double sellPrice,
			String section, String description) {
		super();
		this.code = code;
		this.name = name;
		this.unit = unit;
		this.section = section;
		this.description = description;
		this.quantity = quantity;
		this.PurchasePrice = purchasePrice;
		this.SellPrice = sellPrice;
	}
	
	
	

	public Good(String code, String name, String quantity, String unit, String purchasePrice, String sellPrice) {
		// TODO Auto-generated constructor stub
		this.code = code;
		this.name = name;
		this.unit = unit;
		this.section = section;
		this.description = description;
		this.quantity = Double.valueOf(quantity);
		this.PurchasePrice = Double.valueOf(purchasePrice);
		this.SellPrice = Double.valueOf(sellPrice);
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getquantity() {
		return quantity;
	}
	public void setquantity(double quantity) {
		this.quantity = quantity;
	}
	public double getPurchasePrice() {
		return PurchasePrice;
	}
	public void setPurchasePrice(double PurchasePrice) {
		this.PurchasePrice = PurchasePrice;
	}

	public double getSellPrice() {
		return SellPrice;
	}

	public void setSellPrice(double sellPrice) {
		SellPrice = sellPrice;
	}
	public String getCode() {
		return code;
	}
	
	public String toString(){
		String str=code+","+name+","+quantity+" ,"+unit+" ,"+PurchasePrice+" ,"+SellPrice+" ,"+description;
		return str;
	}
}
