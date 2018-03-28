package uk.co.dmott.mysupershopper2.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by david on 27/03/18.
 */

public class NewShopItem implements Parcelable {

    private String itemId;
    private String shopItemName;
    private String shopName;
    private int shopItemType;

    public NewShopItem(String itemId, String shopItemName, String shopName, int shopItemType) {
        this.itemId = itemId;
        this.shopItemName = shopItemName;
        this.shopName = shopName;
        this.shopItemType = shopItemType;
    }

    //this was colorResource
    public int getShopItemType() {
        return shopItemType;
    }

    public void setShopItemType(int shopItemType) {
        this.shopItemType = shopItemType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopItemName() {
        return shopItemName;
    }

    public void setShopItemName(String shopItemName) {
        this.shopItemName = shopItemName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemId);
        dest.writeString(shopItemName);
        dest.writeString(shopName);
        dest.writeInt(shopItemType);

    }
    public static final Parcelable.Creator<NewShopItem> CREATOR = new Parcelable.Creator<NewShopItem>() {
        public NewShopItem createFromParcel(Parcel in) {
            return new NewShopItem(in);
        }
        @Override
        public NewShopItem[] newArray(int size) {
            return new NewShopItem[size];
        }
    };

    private NewShopItem(Parcel in) {
        itemId = in.readString();
        shopItemName = in.readString();
        shopName = in.readString();
        shopItemType = in.readInt();

    }



}
