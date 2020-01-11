package transfers_stock_DET;
import java.util.ArrayList;

public class Purchase {

	static ArrayList<Stock> PurchaseStocks = new ArrayList<Stock>();
	static ArrayList<Stock> sumPurchaseStocks = new ArrayList<Stock>();
	static ArrayList<Integer> quantities = new ArrayList<Integer>();
	static ArrayList<String> messages = new ArrayList<String>();

	/*
	 * With this method we create a list, where the orders have been divided in
	 * stocks
	 */

	public static void createPurchaseStocks() throws Exception {
		for (int i = 0; i < Order.getBlackList().size(); i++) {
			for (int k = 0; k < Order.getBlackList().get(i).getA().size(); k++) {
				PurchaseStocks.add(Order.getBlackList().get(i).getA().get(k));
			}
		}
	}

	/*
	 * With this method we create a list, that includes the quantities of every
	 * product id, which are required from the supplier
	 */
	public static ArrayList<Stock> sumPurchaseStocks() throws Exception {
		for (int i = 0; i < Stock.getStocks().size(); i++) {
			quantities.add(0);
		}

		for (int i = 0; i < PurchaseStocks.size(); i++) {
			quantities.add((PurchaseStocks.get(i).id) - 1,
					quantities.get(PurchaseStocks.get(i).id - 1) + PurchaseStocks.get(i).stock);

		}
		for (int i = 0; i < Stock.getStocks().size(); i++) {
			sumPurchaseStocks.add(i,
					new Stock(PurchaseStocks.get(i).id + 1, PurchaseStocks.get(i).name,
							PurchaseStocks.get(i).description, PurchaseStocks.get(i).volume,
							PurchaseStocks.get(i).minQuantity, quantities.get(i), PurchaseStocks.get(i).price));

		}
		return sumPurchaseStocks;

	}

	/*
	 * With this method, we check the minimum stock's Quantity for every product and
	 * we find the final quantity, that is required from the supplier
	 */

	public static ArrayList<Stock> finalQuantities() throws Exception {
		for (int i = 0; i < Stock.getStocks().size(); i++) {
			if (sumPurchaseStocks.get(i).stock > Stock.getStocks().get(i).stock) {
				int fquantity = sumPurchaseStocks.get(i).stock - Stock.getStocks().get(i).stock
						+ Stock.getStocks().get(i).minQuantity;
				sumPurchaseStocks.add(i,
						new Stock(PurchaseStocks.get(i).id + 1, PurchaseStocks.get(i).name,
								PurchaseStocks.get(i).description, PurchaseStocks.get(i).volume,
								PurchaseStocks.get(i).minQuantity, fquantity, PurchaseStocks.get(i).price));
			} else {
				if (Stock.getStocks().get(i).stock
						- sumPurchaseStocks.get(i).stock < Stock.getStocks().get(i).minQuantity) {
					int fquantity = Stock.getStocks().get(i).minQuantity;
					sumPurchaseStocks.add(i,
							new Stock(PurchaseStocks.get(i).id + 1, PurchaseStocks.get(i).name,
									PurchaseStocks.get(i).description, PurchaseStocks.get(i).volume,
									PurchaseStocks.get(i).minQuantity, fquantity, PurchaseStocks.get(i).price));
				}
			}
		}
		return sumPurchaseStocks;
	}

	/*
	 * With this method we send messages to the supplier , which include the final
	 * required quantities and we refresh the stocks
	 */

	public static ArrayList<String> messages() throws Exception {

		for (int i = 0; i < sumPurchaseStocks.size(); i++) {
			messages.add(
					"We need " + sumPurchaseStocks.get(i).stock + " of product with id " + sumPurchaseStocks.get(i).id);
			Stock.getStocks().add(i, sumPurchaseStocks.get(i));

		}
		return messages;
	}

	/* With this method we refresh the following lists */

	public static void refreshLists() throws Exception {
		for (int i = 0; i < Order.getBlackList().size(); i++) {
			Order.getGoodOrders().add(Order.getBlackList().get(i));
			Order.getBlackList().remove(i);
		}

	}

}