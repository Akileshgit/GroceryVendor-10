package Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Rajesh Dabhi on 24/6/2017.
 */

public class Top_Selling_model {

    String product_id;
    String product_name;
    String product_image;
    String price;
    String top;
    String product_name_arb;

    @SerializedName("sub_cat")
    ArrayList<Category_subcat_model> category_sub_datas;

    public String getProduct_id() {
        return product_id;
    }


    public String getProduct_name_arb() {
        return product_name_arb;
    }

    public void setProduct_name_arb(String product_name_arb) {
        this.product_name_arb = product_name_arb;
    }


    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public ArrayList<Category_subcat_model> getCategory_sub_datas() {
        return category_sub_datas;
    }
}
