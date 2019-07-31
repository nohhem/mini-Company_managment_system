
public class Customer {
	private String name,companyName,phone1,phone2,address;
	private long date;
	private int id;
	
	
	
	
	
	public Customer(int id,String name, String companyName, String phone1, String phone2, String address) {
		super();
		this.setId(id);
		this.name = name;
		this.companyName = companyName;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.address = address;
	}
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPhone1() {
		return phone1;
	}
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String toString(){
		return id+" - "+name;
	}
}
