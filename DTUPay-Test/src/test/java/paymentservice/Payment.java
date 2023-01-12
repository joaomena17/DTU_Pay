package paymentservice;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	String cid,mid;
	int amount;

	/*public Payment(String cid, String mid, int amount){
		this.cid = cid;
		this.mid = mid;
		this.amount = amount;
	}*/

}